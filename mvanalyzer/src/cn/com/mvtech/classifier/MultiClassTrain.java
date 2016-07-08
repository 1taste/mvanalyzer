package cn.com.mvtech.classifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cn.com.mvtech.analyzer.NLPIRAnalyzer;
import cn.com.mvtech.analyzer.Term;
import cn.com.mvtech.filter.StopWordsTermFilter;
import cn.com.mvtech.filter.TermFilterExecutor;
import cn.com.mvtech.selection.DFFeatureSelector;
import cn.com.mvtech.selection.Feature;
import cn.com.mvtech.selection.FeatureSelector;
import cn.com.mvtech.svm.svm_train;
import cn.com.mvtech.vector.TFIDFConstructor;

/**
 * 实现由纯文本->SVM分类模型的训练流程。
 * 
 * SVM文本分类模型训练流程
 * step1. 中文分类语料库准备
 * step2. 分词
 * step3. 去停用词 
 * step4. 特征选择
 * step5. 特征权重量化（特征向量构造）
 * step6. 归一化处理（可选）
 * step7. SVM训练
 * step8. 参数优化
 * step9. 输出训练模型
 *  
 * @author zhaokc
 *
 */
public class MultiClassTrain {
	
	private static final Logger LOG = Logger.getLogger(MultiClassTrain.class);
	
	private static Map<String, Map<String,Term>> catagoryTerms = new HashMap<String, Map<String,Term>>();
	
	private static Map<String, List<Map<String,Term>>> all = new HashMap<String, List<Map<String,Term>>>();
		
	private static String ROOT_FOLDER = "E:\\SVM_TRAIN";	
		
	private static List<String> stopWordList = new ArrayList<String>();
	
	private static int total = 0;
	
	public static void main(String[] args) throws Exception {
		
		File root = new File(ROOT_FOLDER+File.separator+"1-Source");
		
		PropertyConfigurator.configure(MultiClassTrain.class.getClassLoader().getResource("log4j.properties"));
		
		loadStopWords();
		
		//分词
		/*NLPIRAnalyzer analyzer = NLPIRAnalyzer.getInstance();
		for (File catagory : root.listFiles()) {
			LOG.info("processing segment: "+catagory.getName()+"...");
			List<Map<String,Term>> list = new ArrayList<Map<String,Term>>();
			Map<String,Term> catagoryTerm = new HashMap<String,Term>();
			for (File doc : catagory.listFiles()) {				
				Map<String,Term> tms = analyzer.getTerms(doc);
				tms = filterWords(tms);
				list.add(tms);
				catagoryTerm.putAll(tms);
				total++;
			}
			all.put(catagory.getName(), list);
			catagoryTerms.put(catagory.getName(), catagoryTerm);
			
			LOG.info(catagory.getName()+" - 文档数:"+list.size()+", 分词数:"+catagoryTerm.size());
		}
		
		LOG.info("process done total="+total);
		
		//输出词袋模型
		outputSegment(all);*/
		
		reloadSeg();
		//特征选择
		FeatureSelector feaSelector = new DFFeatureSelector();
		Map<String, List<Feature>> featureMap = feaSelector.extract(all, 2500);

		//输出特征选择结果
		outputFeature(featureMap);
		
		//特征权重量化
		TFIDFConstructor vectorGenerator = new TFIDFConstructor();
		vectorGenerator.generateVector(all, featureMap.get("ALL"), total,ROOT_FOLDER+File.separator+"4-TrainData");
		
		train();
	}
	
	public static void reloadSeg() throws IOException {
		all = loadSegments(ROOT_FOLDER+File.separator+"2-Segment");
		
		int totalDocs = 0;
		for (String catagory : all.keySet()) {
			Map<String,Term> catagoryTerm = new HashMap<String,Term>();
			for (Map<String, Term> terms : all.get(catagory)) {
				catagoryTerm.putAll(terms);
			}
			catagoryTerms.put(catagory, catagoryTerm);
			totalDocs += all.get(catagory).size();
		}
		total = totalDocs;
	}
	
	
	public static void train() throws IOException {
		String train = "E:\\SVM_TRAIN\\4-TrainData\\trained.tra";
		String model = "E:\\SVM_TRAIN\\5-Model\\svm.m";
		String[] argv = new String[]{"-h","0","-t","0","-c","10","-s","2",train,model};
		svm_train.main(argv);
	}
	
	
	
	public static void loadStopWords() {
		try {
			List<String> list = FileUtils.readLines(new File("E:\\SVM_TRAIN\\DB-StopWord\\stop_words_ch.txt"));
			for (String line : list) {
				stopWordList.add(line.trim());
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Map<String,Term> filterWords(Map<String,Term> map) {
		TermFilterExecutor filterExecutor = new TermFilterExecutor(map);
		filterExecutor.addFilter(new StopWordsTermFilter(stopWordList));
		return filterExecutor.execute();
	}
	
	
	public static void outputSegment(Map<String, List<Map<String,Term>>> bagWordsModel) throws IOException {
		String sep = File.separator;
		StringBuilder appender = new StringBuilder();
		for (String catagory : bagWordsModel.keySet()) {
			
			LOG.info("output segment: "+catagory +"...");
			
			int idx = 1;
			for (Map<String,Term> doc : bagWordsModel.get(catagory)) {
				appender.setLength(0);
				for (String key : doc.keySet()) {
					Term term = doc.get(key);
					appender.append(term.getText()).append(" ")
							.append(term.getPos()).append(" ")
							.append(term.getFrequent()).append("\r\n");
				}
				
				File file = new File(ROOT_FOLDER+sep+"2-Segment"+sep+catagory+sep+(idx++)+".seg");				
				FileUtils.writeStringToFile(file, appender.toString());
			}
		}
	}
	
	public static void outputFeature(Map<String,List<Feature>> featureMap) throws IOException {
		
		String sep = File.separator;
		StringBuilder sb = new StringBuilder();
		for (String key : featureMap.keySet()) {
			List<Feature> features = featureMap.get(key);
			sb.setLength(0);
			for (Feature feature : features ) {
				sb.append(feature.getText()).append(" ").append(feature.getId())
				  .append(" ").append(feature.getDocCount()).append("\r\n");
			}
			File file = new File(ROOT_FOLDER+sep+"3-Feature"+sep+key+".fea");
			
			LOG.info("output feature , file:"+file.getName());
			
			FileUtils.writeStringToFile(file, sb.toString());
		}
	}
	
	public static Map<String, List<Map<String,Term>>> loadSegments(String path) throws IOException {
		File root = new File(path);
		Map<String, List<Map<String,Term>>> map = new HashMap<String, List<Map<String,Term>>>();
		for (File catagory : root.listFiles()) {
			List<Map<String,Term>> list = new ArrayList<Map<String,Term>>();
			for (File doc : catagory.listFiles()) {
				List<String> terms = FileUtils.readLines(doc);
				Map<String,Term> termMap = new HashMap<String,Term>();
				for (String str : terms) {
					String[] arr = str.split(" ");
					Term tm = new Term();
					tm.setText(arr[0]);
					tm.setPos(arr[1]);
					tm.setFrequent(Integer.valueOf(arr[2]));
					termMap.put(arr[0],tm);
				}
				list.add(termMap);
			}
			map.put(catagory.getName(), list);
		}
		return map;
	}
}
