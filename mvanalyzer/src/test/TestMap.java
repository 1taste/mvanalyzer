package test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import cn.com.mvtech.analyzer.Term;

public class TestMap {
	public static void main(String[] args) {
		Map<String,Term> map = new HashMap<String,Term>();
		map.put("name", new Term());
		map.put("age",new Term());
		map.put("hah", new Term());
		
		remove(map);
		System.out.println(map.size());
		
		//System.err.println(Math.pow(-2, 2));
		
		//System.out.println(CHI(9833, 1, 36, 467, 9329));
		//loadStopWords();
		double a = 1/60;
		System.out.println(a);
	}
	
	public static void remove(Map<String,Term> map) {
		map.remove("name");
	}
	
	public static double CHI(int n, double a, double b, double c, double d) {
		System.out.println((a+c)*(a+b)*(b+d)*(c+d));
		return Math.pow((a*d)-(b*c),2)*n /( (a+c)*(a+b)*(b+d)*(c+d) ) ;
	}
	
	public static void loadStopWords() {
		try {
			List<String> list = FileUtils.readLines(new File("E:\\DB\\CORPUS\\stop\\stop_words_ch.txt"));
			for (String line : list) {
				System.out.print(line);
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
