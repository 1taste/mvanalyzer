package cn.com.mvtech.analyzer;

import java.util.List;

import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;

public class Test {
	public static void main(String[] args) {
		System.out.println(BaseAnalysis.parse("中国人不打中国人"));
		List<org.ansj.domain.Term> terms = ToAnalysis.parse("中国人不打中国人");
		
		for (org.ansj.domain.Term t : terms) {
			//System.out.println(t.getName()+"-"+t.getNatureStr());
		}
	}
}
