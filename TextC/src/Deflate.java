import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Deflate {
	public static void comp(String inFileName, String outFileName) {
		byte bytes[] = null;
		try {
			bytes = HuffmanUtility.ReadFile(inFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte huffA[] = null;//Huffman.AdaptiveHuffmanC(bytes);
		byte huffV[] = null;;
		/*try {
			huffV = Huffman.VerboseHuffmanC(bytes);
		} catch (UnsupportedEncodingException e) {
			huffV = null;
		}*/
		byte LZSS[] = LZSSFull.LZSS.ByteEncoder.encode(bytes);
		byte LZHuffA[] = null;
		byte LZHuffV[] = null;
		byte HuffALZ[] = null;
		byte HuffVLZ[] = null;
		
		byte encoded[][] = new byte[][] {
			bytes, // Unencoded
			huffA,
			huffV,
			LZSS,
			LZHuffA,
			LZHuffV,
			HuffALZ,
			HuffVLZ
		};
		
		// Find encoding with best performance
		int encoder = 0;
		for(int i = 1; i < 8; i++) {
			if(encoded[i] != null && encoded[i].length < bytes.length) {
				bytes = encoded[i];
				encoder = i;
			}
		}
		byte out[] = new byte[bytes.length+1];
		out[0] = (byte) encoder;
		for(int i = 1; i < out.length; i++) {
			out[i] = bytes[i-1];
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
	
	public static void decomp(String inFileName, String outFileName) {
		byte bytes[] = null;
		try {
			bytes = HuffmanUtility.ReadFile(inFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte out[] = new byte[bytes.length-1];
		for(int i = 1; i < bytes.length; i++) {
			out[i-1] = bytes[i];
		}
		switch (bytes[0]) {
			case(0) :
				break;
			case(1) :
				out = Huffman.AdaptiveHuffmanD(out, false).bytes;
				break;
			case(2) :
				try {
					out = Huffman.VerboseHuffmanD(out);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;
			case(3) :
				out = LZSSFull.LZSS.ByteEncoder.decode(bytes);
				break;
			case(4) :
				break;
			case(5) :
				break;
			case(6) :
				break;
			case(7) :
				break;
			case(8) :
				break;
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
}