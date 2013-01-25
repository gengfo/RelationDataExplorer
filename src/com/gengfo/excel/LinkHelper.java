package com.gengfo.excel;

import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;

public class LinkHelper {

	public static WritableHyperlink buildLink(CellBean cb, WritableSheet ws) {

		WritableHyperlink link = new WritableHyperlink(cb.position.x,
				cb.position.y, cb.getCellConent(), ws, 0, 0);

		return link;

	}

}
