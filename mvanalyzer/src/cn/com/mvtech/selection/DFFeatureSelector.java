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
 * 文档频率
 * 公式
 * 
 * @author zhaokc
 *
 */
public class DFFeatureSelector implements FeatureSelector {
	
	
	private static final Logger LOG = Logger.getLogger(DFFeatureSelector.class);
	
	
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
				int docCount = getDocCount(word, data);
				
				Feature feature = new Feature();
				feature.setText(word);
				feature.setValue(docCount);
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
