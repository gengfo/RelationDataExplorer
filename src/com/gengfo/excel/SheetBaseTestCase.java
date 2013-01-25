package com.gengfo.excel;

import junit.framework.TestCase;

public abstract class SheetBaseTestCase extends TestCase {

	public static SheetContentBean scBean = null;
	public static String toFileName = null;

	protected void setUp() {
		toFileName = "test.xls";
		
		
		
		SheetContentBean scBean = SheetContentBeanGenerator.getSimle();
	}

	protected void tearDown() {
		scBean = null;
	}

}
