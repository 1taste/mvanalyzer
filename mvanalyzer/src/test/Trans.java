package test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Trans {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String src = "E:\\DB\\CORPUS\\SOURCE\\CLASS-D";
		String dest = "E:\\DB\\CORPUS\\SOURCE\\CLASS-E\\";
		
		File srcFile = new File(src);
		for (File file : srcFile.listFiles()) {
			List<String> list = FileUtils.readLines(file,"GBK");
			for (int i=0;i<12;i++) {
				list.remove(0);
			}
			for (int i=0;i<10;i++) {
				list.remove(list.size()-1);
			}
			FileUtils.writeLines(new File(dest+file.getName()), list);
		}
	}

}
