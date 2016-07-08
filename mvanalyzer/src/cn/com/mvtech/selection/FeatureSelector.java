package cn.com.mvtech.selection;

import java.util.List;
import java.util.Map;

import cn.com.mvtech.analyzer.Term;

/**
 * 特征选择接口
 * 任何特征选择算法需实现extract方法，并按照要求返回相关格式化数据。
 * 
 * @author zhaokc
 * 
 */
public interface FeatureSelector {
	
	/**
	 * 特征选择方法
	 * 传入需要抽取的数据.特征维度。将结果输出到指定目录
	 * 
	 * 参数说明：
	 * 
	 * data：需要处理的数据集
	 * Map<分类名,List<文章>> 存储各个分类下的文本内容List,List内每个元素为一个文档,
	 * Map存储该文档所有分词信息（以词语名称作为键,分词对象为值）
	 * 
	 * dimension： 特征纬度
	 * 需要抽取的特征维度,即特征词数量。传入-1代表选取所有分词作为特征。
	 * 	 *  
	 * @param data 数据集:  List<Map<词名,分词对象>>
	 * @param dimension 维度,即选取多少特征词。-1为选取全部
	 */
	public Map<String,List<Feature>> extract(Map<String, List<Map<String,Term>>> data, int dimension);
}
