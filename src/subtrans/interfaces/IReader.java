package subtrans.interfaces;

import java.util.List;

import subtrans.model.TextSequence;

/**
 * 
 * @author kamilburczyk
 * http://leksykot.top.hell.pl/lx3/B/subtitle_formats
 * Interface for objects that read file with specific subtitle format.
 */
public interface IReader {
	/**
	 * Method reads file with specific subtitle format.
	 * @return List<TextSequence> list of unified objects, that each contains start and end mark and text sequence.
	 */
	public List<TextSequence> readFile();
}
