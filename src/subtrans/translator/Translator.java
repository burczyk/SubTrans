package subtrans.translator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import subtrans.builder.GeneralDBBuilder;
import subtrans.exception.NotADirectoryException;
import subtrans.interfaces.ITranslator;
import subtrans.model.TextSequence;
import subtrans.model.Translation;
import subtrans.reader.TmpReader;

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

	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("Za mało argumentów. Wywołanie: java Translator sciezka_eng sciezka_pl nowy_plik_z_napisami");
			System.exit(1);
		}

		try {
			HistoricalTranslator ht = new HistoricalTranslator(new GeneralDBBuilder(new File(args[0]), new File(args[1])).buildDatabase());
			DictionaryTranslator dt = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
			Translator translator = new Translator(ht, dt);

			TmpReader reader = new TmpReader(new File(args[2]));
			List<TextSequence> sequences = reader.readFile();

			for (TextSequence sequence : sequences) {
				System.out.println(sequence.getSequence() + " <---> " + Translator.translationsToString(translator.translate(sequence.getSequence())));
			}

		} catch (NotADirectoryException e) {
			e.printStackTrace();
		}
	}

}
