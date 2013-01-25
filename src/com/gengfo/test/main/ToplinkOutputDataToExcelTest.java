package com.gengfo.test.main;

import com.gengfo.common.CommonConstants;
import com.gengfo.mapping.utils.DBConstants;
import com.gengfo.mapping.utils.MappingHelper;

public class ToplinkOutputDataToExcelTest {

	private static String theTableName = DBConstants.TB_AR_INV_NOTE;

	private static String theDescriptorName = "ARNote";

	public static void main(String args[]) {

		MappingHelper.dbEnv = DBConstants.DB_DEV;
		
		//MappingHelper.mappingType = CommonConstants.MAPPING_TYPE_TOPLINK;
		
	
		String toFileName = "toExcelSheetsWithData.xls";
		MappingHelper.outputDataToplink("ARNote", "oid", "1950711", toFileName);

		System.out.println("Done!");

	}

}
