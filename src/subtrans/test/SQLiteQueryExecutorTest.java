package subtrans.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import subtrans.sqlite.SQLiteQueryExecutor;

public class SQLiteQueryExecutorTest {
	@Test
	public void testCatTranslation() {
		SQLiteQueryExecutor executor = new SQLiteQueryExecutor("dictionary/OxfordDictionaryPJN.db");
		assertNotNull(executor.translate("cat"));
	}
}
