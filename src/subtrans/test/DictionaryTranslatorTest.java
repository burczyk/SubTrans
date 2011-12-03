package subtrans.test;

import org.junit.Test;

import subtrans.translator.DictionaryTranslator;

public class DictionaryTranslatorTest {
	@Test
	public void testCatDefinition() {
		DictionaryTranslator translator = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
		translator.translate("cat");
	}
}
