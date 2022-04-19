// 211RDB204 Juris Ozoliņš
// 211RDB276 Toms Zvirbulis

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Map.Entry;

//Stores key and value pair with node functionality for implementing a binary tree.
class Value implements Comparable<Value> {
	Byte key = null;
	Integer value;
	Byte side = -1;
	public static int childCount = 2;
	Value child[] = new Value[childCount], parent = null;
	// Used to mark last byte in full tree, because it's key will be indistinguishable from the null-byte.
	boolean isLast = false;
	
	// Makes a Value that acts as a node in a tree.
	public Value(Value _child[], int _value) {
		child = _child;
		value = _value;
	}

	// Makes a Value that stores data.
	public Value(byte _key, int _value) {
		key = _key;
		value = _value;
	}
	public Value(byte _key) {
		key = _key;
	}
	
	// Calculate the sum of children's value.
	public static Integer childSum(Value ch[]) {
		Integer sum = 0;
		for(Value c : ch) {
			sum += c.value;
		}
		return sum;
	}
	
	@Override
	public int compareTo(Value val) {
		return value.compareTo(val.value);
 }
}

public class Main {
	public static void main(String[] args) {
		String choiseStr;
		String fileName, outFileName, firstFile, secondFile;
		Scanner sc = new Scanner(System.in);
		
		loop: while (true) {
			
			choiseStr = sc.next();
								
			switch (choiseStr) {
			case "comp":
				System.out.print("source file name: ");
				fileName = sc.next();
				System.out.print("archive name: ");
				outFileName = sc.next();
				comp(fileName, outFileName);
				break;
			case "decomp":
				System.out.print("archive name: ");
				fileName = sc.next();
				System.out.print("file name: ");
				outFileName = sc.next();
				decomp(fileName, outFileName);
				break;
			case "size":
				System.out.print("file name: ");
				fileName = sc.next();
				size(fileName);
				break;
			case "evaluate":
				System.out.print("first file name: ");
				firstFile = sc.next();
				System.out.print("second file name: ");
				secondFile = sc.next();
				eavluate(firstFile, secondFile);
				break;
			case "equal":
				System.out.print("first file name: ");
				firstFile = sc.next();
				System.out.print("second file name: ");
				secondFile = sc.next();
				System.out.println(equal(firstFile, secondFile));
				break;
			case "about":
				about();
				break;
			case "test":
				File testFolder = new File("tests");
				String failed = "";
				double totalAvg = 0;
				int count = 0;
				for (File testGroup : testFolder.listFiles()) {
					System.out.println("-------------------------------------------------------------------");
					System.out.printf("[Doing tests from %s]\n", testGroup.getName());
					double avg = 0;
					for(File t : testGroup.listFiles()) {
						Double rez = test(t.getPath(), "results/" + t.getParentFile().getName() + "/" + t.getName());
						if(rez != null) {
							avg += rez;
							totalAvg += rez;
							count++;
						} else {
							failed += t.getPath() + "\n";
						}
					}
					System.out.printf("Average score: %.1f%%\n", avg/testGroup.listFiles().length);
			    }
				System.out.println("-------------------------------------------------------------------");
				if(count > 0) {
					System.out.printf("Total average score: %.1f%%\n", totalAvg/count);
				}
				System.out.println();
				if(!failed.equals("")) {
					System.out.printf("Tests failed: \n%s\n", failed);
				} else {
					System.out.printf("All test completed successfully!");
				}
				break;
			case "-e":
			case "exit":
				sc.close();
				break loop;
			}
		}
	}

	public static void comp(String fileName, String outFileName) {
		// Helps keep track of what goes where.
		ArrayList<Value> data = new ArrayList<Value>();
		HashMap<Byte, Value> map = new HashMap<Byte, Value>();
		Value top;
		int treeSize = 1;
		// Get character data from input file.
		HashMap<Byte, Integer> words = new HashMap<Byte, Integer>();
		Boolean nullFound = false;
		FileInputStream file;
		byte[] bytes;
		try {
			// Take input file and convert it to byte array.
			file = new FileInputStream(fileName);
			bytes = new byte[file.available()];
			file.read(bytes);
			file.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// Save individual characters and keep track of how many times they're used.
		for (byte b : bytes) {
			if(b == 0) {
				nullFound = true;
			}
			if(words.containsKey(b)) {
				words.merge(b, 1, Integer::sum);
			}else {
				words.put(b, 1);
			}
		}
		
		// If necessary, find a free byte in the text that will be used to represent null.
		byte nullByte = 0;
		boolean full = nullFound;
		if(nullFound) {
			for(int i = 0; i < 256; i++) {
				if(!words.containsKey((byte)(i))) {
					nullByte = (byte)(i);
					full = false;
					break;
				}
			}
		}
		
		// Create frequency tree using char values used for future size reference.
		// Convert character data into sorted array.
		for(Entry<Byte, Integer> e : words.entrySet()) {
			Value val = new Value(e.getKey(), e.getValue());
			int index = 0;
			while(index < data.size() && data.get(index).value < val.value) {
				index++;
			}
			data.add(index, val);
			map.put(val.key, data.get(index));
		}
		
		PriorityQueue<Value> dataQueue = new PriorityQueue<Value>(data);
		
		// If all bytes are used, use byte with least priority as null-byte.
		if(full) {
			nullByte = data.get(0).key;
		}
		
		// Convert sorted array into tree of values. The largest values are at the top.
		while(dataQueue.size() > 1) {
			Value c[] = new Value[Value.childCount];
			for(int i = 0; i < c.length; i++) {
				c[i] = dataQueue.poll();
			}
			Value parent = new Value(c, Value.childSum(c));
			
			// Save parent and side information for path building.
			for(byte i = 0; i < c.length; i++) {
				c[i].parent = parent;
				c[i].side = i;
			}
			
			dataQueue.add(parent);
			treeSize+=Value.childCount;
		}
		
		top = dataQueue.poll();
		//PrintTree(top);
		
		// Encode tree into byte array using frequency tree.
		byte[] head = TreeToBytes(treeSize, top, nullByte);
		
		ArrayList<Byte> body = new ArrayList<Byte>();
		Byte curByte = 0;
		int index = 0;
		for(byte by : bytes) {
			for(byte bit : GetPath(map, by)) {
				curByte = (byte) (curByte | (bit << (7-(index++))));
				if(index >= 8) {
					body.add(curByte);
					curByte = 0;
					index = 0;
				}
			}
		}
		if(index != 8) {
			body.add(curByte);
		}

		byte[] out = new byte[2+head.length+body.size()];
		// Store information about how many filler bits exist at the end of the file.
		out[0] = (byte)((7-index) << 5);
		out[1] = nullByte;
		
		// Assemble final output.
		index = 2;
		for(byte h : head) {
			out[index++] = h;
		}
		for(byte b : body) {
			out[index++] = b;
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
	
	public static void decomp(String fileName, String outFileName) {
		FileInputStream in = null;
		byte[] bytes;
		try {
			// Take input file and convert it to byte array.
			in = new FileInputStream(fileName);
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
		
		// Mask off first 5 bits to get amount of filler bits at the end of file.
		byte fillerBits = (byte)((bytes[0] & 224) >> 5);
		byte nullByte = bytes[1];
		
		// Reassemble frequency tree.
		int cursor = 2;
		int valueCount = 0;
		Queue<Value> queue = new LinkedList<Value>();
		top = new Value(bytes[cursor]);
		queue.add(top);
		
		while(queue.size() != 0 && valueCount < 255) {
			Value val = queue.poll();
			if(val.key == nullByte) {
				for(int i = 0; i < Value.childCount; i++) {
					val.child[i] = new Value(bytes[++cursor]);
					if(bytes[cursor] == nullByte) {
						queue.add(val.child[i]);
					} else {
						valueCount++;
					}
				}
			} 
		}
		
		//PrintTree(top);
		
		// Decode text and save it as byte array.
		int bitCursor = 7;
		ArrayList<Byte> text = new ArrayList<Byte>();
		Value val = top;

		cursor++;
		if(top.key == nullByte) {
			while(cursor < bytes.length-1 || (cursor == bytes.length-1 && (bitCursor > fillerBits || val.key != nullByte))) {
				Value child = val.child[(bytes[cursor] >> bitCursor) & 1];
				if(val.key == nullByte && child != null) {
					val = child;
					if(bitCursor > 0) {
						bitCursor--;
					} else {
						bitCursor = 7;
						cursor++;
					}
				} else {
					text.add(val.key);
					val = top;
				}
				/*if(cursor == bytes.length-1) {
					System.out.printf("%d: %s %s added %s\n", cursor, (bitCursor), fillerBits, val.key);
				}*/
			}
		} else {
			while(cursor < bytes.length-1 || (cursor == bytes.length-1 && bitCursor > fillerBits)) {
				if(bitCursor > 0) {
					bitCursor--;
				} else {
					bitCursor = 7;
					cursor++;
				}
				text.add(val.key);
			}
		}
		byte output[] = new byte[text.size()];
		int index = 0;
		
		for(Byte b : text) {
			output[index++] = b;
		}
		
		// Save to file.
		try {
			FileOutputStream out = new FileOutputStream(outFileName);
			out.write(output);
			out.close();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	static byte[] GetPath (HashMap<Byte, Value> map, Byte target) {
		ArrayList<Byte> rez = new ArrayList<Byte>();
		Value val = map.get(target);
		if(val.side == -1) {
			return new byte[1];
		}
		while(val.side != -1) {
			rez.add(0, val.side);
			val = val.parent;
		}
		byte out[] = new byte[rez.size()];
		int index = 0;
		for(byte b : rez) {
			out[index++] = b;
		}
		return out;
	}
	
	// Returns Byte array of tree data.
	public static byte[] TreeToBytes(int treeSize, Value top, byte nullByte) {
		byte[] out = new byte[treeSize];
		int index = 0;
		ArrayList<Value> layer = new ArrayList<Value>();
		
		if(top.key == null) {
			out[index++] = nullByte;
		}else {
			out[index++] = top.key;
		}
		
		for(Value child : top.child) {
			if(child != null) {
				layer.add(child);
			}
		}
		
		while(layer.size() > 0) {
			ArrayList<Value> newLayer = new ArrayList<Value>();
			
			for(Value v : layer) {
				if(v.key == null) {
					out[index++] = nullByte;
				}else {
					out[index++] = v.key;
				}
				
				for(Value child : v.child) {
					if(child != null) {
						newLayer.add(child);
					}
				}
			}
			layer = newLayer;
		}
		
		return out;
	}
	
	// Prints contents of data array.
	public void PrintData(ArrayList<Value> data) {
		String out = "";
		for(Value val : data) {
			out = ((val.key == 10) ? "[n]" : (char) (val.key & 0xFF)) + " " + out;
		}
		System.out.println(out);
	}
	
	// Prints contents of frequency tree recursively.
	public static void PrintTree(Value first) {
		ArrayList<Value> layer = new ArrayList<Value>();
		
		System.out.print(first.key + "\n");
		
		for(Value child : first.child) {
			if(child != null) {
				layer.add(child);
			}
		}
		
		while(layer.size() > 0) {
			ArrayList<Value> newLayer = new ArrayList<Value>();
			
			int index = 0;
			for(Value v : layer) {
				System.out.print(v.key);
				
				if(index%2==0 || index == layer.size()-1) {
					System.out.printf(" ");
				} else {
					System.out.printf(" ^ ");
				}
				
				for(Value child : v.child) {
					if(child != null) {
						newLayer.add(child);
					}
				}
			}
			index++;
			System.out.println();
			layer = newLayer;
		}
	}
	
	public static Double eavluate(String firstFile, String secondFile) {
		Double first, second;
		try {
			FileInputStream f = new FileInputStream(firstFile);
			first = (double) f.available();
			f.close();
			f = new FileInputStream(secondFile);
			second = (double) f.available();
			f.close();
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
			return -1.0;
		}
		System.out.printf("%s to %s saves %.1f%%\n", firstFile, secondFile, ((1-second/first)*100));
		return (1-second/first)*100;
	}
	
	public static void size(String fileName) {
		try {
			FileInputStream f = new FileInputStream(fileName);
			System.out.println("size: " + f.available());
			f.close();
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		
	}
	
	public static boolean equal(String firstFile, String secondFile) {
		try {
			FileInputStream f1 = new FileInputStream(firstFile);
			FileInputStream f2 = new FileInputStream(secondFile);
			int k1, k2;
			byte[] buf1 = new byte[f1.available()];
			byte[] buf2 = new byte[f2.available()];
			k1 = f1.read(buf1);
			k2 = f2.read(buf2);
			if (k1 != k2) {
				f1.close();
				f2.close();
				System.out.printf("Expected size was %d, actual size is %d\n", k1, k2);
				return false;
			}
			for (int i=0; i<k1; i++) {
				if (buf1[i] != buf2[i]) {
					f1.close();
					f2.close();
					System.out.printf("Value should have been %d, instead was %d\n", buf1[i], buf2[i]);
					return false;
				}
					
			}
			f1.close();
			f2.close();
			return true;
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	static Double test(String fileName, String outFileName) {
		comp(fileName, "temp.dat"); 
		decomp("temp.dat", outFileName);
		if(!equal(fileName, outFileName)) {
			System.out.println("Test Failed!");
			return null;
		}
		return eavluate(fileName, "temp.dat");
	}
	
	public static void about() {
		System.out.println("211RDB204 Juris Ozoliņš");
		System.out.println("211RDB276 Toms Zvirbulis");
	}
}
