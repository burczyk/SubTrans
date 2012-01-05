package subtrans.test;

import java.util.Arrays;

import org.junit.Test;

import subtrans.translator.DictionaryTranslator;

public class DictionaryTranslatorTest {
	@Test
	public void testCatDefinition() {
		DictionaryTranslator translator = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
		// translator.translate("cat");
		System.out.println(translator.translate("The tox screen was clean"));
	}

	@Test
	public void testListOfPossibleTranslations() {
		DictionaryTranslator translator = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
		System.out.println(translator.listOfPossibleTranslations(Arrays.asList(new String[] { "was" })));
	}

	@Test
	public void regexpTest() {
		DictionaryTranslator translator = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
		System.out.println(translator.extractGBWords("<GB>was</GB> /w?z, w?z/ pt<GB> &rarr; be</GB>"));
	}

	@Test
	public void regexp2Test() {
		DictionaryTranslator translator = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
		System.out.println(translator.extractPLWords("<GB>Warsaw</GB> /'w??s??/<P>I prn<PL> Warszawa </PL> f<PL></PL></P><P>II modif<PL> warszawski</PL></P>"));
	}
}
