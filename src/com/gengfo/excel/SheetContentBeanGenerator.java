package com.gengfo.excel;


public class SheetContentBeanGenerator {
	
	
	public static SheetContentBean getSimle(){
		SheetContentBean scBean = new SheetContentBean();
		
		CellBean cb = CellBeanGenerator.getCellBeanWithoutLink();
		
		scBean.cellsList.add(cb);

		scBean.setSheetName("simpleSheetName");
		
		return scBean;
		
	}

	public static SheetContentBean getAnother(){
		SheetContentBean scBean = new SheetContentBean();
		
		CellBean cb = CellBeanGenerator.getCellBeanWithLink();
		
		scBean.cellsList.add(cb);

		scBean.setSheetName("anotherSheetName");
		
		return scBean;
		
	}

}
