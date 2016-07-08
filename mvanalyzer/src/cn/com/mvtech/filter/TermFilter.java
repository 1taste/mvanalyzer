package cn.com.mvtech.filter;

import java.util.Map;

import cn.com.mvtech.analyzer.Term;

/**
 * 分词过滤器
 * 
 * @author zhaokc
 *
 */
public interface TermFilter {
	Map<String, Term> filter(Map<String, Term> termsMap);
}
