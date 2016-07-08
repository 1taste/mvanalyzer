package cn.com.mvtech.vector;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import cn.com.mvtech.analyzer.Term;
import cn.com.mvtech.selection.Feature;

public class TFIDFConstructor {

	public void generateVector(
			Map<String, List<Map<String, Term>>> bagWordsModel,
			List<Feature> features,Integer totalDocs, String outputDir) throws IOException {
		
		String sep = File.separator;		
		// 生成特征向量
		for (String key : bagWordsModel.keySet()) {
			List<Map<String, Term>> articles = bagWordsModel.get(key);
			for (Map<String, Term> m : articles) {
				File file = new File(outputDir +sep+ key + sep
						+ UUID.randomUUID() + ".tra");
				StringBuilder sb = new StringBuilder();
				sb.append(Integer.valueOf(key));
				
				for (Feature feature : features) {
					String text = feature.getText();
					if (m.containsKey(text)) {
						double tf = (double) m.get(text).getFrequent() / (double) getTermCountInArticle(m);
						double idf = Math.log(totalDocs / (feature.getDocCount() + 1));
						
						System.out.println(tf+":"+idf);
						
						sb.append(" ").append(feature.getId()).append(":")
								.append(tf * idf);
					}
				}
				
				FileUtils.write(file, sb.toString());
				
			}
		}
		//合并文件,生成训练数据
		merge(outputDir, outputDir+File.separator+"trained.tra");

	}
	
	public void generateVectorSingle(Map<String, Term> doc,
			List<Feature> features,Integer totalDocs, String outputDir) throws IOException {
		
		File file = new File(outputDir);
		StringBuilder sb = new StringBuilder();
		sb.append("1");
		for (Feature feature : features) {
			String text = feature.getText();
			if (doc.containsKey(text)) {
				double tf = (double) doc.get(text).getFrequent() / (double) getTermCountInArticle(doc);
				double idf = Math.log(totalDocs / (feature.getDocCount() + 1));
				sb.append(" ").append(feature.getId()).append(":")
						.append(tf * idf).append(" ");
			}
		}
		
		FileUtils.write(file, sb.toString());
	}
	
	
	private int getTermCountInArticle(Map<String,Term> map) {
		int count = 0;
		for (String key : map.keySet()) {
			count += map.get(key).getFrequent();
		}
		return count;
	}
	
	private int getDocCount(String word, Map<String, List<Map<String, Term>>> all)  {
		int count = 0;
		Set<String> keys = all.keySet();		
		for (String key : keys) {
			List<Map<String,Term>> list = all.get(key);
			for (Map<String,Term> map : list) {
				if (map.containsKey(word)) {
					count++;
				}
			}			
		}		
		return count;
	}
	
	private void merge(String source, String dest) throws IOException {
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
}
