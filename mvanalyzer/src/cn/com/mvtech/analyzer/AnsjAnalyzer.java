package cn.com.mvtech.analyzer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.io.FileUtils;

public class AnsjAnalyzer implements DocumentAnalyzer {

	@Override
	public String processSegment(File file) {		
		
		return null;
	}

	@Override
	public Map<String, Term> getTerms(File file) {
		Map<String,Term> terms = new HashMap<String,Term>();
		try {
			List<org.ansj.domain.Term> list = ToAnalysis.parse(FileUtils.readFileToString(file));
			for (org.ansj.domain.Term term : list) {
				Term t = terms.get(term.getName());
				if (!terms.containsKey(term.getName())) {
					t = new Term();
					t.setText(term.getName());
					t.setPos(term.getNatureStr());
					t.setFrequent(1);
				} else {
					t.increFreq();
				}
				terms.put(term.getName(), t);
			}
			
			return terms;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
