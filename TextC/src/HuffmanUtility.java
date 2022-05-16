import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

class HuffmanUtility {
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
			System.out.print("[");
			for(int i = 0; i < first.key.length; i++) {
				System.out.print(first.key[i]);
				if(i != first.key.length-1) {
					System.out.print(",");
				}
			}
			System.out.println("]");
			/*byte[] temp = new byte[first.key.length];
			int index = 0;
			for(byte b : first.key) {
				temp[index++] = b==10 ? 110 : b;
			}
			System.out.println("[" + new String(temp, "ISO-8859-1") + "]");*/
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
					System.out.print("[");
					for(int i = 0; i < v.key.length; i++) {
						System.out.print(v.key[i]);
						if(i+1 == v.key.length) {
							System.out.print("]");
						} else {
							System.out.print(",");
						}
					}/*
					byte[] temp = new byte[v.key.length];
					int i = 0;
					for(byte b : v.key) {
						temp[i++] = (b==10) ? 110 : b;
					}
					System.out.print("[" + new String(temp, "ISO-8859-1") + "]");*/
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
}