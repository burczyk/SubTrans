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
		if (words.length > 1) {
			String wholeSequenceTranslated = translateSequence(Arrays.asList(words));
			if (wholeSequenceTranslated != null) {
				translations.add(new Translation(sequence, wholeSequenceTranslated));
			}
		} else { // words.length == 1
			translations.add(new Translation(sequence, getBestTranslation(words[0])));
		}
		return translations;
	}

	public String translateSequence(List<String> originalSequence) {
		String result;
		String sequenceString = listToString(originalSequence);
		for (String word : originalSequence) {
			List<String> rawTranslations = executor.translate(getDefaultForm(word)); // translation for each word
			for (String rawTranslation : rawTranslations) {
				result = getCorrespondingPLTranslation(sequenceString, rawTranslation);
				if (result != null) {
					System.err.println("Sequence: " + sequenceString + " <---> " + result);
					return result;
				}
			}
		}

		return null;
	}

	private String listToString(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for (String s : list) {
			sb.append(s);
			sb.append(" ");
		}
		return sb.toString().trim();
	}

	public String getBestTranslation(String word) {
		if (!notTranslateableWords.contains(word.toLowerCase())) {
			word = removePunctationSigns(word.toLowerCase());
			List<String> rawTranslations = executor.translate(word); // XML that contains definition for given word
			if (rawTranslations.size() > 0) {
				List<String> possibleTranslations = new ArrayList<String>();
				for (String rawTranslation : rawTranslations) {
					possibleTranslations.addAll(extractPLWords(rawTranslation));
				}
				if (possibleTranslations.size() > 0) {
					return possibleTranslations.get(0) + " ";
				}

			} else {
				// When there is no polish translation it means, that word leads to another version, e.g. was --> be
				for (String rawTranslation : rawTranslations) {
					List<String> listOfVersions = extractGBWords(rawTranslation); // other versions of given word
					for (String version : listOfVersions) {
						if (!version.equals(word)) {
							// System.out.println("wywolanie rekurencyjne dla slowa: " + version);
							return getBestTranslation(version);
						}
					}
				}
				return word + " ";
			}
		} else {
			return " ";
		}
		return word + " ";
	}

	// returns default form of given word, e.g. was --> be; cat --> cat
	public String getDefaultForm(String word) {
		List<String> rawTranslations = executor.translate(word);
		for (String rawTranslation : rawTranslations) {
			if (hasTranslation(rawTranslation)) {
				return word;
			} else {
				List<String> listOfVersions = extractGBWords(rawTranslation); // other versions of given word
				for (String version : listOfVersions) {
					if (!version.equals(word)) {
						return getDefaultForm(version);
					}
				}
			}
		}
		return word;
	}

	public String clean(String s) {
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

	private String removeSpecialCharacters(String s) {
		return s.replaceAll("&\\w+;", "").trim();
	}

	public List<Translation> listOfPossibleTranslations(List<String> rawTranslations) {
		List<Translation> translations = new ArrayList<Translation>();
		for (String rawTranslation : rawTranslations) {
			List<String> definitions = extractGBWords(rawTranslation);
			for (String definition : definitions) {
				String correspondingTranslation = getCorrespondingPLTranslation(definition, rawTranslation);
				if (correspondingTranslation != null && correspondingTranslation.length() > 0) {
					translations.add(new Translation(removeXMLTags(definition), removePunctationSigns(correspondingTranslation)));
				}
			}
		}
		Collections.sort(translations, new TranslationLengthComparator());
		return translations;
	}

	public String getCorrespondingPLTranslation(String gbSequence, String rawTranslation) {
		Pattern p = Pattern.compile("<GB\\b[^>]*>\\s*" + addSlashes(gbSequence) + "</GB><PL>(.*?)</PL>");
		Matcher m = p.matcher(rawTranslation);
		if (m.find()) {
			if (m.group(1) != null) {
				String tagContent = removeSpecialCharacters(m.group(1));
				if (tagContent.length() > 0) {
					return tagContent.trim();
				}
			}
		}
		return null;
	}

	private String addSlashes(String s) {
		String[] signs = new String[] { "(", ")", "[", "]", "?", "!", ".", ",", ":" };
		for (String sign : signs) {
			s = s.replace(sign, "\\" + sign);
		}
		return s;
	}

	public List<String> extractGBWords(String s) {
		List<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile("<GB\\b[^>]*>(.*?)</GB>");
		Matcher m = p.matcher(s);
		while (m.find()) {
			String tagContent = clean(m.group(1)).trim();
			if (tagContent != null && tagContent.length() > 0) {
				result.add(tagContent);
			}
		}
		return result;
	}

	public List<String> extractPLWords(String s) {
		List<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile("<PL\\b[^>]*>(.*?)</PL>");
		Matcher m = p.matcher(s);
		while (m.find()) {
			String tagContent = removeSpecialCharacters(m.group(1));
			if (tagContent.length() > 0) {
				result.add(tagContent);
			}
		}
		return result;
	}

	public boolean hasTranslation(String s) {
		Pattern p = Pattern.compile("<PL\\b[^>]*>(.*?)</PL>");
		Matcher m = p.matcher(s);
		if (m.find()) {
			return true;
		} else {
			return false;
		}
	}

}
