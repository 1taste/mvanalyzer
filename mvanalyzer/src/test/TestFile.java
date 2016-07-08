package test;

import java.io.File;

public class TestFile {

	public static void main(String[] args) {
		String trainRoot = "E:\\DB\\CORPUS\\train\\";
		String testRoot = "E:\\DB\\CORPUS\\test\\";
		
		for (File dir : new File(trainRoot).listFiles()) {
			for (File f : dir.listFiles()) {
				int index = Integer.valueOf(f.getName().substring(0, f.getName().lastIndexOf(".")));
				if (index > 1799) {
					f.delete();
				}
			}
		}
		
		for (File dir : new File(testRoot).listFiles()) {
			for (File f : dir.listFiles()) {
				int index = Integer.valueOf(f.getName().substring(0, f.getName().lastIndexOf(".")));
				if (index <= 1799) {
					f.delete();
				}
			}
		}
	}

}
