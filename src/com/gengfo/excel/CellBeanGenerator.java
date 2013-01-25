package com.gengfo.excel;


public class CellBeanGenerator {

	public static CellBean getCellBeanWithoutLink() {

		CellBean cb = new CellBean();
		Position p = new Position(2, 3);
		cb.setPosition(p);
		cb.setCellConent("simple cell");
		
		return cb;

	}

	public static CellBean getCellBeanWithLink() {

		CellBean cb = new CellBean();
		Position p = new Position(1, 1);
		cb.setPosition(p);
		cb.setCellConent("has link cell");
		
		return cb;

	}

}
