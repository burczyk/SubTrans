package subtrans.translators;

import subtrans.interfaces.ITranslator;
import subtrans.models.Translation;

public class Translator implements ITranslator {
	private HistoricalTranslator historicalTranslator;
	private DictionaryTranslator dictionaryTranslator;
	
	public Translator(HistoricalTranslator historicalTranslator, DictionaryTranslator dictionaryTranslator) {
		super();
		this.historicalTranslator = historicalTranslator;
		this.dictionaryTranslator = dictionaryTranslator;
	}

	@Override
	public Translation translate(String sequence) {
		Translation t = historicalTranslator.translate(sequence);
		if(t != null) System.out.println(t.getSequence() + " <---> " + t.getTranslation());
		return null;
	}

}
