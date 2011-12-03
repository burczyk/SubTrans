package subtrans.comparator;

import java.util.Comparator;

import subtrans.model.Translation;

/**
 * 
 * @author kamilburczyk
 * 
 *         Compares translations according to their occurences value: higher
 *         occurences is better.
 */
public class TranslationComparator implements Comparator<Translation> {

	@Override
	public int compare(Translation t1, Translation t2) {
		if (t1.getOccurences() > t2.getOccurences()) {
			return -1;
		} else if (t1.getOccurences() == t2.getOccurences()) {
			return 0;
		} else {
			return 1;
		}
	}

}
