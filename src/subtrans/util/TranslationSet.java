package subtrans.util;

import java.util.Set;

import subtrans.model.Translation;

public class TranslationSet {
	public static Translation containsSequence(Set<Translation> translationsSet, String sequence, String translation) {
		for (Translation translationInSet : translationsSet) {
			if (translationInSet.getSequence().equals(sequence) && translationInSet.getTranslation().equals(translation)) {
				return translationInSet;
			}
		}
		return null;
	}
}
