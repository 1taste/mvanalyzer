package cn.com.mvtech.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.com.mvtech.analyzer.Term;

/**
 * 卡方检验法
 * 公式
 * Math.pow((a*d)-(b*c),2)*n /( (a+c)*(a+b)*(b+d)*(c+d) ) 
 * 
 * A:在一个类别中，包含某个词的文档的数量
 * B:排除该类别，其他类别包含某个词的文档的数量
 * C:在一个类别中，不包含某个词的文档的数量
 * D:不在该分类下，且不包含这个词的文档数量
 * N:训练集的总文档数
 * 
 * @author zhaokc
 *
 */
public class CHIFeatureSelector implements FeatureSelector {
	
	
	private static final Logger LOG = Logger.getLogger(CHIFeatureSelector.class);
	
	
	@Override
	public Map<String, List<Feature>> extract(
			Map<String, List<Map<String, Term>>> data, int dimension) {
		
		//存储每个分类下，所有分词信息（去除重复数据）。
		Map<String, Map<String,Term>> catagoryTerms = new HashMap<String, Map<String,Term>>();
		
		int totalDocs = 0;
		for (String catagory : data.keySet()) {
			Map<String,Term> catagoryTerm = new HashMap<String,Term>();
			for (Map<String, Term> terms : data.get(catagory)) {
				catagoryTerm.putAll(terms);
			}
			catagoryTerms.put(catagory, catagoryTerm);
			totalDocs += data.get(catagory).size();
		}
		
		Map<String, List<Feature>> resultMap = new HashMap<String, List<Feature>>();
		int i = 0;
		for (String cat : catagoryTerms.keySet()) {
			
			LOG.info("extract feature: "+cat+" ...");
			
			List<Feature> featureList = new ArrayList<Feature>();
			Map<String,Term> catT = catagoryTerms.get(cat);
			for (String word : catT.keySet()) {
				int a = getDocCountInCat(word, cat, data);
				int b = getDocCountNotInCat(word, cat, data);
				int c = getNotDocCountInCat(word, cat, data);
				int d = getNotDocCountNotInCat(word, cat, data);				
				double chi = CHI(totalDocs, a, b, c, d);
				
				int docCount = getDocCount(word, data);
				
				Feature feature = new Feature();
				feature.setText(word);
				feature.setValue(chi);
				feature.setId(i++);
				feature.setDocCount(docCount);
				featureList.add(feature);
				
			}
			Collections.sort(featureList);
			int size = featureList.size();
			
			List<Feature> topFeatures = featureList.subList(size > dimension ? size-dimension : 0, size);

			resultMap.put(cat, topFeatures);
		}
		
		resultMap.put("ALL",mergeFeature(resultMap));
		
		LOG.info("all catagories extract finished! ");
		
		return resultMap;
	}
	
	
	private List<Feature> mergeFeature(Map<String, List<Feature>> featureMap) {
		List<Feature> resultList = new ArrayList<Feature>();
		Set<String> sets = new HashSet<String>();
		for (String key : featureMap.keySet()) {
			List<Feature> featureList = featureMap.get(key);
			for (Feature feature : featureList) {
				if (!sets.contains(feature.getText())) {
					resultList.add(feature);
					sets.add(feature.getText());
				} 
			}
			
		}
		return resultList;
	} 
	
	
	private static double CHI(double n, double a, double b, double c, double d) {
		return Math.pow((a*d)-(b*c),2)*n /( (a+c)*(a+b)*(b+d)*(c+d) ) ;
	}
	
	/**
	 * A：在一个类别中，包含某个词的文档的数量
	 * @param word
	 * @return
	 */
	private int getDocCountInCat(String word, String catagory, Map<String, List<Map<String, Term>>> all) {
		int count = 0;
		List<Map<String,Term>> list = all.get(catagory);
		
		for (Map<String,Term> map : list) {
			if (map.containsKey(word)) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * B:排除该类别，其他类别包含某个词的文档的数量
	 * @param word
	 * @param catagory
	 * @return
	 */
	private int getDocCountNotInCat(String word, String catagory, Map<String, List<Map<String, Term>>> all) {
		int count = 0;
		Set<String> keys = all.keySet();
		for (String key : keys) {
			if (!key.equals(catagory)) {
				List<Map<String,Term>> list = all.get(key);
				for (Map<String,Term> map : list) {
					if (map.containsKey(word)) {
						count++;
					}
				}
			}
		}		
		return count;
	}
	
	/**
	 * C:在一个类别中，不包含某个词的文档的数量
	 * @param word
	 * @param catagory
	 * @return
	 */
	private int getNotDocCountInCat(String word, String catagory, Map<String, List<Map<String, Term>>> all) {
		
		int count = 0;
		List<Map<String,Term>> list = all.get(catagory);
		
		for (Map<String,Term> map : list) {
			if (!map.containsKey(word)) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * D:不在该分类下，且不包含这个词的文档数量
	 * @param word
	 * @param catagory
	 * @return
	 */
	private int getNotDocCountNotInCat(String word, String catagory, Map<String, List<Map<String, Term>>> all)  {
		int count = 0;
		Set<String> keys = all.keySet();		
		for (String key : keys) {
			if (!key.equals(catagory)) {
				List<Map<String,Term>> list = all.get(key);
				for (Map<String,Term> map : list) {
					if (!map.containsKey(word)) {
						count++;
					}
				}			
			}
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
}
