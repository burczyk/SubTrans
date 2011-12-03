package subtrans.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GeneralFileReader {
	private File file;

	public GeneralFileReader(File file) {
		this.file = file;
	}
	
	public List<String> readFile(){
		BufferedReader br;
		String line;
		List<String> listOfLines = new LinkedList<String>();
		
		try {
			br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null){
				listOfLines.add(line);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return listOfLines;
	}
}
