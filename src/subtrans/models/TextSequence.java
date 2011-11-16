package subtrans.models;

public class TextSequence {
	private String startMark;
	private String endMark;
	private String sequence;

	public TextSequence(String startMark, String endMark, String sequence) {
		this.startMark = startMark;
		this.endMark = endMark;
		this.sequence = sequence;
	}

	public String getStartMark() {
		return startMark;
	}

	public String getEndMark() {
		return endMark;
	}

	public String getSequence() {
		return sequence;
	}
	
}
