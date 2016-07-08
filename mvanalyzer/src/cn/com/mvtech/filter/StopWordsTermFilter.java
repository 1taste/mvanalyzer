package cn.com.mvtech.filter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import cn.com.mvtech.analyzer.Term;

public class StopWordsTermFilter implements TermFilter {
	
	private List<String> stopWords = new ArrayList<String>();
	
	public StopWordsTermFilter(List<String> stopWords) {
		this.stopWords = stopWords;
	}
	
	public StopWordsTermFilter(String filePath, String encode) {
		File file = new File(filePath);
		try {
			stopWords = FileUtils.readLines(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Map<String, Term> filter(Map<String, Term> termsMap) {
		List<String> removes = new ArrayList<String>();
		Set<String> keys = termsMap.keySet();
		for (String key : keys) {
			//停用词
			if (stopWords.contains(key)) {
				removes.add(key);
				continue;
			}
			if(key.matches("\\d+.\\d+")) {
				removes.add(key);
				continue;
			}
		}
		for (String remove : removes) {
			termsMap.remove(remove);
		}
		return termsMap;
	}

}
