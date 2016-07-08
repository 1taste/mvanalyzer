package cn.com.mvtech.selection;


public class Feature implements Comparable<Feature>{
	
	private int id;
	
	private String text;
	
	private double value;
	//该词被多少个文档包含
	private int docCount;
		
	@Override
	public int compareTo(Feature o) {
		if (this.value > o.value) {
			return 1;
		}
		if (this.value < o.value) {
			return -1;
		}
		return 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getDocCount() {
		return docCount;
	}

	public void setDocCount(int docCount) {
		this.docCount = docCount;
	}
	
	
}
 