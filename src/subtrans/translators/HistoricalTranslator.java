package subtrans.translators;

import java.util.List;

import subtrans.interfaces.ITranslator;
import subtrans.models.Translation;

public class HistoricalTranslator implements ITranslator {
	private List<Translation> historicalTranslations;
	
	public HistoricalTranslator(List<Translation> historicalTranslations) {
		super();
		this.historicalTranslations = historicalTranslations;
	}

	@Override
	public Translation translate(String sequence) {
		for(Translation translation: historicalTranslations){
			if(sequence.equals(translation.getSequence())){
				return translation;
			}
		}
		return null;
	}

}
