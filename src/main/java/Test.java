import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class Test {

	public static void main(String[] args) throws Exception {

		int numWords = 500000;
		int wordSize = 20;
		testTimes(numWords, wordSize);
//		testSizeCustom(numWords, wordSize);
//		testSizeHashMap(numWords, wordSize);
		
		
	}
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	private static void testSizeCustom(int size, int wordSize) throws Exception{		

		StringIntMap map = new StringIntMap(size, wordSize);

		for (int i = 0; i < size; i++) {
			int index = map.indexOf(padRight(""+i, wordSize));
		}
		
		boolean finished = false;
		while (!finished) 
			Thread.sleep(1000);
		
		System.out.println(map.getMaxDepth());
	}
	private static void testSizeHashMap(int size, int wordSize) throws Exception{		

		HashSet<String> map = new HashSet(size);

		for (int i = 0; i < size; i++) {
			map.add(padRight(""+i, wordSize));
		}
		
		boolean finished = false;
		while (!finished) 
			Thread.sleep(1000);
		
		System.out.println(map.size());
	}

	private static void testTimes(int size, int wordSize) throws Exception{		
		String[] data = buildData(size, wordSize);
		String[] dataCopy = buildData(size, wordSize);
		System.out.println("Built test data");
		testHashSet(data, dataCopy);
		testCustomSet(data, dataCopy, wordSize);
		testHashSet(data, dataCopy);
		testCustomSet(data, dataCopy, wordSize);
	}

	private static String[] buildData(int size, int wordSize) { 
		String[] data = new String[size];
		for (int i = 0; i < size; i++) {
			String s = padRight(""+i, wordSize);
			data[i] = s;
		}
		return data;
	}
	private static void testCustomSet(String[] data, String[] dataCopy, int wordSize) throws Exception {
		StringIntMap map = new StringIntMap(data.length, wordSize);
		long start = System.nanoTime();
		for (int i = 0; i < data.length; i++) {
			int index = map.indexOf(data[i]);

			if (index != i+1) { 
				throw new Exception ("Expected index to be " + i);
			}
		}
		long endInsert = System.nanoTime();
		for (int i = 0; i < data.length; i++) {
			int index = map.indexOf(dataCopy[i]);

			if (index != i+1) { 
				throw new Exception ("Expected index to be " + i + " but got " + index);
			}
		}
		long endRead = System.nanoTime();
		System.out.println("StringIntMap \ttook insert:" + (endInsert-start)/data.length + " read: " + (endRead-endInsert)/data.length + ". maxDepth = " + map.getMaxDepth());
	}
	private static void testHashSet(String[] data, String[] dataCopy) throws Exception {
		HashSet map = new HashSet(data.length);
		long start = System.nanoTime();
		for (int i = 0; i < data.length; i++) {
			map.add(data[i]);			

		}
		long endInsert = System.nanoTime();
		for (int i = 0; i < data.length; i++) {

			if (!map.contains(dataCopy[i])) { 
				throw new Exception ("Expected " + dataCopy[i] + " but got nothing");				
			}
		}
		long endRead = System.nanoTime();
		System.out.println("HashSet \ttook insert:" + (endInsert-start)/data.length + " read: " + (endRead-endInsert)/data.length);

		
		
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
