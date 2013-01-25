package com.gengfo.excel;

import jxl.write.Label;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class CellBean {

	public String sheetName;

	public Position position;

	public String cellConent;

	public WritableHyperlink link;

	public CellBean() {

	}

	public CellBean(String sheetName, Position p, String cellContent) {
		this(sheetName, p, cellContent, null);
	}

	public CellBean(String sheetName, Position p, String cellContent,
			WritableHyperlink whl) {

		this.sheetName = sheetName;
		this.position = p;
		this.cellConent = cellContent;
		this.link = whl;

	}

	public Label getLabel() {
		return new Label(this.getPosition().x, this.getPosition().y, this
				.getCellConent());
	}

	public WritableHyperlink buildLink(WritableSheet ws) {

		WritableHyperlink link = new WritableHyperlink(this.position.x,
				this.position.y, this.getCellConent(), ws, 0, 0);
		this.link = link;
		return link;

	}

	public WritableHyperlink linkTo(WritableWorkbook workBook,
			String sheetName, CellBean toCb) {
		WritableHyperlink link = null;
		WritableSheet ws = WritableSheetFactory.getSheet(workBook, sheetName);

		if (null != toCb) {
			link = new WritableHyperlink(this.position.x, this.position.y, this
					.getCellConent(), ws, toCb.getPosition().x, toCb
					.getPosition().y);
		} else {
			link = new WritableHyperlink(this.position.x, this.position.y, this
					.getCellConent(), ws, CellConstants.DEFAULT_CELL_POSITON_X,
					CellConstants.DEFAULT_CELL_POSITON_Y);
		}
		this.link = link;
		

		return link;
	}

	public void buildLinkToNewSheet(WritableWorkbook workBook,
			String sheetName, CellBean toCb) {
		if (null == toCb) {
			// TODO: new facotry class to build workbook and sheet
			WritableSheet ws = workBook.createSheet(sheetName, 0);
			WritableHyperlink link = new WritableHyperlink(this.position.x,
					this.position.y, this.getCellConent(), ws, toCb
							.getPosition().x, toCb.getPosition().y);
		} else {

		}
		this.link = link;
	}

	// cb.buildLinkToNewSheet();

	public void addLink(WritableSheet ws, CellBean toCb) {

		return;

	}

	public String getCellConent() {
		return cellConent;
	}

	public void setCellConent(String cellConent) {
		this.cellConent = cellConent;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public WritableHyperlink getWhl() {
		return link;
	}

	public void setWhl(WritableHyperlink whl) {
		this.link = whl;
	}

	public WritableHyperlink getLink() {
		//here may return null link,so should check it
		return link;
	}

	public void setLink(WritableHyperlink link) {
		this.link = link;
	}
	
	public  String toString(){
		return this.cellConent;
	}

}
