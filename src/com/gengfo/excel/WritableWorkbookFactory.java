package com.gengfo.excel;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

public class WritableWorkbookFactory {

	// TODO: to form sigleton pattern for the factory
	static Logger logger = Logger.getLogger(WritableWorkbookFactory.class);

	public static WritableWorkbook getWorkBook(String fileName)
			throws IOException {

		logger.debug("enter WritableSheetFactory.getSheet " + fileName);

		WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));

		return workbook;

	}

}
