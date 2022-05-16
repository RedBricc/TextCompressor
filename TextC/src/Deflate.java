import java.io.FileOutputStream;
import java.io.IOException;
import java.util.PriorityQueue;

public class Deflate {
	public static void comp(String inFileName, String outFileName) throws Exception {
		byte bytes[] = null;
		PriorityQueue<IntByteArrPair> queue = new PriorityQueue<IntByteArrPair>();
		try {
			bytes = HuffmanUtility.ReadFile(inFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte outBytes[] = new byte[bytes.length+1];
		outBytes[0] = 0;
		for(int i = 1; i < outBytes.length; i++) {
			outBytes[i] = bytes[i-1];
		}
		queue.add(new IntByteArrPair(outBytes.length, outBytes));
		byte huffA[] = null;
		try {
			huffA = Huffman.AdaptiveHuffmanC(bytes);
			queue.add(new IntByteArrPair(huffA.length, huffA));
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte huffV[] = Huffman.VerboseHuffmanC(bytes);/*
		try {
			huffV = Huffman.VerboseHuffmanC(bytes);
			queue.add(new IntByteArrPair(huffV.length, huffV));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		byte LZSS[] = null;
		try {
			LZSS = LZSSFull.LZSS.ByteEncoder.encode(bytes);
			queue.add(new IntByteArrPair(LZSS.length, LZSS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			byte LZHuffA[] = Huffman.AdaptiveHuffmanC(LZSS);
			LZHuffA[0] = (byte) ((LZHuffA[0] & -16) | 4);
			queue.add(new IntByteArrPair(LZHuffA.length, LZHuffA));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			byte LZHuffV[] = Huffman.VerboseHuffmanC(LZSS);
			LZHuffV[0] = (byte) ((LZHuffV[0] & -16) | 5);
			queue.add(new IntByteArrPair(LZHuffV.length, LZHuffV));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			byte HuffALZ[] = LZSSFull.LZSS.ByteEncoder.encode(huffA);
			HuffALZ[0] = 6;
			queue.add(new IntByteArrPair(HuffALZ.length, HuffALZ));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			byte HuffVLZ[] = LZSSFull.LZSS.ByteEncoder.encode(huffV);
			HuffVLZ[0] = 7;
			queue.add(new IntByteArrPair(HuffVLZ.length, HuffVLZ));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Find encoding with best performance
		byte out[] = null;
		byte decode[];
		do {
			if(queue.isEmpty())
				throw new Exception("Could not encode given file!");
			out = queue.poll().bytes;
			decode = decomp(out);
		} while(!Main.equal(bytes, decode));
		
		// Save to file.
		try {
			FileOutputStream output = new FileOutputStream(outFileName);
			output.write(out);
			output.close();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void decomp(String inFileName, String outFileName) throws Exception {
		byte bytes[] = null;
		try {
			bytes = HuffmanUtility.ReadFile(inFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte out[] = null;
		switch (bytes[0] & 15) {
			case(0) :
				System.out.println("Literal");
				out = new byte[bytes.length-1];
				int index = 0;
				for(int i = 0; i < out.length; i++) {
					out[i] = bytes[i+1];
				}
				break;
			case(1) :
				try {
					System.out.println("AdaptiveHuffmanD");
					out = Huffman.AdaptiveHuffmanD(bytes, false).bytes;
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case(2) :
				try {
					System.out.println("VerboseHuffmanD");
					out = Huffman.VerboseHuffmanD(bytes);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case(3) :
				try {
					System.out.println("LZSS");
					out = LZSSFull.LZSS.ByteEncoder.decode(bytes);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case(4) :
				try {
					System.out.println("LZSS => AdaptiveHuffmanD");
					out = LZSSFull.LZSS.ByteEncoder.decode(bytes);
					out = Huffman.AdaptiveHuffmanD(out, false).bytes;
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case(5) :
				try {
					System.out.println("LZSS => VerboseHuffmanD");
					out = LZSSFull.LZSS.ByteEncoder.decode(bytes);
					out = Huffman.VerboseHuffmanD(out);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case(6) :
				try {
					System.out.println("AdaptiveHuffmanD => LZSS");
					out = Huffman.AdaptiveHuffmanD(bytes, false).bytes;
					out = LZSSFull.LZSS.ByteEncoder.decode(out);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case(7) :
				try {
					System.out.println("VerboseHuffmanD => LZSS");
					out = Huffman.VerboseHuffmanD(bytes);
					out = LZSSFull.LZSS.ByteEncoder.decode(out);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				throw new Exception("Could not decode given file!");
		}
		
		// Save to file.
		try {
			FileOutputStream output = new FileOutputStream(outFileName);
			output.write(out);
			output.close();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static byte[] decomp(byte bytes[]) throws Exception {
		byte out[] = null;
		switch (bytes[0] & 15) {
			case(0) :
				out = new byte[bytes.length-1];
				int index = 0;
				for(int i = 0; i < out.length; i++) {
					out[i] = bytes[i+1];
				}
				break;
			case(1) :
				try {
					out = Huffman.AdaptiveHuffmanD(bytes, false).bytes;
				} catch (Exception e) {}
				break;
			case(2) :
				try {
					out = Huffman.VerboseHuffmanD(bytes);
				} catch (Exception e) {}
				break;
			case(3) :
				try {
					out = LZSSFull.LZSS.ByteEncoder.decode(bytes);
				} catch (Exception e) {}
				break;
			case(4) :
				try {
					System.out.println("LZSS => AdaptiveHuffmanD");
					out = Huffman.AdaptiveHuffmanD(bytes, false).bytes;
					out = LZSSFull.LZSS.ByteEncoder.decode(out);
				} catch (Exception e) {}
				break;
			case(5) :
				try {
					System.out.println("LZSS => VerboseHuffmanD");
					out = Huffman.VerboseHuffmanD(bytes);
					out = LZSSFull.LZSS.ByteEncoder.decode(out);
				} catch (Exception e) {}
				break;
			case(6) :
				try {
					System.out.println("AdaptiveHuffmanD => LZSS");
					out = LZSSFull.LZSS.ByteEncoder.decode(bytes);
					out = Huffman.AdaptiveHuffmanD(out, false).bytes;
				} catch (Exception e) {}
				break;
			case(7) :
				try {
					for(byte b : bytes)
						System.out.print(b + " ");
					System.out.println("VerboseHuffmanD => LZSS");
					out = LZSSFull.LZSS.ByteEncoder.decode(bytes);
					for(byte b : out)
						System.out.println(b);
					out = Huffman.VerboseHuffmanD(out);
				} catch (Exception e) {}
				break;
		}
		
		return out;
	}
}