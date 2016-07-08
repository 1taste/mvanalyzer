package cn.com.mvtech.classifier;

import java.io.IOException;

import cn.com.mvtech.svm.svm_train;

public class Test {
	public static void main(String[] args) throws IOException {
		train();
	}
	
	public static void train() throws IOException {
		String train = "E:\\SVM_TRAIN\\4-TrainData\\trained.tra";
		String model = "E:\\SVM_TRAIN\\5-Model\\svm.m";
		String[] argv = new String[]{"-h","0","-t","0","-c","10","-s","2",train,model};
		svm_train.main(argv);
	}
}
