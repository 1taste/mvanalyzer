package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Gen {
	
	private static String ROOT = "E:\\DB\\TEXT\\C0001\\";
	
	private static int count = 1;
		
	public static void main(String[] args) {
		build(new File("F:\\项目资料\\不良信息\\图片数据\\"));
		
	}
	
	
	public static void build(File f) {
		try {
			if (f.isDirectory()) {
				for (File fs : f.listFiles()) {
					build(fs);
				}
			} else {
				if (f.length() > 2*1024 && f.length() < 100*1024 && f.getName().endsWith(".txt")) {
					FileUtils.copyFile(f, new File(ROOT+(count++)+".txt"));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
