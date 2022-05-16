
// Stores an integer value paired with byte array.
class IntByteArrPair implements Comparable<IntByteArrPair> {
	Integer value;
	byte bytes[];
	
	public IntByteArrPair (int v, byte b[]) {
		value = v;
		bytes = b;
	}

	@Override
	public int compareTo(IntByteArrPair val) {
		return value.compareTo(val.value);
	}
}