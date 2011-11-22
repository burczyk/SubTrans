package subtrans.readers;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import subtrans.interfaces.IReader;
import subtrans.models.TextSequence;

/**
 * 
 * @author kamilburczyk
 *
 * Reads subtitles in format: start ':' tekst [ '|' tekst ... ]
 * 00:05:22:Wiesz co będzie pierwszą rzeczą|jaką zrobię po powrocie do domu?
 * 00:05:24:Pozbędziesz się pryszczy?
 */
public class TmpReader implements IReader {

	private File file;
	
	public TmpReader(File file) {
		super();
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
					startMark = line.substring(0, 9);
					endMark = "";
					sequence = line.substring(9, line.length());
					sequencesList.add(new TextSequence(startMark, endMark, sequence));
				} catch(StringIndexOutOfBoundsException e){
					System.err.println("Something wrong in file: " + file.getName() + " sequence: " + line);
				}
			}
		}
		
		return sequencesList;
	}

}
