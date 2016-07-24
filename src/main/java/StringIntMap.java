
public class StringIntMap {

	private static final int LENGTH = 1;
	private static final int START_CHARS = 2;
	private static final int NEXT = 3;

	private int maxDepth = 0;
	
	private int[] hashTable;
	
	private int[] stringTable;
	
	private char[] charArray;
	
	
	private int numStrings = 1;
	private int numChars = 0;
	private int hashMask;
	private int numHashBits;
	
	
	public StringIntMap(int capacity, int wordSize) {
		
		numHashBits = (int)(Math.log(capacity) / Math.log(2)) + 1;
		int tableSize = (int)Math.pow(2, numHashBits);
		hashMask = tableSize-1;
		hashTable = new int[tableSize];
		stringTable = new int[(4+2)*capacity];
		charArray = new char[wordSize*capacity * 3 / 2];
	}
	
	public int getMaxDepth() {
		return maxDepth;
	}
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

	public int indexOf(String s) {

		int hashCode = hash(s);
		int hashIndex = hashCode & hashMask;
		
		int startListIndex = hashTable[hashIndex];
		int length = s.length();
		
		if (startListIndex == 0) {
		
			// insert new string
			return insertString(s, hashCode, hashIndex);

		} else {
			//int depth = 0;
			while (startListIndex != 0) {

				int offset = startListIndex * 4;
				if (hashCode == stringTable[offset] && length == stringTable[offset+LENGTH] && equals(s, stringTable[offset+START_CHARS])) {
//					maxDepth = Math.max(maxDepth,  depth);
					return startListIndex;
				}
				
				startListIndex = stringTable[offset+NEXT];
	//			depth++;

			}
			
		//		maxDepth = Math.max(maxDepth,  depth);
			return insertString(s, hashCode, hashIndex);

		}
		
	}
	
	private synchronized int insertString(String s, int hashcode, int hashIndex) {

//		ensureCapacity(s);

		int stringListIndex = hashTable[hashIndex];

		if (stringListIndex == 0) {			

			hashTable[hashIndex] = addStringToTable(s, hashcode);
			return hashTable[hashIndex];		
		}
		else {
			// Locate the end (exiting early if we find the string we are inserting)
			while (true) {
				
				// do we have a match ?
				int offset = stringListIndex * 4;
				if (hashcode == stringTable[offset] && s.length() == stringTable[offset+LENGTH] && 
						equals(s, stringTable[offset+START_CHARS])) {
					return stringListIndex;
				}
				// Are we at the end?
				stringListIndex = stringTable[offset+NEXT];
				if (stringListIndex == 0) {
					stringTable[offset+NEXT] = addStringToTable(s, hashcode);
					return stringTable[offset+NEXT];		
				} 
			}			
		}
	}

	private int addStringToTable(String s, int hashcode) {
		int stringListIndex;
		stringListIndex = numStrings++;

		int offset = stringListIndex * 4	;
		stringTable[offset] = hashcode;
		stringTable[offset+LENGTH] = s.length();
		stringTable[offset+START_CHARS] = numChars;

		for (int i = 0; i < s.length(); i++) {
			charArray[numChars++] = s.charAt(i);
		}
		return stringListIndex;
	}

	private void ensureCapacity(String s) {
		// TODO Auto-generated method stub
		
	}

	public String getString(int index) {

		int offset = index << 2;
		
		return new String(charArray, stringTable[offset+START_CHARS], stringTable[offset+LENGTH]);
	}
	
	public boolean equals(String s, int offset) {
		
		for (int i = 0; i < s.length(); i++) {
			if (charArray[offset++] != s.charAt(i)) {
				return false;
			};
		}
		return true;
		
	}

	public int compareFragment(String s, int chIndex, int stepIndex) {
		// TODO Auto-generated method stub
		return 0;
	}
}
