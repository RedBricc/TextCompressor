
// Stores key and value pair with node functionality for implementing a binary tree.
public class Value implements Comparable<Value> {
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