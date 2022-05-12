import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Deflate {
	public static void comp(String inFileName, String outFileName, int method) {
		byte out[] = null;
		byte bytes[] = null;
		try {
			bytes = HuffmanUtility.ReadFile(inFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		switch(method) {
			case 1:
				System.out.println("Deflate algorithm isn't implemented yet!");
				break;
			case 2:
				out = Huffman.AdaptiveHuffmanC(bytes);
				break;
			case 3:
				try {
					out = Huffman.VerboseHuffmanC(bytes);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				System.out.println("LZSS algorithm isn't implemented yet!");
				break;
			default:
				System.out.println("No algorithm with this number exists!");
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
	
	public static void decomp(String inFileName, String outFileName, int method) {
		byte out[] = null;
		byte bytes[] = null;
		try {
			bytes = HuffmanUtility.ReadFile(inFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		switch(method) {
			case 1:
				System.out.println("Deflate algorithm isn't implemented yet!");
				break;
			case 2:
				out = Huffman.AdaptiveHuffmanD(bytes, false).bytes;
				break;
			case 3:
				try {
					out = Huffman.VerboseHuffmanD(bytes);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				System.out.println("LZSS algorithm isn't implemented yet!");
				break;
			default:
				System.out.println("No algorithm with this number exists!");
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