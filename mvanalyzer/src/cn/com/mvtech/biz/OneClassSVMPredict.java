package cn.com.mvtech.biz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import cn.com.mvtech.analyzer.AnsjAnalyzer;
import cn.com.mvtech.analyzer.Term;
import cn.com.mvtech.filter.StopWordsTermFilter;
import cn.com.mvtech.filter.TermFilterExecutor;
import cn.com.mvtech.selection.Feature;
import cn.com.mvtech.selection.FeatureHelper;
import cn.com.mvtech.svm.svm_predict;
import cn.com.mvtech.vector.TFIDFConstructor;

public class OneClassSVMPredict {
	
	private String repository;
	
	private String modelPath;
	
	private String featurePath;
	
	private String stopWordsPath;
	
	private List<String> stopWordList = new ArrayList<String>();
	
	public OneClassSVMPredict(String repository) {
		this.repository = repository;
		this.modelPath = this.repository + File.separator + "predict" + File.separator + "svm.m";
		this.featurePath = this.repository + File.separator + "predict" + File.separator + "all.fea";
		this.stopWordsPath = this.repository + File.separator + "predict" + File.separator + "stop_words_ch.txt";
		
		loadStopWords();
	}	
	
	public boolean predict(String fileName) {
		
		try {
			//step.1 分词
			AnsjAnalyzer analyzer = new AnsjAnalyzer();
			Map<String, Term> term = analyzer.getTerms(new File(fileName));
			
			//step.2 去停用词
			term = filterWords(term);
			
			//step.3 特征量化
			List<Feature> featureList = FeatureHelper.loadFeature(featurePath);
			
			//step.4 特征权重量化
			String target = "f:\\test\\"+UUID.randomUUID()+".txt";
			TFIDFConstructor vectorGenerator = new TFIDFConstructor();
			vectorGenerator.generateVectorSingle(term, featureList, 1832, target);
			
			//step.5 预测
			String result = "f:\\result\\"+UUID.randomUUID()+".txt";
			String[] argv = new String[]{
					"-q",
					target,
					this.modelPath,
					result};
			
			svm_predict.main(argv);
			
			String rs = FileUtils.readFileToString(new File(result));
			new File(result).delete();
			
			if (Double.valueOf(rs) == 1) {
				return true;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void loadStopWords() {
		try {
			List<String> list = FileUtils.readLines(new File(stopWordsPath));
			for (String line : list) {
				stopWordList.add(line.trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Map<String,Term> filterWords(Map<String,Term> map) {
		TermFilterExecutor filterExecutor = new TermFilterExecutor(map);
		filterExecutor.addFilter(new StopWordsTermFilter(stopWordList));
		return filterExecutor.execute();
	}
	
	public static void main(String[] args) {
		OneClassSVMPredict predict = new OneClassSVMPredict("E:\\classifier-repository\\oneclass\\porn");
		//File f = new File("E:\\DB\\CORPUS\\SOURCE\\CLASS-E\\");
		//File f = new File("E:\\DB\\CORPUS\\SOURCE\\CLASS-A\\C999-Porn\\");
		File f = new File("F:\\TEST-00\\");
		double total = f.listFiles().length;
		System.out.println("total: "+total);
		int i = 0;
		for (File file : f.listFiles()) {
			boolean result = predict.predict(file.getAbsolutePath());
			if (result) {
				i++;
				System.out.println("100%");
			} else {
				System.err.println("incorrect:file="+file.getName());
			}
		}
		System.out.println("correct:" + i);
		System.out.println("rate:" + (i/total));
		
	}
}
