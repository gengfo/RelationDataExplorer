package com.gengfo.excel;

import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class WritableSheetFactory {

	// TODO: to form sigleton pattern for the factory
	static Logger logger = Logger.getLogger(WritableSheetFactory.class);

	public static WritableSheet getSheet(WritableWorkbook wwb, String sheetName) {

		logger.debug("enter WritableSheetFactory.getSheet " + sheetName);
		
		//if sheet too long
		if (sheetName.length() >10){
			sheetName = sheetName.substring(sheetName.length()-10);
		}

		if (null == wwb || StringUtils.isEmpty(sheetName)) {
			return null;
		}

		if (null != wwb.getSheet(sheetName)) {
			return wwb.getSheet(sheetName);
		} else {
			return wwb.createSheet(sheetName, 0);
		}
		
		

	}

}
