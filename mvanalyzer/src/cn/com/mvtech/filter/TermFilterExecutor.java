package cn.com.mvtech.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.mvtech.analyzer.Term;

public class TermFilterExecutor {
	
	
	private Map<String, Term> termsMap ;
	
	private List<TermFilter> filters = new ArrayList<TermFilter>();
	
	
	public TermFilterExecutor(Map<String, Term> termsMap) {
		this.termsMap = termsMap;
	}
	
	public void addFilter(TermFilter filter) {
		filters.add(filter);
	}
	
	public Map<String, Term> execute() {
		if (!filters.isEmpty()) {
			for (TermFilter filter : filters) {
				termsMap = filter.filter(termsMap);
			}
		}
		return termsMap;
	}
}
