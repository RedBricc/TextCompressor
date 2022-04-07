import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.println("211RDB204 Juris Ozoliņš");
		// Get input file location from user.
		String fileName;
		System.out.println("input file name:");
		//fileName = sc.nextLine();
		fileName = "kennedy.xls";
		sc.close();
		Comp(fileName, "temp.dat");
		Decomp("temp.dat", "decomp.txt");
	}
	
	// Takes encoded text file and prints it to an output file.
	static void Decomp(String inFileName, String outFileName) {
		FileInputStream in = null;
		byte[] bytes;
		try {
			// Take input file and convert it to byte array.
			in = new FileInputStream(inFileName);
			bytes = new byte[in.available()];
			in.read(bytes);
			in.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		Value top;
		
		byte fillerBits = bytes[0];
		//byte compLayer = bytes[1];
		byte nullByte = bytes[2];
		
		// Reassemble frequency tree.
		Queue<Value> queue = new LinkedList<Value>();
		int cursor = 3;
		top = new Value(bytes[cursor]);
		queue.add(top);
		
		while(queue.size() != 0) {
			Value val = queue.poll();
			if(val.key == 0) {
				for(int i = 0; i < Value.childCount; i++) {
					val.child[i] = new Value(bytes[++cursor]);
					if(bytes[cursor] == 0) {
						queue.add(val.child[i]);
					}
				}
			}
		}
		
		CompTree.PrintTree(top);
		
		// Decode text and save it as byte array.
		int bitCursor = 7;
		ArrayList<Byte> text = new ArrayList<Byte>();
		Value val = top;
		cursor++;
		while(cursor < bytes.length-1 || (cursor == bytes.length-1 && 8-bitCursor >= fillerBits)) {
			if(val.key == 0) {
				val = val.child[(bytes[cursor] >> bitCursor) & 1];
				if(bitCursor > 0) {
					bitCursor--;
				} else {
					bitCursor = 7;
					cursor++;
				}
			} else {
				if(val.key == nullByte) {
					text.add((byte) 0);
				} else {
					text.add(val.key);
				}
				val = top;
			}
		}
		byte output[] = new byte[text.size()];
		int index = 0;
		
		for(Byte b : text) {
			output[index++] = b;
		}
		
		// Save to file.
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outFileName));
			out.write(new String(output, StandardCharsets.UTF_8));
			out.close();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	static Boolean Comp(String fileName, String outFileName) {
		// Get character data from input file.
		HashMap<Byte, Integer> words = new HashMap<Byte, Integer>();
		String text = "";
		Boolean nullFound = false;
		try {
			sc = new Scanner(new FileReader(fileName));
			while (sc.hasNextLine()) {
				// Save whole text for later use.
				String line = sc.nextLine() + ((sc.hasNextLine()) ? "\n" : "");
				text += line;
				byte[] bytes = line.getBytes(StandardCharsets.UTF_8);
				
				// Save individual characters and keep track of how many times they're used.
				for(byte b : bytes) {
					if(b == 1) {
						nullFound = true;
					}
					if(words.containsKey(b)) {
						words.merge(b, 1, Integer::sum);
					}else {
						words.put(b, 1);
					}
				}
				
			}
			
			sc.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		// If necessary, find a free byte in the text that will be used to replace any characters with the value of 0 (null).
		// The tree decompiling process relies on this symbol to not be present.
		byte nullByte = 1;
		if(nullFound) {
			for(int i = 2; i < 256; i++) {
				if(!words.containsKey((byte)(i))) {
					nullByte = (byte)(i);
				}
			}
		}
		
		// Create frequency tree using char values used for future size reference.
		CompTree tree = new CompTree(words.entrySet(), nullByte);
		//tree.PrintData();
		//CompTree.PrintTree(tree.top);
		
		// Save to file.
		try {
			FileOutputStream out = new FileOutputStream(outFileName);
			out.write(tree.Compile(text, nullByte));
			out.close();
			return true;
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
