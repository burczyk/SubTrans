package subtrans.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import subtrans.translator.DictionaryTranslator;

public class DictionaryTranslatorTest {
	@Test
	public void testCatDefinition() {
		DictionaryTranslator translator = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
		translator.translate("be quiet!");
		translator.translate("be quiet! dog");
		System.out.println(translator.translate("Adam was over six feet (tall)"));
		System.out.println(translator.translate("Give you a big hint."));
		System.out.println(translator.translate("Is it a dead animal?"));
		System.out.println(translator.translate("and bring out Emma's cake."));
		System.out.println(translator.translate("He's so new to this."));
		System.out.println(translator.translate("How's he gonna learn|if we stop his act?"));
	}

	@Test
	public void testGetCorrespondingPLTranslation() {
		DictionaryTranslator translator = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
		assertTrue(translator.getCorrespondingPLTranslation("Adam was over six feet (tall)", "metres</GB><PL> na 5;</PL><GB> Adam was over six feet (tall)</GB><PL> Adam ma ponad szesc stop (wzrostu);</PL>asdf").length() > 0);
		assertTrue(translator.getCorrespondingPLTranslation("be quiet!", "<GB> be quiet!</GB><PL> cicho bądź</PL>").length() > 0);
	}

	@Test
	public void testTranslateSequence() {
		DictionaryTranslator translator = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
		assertEquals(translator.translateSequence(Arrays.asList(new String[] { "be", "quiet!" })), "cicho bądź!;");
	}

	@Test
	public void testGetDefaultForm() {
		DictionaryTranslator translator = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
		assertEquals(translator.getDefaultForm("cat"), "cat");
		assertEquals(translator.getDefaultForm("was"), "be");
	}

	@Test
	public void testClearDefinition() {
		DictionaryTranslator translator = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
		assertEquals(translator.clean("cat<SUP>1</SUP>"), "cat");
	}

	@Test
	public void testGetBestTranslation() {
		DictionaryTranslator translator = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
		assertEquals(translator.getBestTranslation("cat").trim(), "kot");
		assertEquals(translator.getBestTranslation("and").trim(), "i");
	}

	@Test
	public void regexpTest() {
		DictionaryTranslator translator = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
		assertEquals(translator.extractGBWords("<GB>was</GB> /w?z, w?z/ pt<GB> &rarr; be</GB>").size(), 2);
	}

	@Test
	public void regexp2Test() {
		DictionaryTranslator translator = new DictionaryTranslator("dictionary/OxfordDictionaryPJN.db");
		assertEquals(translator.extractPLWords("<GB>Warsaw</GB> /'w??s??/<P>I prn<PL> Warszawa </PL> f<PL></PL></P><P>II modif<PL> warszawski</PL></P>").size(), 2);
	}

}
