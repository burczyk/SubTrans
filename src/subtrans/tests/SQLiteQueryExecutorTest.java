package subtrans.tests;

import org.junit.Test;

import subtrans.sqlite.SQLiteQueryExecutor;

public class SQLiteQueryExecutorTest {
	@Test
	public void testCatTranslation() {
		SQLiteQueryExecutor executor = new SQLiteQueryExecutor("dictionary/OxfordDictionary.db");
		System.out.println(executor.translate("cat"));
	}
}
