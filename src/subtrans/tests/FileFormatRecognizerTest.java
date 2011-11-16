package subtrans.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import subtrans.helpers.FileFormatRecognizer;
import subtrans.models.FileFormat;

public class FileFormatRecognizerTest {
	@Test
	public void recognizeMicroDVDTest(){
		assertEquals(FileFormatRecognizer.getFileFormat(new File("subtitles/eng/house_s01e01.txt")), FileFormat.MICRO_DVD);
	}
	
	@Test
	public void recognizeNotRecognizedTest(){
		assertEquals(FileFormatRecognizer.getFileFormat(new File("src/subtrans/interfaces/IReader.java")), FileFormat.NOT_RECOGNIZED);
	}
}
