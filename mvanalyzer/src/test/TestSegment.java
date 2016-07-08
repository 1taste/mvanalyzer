package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import cn.com.mvtech.analyzer.NLPIRAnalyzer;

public class TestSegment {
	public static void main(String[] args) throws IOException {
		//File file = new File("E:\\DB\\CORPUS\\train\\C999-Porn\\45.txt");
		File file = new File("E:\\DB\\CORPUS\\SOURCE\\CLASS-B\\ClassFile\\C7-Car\\0.txt");
		NLPIRAnalyzer analyzer = NLPIRAnalyzer.getInstance();
		
		String result = analyzer.processSegment(file);
		System.out.println(result);
		//System.out.println(analyzer.getTerms(file));
		analyzer.exit();
		
		check();
	}
	
	public static void check() throws IOException {
		File f = new File("E:\\DB\\CORPUS\\test\\C999-Porn\\");
		
		for (File file : f.listFiles()) {
			String a = FileUtils.readFileToString(file, "utf-8");
			if (a.contains("锛")) {
				System.out.println(file.getName());
			}
		}
	}
}
