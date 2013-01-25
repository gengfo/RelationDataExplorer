package com.gengfo.excel;

import java.util.ArrayList;
import java.util.List;

import jxl.write.WritableSheet;

public class WorkbookContentBean {

	/**
	 * to contain the writable sheet
	 */

	List sheetContentBeanList;

	public WorkbookContentBean() {

		this.sheetContentBeanList = new ArrayList();

	}

	public void addSheet(WritableSheet ws) {

		sheetContentBeanList.add(ws);

	}

	public void removeSheet(WritableSheet ws) {
		// TODO: gengfo

		// sheetContentBeanList.remove(ws);

	}

}
