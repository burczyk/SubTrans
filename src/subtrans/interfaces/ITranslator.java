package subtrans.interfaces;

import java.util.List;

import subtrans.model.Translation;

public interface ITranslator {
	List<Translation> translate(String sequence);
}
