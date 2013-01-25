package com.gengfo.excel.test;

import java.util.ArrayList;
import java.util.List;

import com.gengfo.excel.EclipselinkDescriptorToExcel;

public class SimpleDate2ExcelTest {

	public static void main(String[] args) {

		List<String> keyList = new ArrayList<String>();

		keyList.add("ARNote-OID-50481");
		keyList.add("ServiceOffice-OID-54");

		String toFileName = "toExcelSheetsWithData.xls";

		EclipselinkDescriptorToExcel.toExcelSheetsWithData(toFileName, keyList);
		
		
		System.out.println("Done");

	}

}
