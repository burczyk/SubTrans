package subtrans.helper;

import java.io.File;
import java.io.FilenameFilter;

public class TheSameFileNameFilter implements FilenameFilter {
	private String originalName;
	
	public TheSameFileNameFilter(String originalName) {
		this.originalName = originalName;
	}

	@Override
	public boolean accept(File dir, String name) {
		if(name.equals(originalName)){
			return true;
		}
		return false;
	}

}
