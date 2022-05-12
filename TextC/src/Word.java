
// Stores key and value pair with node functionality for implementing a verbose binary tree.
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