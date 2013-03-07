package com.gengfo.test.toplink;

import com.gengfo.mapping.utils.DBConstants;
import com.gengfo.mapping.utils.CommonMappingHelper;
import com.gengfo.or.OR4ToplinkHelper;

public class ToplinkShowRepositoryChargeInDbTest {

	private static String theTableName = DBConstants.TB_AR_INV_CHG_POOL;
	//private static String theTableName = DBConstants.TB_AR_INV_REF_TPMC;

	public static void main(String args[]) {
		//DBConstants.TB_AR_INV_CHG_POOL
		//String tbOid = "22168";
		//DBHelperR0202.getTableValue(theTableName, tbOid);
		//DBHelperR0202.showTableAndRelContent(theTableName, tbOid);

		//DBConstants.TB_AR_INV_CHG_POOL
		//String tbOid = "29468";
		String tbOid = "87739";
		
		CommonMappingHelper.retrieveSingleTableContent(theTableName, null, tbOid);
		OR4ToplinkHelper.outputDataToplink(theTableName, null, tbOid, null);
		 
		// //DBConstants.TB_AR_INV_REF_TPMC
		// String tbOid = "52531";
		// DBHelperR0202.getTableValue(theTableName, tbOid);
		// DBHelperR0202.showTableAndRelContent(theTableName, tbOid);
		
		System.out.println("Done!");

	}

}
