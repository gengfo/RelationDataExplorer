package com.gengfo.or.common;

import java.io.IOException;
import java.util.List;

import jxl.write.Label;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import com.gengfo.excel.SheetContentBean;
import com.gengfo.excel.SheetContentBeanGenerator;
import com.gengfo.excel.WritableSheetFactory;

public class ExlOutputSheetHelper {

	static Logger logger = Logger.getLogger(ExlOutputSheetHelper.class);

	public static void output(String toFileName,
			SheetContentBean sheetContentBean) throws IOException {

		logger.debug("enter output excel");
		/**
		 * create new workbook
		 */
		WritableWorkbook workbook = WritableWorkbookFactory
				.getWorkBook(toFileName);

		WritableSheet sheet = workbook.createSheet("All Tables", 0);

		logger.debug("exit output excel");

	}

	public static void fillSheet(WritableWorkbook workbook,
			SheetContentBean scBean) {
		logger.info("to fill sheet: " + scBean.getSheetName());
		WritableSheet sheet = WritableSheetFactory.getSheet(workbook, scBean
				.getSheetName());

		fillSheet(sheet, scBean);
		
	}

	public static void fillSheet(WritableSheet sheet, SheetContentBean scBean) {

		List labelList = scBean.getLables();
		if (null != labelList && labelList.size() != 0) {
			fillSheetWithLables(sheet, labelList);
		}

		List linkList = scBean.getLinks();
		if (null != linkList && linkList.size() != 0) {
			fillSheetWithLinks(sheet, linkList);
		}

	}

	public static void fillSheetWithLables(WritableSheet ws, List lableList) {
		//logger.info("enter fillSheetWithLables for sheet " + ws.getName());
		if (null == ws || null == lableList || lableList.size() == 0) {
			//logger.info("labble list is null in in " + ws.getName());
			return;
		}

		for (int i = 0; i < lableList.size(); i++) {
			try {
				ws.addCell((Label) lableList.get(i));
				//logger.info("add cell in lablelist " + i);
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
		//logger.info("exit fillSheetWithLables for sheet " + ws.getName());
	}

	public static void fillSheetWithLinks(WritableSheet ws, List linkList) {
		//logger.info("enter fillSheetWithLinks for sheet " + ws.getName());
		if (null == ws || null == linkList || linkList.size() == 0) {
			//logger.info("Link list is null in " + ws.getName());
			return;
		}

		for (int i = 0; i < linkList.size(); i++) {
			try {
				ws.addHyperlink((WritableHyperlink) linkList.get(i));
				//logger.info("add link in linklist " + i);
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}

		//logger.info("exit fillSheetWithLinks for sheet " + ws.getName());
	}

	public static void main(String args[]) {

		SheetContentBean scBean = SheetContentBeanGenerator.getSimle();

		String toFileName = "test.xls";

	}
}
