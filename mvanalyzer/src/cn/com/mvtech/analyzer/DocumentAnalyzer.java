package cn.com.mvtech.analyzer;

import java.io.File;
import java.util.Map;

/**
 * 分词器接口
 * @author zhaokc
 *
 */
public interface DocumentAnalyzer {
	
	/**
	 * 对指定文件进行分词,返回分词后的字符串
	 * @param file
	 * @return
	 */
	String processSegment(File file);
	
	/**
	 * 对指定文件进行分词，返回分词结果
	 * @param file
	 * @return Map<词名,分词对象>
	 */
	Map<String, Term> getTerms(File file);
}
