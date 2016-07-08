package cn.com.mvtech.analyzer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * 中科院ictas分词器包装类
 * @author zhaokc
 *
 */
public class NLPIRAnalyzer implements DocumentAnalyzer{
	
	
	private static NLPIRAnalyzer analyzer = new NLPIRAnalyzer();
		
	private String encoding = "UTF-8";
	
	private NLPIRAnalyzer() {
		String argu = "";
		String system_charset = "GBK";
		int charset_type = 1;
		try {
			if (!CLibrary.Instance.NLPIR_Init(argu.getBytes(system_charset),
					charset_type, "0".getBytes(system_charset))) {
				System.err.println("初始化失败！");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
	public static NLPIRAnalyzer getInstance() {
		return analyzer;
	}
	
	@Override
	public String processSegment(File file) {
		if (file == null || !file.exists()) {
			throw new IllegalArgumentException();
		}
		try {
			return CLibrary.Instance.NLPIR_ParagraphProcess(FileUtils.readFileToString(file,encoding), 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Map<String, Term> getTerms(File file) {
		
		Map<String,Term> terms = new HashMap<String,Term>();
		String result = processSegment(file);
		
		String[] words = result.split("\\s+");	
		for (String s : words){
			String[] arr = s.split("/");
			if (arr.length >= 2) {
				String word = arr[0].trim();
				String pos = arr[1];
				if (word == null || "".equals(word)) {
					continue;
				}
				Term term = terms.get(word);
				if (!terms.containsKey(word)) {
					term = new Term();
					term.setText(word);
					term.setPos(pos);
					term.setFrequent(1);
				} else {
					term.increFreq();
				}
				terms.put(word, term);
			}
		}		
		return terms;
	}
	
	
	public void exit() {
		CLibrary.Instance.NLPIR_Exit();
	}
	
	public interface CLibrary extends Library {

		// 定义并初始化接口的静态变量
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				"D:\\Work\\workspace\\eclipse\\common\\mvanalyzer\\lib\\NLPIR.dll", CLibrary.class);

		
		public boolean NLPIR_Init(byte[] sDataPath, int encoding,
				byte[] sLicenceCode);

		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);
		
		public String NLPIR_GetKeyWords(String sLine,int nMaxKeyLimit,boolean bWeightOut);
		
		public String NLPIR_WordFreqStat(String text);
		
		public void NLPIR_Exit();
	}
}
