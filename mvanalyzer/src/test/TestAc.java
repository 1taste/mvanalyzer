package test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class TestAc {
	
	public static int[] labels = new int[]{8, 10, 13, 14, 16, 20, 22, 23, 24};
	
	public static void main(String[] args) throws IOException {
		
		
		File test = new File("E:\\DB\\CORPUS\\test_trained\\trained.txt");
		File result = new File("E:\\DB\\CORPUS\\test_scale\\result.txt");
		
		List<String> testList = FileUtils.readLines(test);
		List<String> resultList = FileUtils.readLines(result);
		resultList.remove(0);
		int count = 0;
		for (int i = 0; i< testList.size();i++) {
			String txt = testList.get(i);
			String rs = resultList.get(i);
			double catagory = Double.valueOf(txt.split(" ")[0]);
			double rcat = Double.valueOf(rs.split(" ")[0]);
			//System.out.println(catagory+":"+rcat);
			if (catagory == rcat) {
				count++;
				System.out.println(catagory+":"+rs.split(" ")[getLabelIndex(catagory)+1]);
			}
			
		}
		System.out.println(count);
	}
	
	public static void getMaxRate(String s) {
		/*
		List<String>
		String[] arr = s.split(" ");
		for (int i = 1;i< arr.length;i++) {
			
		}
		
		Collections.sort(list);*/
	}
	
	
	public static int getLabelIndex(double label) {
		for (int i=0;i<labels.length;i++) {
			if (labels[i] == label) {
				return i;
			}
		}
		return -1;
	}
}
