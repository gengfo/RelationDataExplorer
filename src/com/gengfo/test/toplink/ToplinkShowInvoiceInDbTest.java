package com.gengfo.test.toplink;

import com.gengfo.mapping.utils.DBConstants;
import com.gengfo.mapping.utils.CommonMappingHelper;
import com.gengfo.or.OR4ToplinkHelper;



public class ToplinkShowInvoiceInDbTest {
	
	private static String theTableName = DBConstants.TB_AR_INV_NOTE;
	
	private static String theDescriptorName = "ARNote";

	public static void main(String args[]) {
		//String tbOid = "36861";
		//String tbOid = "26411";
		//String tbOid = "405";
		//String tbOid = "35811";
		//String tbOid = "35761";
		//String tbOid = "41427";
		//String tbOid = "41426";
		//String tbOid = "7424";
		//String tbOid = "40226";
		//String tbOid = "45961";
		//String tbOid = "43252";
		CommonMappingHelper.dbEnv = DBConstants.DB_DEV;
		String tbOid = "50481";
		//String tbOid = "64983";
		
		//DBHelperR0202.retrieveSingleTableContent(theTableName, "oid", tbOid);

		OR4ToplinkHelper.outputDataToplink(theDescriptorName, "oid", tbOid, null);

		System.out.println("Done!");

	}

}
