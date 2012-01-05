package subtrans.translator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import subtrans.comparator.TranslationLengthComparator;
import subtrans.interfaces.ITranslator;
import subtrans.model.Translation;
import subtrans.sqlite.SQLiteQueryExecutor;

public class DictionaryTranslator implements ITranslator {

	private SQLiteQueryExecutor executor;
	private Set<String> notTranslateableWords = new HashSet<String>(Arrays.asList(new String[] { "a", "an" }));

	public DictionaryTranslator(String databaseFileName) {
		super();
		this.executor = new SQLiteQueryExecutor(databaseFileName);
	}

	@Override
	public List<Translation> translate(String sequence) {
		List<Translation> translations = new ArrayList<Translation>();
		String[] words = sequence.split(" ");
		if (words.length == 1) {
			List<String> rawTranslations = executor.translate(sequence);
			System.out.println(rawTranslations);
		} else {
			String translation = "";
			for (String word : words) {
				if (!notTranslateableWords.contains(word.toLowerCase())) {
					word = removePunctationSigns(word.toLowerCase());
					List<String> rawTranslations = executor.translate(word);
					if (rawTranslations.size() > 0) {
						List<Translation> possibleTranslations = listOfPossibleTranslations(rawTranslations);
						if (possibleTranslations.size() > 0) {
							translation += possibleTranslations.get(0).getTranslation() + " ";
						} else {
							translation += word + " ";
						}
					} else {
						translation += word + " ";
					}
				}
			}
			translations.add(new Translation(sequence, translation.trim()));
		}

		return translations;
	}

	private String clean(String s) {
		return removePunctationSigns(removeBraces(removeXMLTags(s)));
	}

	private String removeXMLTags(String s) {
		return s.replaceAll("<.*>", "");
	}

	private String removeBraces(String s) {
		return s.replaceAll("\\(.*\\)", "").replaceAll("\\[.*\\]", "");
	}

	private String removePunctationSigns(String s) {
		String[] signs = new String[] { ";", "\\:", "\\,", "\\.", "\\?" };
		for (String sign : signs) {
			s = s.replaceAll(sign, "");
		}
		return s.trim();
	}

	public List<Translation> listOfPossibleTranslations(List<String> rawTranslations) {
		List<Translation> translations = new ArrayList<Translation>();
		int beginIndex = 0;
		for (String rawTranslation : rawTranslations) {
			int gbStart = 0;
			int gbEnd = 0;
			int plStart = 0;
			int plEnd = 0;
			boolean found = false;

			while (gbStart > -1 && gbEnd > -1 && plStart > -1 && plEnd > -1) {
				gbStart = rawTranslation.indexOf("<GB>", beginIndex);
				gbEnd = rawTranslation.indexOf("</GB>", beginIndex);
				plStart = rawTranslation.indexOf("<PL>", beginIndex);
				plEnd = rawTranslation.indexOf("</PL>", beginIndex);

				try {
					if (gbStart > -1 && gbEnd > -1 && plStart > -1 && plEnd > -1) {
						String definition = rawTranslation.substring(gbStart + "<GB>".length(), gbEnd).trim();
						String translation = rawTranslation.substring(plStart + "<PL>".length(), plEnd).trim();

						String cleanDef = clean(definition);
						String cleanTrans = clean(translation);

						if (cleanTrans.length() > 0) {
							translations.add(new Translation(removeXMLTags(definition), removePunctationSigns(translation)));
							found = true;
						}

						beginIndex = plEnd + 5;
					}
				} catch (StringIndexOutOfBoundsException e) {
					beginIndex = plEnd + 5;
					// System.err.println("wyjatek ;)" + plEnd);
				}
			}

			if (!found) {

			}
		}
		Collections.sort(translations, new TranslationLengthComparator());
		return translations;
	}

	public List<String> extractGBWords(String s) {
		List<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile("<GB\\b[^>]*>(.*?)</GB>");
		Matcher m = p.matcher(s);
		while (m.find()) {
			String tagContent = m.group(1);
			result.add(tagContent);
		}
		return result;
	}

	public List<String> extractPLWords(String s) {
		List<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile("<PL\\b[^>]*>(.*?)</PL>");
		Matcher m = p.matcher(s);
		while (m.find()) {
			String tagContent = m.group(1);
			result.add(tagContent);
		}
		return result;
	}

}
