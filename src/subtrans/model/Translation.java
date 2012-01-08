package subtrans.model;

public class Translation {
	private String sequence;
	private String translation;
	private int occurences;

	public Translation(String sequence, String translation) {
		super();
		this.sequence = sequence;
		this.translation = translation;
		this.occurences = 1;
	}

	public Translation(String sequence, String translation, int occurences) {
		super();
		this.sequence = sequence;
		this.translation = translation;
		this.occurences = occurences;
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

	public int getOccurences() {
		return occurences;
	}

	public void setOccurences(int occurences) {
		this.occurences = occurences;
	}

	public void increaseOccurences() {
		++this.occurences;
	}

	public String toString() {
		return this.sequence + " <---> " + this.translation;
	}

}
