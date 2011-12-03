package subtrans.reader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import subtrans.interfaces.IReader;
import subtrans.model.TextSequence;

/**
 * 
 * @author kamilburczyk
 *
 * Reads subtitles in format: '{' start '}' '{' stop '}' tekst [ '|' tekst ... ]
 * {5222}{5271}Wiesz co będzie pierwszą rzeczą,|jaką zrobię po powrocie do domu?
 * {5272}{5292}Pozbędziesz się pryszczy?
 */
public class MicroDVDReader implements IReader {
	
	private File file;
	
	public MicroDVDReader(File file) {
		this.file = file;
	}

	@Override
	public List<TextSequence> readFile() {
		List<TextSequence> sequencesList = new LinkedList<TextSequence>();
		GeneralFileReader generalReader = new GeneralFileReader(file);
		List<String> listOfLines = generalReader.readFile();
		String startMark;
		String endMark;
		String sequence;
		
		for(String line : listOfLines){
			line = line.trim();	//removes spaces in the beginning and in the end
			if(!"".equals(line)){
				try{
					startMark = line.substring(1, line.indexOf("}"));
					line = line.substring(line.indexOf("}") + 2);
					endMark = line.substring(0, line.indexOf("}"));
					sequence = line.substring(line.indexOf("}") + 1).replaceAll("\\{.+\\}", ""); //sometimes subtitles look like {5272}{5292}{y:i}ala123 and {y:i} if formatting tag, so we can remove it for translation purposes.
					sequencesList.add(new TextSequence(startMark, endMark, sequence));
				} catch(StringIndexOutOfBoundsException e){
					System.err.println("Something wrong in file: " + file.getName() + " sequence: " + line);
				}
			}
		}
		
		return sequencesList;
	}

}
