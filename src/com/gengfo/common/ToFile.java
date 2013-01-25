package com.gengfo.common;

import java.io.FileWriter;
import java.io.IOException;

public class ToFile {
	private String basicPath;

	public String getBasicPath() {
		return basicPath;
	}

	public void setBasicPath(String basicPath) {
		this.basicPath = basicPath;
	}

	public void trans2File(String content, String relFileName)
			throws IOException {

		// FileWriter fos = new FileOutputStream(basicPath + relFileName);
		FileWriter fos = new FileWriter(basicPath + relFileName);
		fos.write(content);
		fos.flush();
		fos.close();

	}

	public void trans2File(Object obj, String relFileName) throws IOException {
		String content = ToStringBuilder.toString(obj);
		trans2File(content, relFileName);
	}
}
