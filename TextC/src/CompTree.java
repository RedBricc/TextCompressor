import java.util.PriorityQueue;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;


// Functions needed to encode text files to byte array.
public class CompTree {
	// Helps keep track of what goes where.
	public ArrayList<Value> data = new ArrayList<Value>();
	public HashMap<Byte, Value> map = new HashMap<Byte, Value>();
	public Value top;
	public int treeSize = 1;
	
	public CompTree(Set<Entry<Byte, Integer>> set, byte nullByte) {
		// Convert character data into sorted array.
		for(Entry<Byte, Integer> e : set) {
			Value val = new Value(e.getKey(), e.getValue());
			if(val.key == 0) {
				val.key = nullByte;
			}
			int index = 0;
			while(index < data.size() && data.get(index).value < val.value) {
				index++;
			}
			data.add(index, val);
			map.put(val.key, data.get(index));
		}
		
		PriorityQueue<Value> dataQueue = new PriorityQueue<Value>(data);
		
		// Convert sorted array into tree of values. The largest values are at the top.
		while(dataQueue.size() > 1) {
			Value c[] = new Value[Value.childCount];
			for(int i = 0; i < c.length; i++) {
				c[i] = dataQueue.poll();
			}
			Value parent = new Value(c, Value.childSum(c));
			
			// Save parent information for traversal as well as side for path building later.
			for(byte i = 0; i < c.length; i++) {
				c[i].parent = parent;
				c[i].side = i;
			}
			
			dataQueue.add(parent);
			treeSize+=Value.childCount;
		}
		
		top = dataQueue.poll();
	}
	
	// Returns byte array of encoded data with header.
	public byte[] Compile(String text, byte nullByte) {
		byte[] head = TreeToString();
		
		ArrayList<Byte> body = new ArrayList<Byte>();
		Byte curByte = 0;
		int index = 0;
		for(byte by : text.getBytes(StandardCharsets.UTF_8)) {
			for(byte bit : GetPath((by == 0) ? nullByte : by)) {
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
		byte[] out = new byte[3+head.length+body.size()];
		// Store information about how many filler bits exist at the end of the file as well as what compression iteration level was used
		// and what character codes for null.
		out[0] = (byte)(8-index);
		out[1] = (byte)(0);
		out[2] = nullByte;
		
		// Assemble final output.
		index = 3;
		for(byte h : head) {
			out[index++] = h;
		}
		for(byte b : body) {
			out[index++] = b;
		}
		
		return out;
	}
	
	byte[] GetPath (Byte target) {
		ArrayList<Byte> rez = new ArrayList<Byte>();
		Value val = map.get(target);
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
	
	// Returns String of tree data. If value key is null, save it as the null symbol.
	// TODO: implement word functionality!
	public byte[] TreeToString() {
		byte[] out = new byte[treeSize];
		int index = 0;
		ArrayList<Value> layer = new ArrayList<Value>();
		
		if(top.key == null) {
			out[index++] = 0;
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
					out[index++] = 0;
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
	public void PrintData() {
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
}
