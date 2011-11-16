package subtrans.builders;

import java.util.LinkedList;
import java.util.List;

import subtrans.interfaces.ITranslationBase;
import subtrans.models.TextSequence;
import subtrans.models.Translation;

/**
 * 
 * @author kamilburczyk
 * Creates List<Translation> for subtitles in MicroDVD format.
 */
public class MicroDVDDBBuilder implements ITranslationBase {

	private List<TextSequence> sequencesList;
	private List<TextSequence> translationsList;
	
	public MicroDVDDBBuilder(List<TextSequence> sequencesList, List<TextSequence> translationsList) {
		this.sequencesList = sequencesList;
		this.translationsList = translationsList;
	}

	/**
	 * Method tries to match corresponding subtitiles considering startMark.
	 */
	@Override
	public List<Translation> buildDatabase() {
		List<Translation> translations = new LinkedList<Translation>();
		for(TextSequence sequence : sequencesList){
			try{
				int startMarkSeq = Integer.parseInt(sequence.getStartMark());
				int minDiff = Integer.MAX_VALUE;
				int minDiffPos = -1;
				for(TextSequence translation: translationsList){
					int startMarkTrans = Integer.parseInt(translation.getStartMark());
					if(Math.abs(startMarkSeq - startMarkTrans) < minDiff){
						minDiff = Math.abs(startMarkSeq - startMarkTrans);
						minDiffPos = translationsList.indexOf(translation);
					}
				}
				translations.add(new Translation(sequence.getSequence(), translationsList.get(minDiffPos).getSequence()));
			} catch(NumberFormatException e){
				System.err.println(String.format("Wrong format for sequence: {%s}{%s}%s", sequence.getStartMark(), sequence.getEndMark(), sequence.getSequence()));
			}
		}
		
		return translations;
	}

}
