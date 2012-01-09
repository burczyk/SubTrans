package subtrans.test;

import java.io.File;

import org.junit.Test;

import subtrans.builder.GeneralDBBuilder;
import subtrans.exception.NotADirectoryException;

public class GeneralDBBuilderTest {
	@Test
	public void testBuilder() {
		GeneralDBBuilder builder = new GeneralDBBuilder(new File("subtitles/eng"), new File("subtitles/pl"));
		try {
			builder.buildDatabase();
		} catch (NotADirectoryException e) {
			e.printStackTrace();
		}
	}
}
