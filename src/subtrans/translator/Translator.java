package subtrans.translator;

import java.util.ArrayList;
import java.util.List;

import subtrans.interfaces.ITranslator;
import subtrans.model.Translation;

public class Translator implements ITranslator {
	private HistoricalTranslator historicalTranslator;
	private DictionaryTranslator dictionaryTranslator;

	public Translator(HistoricalTranslator historicalTranslator, DictionaryTranslator dictionaryTranslator) {
		super();
		this.historicalTranslator = historicalTranslator;
		this.dictionaryTranslator = dictionaryTranslator;
	}

	@Override
	public List<Translation> translate(String sequence) {

		String[] words = sequence.split(" ");
		List<Translation> translations = new ArrayList<Translation>();
		for (String word : words) {
			translations.add(new Translation(clean(word), "", 0));
		}

		// Translate subsequences
		for (int subsequenceLength = words.length; subsequenceLength > 1; subsequenceLength--) {
			for (List<Translation> subsequence : generateSubsequences(translations, subsequenceLength)) {
				List<Translation> historicalTranslations = historicalTranslator.translate(translationListToString(subsequence));

				if (historicalTranslations != null && historicalTranslations.size() > 0) {
					markTranslationsAsDone(subsequence);
					subsequence.get(0).setTranslation(historicalTranslations.get(0).getTranslation());
				} else {
					List<Translation> dictionaryTranslations = dictionaryTranslator.translate(translationListToString(subsequence));
					if (dictionaryTranslations != null && dictionaryTranslations.size() > 0) {
						markTranslationsAsDone(subsequence);
						subsequence.get(0).setTranslation(dictionaryTranslations.get(0).getTranslation());
					}
				}
			}
		}

		// Translate all empty translations
		for (Translation t : translations) {
			if (t.getOccurences() < 1) {
				List<Translation> dictionaryTranslations = dictionaryTranslator.translate(t.getSequence());
				if (dictionaryTranslations != null && dictionaryTranslations.size() > 0) {
					t.setTranslation(dictionaryTranslations.get(0).getTranslation());
				}
			}
		}

		return translations;
	}

	private String clean(String s) {
		String[] signs = new String[] { ";", "\\:", "\\,", "\\.", "\\?", "\\|" };
		for (String sign : signs) {
			s = s.replaceAll(sign, "");
		}
		signs = new String[] { "\\|" };
		for (String sign : signs) {
			s = s.replaceAll(sign, " ");
		}
		return s.trim().toLowerCase();
	}

	private void markTranslationsAsDone(List<Translation> list) {
		for (Translation t : list) {
			if (t.getOccurences() < 1) {
				t.setOccurences(1);
			}
		}
	}

	private String translationListToString(List<Translation> list) {
		StringBuilder sb = new StringBuilder();
		for (Translation t : list) {
			sb.append(t.getSequence());
			sb.append(" ");
		}
		return sb.toString().trim().toLowerCase();
	}

	public static String translationsToString(List<Translation> list) {
		StringBuilder sb = new StringBuilder();
		for (Translation t : list) {
			sb.append(t.getTranslation());
			sb.append(" ");
		}
		return sb.toString().trim();
	}

	public List<List<Translation>> generateSubsequences(List<Translation> sequence, int length) {
		List<List<Translation>> result = new ArrayList<List<Translation>>();
		if (sequence.size() > length) {
			for (int i = 0; i <= sequence.size() - length; ++i) {
				List<Translation> subSequence = sequence.subList(i, i + length);
				result.add(subSequence);
			}
		} else {
			result.add(sequence);
		}
		return result;
	}

}
