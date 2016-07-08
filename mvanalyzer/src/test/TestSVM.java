package test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import cn.com.mvtech.svm.svm_predict;
import cn.com.mvtech.svm.svm_scale;
import cn.com.mvtech.svm.svm_train;

public class TestSVM {
	
	public static void main(String[] args) throws Exception {
		
		//merge("E:\\DB\\CORPUS\\trained\\","E:\\DB\\CORPUS\\trained\\trained.txt");
		//scale("E:\\DB\\CORPUS\\trained\\trained.txt","E:\\DB\\CORPUS\\trained_scale\\trained.txt");
		train();
		
		
		processTest();
	}
	
	public static void train() throws IOException {
		String train = "E:\\DB\\CORPUS\\trained\\trained.txt";
		String model = "E:\\DB\\CORPUS\\trained_scale\\model.txt";
		String[] argv = new String[]{"-h","0","-t","0","-c","10","-b","1",train,model};
		svm_train.main(argv);
	}
	
	public static void scale(String source, String dest) throws IOException {
		String output = source;
		String scale = dest;
		String[] argv = new String[]{"-l","0","-u","1",
				"-s",scale,output};
		svm_scale.main(argv);
		
		//List<String> list = FileUtils.readLines(new File(dest));
		//list.remove(0);
		//FileUtils.writeLines(new File(dest), list);
	}
	
	public static void scale(String source, String dest,String r) throws IOException {
		String output = source;
		String scale = dest;
		String[] argv = new String[]{"-l","0","-u","1",
				"-r",r,output};
		svm_scale.main(argv);
		
		//List<String> list = FileUtils.readLines(new File(dest));
		//list.remove(0);
		//FileUtils.writeLines(new File(dest), list);
	}
	
	
	public static void merge(String source, String dest) throws IOException {
		File sources = new File(source);
		File trained = new File(dest);
		if (trained.exists()) {
			trained.delete();
		}
		
		for (File dir : sources.listFiles()) {
			if (dir.isDirectory()) {
				for (File file : dir.listFiles()) {
					FileUtils.writeLines(trained,FileUtils.readLines(file),true );
				}
			}
			
		}
	}
	
	
	public static void processTest() throws IOException {
		//merge("E:\\DB\\CORPUS\\test_trained\\","E:\\DB\\CORPUS\\test_trained\\trained.txt");
		//scale("E:\\DB\\CORPUS\\test_trained\\trained.txt","E:\\DB\\CORPUS\\test_scale\\trained.txt","E:\\DB\\CORPUS\\trained_scale\\trained.txt");
		
		
		String[] argv = new String[]{"-b","1","E:\\DB\\CORPUS\\test_trained\\trained.txt",
				"E:\\DB\\CORPUS\\trained_scale\\model.txt",
				"E:\\DB\\CORPUS\\test_scale\\result.txt"};
		svm_predict.main(argv);
	}
}
