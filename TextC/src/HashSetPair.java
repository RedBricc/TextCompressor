import java.util.HashSet;

// Stores two paired hash sets, used for optimizing word based encoding.
class HashSetPair {
	HashSet<Integer> firstSet;
	HashSet<Integer> secondSet;
	
	public HashSetPair(HashSet<Integer> first, HashSet<Integer> second) {
		firstSet = first;
		secondSet = second;
	}
}