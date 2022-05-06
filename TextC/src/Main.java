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

public class Main {
	public static void main(String[] args) {
		String choiseStr;
		String fileName, outFileName, firstFile, secondFile;
		Scanner sc = new Scanner(System.in);
		
		test(3);
		
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
		switch(method) {
			case 1:
				System.out.println("Deflate algorithm isn't implemented yet!");
				break;
			case 2:
				AdaptiveHuffmanC(inFileName, outFileName);
				break;
			case 3:
				try {
					VerboseHuffmanC(inFileName, outFileName);
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
	}
	
	public static void decomp(String inFileName, String outFileName, int method) {
		switch(method) {
			case 1:
				System.out.println("Deflate algorithm isn't implemented yet!");
				break;
			case 2:
				AdaptiveHuffmanD(inFileName, outFileName);
				break;
			case 3:
				try {
					VerboseHuffmanD(inFileName, outFileName);
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
	}
	
	static void VerboseHuffmanC(String inFileName, String outFileName) throws UnsupportedEncodingException {
		// Helps keep track of what goes where.
		ArrayList<Word> data = new ArrayList<Word>();
		Word top;
		// A word here is defined as a sequence of bytes.
		int longestWordLength = 1;
		int treeSize = 1;
		// Get character data from input file.
		Boolean nullFound = false;
		byte[] bytes;
		try {
			bytes = ReadFile(inFileName);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		byte[] nullBytes = new byte[] {0};
		
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
				if(b == 0) {
					nullFound = true;
				}
			}
			if(posArray.size() > max) {
				max = posArray.size();
			} else if (posArray.size() < min) {
				min = posArray.size();
			}
			posArray.add(bytePos++);
			words.put(key, posArray);
		}
		System.out.println("Unique characters: " + words.size());
		// Fill priority queue. This will be used to iterate through the word map.
		PriorityQueue<Word> queue = new PriorityQueue<Word>();
		for(Entry<String, HashSet<Integer>> val : words.entrySet()) {
			Word word = new Word(val.getKey(), val.getValue().size());
			word.reverse = true;
			queue.add(word);
		}
		
		// Iterate through word map and merge recurring byte sequences.
		int colabGain2 = 0, colabGain3 = 0, colabGain5 = 0;
		int flagCount = (int) Math.ceil(bytes.length/8);
		long look = 0, check1 = 0, check2 = 0, check3 = 0, start = System.nanoTime();
		while(!queue.isEmpty()) {
			Word val = queue.poll();
			//long start = System.nanoTime();
			// Create hash map of all possible words that could be created by appending the next word.
			HashMap<String, HashSetPair> nextWords = new HashMap<String, HashSetPair>();
			if (words.get(val.stringValue) == null)
				continue;
			for(int index : words.get(val.stringValue)) {
				start = System.nanoTime();
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
				look += System.nanoTime()-start;
			}
			
			// Iterate through words and append any worthwhile words.
			for(Entry<String, HashSetPair> word : nextWords.entrySet()) {
				if(words.get(val.stringValue) == null)
					continue;
				int count = word.getValue().firstSet.size();
				// It is never efficient to save a word of length 1.
				if(count < 2) 
					continue;
				int length = word.getKey().length();
				int newLWL = (length > longestWordLength) ? length : longestWordLength;
				
				if(words.containsKey(word.getKey())) {
					// word already exists, so append the new indexes to it's index array.
					HashSet<Integer> newI = word.getValue().firstSet;
					words.put(word.getKey(), newI);
				} else {
					// Keep track of how much space could be saved by adding the word as well as the cost of doing so.
					int gain = 0;
					// Word has to be saved in full length inside header.
					Double cost = (double) length;
					
					// Amount of bytes compressed
					gain += ((1 / (1 + Math.log(word.getKey().length() - val.stringValue.length()) / Math.log(2))) * word.getValue().secondSet.size() * (1+(Math.log(bytes.length+flagCount) / Math.log(2))*(Math.log(1+((word.getValue().secondSet.size()-min < 0) ? 0 : ((max-word.getValue().secondSet.size() < 0) ? 1 : (((word.getValue().secondSet.size()-min)/max))))*10) / Math.log(2))*0.477))
							+ ((1 / (1 + Math.log(val.stringValue.length()) / Math.log(2))) * word.getValue().firstSet.size() * (1+(Math.log(bytes.length+flagCount) / Math.log(2))*(Math.log(1+((word.getValue().firstSet.size()-min < 0) ? 0 : ((max-word.getValue().firstSet.size() < 0) ? 1 : (((word.getValue().firstSet.size()-min)/max))))*10) / Math.log(2))*0.477));
					// Approximation of how much each encoded word takes up.
					// 1 / LOG(newLWL;2) * COUNT * (1+LOG(TOTAL;2)*LOG(1+VAL*10;2)*0,477)
					// TODO: find approximation without the value TOTAL.
					// Where VAL is COUNT normalized to between 0 and 1.
					cost += (1 / (1 + Math.log(newLWL) / Math.log(2))) * count * (1+(Math.log(bytes.length+flagCount) / Math.log(2))*(Math.log(1+((count-min < 0) ? 0 : ((max-count < 0) ? 1 : (((count-min)/max))))*10) / Math.log(2))*0.477);
					// Adding words adds one extra null byte to the tree.
					if(count < words.get(val.stringValue).size()) {
						cost += 1 / (1 + Math.log(newLWL) / Math.log(2));
					}
					// If maxWordLength increases past certain multiples of two, two times more flag bytes are needed.
					boolean newStep = Math.floor(1 + Math.log(newLWL) / Math.log(2)) > Math.floor(1 + Math.log(longestWordLength) / Math.log(2));
					int newBitsPerWord = (int) (1 + Math.log(newLWL) / Math.log(2));
					if(newStep) {
						if (newBitsPerWord >= 5) {
							cost += flagCount<<1;
							gain += colabGain5;
							colabGain5 += gain;
						} else if (newBitsPerWord >= 3) {
							cost += flagCount<<1;
							gain += colabGain3;
							colabGain3 += gain;
						} else if (newBitsPerWord >= 2) {
							cost += flagCount<<1;
							gain += colabGain2;
							colabGain2 += gain;
						}
					}
					//iSystem.out.printf("%d vs %f\n", gain, cost);
					
					// Add word if it is worth it.
					if(gain > cost) {
						if(newLWL > 255)
							continue;
						else if (newLWL > longestWordLength) {
							if(newStep) {
								if (newBitsPerWord == 2) {
									flagCount = flagCount << 1;
								} else if (newBitsPerWord == 3) {
									flagCount = flagCount << 1;
								} else if (newBitsPerWord == 5) {
									flagCount = flagCount << 1;
								}
							}
							longestWordLength = newLWL;
						}
						words.put(word.getKey(), word.getValue().firstSet);
						// Remove second part.
						start = System.nanoTime();
						String secondPart = word.getKey().substring(val.stringValue.length());
						if(words.get(secondPart).size() == word.getValue().firstSet.size()) {
							words.remove(secondPart);
						} else {
							words.get(secondPart).removeAll(word.getValue().secondSet);
						}
						check1 += System.nanoTime()-start;
						start = System.nanoTime();
						// Update start locations.
						for(int in : word.getValue().firstSet) {
							wordLocations.put(in, word.getKey());
							for(int i = 1; i < word.getKey().length(); i++) {
								wordLocations.remove(in+i);
							}
						}
						queue.add(new Word(word.getKey(), word.getValue().firstSet.size()));
						check2 += System.nanoTime()-start;
						start = System.nanoTime();
						// Remove references to parts.
						if(val.value == word.getValue().firstSet.size()) {
							words.remove(val.stringValue);
						} else {
							words.get(val.stringValue).removeAll(word.getValue().firstSet);
						}
						check3 += System.nanoTime()-start;
					}
					
					//System.out.println("     Choosing took: " + (System.nanoTime()-start) + "ns");
				}
			}
		}
		System.out.println("Looking took: " + look/1000000000 + "s");
		System.out.println("Second part update took: " + check1/1000000000 + "s");
		System.out.println("Start location update took: " + check2/1000000000 + "s");
		System.out.println("First part update took: " + check3/1000000000 + "s");
		
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
			// Use String as wrapper for byte array as arrays can't be hashed.
			if(wordMap.containsKey(new String(target, "ISO-8859-1"))) {
				wordMap.merge(new String(target, "ISO-8859-1"), 1, Integer::sum);
			}else {
				wordMap.put(new String(target, "ISO-8859-1"), 1);
			}
		}
		
		// If necessary, find a free byte in the text that will be used to represent null.
		int charCount = 1;
		boolean full = nullFound;
		while(full && charCount < 17) {
			byte[] test = new byte[charCount];
			// Test all possible values to find the smallest free one.
			for(int j = 0; j < charCount; j++) {
				for(int i = 0; i < 256; i++) {
					test[j] = (byte) i;
							
					if(!wordMap.containsKey(new String(test, "ISO-8859-1"))) {
						nullBytes = test;
						full = false;
						break;
					}
				}
			}
			charCount++;
		}
		
		if(full) {
			System.out.println("Could not find suitable byte to replace null!");
			return;
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
				byteCount += (c[i].key == null) ? 1 : c[i].key.length;
			}
			
			dataQueue.add(parent);
			treeSize += byteCount;
		}
		
		top = dataQueue.poll();
		//PrintTree(top, new Byte[] {0});
		
		// Encode tree into byte array using frequency tree.
		int bitsPerWord = (int) (1 + Math.log(longestWordLength) / Math.log(2));
		byte[] head = WordTreeToBytes(treeSize, top, nullBytes, bitsPerWord);
		
		ArrayList<Byte> body = new ArrayList<Byte>();
		Byte curByte = 0;
		int index = 0;
		size = (longestWordLength < bytes.length) ? longestWordLength : (bytes.length);
		for(int i = 0; i < bytes.length;i+=size+1) {
			// Find the longest possible byte sequence.
			size = (i+longestWordLength < bytes.length) ? longestWordLength : (bytes.length - i);
			byte target[];
			do {
				target = new byte[size--];
				for(int a = 0; a < target.length; a++) {
					target[a] = bytes[i+a];
				}
				if(size < 0) {
					System.out.printf("Could not find value [%s] in tree at byte: %d [%d]\n", new String(new byte[] {bytes[i]}, "ISO-8859-1"), i, bytes[i]);
					break;
				}
			} while(!map.containsKey(new String(target, "ISO-8859-1")));
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
		index = 2 + nullBytes.length;
		byte[] out = new byte[index+head.length+body.size()];
		// Store information about how many filler bits exist at the end of the file in first 4 bits.
		out[0] = (byte)((fillerBits) << 5);
		// Store how many bytes represent null in the remaining bits.
		out[0] = (byte) (out[0] | (nullBytes.length-1));
		// Store now many bits will be used to represent word length.
		out[1] = (byte) (bitsPerWord-1);
		// Store nullbytes in third byte and onwards.
		for(int i = 0; i < nullBytes.length; i++) {
			out[i+2] = nullBytes[i];
		}
		
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
	
	static void VerboseHuffmanD(String inFileName, String outFileName) throws UnsupportedEncodingException {
		byte[] bytes;
		try {
			bytes = ReadFile(inFileName);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		Word top;
		//System.out.println("First byte: " + bytes[0]);
		// Mask off first 5 bits to get amount of filler bits at the end of file.
		byte fillerBits = (byte)((bytes[0] & 224) >> 5);
		// Get null byte information.
		Byte nullBytes[] = new Byte[(bytes[0] & 15)+1];
		// Get how many bits represent one byte sequence length in size flag.
		int bitsPerWord = bytes[1]+1;
		// Get how many bits represent the length of a word.
		for(int i = 0; i < nullBytes.length; i++) {
			nullBytes[i] = bytes[2+i];
		}
		
		// Reassemble frequency tree.
		int cursor = 2+nullBytes.length;
		Queue<Word> queue = new LinkedList<Word>();
		
		int bitCursor = 8-bitsPerWord;
		int bitMask = (2 << (bitsPerWord-1))-1;
		int flagIndex = cursor++;
		byte flag = bytes[flagIndex];
		int wordSize = (flag >> bitCursor)+1;
		Byte word[] = new Byte[wordSize];
		for(int i = 0; i < wordSize; i++) {
			word[i] = bytes[cursor++];
		}
		top = new Word(word);
		queue.add(top);
		
		while(!queue.isEmpty()) {
			Word val = queue.poll();
			if(CompareArrays(val.key, nullBytes)) {
				for(int i = 0; i < Word.childCount; i++) {
					bitCursor-=bitsPerWord;
					if(bitCursor < 0) {
						bitCursor = 8-bitsPerWord;
						flagIndex = cursor++;
						flag = bytes[flagIndex];
					}
					wordSize = (((flag >> bitCursor)) & bitMask)+1;
					word = new Byte[wordSize];
					for(int s = 0; s < wordSize; s++) {
						word[s] = bytes[cursor++];
					}
					val.child[i] = new Word(word);
					if(CompareArrays(word, nullBytes)) {
						queue.add(val.child[i]);
					}
				}
			} 
		}
		
		//PrintTree(top, nullBytes);
		
		// Decode text and save it as byte array.
		bitCursor = 7;
		ArrayList<Byte> text = new ArrayList<Byte>();
		Word val = top;

		if(CompareArrays(top.key, nullBytes)) {
			while(cursor < bytes.length-1 || (cursor == bytes.length-1 && (bitCursor > fillerBits || !CompareArrays(val.key, nullBytes)))) {
				Word child = val.child[(bytes[cursor] >> bitCursor) & 1];
				if(CompareArrays(val.key, nullBytes) && child != null) {
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
	
	static void AdaptiveHuffmanC(String inFileName, String outFileName) {
		// Helps keep track of what goes where.
		ArrayList<Value> data = new ArrayList<Value>();
		HashMap<Byte, Value> map = new HashMap<Byte, Value>();
		Value top;
		int treeSize = 1;
		// Get character data from input file.
		HashMap<Byte, Integer> words = new HashMap<Byte, Integer>();
		Boolean nullFound = false;
		byte[] bytes;
		try {
			bytes = ReadFile(inFileName);
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
	
	static void AdaptiveHuffmanD(String inFileName, String outFileName) {
		byte[] bytes;
		try {
			bytes = ReadFile(inFileName);
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
	public static byte[] WordTreeToBytes(int treeSize, Word top, byte nullByte[], int bitsPerWord) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		int freeBits = 8-bitsPerWord;
		int flagIndex = 0;
		ArrayList<Word> layer = new ArrayList<Word>();
		
		out.add((byte) ((top.key == null) ? ((nullByte.length-1) << freeBits) : ((top.key.length-1) << freeBits)));
		
		if(top.key == null) {
			for(byte b : nullByte) {
				out.add(b);
			}
		}else {
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
				if(v.key == null) {
					if(freeBits < bitsPerWord) {
						freeBits = 8-bitsPerWord;
						flagIndex = out.size();
						out.add((byte) 0);
					} else {
						freeBits -= bitsPerWord;
					}
					out.set(flagIndex, (byte) (out.get(flagIndex) | ((nullByte.length-1) << freeBits)));
					for(byte b : nullByte) {
						out.add(b);
					}
				}else {
					if(freeBits < bitsPerWord) {
						freeBits = 8-bitsPerWord;
						flagIndex = out.size();
						out.add((byte) 0);
					} else {
						freeBits -= bitsPerWord;
					}
					out.set(flagIndex, (byte) (out.get(flagIndex) | ((v.key.length-1) << freeBits)));
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
		
		byte output[] = new byte[out.size()];
		int index = 0;
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
	public static void PrintTree(Word first, Byte[] nullBytes) throws UnsupportedEncodingException {
		ArrayList<Word> layer = new ArrayList<Word>();
		
		if(first.key == null || CompareArrays(first.key, nullBytes)) {
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
				if(v.key == null || CompareArrays(v.key, nullBytes)) {
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
		File testRoot = new File("tests");
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
				System.out.println("Total encding time: " + (end-start)/1000000000 + "s");
				Double rez = eavluate(fileName, archiveName);
				System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - -");
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
			System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - -");
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
	}
	
	public static void about() {
		System.out.println("211RDB204 Juris Ozoliņš");
		System.out.println("211RDB276 Toms Zvirbulis");
	}
}
