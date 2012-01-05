package subtrans.comparator;

import java.util.Comparator;

import subtrans.model.Translation;

/**
 * 
 * @author kamilburczyk
 * 
 *         Compares translations according to the count of words in translation string: lower is better.
 */
public class TranslationLengthComparator implements Comparator<Translation> {
	@Override
	public int compare(Translation t1, Translation t2) {
		int t1Words = t1.getTranslation().split(" ").length;
		int t2Words = t2.getTranslation().split(" ").length;

		if (t1Words > t2Words) {
			return 1;
		} else if (t1Words == t2Words) {
			return 0;
		} else {
			return -1;
		}
	}
}
