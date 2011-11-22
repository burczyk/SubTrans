package subtrans.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

import subtrans.models.TextSequence;
import subtrans.readers.MicroDVDReader;
import subtrans.readers.TmpReader;

public class ReadersTest {
	
	@Test
	public void testMicroDVDReader(){
		MicroDVDReader reader = new MicroDVDReader(new File("subtitles/pl/house.1x01.pilot.ws_dvdrip_xvid-fov.txt"));
		List<TextSequence> sequencesList = reader.readFile();
		for(TextSequence sequence: sequencesList){
			System.out.println(String.format("{%s}{%s}%s", sequence.getStartMark(), sequence.getEndMark(), sequence.getSequence()));
		}
		assertEquals(sequencesList.size(), 625);
		assertEquals(sequencesList.get(2).getStartMark(), "1175");
	}
	
	@Test
	public void testTmpReader(){
		TmpReader reader = new TmpReader(new File("subtitles/new/House.S08E06.HDTV.XviD-LOL.[VTV].avi-ENG.txt"));
		List<TextSequence> sequencesList = reader.readFile();
		for(TextSequence sequence: sequencesList){
			System.out.println(String.format("%s%s%s", sequence.getStartMark(), sequence.getEndMark(), sequence.getSequence()));
		}
		assertEquals(sequencesList.size(), 887);
		assertEquals(sequencesList.get(2).getStartMark(), "00:00:05:");
	}	
}
