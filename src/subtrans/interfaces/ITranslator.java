package subtrans.interfaces;

import subtrans.models.Translation;

public interface ITranslator {
	Translation translate(String sequence);
}
