// 211RDB204 Juris Ozoliņš 5
// 211RDB276 Toms Zvirbulis 3

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		String choiseStr;
		String fileName, outFileName, firstFile, secondFile;
		Scanner sc = new Scanner(System.in);
		
		loop: while (true) {
			choiseStr = sc.next();
								
			switch (choiseStr) {
			case "comp":
				System.out.print("source file name: ");
				fileName = sc.next();
				System.out.print("archive name: ");
				outFileName = sc.next();
				try {
					Deflate.comp(fileName, outFileName);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;
			case "decomp":
				System.out.print("archive name: ");
				fileName = sc.next();
				System.out.print("file name: ");
				outFileName = sc.next();
				try {
					Deflate.decomp(fileName, outFileName);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;
			case "size":
				System.out.print("file name: ");
				fileName = sc.next();
				size(fileName);
				break;
			case "evaluate":
				System.out.print("first file name: ");
				firstFile = sc.next();
				System.out.print("second file name: ");
				secondFile = sc.next();
				eavluate(firstFile, secondFile);
				break;
			case "equal":
				System.out.print("first file name: ");
				firstFile = sc.next();
				System.out.print("second file name: ");
				secondFile = sc.next();
				System.out.println(equal(firstFile, secondFile));
				break;
			case "about":
				about();
				break;
			case "test":
				test();
				break;
			case "-e":
			case "e":
			case "exit":
				sc.close();
				break loop;
			}
		}
	}

	public static Double eavluate(String firstFile, String secondFile) {
		Double first, second;
		try {
			FileInputStream f = new FileInputStream(firstFile);
			first = (double) f.available();
			f.close();
			f = new FileInputStream(secondFile);
			second = (double) f.available();
			f.close();
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
			return -1.0;
		}
		System.out.printf("%s to %s saves %.1f%%\n", firstFile, secondFile, ((1-second/first)*100));
		return (1-second/first)*100;
	}
	
	public static void size(String fileName) {
		try {
			FileInputStream f = new FileInputStream(fileName);
			System.out.println("size: " + f.available());
			f.close();
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		
	}
	
	public static boolean equal(String firstFile, String secondFile) {
		try {
			FileInputStream f1 = new FileInputStream(firstFile);
			FileInputStream f2 = new FileInputStream(secondFile);
			int k1, k2;
			byte[] buf1 = new byte[f1.available()];
			byte[] buf2 = new byte[f2.available()];
			k1 = f1.read(buf1);
			k2 = f2.read(buf2);
			if (k1 != k2) {
				f1.close();
				f2.close();
				System.out.printf("Expected size was %d, actual size is %d\n", k1, k2);
				return false;
			}
			for (int i=0; i<k1; i++) {
				if (buf1[i] != buf2[i]) {
					f1.close();
					f2.close();
					System.out.printf("Value at %d should have been %d, instead was %d\n", i, buf1[i], buf2[i]);
					return false;
				}
					
			}
			f1.close();
			f2.close();
			return true;
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	public static boolean equal(byte[] buf1, byte[] buf2) {
		if (buf1.length != buf2.length) {
			System.out.printf("Expected size was %d, actual size is %d\n", buf1.length, buf2.length);
			return false;
		}
		for (int i=0; i < buf1.length; i++) {
			if (buf1[i] != buf2[i]) {
				System.out.printf("Value at %d should have been %d, instead was %d\n", i, buf1[i], buf2[i]);
				return false;
			}
				
		}
		return true;
	}
	
	static void test() {
		File testRoot = new File("tests/html2");
		if(testRoot.isFile()) {
			String fileName = testRoot.getPath();
			String outFileName = "results/" + testRoot.getParentFile().getName() + "/" + testRoot.getName();
			String archiveName = "archives/" + testRoot.getParentFile().getName() + "/" + testRoot.getName();
			long start = System.nanoTime();
			new File(archiveName).delete();
			try {
				Deflate.comp(fileName, archiveName);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			try {
				Deflate.decomp(archiveName, outFileName);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			long end = System.nanoTime();
			Double rez = eavluate(fileName, archiveName);
			System.out.println("Time taken: " + (end-start)/1000000000 + "s");
			if(rez == null || !equal(fileName, outFileName)) {
				System.out.println("Test Failed!");
				System.out.println(1/0);
			}
			return;
		}
			
		String failed = "";
		double totalAvg = 0;
		int count = 0;
		System.out.println("Test folders found:");
		int index = 1;
		ArrayList<File> testFolders = new ArrayList<File>();
		PriorityQueue<File> queue = new PriorityQueue<File>();
		queue.add(testRoot);
		while (!queue.isEmpty()) {
			File testFile = queue.poll();
			for(File file : testFile.listFiles()) {
				if(file.isDirectory()) {
					queue.add(file);
				} else if (file.isFile()) {
					System.out.printf("%d. %s\n", index++, testFile.getName());
					testFolders.add(testFile);
					break;
				}
			}
		}
		
		System.out.println("-------------------------------------------------------------------");
		long fullTime = System.nanoTime();
		for (File testGroup : testFolders) {
			System.out.printf("[Doing tests from %s]\n", testGroup.getName());
			double avg = 0;
			for(File t : testGroup.listFiles()) {
				String fileName = t.getPath();
				String outFileName = "results/" + t.getParentFile().getName() + "/" + t.getName();
				String archiveName = "archives/" + t.getParentFile().getName() + "/" + t.getName();
				long start = System.nanoTime();
				new File(archiveName).delete();
				System.out.println("Archiving " + fileName + "...");
				try {
					Deflate.comp(fileName, archiveName);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(1/0);
				} 
				long end = System.nanoTime();
				try {
					Deflate.decomp(archiveName, outFileName);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(1/0);
				}
				Double rez = eavluate(fileName, archiveName);
				System.out.println("Total encoding time: " + (end-start)/1000000 + "ms");
				System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
				if(rez != null && equal(fileName, outFileName)) {
					avg += rez;
					totalAvg += rez;
					count++;
				} else {
					System.out.println("Test Failed!");
					System.out.println(1/0);
					failed += t.getPath() + "\n";
				}
			}
			System.out.printf("Average score: %.1f%%\n", avg/testGroup.listFiles().length);
			System.out.println("-------------------------------------------------------------------");
	    }
		System.out.println("Total time taken: " + (System.nanoTime()-fullTime)/1000000000 + "s");
		if(count > 0) {
			System.out.printf("Total average score: %.1f%%\n", totalAvg/count);
		}
		System.out.println();
		if(!failed.equals("")) {
			System.out.printf("Tests failed: \n%s\n", failed);
		} else {
			System.out.printf("All test completed successfully!");
		}
	}
	
	public static void about() {
		System.out.println("211RDB204 Juris Ozoliņš");
		System.out.println("211RDB276 Toms Zvirbulis");
	}
}
