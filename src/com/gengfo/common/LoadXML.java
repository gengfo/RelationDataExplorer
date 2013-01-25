package com.gengfo.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class LoadXML {

	public static String loadXml2String(String filePath) throws FileNotFoundException {
		String content = null;

		File f = new File(filePath);
		InputStream xml = new FileInputStream(f);
		
		return content;
		
	}

}
