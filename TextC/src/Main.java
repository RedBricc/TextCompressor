// 211RDB204 Juris Ozoliņš
// 211RDB276 Toms Zvirbulis

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

//Stores key and value pair with node functionality for implementing a verbose binary tree.
class Word implements Comparable<Word> {
	Byte[] key = null;
	Integer value;
	Byte side = -1;
	String stringValue;
	boolean reverse = false;
	public static int childCount = 2;
	Word child[] = new Word[childCount], parent = null;
	// Used to mark last byte in full tree, because it's key will be indistinguishable from the null-byte.
	boolean isLast = false;
	
	// Makes a Value that acts as a node in a tree.
	public Word(Word _child[], Integer _value) {
		child = _child;
		value = _value;
	}

	// Makes a Value that stores data.
	public Word(Byte _key[], int _value) {
		key = _key;
		value = _value;
	}
	public Word(Byte _key[]) {
		key = _key;
	}
	public Word(String _string, int _value) {
		stringValue = _string;
		value = _value;
	}
	
	// Calculate the sum of children's value.
	public static Integer calculateValue(Word ch[]) {
		Integer sum = 0;
		for(Word c : ch) {
			sum += c.value;
		}
		return sum;
	}
	
	// Compare two Word objects based on value.
	@Override
	public int compareTo(Word val) {
		return reverse ? val.value.compareTo(value) : value.compareTo(val.value);
 }
}

class HashSetPair {
	HashSet<Integer> firstSet;
	HashSet<Integer> secondSet;
	
	public HashSetPair(HashSet<Integer> first, HashSet<Integer> second) {
		firstSet = first;
		secondSet = second;
	}
}

class IntByteArrPair {
	int value;
	byte bytes[];
	
	public IntByteArrPair (int v, byte b[]) {
		value = v;
		bytes = b;
	}
}

public class Main {
	// TODO: Implement Huffman coding for flag encoding.
	/*static Value smallFlagTree;
	static Value mediumFlagTree;
	static Value largeFlagTree;
	static Value fullFlagTree;
	
	static int smallFlagArray[];
	static int mediumFlagArray[];
	static int largeFlagArray[];
	static int fullFlagArray[];*/
	
	public static void main(String[] args) {
		/*smallFlagArray = new int[] {6558, 725, 2298, 1664, 715, 392, 194, 133, 102, 72, 43, 38, 46, 24, 24, 30, 16, 14, 10, 10, 7, 6, 5, 3, 3, 1, 4, 3, 1, 2, 2, 2, 3, 1, 2,1};
		mediumFlagArray = new int[] {44626, 4493, 15442, 11388, 4937, 2731, 1509, 973, 714, 520, 375, 281, 288, 163, 150, 181, 97, 82, 62, 57, 37, 30, 23, 21, 17, 7, 19, 14, 13, 8, 9, 8, 15, 3, 7, 4, 3, 1, 2, 1, 2, 1, 2, 3, 1, 1, 2, 2, 1};
		largeFlagArray = new int[] {151642, 11164, 41998, 36477, 26776, 11844, 7177, 6141, 3915, 1662, 995, 1076, 535, 304, 279, 293, 209, 148, 121, 111, 74, 62, 50, 46, 44, 20, 31, 27, 19, 15, 20, 12, 39, 4, 16, 7, 6, 4, 6, 2, 2, 1, 2, 4, 4, 1, 5, 2, 4, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 13, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1};
		fullFlagArray = new int[] {151642, 11164, 41998, 36477, 26776, 11844, 7177, 6141, 3915, 1662, 995, 1076, 535, 304, 279, 293, 209, 148, 121, 111, 74, 62, 50, 46, 44, 20, 31, 27, 19, 15, 20, 12, 39, 4, 16, 7, 6, 4, 6, 2, 2, 1, 2, 4, 4, 1, 5, 2, 4, 1, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 13, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 8, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		
		ArrayList<Value> data = new ArrayList<Value>();
		for(int i = 0; i < smallFlagArray.length; i++) {
			data.add(new Value((byte)i, smallFlagArray[i]));
		}
		PriorityQueue<Value> queue = new PriorityQueue<Value>(data);
		
		while(queue.size() > 1) {
			Value c[] = new Value[Value.childCount];
			for(int i = 0; i < c.length; i++) {
				c[i] = queue.poll();
			}
			Value parent = new Value(c, Value.childSum(c));
			
			// Save parent and side information for path building.
			for(byte i = 0; i < c.length; i++) {
				c[i].parent = parent;
				c[i].side = i;
			}
			
			queue.add(parent);
		}
		
		smallFlagTree = queue.poll();*/
		
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
				comp(fileName, outFileName, 1);
				break;
			case "decomp":
				System.out.print("archive name: ");
				fileName = sc.next();
				System.out.print("file name: ");
				outFileName = sc.next();
				decomp(fileName, outFileName, 1);
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
				System.out.println("Select compression method.");
				System.out.println("1. Deflate");
				System.out.println("2. Adaptive Huffman");
				System.out.println("3. Word Based Huffman");
				System.out.println("4. LZSS");
				Integer method = null;
				do {
					if (method != null)
						System.out.println("input-output error, number out of bounds!");
					while (!sc.hasNextInt()) {
						System.out.println("input-output error, input is not an integer!");
						sc.next();
					}
					method = sc.nextInt();
				} while(method < 1 || method > 4);
				test(method);
				break;
			case "-e":
			case "e":
			case "exit":
				sc.close();
				break loop;
			}
		}
	}

	public static void comp(String inFileName, String outFileName, int method) {
		byte out[] = null;
		byte bytes[] = null;
		try {
			bytes = ReadFile(inFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		switch(method) {
			case 1:
				System.out.println("Deflate algorithm isn't implemented yet!");
				break;
			case 2:
				out = AdaptiveHuffmanC(bytes);
				break;
			case 3:
				try {
					out = VerboseHuffmanC(bytes);
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
			bytes = ReadFile(inFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		switch(method) {
			case 1:
				System.out.println("Deflate algorithm isn't implemented yet!");
				break;
			case 2:
				out = AdaptiveHuffmanD(bytes, false).bytes;
				break;
			case 3:
				try {
					out = VerboseHuffmanD(bytes);
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
	
	static byte[] VerboseHuffmanC(byte[] bytes) throws UnsupportedEncodingException {
		// Helps keep track of what goes where.
		ArrayList<Word> data = new ArrayList<Word>();
		Word top;
		// A word here is defined as a sequence of bytes.
		int longestWordLength = 1;
		int treeSize = 1;
		
		// Curate words from the text by looking for byte sequences that are efficient to save.
		HashMap<String, HashSet<Integer>> words = new HashMap<String, HashSet<Integer>>();
		HashMap<Integer, String> wordLocations = new HashMap<Integer, String>();
		// Fill list with byte values from file.
		int bytePos = 0;
		int min = Integer.MAX_VALUE, max = 0;
		for(byte b : bytes) {
			// Use String as wrapper for byte array as arrays can't be hashed.
			String key = new String(new byte[] {b}, "ISO-8859-1");
			HashSet<Integer> posArray;
			wordLocations.put(bytePos, key);
			if(words.containsKey(key)) {
				posArray = words.get(key);
			}else {
				posArray = new HashSet<Integer>();
			}
			if(posArray.size() > max) {
				max = posArray.size();
			} else if (posArray.size() < min) {
				min = posArray.size();
			}
			posArray.add(bytePos++);
			words.put(key, posArray);
		}
		// Fill priority queue. This will be used to iterate through the word map.
		PriorityQueue<Word> queue = new PriorityQueue<Word>();
		for(Entry<String, HashSet<Integer>> val : words.entrySet()) {
			Word word = new Word(val.getKey(), val.getValue().size());
			word.reverse = true;
			queue.add(word);
		}
		
		// Iterate through word map and merge recurring byte sequences.
		LinkedList<byte[]> wordList = new LinkedList<byte[]>();
		while(!queue.isEmpty()) {
			Word val = queue.poll();
			//long start = System.nanoTime();
			// Create hash map of all possible words that could be created by appending the next word.
			HashMap<String, HashSetPair> nextWords = new HashMap<String, HashSetPair>();
			if (words.get(val.stringValue) == null)
				continue;
			for(int index : words.get(val.stringValue)) {
				if(index + val.stringValue.length() >= bytes.length)
					continue;
				// Use String as wrapper for byte array as arrays can't be hashed.
				if(wordLocations.get(index + val.stringValue.length()) == null) {
					continue;
				}
				String key = val.stringValue + wordLocations.get(index + val.stringValue.length());
				HashSetPair posArray;
				if(nextWords.containsKey(key)) {
					posArray = nextWords.get(key);
				}else {
					posArray = new HashSetPair(new HashSet<Integer>(), new HashSet<Integer>());
				}
				posArray.firstSet.add(index);
				posArray.secondSet.add(index + val.stringValue.length());
				nextWords.put(key, posArray);
			}
			
			// Iterate through words and append any worthwhile words.
			int flagCount = 1;
			for(Entry<String, HashSetPair> word : nextWords.entrySet()) {
				if(words.get(val.stringValue) == null)
					continue;
				int count = word.getValue().firstSet.size();
				// It is never efficient to save a word of length 1.
				if(count < 2) 
					continue;
				int length = word.getKey().length();
				int newLWL = (length > longestWordLength) ? length : longestWordLength;
				if(newLWL > 254)
					continue;
				
				if(words.containsKey(word.getKey())) {
					// word already exists, so append the new indexes to it's index array.
					HashSet<Integer> newI = word.getValue().firstSet;
					words.put(word.getKey(), newI);
				} else {
					// Keep track of how much space could be saved by adding the word as well as the cost of doing so.
					int gain = 0;
					// Word has to be saved in full length inside header.
					Double cost = (double) length + 4.307;
					
					// Amount of bytes compressed
					gain += ((1 / (1 + Math.log(word.getKey().length() - val.stringValue.length()) / Math.log(2))) * word.getValue().secondSet.size() * (1+(Math.log(flagCount) / Math.log(2))*(Math.log(1+((word.getValue().secondSet.size()-min < 0) ? 0 : ((max-word.getValue().secondSet.size() < 0) ? 1 : (((word.getValue().secondSet.size()-min)/max))))*10) / Math.log(2))))
							+ ((1 / (1 + Math.log(val.stringValue.length()) / Math.log(2))) * word.getValue().firstSet.size() * (1+(Math.log(flagCount) / Math.log(2))*(Math.log(1+((word.getValue().firstSet.size()-min < 0) ? 0 : ((max-word.getValue().firstSet.size() < 0) ? 1 : (((word.getValue().firstSet.size()-min)/max))))*10) / Math.log(2))));
					// Approximation of how much each encoded word takes up.
					// 1 / LOG(newLWL;2) * COUNT * (1+LOG(TOTAL;2)*LOG(1+VAL*10;2)*0,477)
					// TODO: find approximation without the value TOTAL.
					// Where VAL is COUNT normalized to between 0 and 1.
					cost += (1 / (1 + Math.log(newLWL) / Math.log(2))) * count * (1+(Math.log(flagCount) / Math.log(2))*(Math.log(1+((count-min < 0) ? 0 : ((max-count < 0) ? 1 : (((count-min)/max))))*10) / Math.log(2)));
					// Adding words adds one flag byte.
					cost++;
					
					// Add word if it is worth it.
					if(gain > cost) {
						if (newLWL > longestWordLength) {
							longestWordLength = newLWL;
						}
						flagCount++;
						words.put(word.getKey(), word.getValue().firstSet);
						// Remove second part.
						String secondPart = word.getKey().substring(val.stringValue.length());
						if(words.get(secondPart).size() == word.getValue().firstSet.size()) {
							words.remove(secondPart);
						} else {
							words.get(secondPart).removeAll(word.getValue().secondSet);
						}
						// Update start locations.
						for(int in : word.getValue().firstSet) {
							wordLocations.put(in, word.getKey());
							for(int i = 1; i < word.getKey().length(); i++) {
								wordLocations.remove(in+i);
							}
						}
						queue.add(new Word(word.getKey(), word.getValue().firstSet.size()));
						// Remove references to parts.
						if(val.value == word.getValue().firstSet.size()) {
							words.remove(val.stringValue);
						} else if (words.get(val.stringValue) != null) {
							words.get(val.stringValue).removeAll(word.getValue().firstSet);
						}
					}
				}
			}
		}
		
		// Check if all words can be properly encoded and make word map.
		HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
		int size;
		for(int i = 0; i < bytes.length; i += size+1) {
			size = (i+longestWordLength < bytes.length) ? longestWordLength : (bytes.length - i);
			byte target[];
			do {
				if(size <= 0) {
					target = new byte[] {bytes[i]};
					break;
				}
				target = new byte[size--];
				for(int a = 0; a < target.length; a++) {
					target[a] = bytes[i+a];
				}
			} while(!words.containsKey(new String(target, "ISO-8859-1")));
			wordList.add(target);
			// Use String as wrapper for byte array as arrays can't be hashed.
			if(wordMap.containsKey(new String(target, "ISO-8859-1"))) {
				wordMap.merge(new String(target, "ISO-8859-1"), 1, Integer::sum);
			}else {
				wordMap.put(new String(target, "ISO-8859-1"), 1);
			}
		}
	
		// Convert byte sequence data into sorted array.
		HashMap<String, Word> map = new HashMap<String, Word>();
		for(Entry<String, Integer> e : wordMap.entrySet()) {
			Byte Bytes[] = new Byte[e.getKey().getBytes("ISO-8859-1").length];
			for(int i = 0; i < e.getKey().getBytes("ISO-8859-1").length; i++) {
				Bytes[i] = e.getKey().getBytes("ISO-8859-1")[i];
			}
			Word val = new Word(Bytes, e.getValue());
			int index = 0;
			while(index < data.size() && data.get(index).value < val.value) {
				index++;
			}
			data.add(index, val);
			map.put(e.getKey(), data.get(index));
		}
		
		PriorityQueue<Word> dataQueue = new PriorityQueue<Word>(data);
		
		// Convert sorted array into tree of values. The largest values are at the top.
		Word c[] = new Word[] {dataQueue.peek(), dataQueue.peek()};
		while(dataQueue.size() > 1) {
			int byteCount = 0;
			c = new Word[Word.childCount];
			for(int i = 0; i < c.length; i++) {
				c[i] = dataQueue.poll();
			}
			Word parent = new Word(c, Word.calculateValue(c));
			
			// Save parent and side information for path building.
			for(byte i = 0; i < c.length; i++) {
				c[i].parent = parent;
				c[i].side = i;
				byteCount += (c[i].key == null) ? 0 : c[i].key.length;
			}
			
			dataQueue.add(parent);
			treeSize += byteCount;
		}
		
		top = dataQueue.poll();
		//PrintTree(top);
		
		// Encode tree into byte array using frequency tree.
		byte[] head = WordTreeToBytes(treeSize, top);
		
		ArrayList<Byte> body = new ArrayList<Byte>();
		Byte curByte = 0;
		int index = 0;
		while(!wordList.isEmpty()) {
			// Get the longest possible byte sequence.
			byte target[] = wordList.poll();
			// Encode byte sequence based on it's location in the tree.
			for(byte bit : GetWordPath(map, new String(target, "ISO-8859-1"))) {
				curByte = (byte) (curByte | (bit << (7-(index++))));
				if(index >= 8) {
					body.add(curByte);
					curByte = 0;
					index = 0;
				}
			}
		}
	
		body.add(curByte);
		int fillerBits = 7-index;
		
		// Assemble final output.
		index = 1;
		byte[] out = new byte[index+head.length+body.size()];
		// Store information about how many filler bits exist at the end of the file in first 4 bits.
		out[0] = (byte)((fillerBits) << 5);
		
		for(byte h : head) {
			out[index++] = h;
		}
		for(byte b : body) {
			out[index++] = b;
		}
		
		return out;
	}
	
	static byte[] VerboseHuffmanD(byte[] bytes) throws UnsupportedEncodingException {
		Word top;
		// Mask off first 5 bits to get amount of filler bits at the end of file.
		byte fillerBits = (byte)((bytes[0] & 224) >> 5);
		IntByteArrPair rez = AdaptiveHuffmanD(bytes, true);
		byte flags[] = rez.bytes;
		
		// Reassemble frequency tree.
		Integer cursor = 1 + rez.value;
		Queue<Word> queue = new LinkedList<Word>();
		
		int flagIndex = 0;
		int flag = flags[flagIndex++] + 128;
		Byte word[] = new Byte[flag];
		for(int i = 0; i < flag; i++) {
			word[i] = bytes[cursor++];
		}
		top = new Word(word);
		queue.add(top);
		
		while(!queue.isEmpty()) {
			Word val = queue.poll();
			if(val.key.length == 0) {
				for(int i = 0; i < Word.childCount; i++) {
					flag = flags[flagIndex++] + 128;
					word = new Byte[flag];
					for(int a = 0; a < flag; a++) {
						word[a] = bytes[cursor++];
					}
					val.child[i] = new Word(word);
					if(word.length == 0) {
						queue.add(val.child[i]);
					}
				}
			} 
		}
		
		//PrintTree(top);
		
		// Decode text and save it as byte array.
		int bitCursor = 7;
		ArrayList<Byte> text = new ArrayList<Byte>();
		Word val = top;

		if(top.key.length == 0) {
			while(cursor < bytes.length-1 || (cursor == bytes.length-1 && (bitCursor > fillerBits || !(val.key.length == 0)))) {
				Word child = val.child[(bytes[cursor] >> bitCursor) & 1];
				if(val.key.length == 0 && child != null) {
					val = child;
					if(bitCursor > 0) {
						bitCursor--;
					} else {
						bitCursor = 7;
						cursor++;
					}
				} else {
					for(byte b : val.key) {
						text.add(b);
					}
					val = top;
				}
			}
		} else {
			while(cursor < bytes.length-1 || (cursor == bytes.length-1 && bitCursor > fillerBits)) {
				if(bitCursor > 0) {
					bitCursor--;
				} else {
					bitCursor = 7;
					cursor++;
				}
				for(byte b : val.key) {
					text.add(b);
				}
			}
		}
		byte output[] = new byte[text.size()];
		int index = 0;
		
		for(Byte b : text) {
			output[index++] = b;
		}
		
		return output;
	}
	
	static boolean CompareArrays(Byte[] first, Byte[] second) {
		if(first.length != second.length) {
			return false;
		}
		for(int i = 0; i < first.length; i++) {
			if(first[i] != second[i]) {
				return false;
			}
		}
		return true;
	}
	
	static boolean CompareArrays(byte[] first, byte[] second) {
		if(first.length != second.length) {
			return false;
		}
		for(int i = 0; i < first.length; i++) {
			if(first[i] != second[i]) {
				return false;
			}
		}
		return true;
	}
	
	static byte[] AdaptiveHuffmanC(byte[] bytes) {
		// Helps keep track of what goes where.
		ArrayList<Value> data = new ArrayList<Value>();
		HashMap<Byte, Value> map = new HashMap<Byte, Value>();
		Value top;
		int treeSize = 1;
		// Get character data from input file.
		HashMap<Byte, Integer> words = new HashMap<Byte, Integer>();
		Boolean nullFound = false;
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
		
		// Create frequency tree using byte values used for future size reference.
		// Convert byte data into sorted array.
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
		body.add(curByte);

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
		
		return out;
	}
	
	static IntByteArrPair AdaptiveHuffmanD(byte[] bytes, boolean hasEOF) {
		Value top;
		
		int cursor = (hasEOF) ? 1 : 0;
		// Mask off first 5 bits to get amount of filler bits at the end of file.
		byte fillerBits = (byte)((bytes[cursor++] & 224) >> 5);
		byte nullByte = bytes[cursor++];
		
		// Reassemble frequency tree.
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
					if(hasEOF && val.key == (byte) 255) {
						break;
					}
					text.add(val.key);
					val = top;
				}
			}
		} else {
			while(cursor < bytes.length-1 || (cursor == bytes.length-1 && bitCursor > fillerBits)) {
				if(bitCursor > 0) {
					bitCursor--;
				} else {
					bitCursor = 7;
					cursor++;
				}
				if(hasEOF && val.key == (byte) 255) {
					break;
				}
				text.add(val.key);
			}
		}
		byte output[] = new byte[text.size()];
		int index = 0;
		
		for(Byte b : text) {
			output[index++] = b;
		}
		
		return new IntByteArrPair(cursor, output);
	}
	
	static byte[] ReadFile(String fileName) throws IOException {
		FileInputStream file;
		// Take input file and convert it to byte array.
		file = new FileInputStream(fileName);
		byte out[] = new byte[file.available()];
		file.read(out);
		file.close();
		return out;
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
	
	static byte[] GetWordPath (HashMap<String, Word> map, String target) {
		ArrayList<Byte> rez = new ArrayList<Byte>();
		Word val = map.get(target);
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
	
	// Returns Byte array of tree data with words.
	public static byte[] WordTreeToBytes(int treeSize, Word top) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		ArrayList<Byte> flags = new ArrayList<Byte>();
		int flagIndex = 0;
		ArrayList<Word> layer = new ArrayList<Word>();
		
		flags.add((byte) ((top.key == null) ? -128 : (top.key.length - 128)));
		
		if(top.key != null) {
			for(byte b : top.key) {
				out.add(b);
			}
		}
		
		for(Word child : top.child) {
			if(child != null) {
				layer.add(child);
			}
		}
		while(layer.size() > 0) {
			ArrayList<Word> newLayer = new ArrayList<Word>();
			
			for(Word v : layer) {
				flags.add((byte) -128);
				flagIndex++;
				
				if(v.key != null) {
					flags.set(flagIndex, (byte) (v.key.length - 128));
					for(byte b : v.key) {
						out.add(b);
					}
				}
				
				for(Word child : v.child) {
					if(child != null) {
						newLayer.add(child);
					}
				}
			}
			layer = newLayer;
		}
		flags.add((byte) 255);
		
		int index = 0;
		byte flagA[] = new byte[flags.size()];
		for(byte b : flags) {
			flagA[index++] = b;
		}
		index = 0;
		byte flag[] = AdaptiveHuffmanC(flagA);
		byte output[] = new byte[flag.length + out.size()];
		for (byte b : flag) {
			output[index++] = b;
		}
		for(byte b : out) {
			output[index++] = b;
		}
		
		return output;
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
	public static void PrintTree(Word first) throws UnsupportedEncodingException {
		ArrayList<Word> layer = new ArrayList<Word>();
		
		if(first.key == null || first.key.length == 0) {
			System.out.println("[0]");
		} else {
			/*System.out.print("[");
			for(int i = 0; i < first.key.length; i++) {
				System.out.print(first.key[i]);
				if(i != first.key.length-1) {
					System.out.print(",");
				}
			}
			System.out.println("]");*/
			byte[] temp = new byte[first.key.length];
			int index = 0;
			for(byte b : first.key) {
				temp[index++] = b==10 ? 110 : b;
			}
			System.out.println("[" + new String(temp, "ISO-8859-1") + "]");
		}
		
		for(Word child : first.child) {
			if(child != null) {
				layer.add(child);
			}
		}
		int index = 0;
		while(layer.size() > 0) {
			ArrayList<Word> newLayer = new ArrayList<Word>();
			
			
			for(Word v : layer) {
				if(v.key == null || v.key.length == 0) {
					System.out.print("[0]");
				} else {
					/*System.out.print("[");
					for(int i = 0; i < v.key.length; i++) {
						System.out.print(v.key[i]);
						if(i+1 == v.key.length) {
							System.out.print("]");
						} else {
							System.out.print(",");
						}
					}*/
					byte[] temp = new byte[v.key.length];
					int i = 0;
					for(byte b : v.key) {
						temp[i++] = (b==10) ? 110 : b;
					}
					System.out.print("[" + new String(temp, "ISO-8859-1") + "]");
				}
				
				if(index%2==0) {
					System.out.printf("^");
				} else {
					System.out.printf(" ");
				}
				
				for(Word child : v.child) {
					if(child != null) {
						newLayer.add(child);
					}
				}
				index++;
			}
			
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
					System.out.printf("Value at %d should have been %d, instead was %d\n", i, buf1[i], buf2[i]);
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
	
	static void test(int method) {
		File testRoot = new File("tests/html");
		if(testRoot.isFile()) {
			String fileName = testRoot.getPath();
			String outFileName = "results/" + testRoot.getParentFile().getName() + "/" + testRoot.getName();
			String archiveName = "archives/" + testRoot.getParentFile().getName() + "/" + testRoot.getName();
			long start = System.nanoTime();
			new File(archiveName).delete();
			comp(fileName, archiveName, method); 
			decomp(archiveName, outFileName, method);
			long end = System.nanoTime();
			Double rez = eavluate(fileName, archiveName);
			System.out.println("Time taken: " + (end-start)/1000000000 + "s");
			if(rez == null || !equal(fileName, outFileName)) {
				System.out.println("Test Failed!");
				System.out.println(1/0);
			}
		}
			
		String failed = "";
		double totalAvg = 0;
		int count = 0;
		System.out.println("Test folders found:");
		int index = 1;
		ArrayList<File> testFolders = new ArrayList<File>();
		PriorityQueue<File> queue = new PriorityQueue<File>();
		queue.add(testRoot);
		while (!queue.isEmpty()) {
			File testFile = queue.poll();
			for(File file : testFile.listFiles()) {
				if(file.isDirectory()) {
					queue.add(file);
				} else if (file.isFile()) {
					System.out.printf("%d. %s\n", index++, testFile.getName());
					testFolders.add(testFile);
					break;
				}
			}
		}
		
		System.out.println("-------------------------------------------------------------------");
		long fullTime = System.nanoTime();
		for (File testGroup : testFolders) {
			System.out.printf("[Doing tests from %s]\n", testGroup.getName());
			double avg = 0;
			for(File t : testGroup.listFiles()) {
				String fileName = t.getPath();
				String outFileName = "results/" + t.getParentFile().getName() + "/" + t.getName();
				String archiveName = "archives/" + t.getParentFile().getName() + "/" + t.getName();
				long start = System.nanoTime();
				new File(archiveName).delete();
				System.out.println("Archiving " + fileName + "...");
				comp(fileName, archiveName, method); 
				long end = System.nanoTime();
				decomp(archiveName, outFileName, method);
				Double rez = eavluate(fileName, archiveName);
				System.out.println("Total encoding time: " + (end-start)/1000000 + "ms");
				System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
				if(rez != null && equal(fileName, outFileName)) {
					avg += rez;
					totalAvg += rez;
					count++;
				} else {
					System.out.println("Test Failed!");
					System.out.println(1/0);
					failed += t.getPath() + "\n";
				}
			}
			System.out.printf("Average score: %.1f%%\n", avg/testGroup.listFiles().length);
			System.out.println("-------------------------------------------------------------------");
	    }
		System.out.println("Total time taken: " + (System.nanoTime()-fullTime)/1000000000 + "s");
		if(count > 0) {
			System.out.printf("Total average score: %.1f%%\n", totalAvg/count);
		}
		System.out.println();
		if(!failed.equals("")) {
			System.out.printf("Tests failed: \n%s\n", failed);
		} else {
			System.out.printf("All test completed successfully!");
		}
	}
	
	public static void about() {
		System.out.println("211RDB204 Juris Ozoliņš");
		System.out.println("211RDB276 Toms Zvirbulis");
	}
}
