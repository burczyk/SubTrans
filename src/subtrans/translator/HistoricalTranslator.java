package subtrans.translator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import subtrans.comparator.TranslationComparator;
import subtrans.interfaces.ITranslator;
import subtrans.model.Translation;
import subtrans.util.TranslationSet;

public class HistoricalTranslator implements ITranslator {
	private List<Translation> historicalTranslations;

	public HistoricalTranslator(List<Translation> historicalTranslations) {
		super();
		this.historicalTranslations = historicalTranslations;
	}

	@Override
	public List<Translation> translate(String sequence) {
		Set<Translation> translationsSet = new HashSet<Translation>();
		for (Translation translation : historicalTranslations) {
			if (sequence.equals(translation.getSequence())) {
				Translation translationInSet;
				if ((translationInSet = TranslationSet.containsSequence(translationsSet, sequence, translation.getTranslation())) != null) {
					translationInSet.increaseOccurences();
				} else {
					translationsSet.add(translation);
				}
			}
		}
		List<Translation> translationsList = new ArrayList<Translation>(translationsSet);
		Collections.sort(translationsList, new TranslationComparator());
		if (translationsList.size() > 0)
			System.err.println(translationsList.get(0));
		return translationsList;
	}

}
