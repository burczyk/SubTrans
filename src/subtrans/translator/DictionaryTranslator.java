package subtrans.translator;

import java.util.List;

import subtrans.interfaces.ITranslator;
import subtrans.model.Translation;
import subtrans.sqlite.SQLiteQueryExecutor;

public class DictionaryTranslator implements ITranslator {

	private SQLiteQueryExecutor executor;

	public DictionaryTranslator(String databaseFileName) {
		super();
		this.executor = new SQLiteQueryExecutor(databaseFileName);
	}

	@Override
	public List<Translation> translate(String sequence) {
		List<String> rawTranslations = executor.translate(sequence);
		System.out.println(rawTranslations);
		return null;
	}

}
