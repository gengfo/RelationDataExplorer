package com.gengfo.excel;

import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;

/**
 * WritableHyperlink backLink = new WritableHyperlink(0, 0, tableName, sheet, 0,
 * j);
 * 
 * usage: int col, int row, String desc, WritableSheet sheet, int destcol, int
 * destrow
 * 
 */
public class LinkBean {

	CellBean sourceCell;

	CellBean desinationCell;

	String description;

	WritableSheet sheet;

	WritableHyperlink link;

	public LinkBean(CellBean sourceCell, CellBean desinationCell,
			WritableSheet sheet, String description) {

		this.sourceCell = sourceCell;
		this.sheet = sheet;
		this.desinationCell = desinationCell;
		this.description = description;

	}

	public WritableHyperlink getLink() {

		WritableHyperlink backLink = new WritableHyperlink(
				this.sourceCell.position.x, this.sourceCell.position.y,
				this.description, this.sheet, this.desinationCell.position.x,
				this.desinationCell.position.y);
		return backLink;

	}

}
