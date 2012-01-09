package subtrans.translator;

import java.util.ArrayList;
import java.util.List;

import subtrans.interfaces.ITranslator;
import subtrans.model.Translation;
import subtrans.sqlite.SQLiteQueryExecutor;

public class HistoricalTranslator implements ITranslator {
	private SQLiteQueryExecutor executor;

	public HistoricalTranslator(SQLiteQueryExecutor executor) {
		super();
		this.executor = executor;
	}

	@Override
	public List<Translation> translate(String sequence) {
		List<Translation> result = new ArrayList<Translation>();
		String translation = executor.getBestTranslation(sequence);
		if (translation != null && translation.length() > 0) {
			System.err.println("Historical translation: " + sequence + " <---> " + translation);
			result.add(new Translation(sequence, translation));
		}
		return result;
	}

}
