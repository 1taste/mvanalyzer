package cn.com.mvtech.analyzer;

public class Term {
	//id
	private String id;
	//分词
	private String text;
	//词性
	private String pos;
	//出现次数
	private int frequent;
	
	public void increFreq() {
		this.frequent++;
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}


	public int getFrequent() {
		return frequent;
	}


	public void setFrequent(int frequent) {
		this.frequent = frequent;
	}

	
	
	
}
