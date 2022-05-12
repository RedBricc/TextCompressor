import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Map.Entry;

class Huffman {
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
		byte[] head = new byte[treeSize];
		int index = 0;
		ArrayList<Value> layer = new ArrayList<Value>();
		
		if(top.key == null) {
			head[index++] = nullByte;
		}else {
			head[index++] = top.key;
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
					head[index++] = nullByte;
				}else {
					head[index++] = v.key;
				}
				
				for(Value child : v.child) {
					if(child != null) {
						newLayer.add(child);
					}
				}
			}
			layer = newLayer;
		}
		
		ArrayList<Byte> body = new ArrayList<Byte>();
		Byte curByte = 0;
		index = 0;
		for(byte by : bytes) {
			for(byte bit : HuffmanUtility.GetPath(map, by)) {
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
	
	static byte[] VerboseHuffmanC(byte[] bytes) throws UnsupportedEncodingException {
		// Helps keep track of what goes where.
		ArrayList<Word> data = new ArrayList<Word>();
		Word top;
		// A word here is defined as a sequence of bytes.
		int longestWordLength = 1;
		
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
			c = new Word[Word.childCount];
			for(int i = 0; i < c.length; i++) {
				c[i] = dataQueue.poll();
			}
			Word parent = new Word(c, Word.calculateValue(c));
			
			// Save parent and side information for path building.
			for(byte i = 0; i < c.length; i++) {
				c[i].parent = parent;
				c[i].side = i;
			}
			
			dataQueue.add(parent);
		}
		
		top = dataQueue.poll();
		//PrintTree(top);
		
		// Encode tree into byte array using frequency tree.
		ArrayList<Byte> headContent = new ArrayList<Byte>();
		ArrayList<Byte> flags = new ArrayList<Byte>();
		int flagIndex = 0;
		ArrayList<Word> layer = new ArrayList<Word>();
		
		flags.add((byte) ((top.key == null) ? -128 : (top.key.length - 128)));
		
		if(top.key != null) {
			for(byte b : top.key) {
				headContent.add(b);
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
						headContent.add(b);
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
		byte head[] = new byte[flag.length + headContent.size()];
		for (byte b : flag) {
			head[index++] = b;
		}
		for(byte b : headContent) {
			head[index++] = b;
		}
		
		ArrayList<Byte> body = new ArrayList<Byte>();
		Byte curByte = 0;
		index = 0;
		while(!wordList.isEmpty()) {
			// Get the longest possible byte sequence.
			byte target[] = wordList.poll();
			// Encode byte sequence based on it's location in the tree.
			for(byte bit : HuffmanUtility.GetWordPath(map, new String(target, "ISO-8859-1"))) {
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
}