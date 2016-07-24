import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class Test {

	public static void main(String[] args) throws Exception {

		testCustomSet();
		testHashSet();
		testCustomSet();
		testHashSet();
	}
	private static void testCustomSet() throws Exception {
		StringIntMap map = new StringIntMap(1000000, 10);
		int size = 100000;
		long start = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			String s = "test" + i;
			int index = map.indexOf(s);

			if (index != i+1) { 
				throw new Exception ("Expected index to be " + i);
			}
		}
		for (int i = 0; i < size; i++) {
			String s = "test" + i;
			int index = map.indexOf(s);

			if (index != i+1) { 
				throw new Exception ("Expected index to be " + i + " but got " + index);
			}
			
//			String storedString = map.getString(i);

//			if (!s.equals(storedString)) { 
//				throw new Exception ("Expected " + s + " but got " + storedString);				
//			}
		}
		long end = System.currentTimeMillis();
		System.out.println("StrngIntMap took " + (end-start));

		
		
	}
	private static void testHashSet() throws Exception {
		HashSet map = new HashSet(1000000);
		int size = 100000;
		long start = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			String s = "test" + i;
			map.add(s);			

		}
		for (int i = 0; i < size; i++) {
			String s = "test" + i;

			if (!map.contains(s)) { 
				throw new Exception ("Expected " + s + " but got nothing");				
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("HashSet took " + (end-start));

		
		
	}
	
	public static void main1(String[] args) {
		DB db = DBMaker.memoryDB().make();
		int size = 100000;

		Map<String,Integer> hashMap = new HashMap<>();
		Map<String,Integer> mapDB = (Map<String,Integer>)db.hashMap("map").make();
		
		testMapWrite(hashMap, size, "hashMap");
		testMapRead(hashMap, size, "hashMap");
		testMapWrite(mapDB, size, "MapDB");
		testMapRead(mapDB, size, "MapDB");

		hashMap.clear();
		mapDB.clear();
		
		testMapWrite(hashMap, size, "hashMap");
		testMapRead(hashMap, size, "hashMap");
		testMapWrite(mapDB, size, "MapDB");
		testMapRead(mapDB, size, "MapDB");

	}

	private static void testMapWrite(Map<String,Integer> map, int size, String mapType) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			map.put("something" + i, i);
		}
		long end = System.currentTimeMillis();
		System.out.println("Time taken for put " + mapType + " = " + (end-start) + " mills");
	}
	private static void testMapRead(Map<String,Integer> map, int size, String mapType) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			int value = map.get("something" + i);
		}
		long end = System.currentTimeMillis();
		System.out.println("Time taken for get " + mapType + " = " + (end-start) + " mills");
	}
}
