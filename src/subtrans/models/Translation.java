package subtrans.models;

public class Translation {
	private String sequence;
	private String translation;
	
	public Translation(String sequence, String translation) {
		super();
		this.sequence = sequence;
		this.translation = translation;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}
	
	
}
