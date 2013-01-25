package com.gengfo.excel;

import java.util.ArrayList;
import java.util.List;

import jxl.write.Label;
import jxl.write.WritableHyperlink;

/**
 * <li>the bean contain the content for descriptors to output to excels
 * <li>sheetName: the sheet name
 * <li>cellList: the lables should show in the sheet linkList: add the links to
 * cells
 * 
 * 
 * @author GENGFO
 * 
 */
public class SheetContentBean {

	public SheetContentBean() {
		this.sheetName = "sheet1";
		this.cellsList = new ArrayList();
		this.linkList = new ArrayList();
	}

	String sheetName;

	/**
	 * the cellsList contains list of cellBean
	 */
	public List cellsList;

	/**
	 * the linkList contain list of **Link
	 * 
	 * @return
	 */
	public List linkList;

	public List getCellBeanList() {
		return cellsList;
	}

	public void setCellsMap(List cellsMap) {
		this.cellsList = cellsMap;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List getLables() {
		List rList = new ArrayList();

		for (int i = 0; i < cellsList.size(); i++) {
			CellBean cb = (CellBean) cellsList.get(i);
			Label aLable = cb.getLabel();

			rList.add(aLable);

		}

		return rList;

	}

	public List getLinks() {
		List rList = new ArrayList();

		for (int i = 0; i < cellsList.size(); i++) {
			CellBean cb = (CellBean) cellsList.get(i);
			WritableHyperlink link = cb.getLink();
			if (null != link) {
				rList.add(link);
			}
		}

		return rList;
	}

}
