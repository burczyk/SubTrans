package subtrans.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

import subtrans.models.TextSequence;
import subtrans.readers.MicroDVDReader;

public class ReadersTest {
	
	@Test
	public void testMicroDVDReader(){
		MicroDVDReader reader = new MicroDVDReader(new File("subtitles/pl/house_s01e01.txt"));
		List<TextSequence> sequencesList = reader.readFile();
		for(TextSequence sequence: sequencesList){
			System.out.println(String.format("{%s}{%s}%s", sequence.getStartMark(), sequence.getEndMark(), sequence.getSequence()));
		}
		assertEquals(sequencesList.size(), 625);
		assertEquals(sequencesList.get(2).getStartMark(), "1175");
	}
}
