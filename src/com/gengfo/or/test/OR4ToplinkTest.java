package com.gengfo.or.test;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gengfo.mapping.utils.DBConstants;
import com.gengfo.mapping.utils.CommonMappingHelper;
import com.gengfo.or.OR4ToplinkHelper;


public class OR4ToplinkTest {
    
    public static Logger log = LogManager.getLogger(OR4ToplinkTest.class);

    public static void main(String args[]) {

        long startTime = System.currentTimeMillis();
    
        CommonMappingHelper.dbEnv = DBConstants.DB_DEV;
        String toFileName = "ipsToExcelSheets.xls";
        
        //sample search 
        //OR4ToplinkHelper.outputDataToplink("ARNote", "oid", "1536", toFileName);
        
        //OR4ToplinkHelper.outputDataToplink("CascadeNumRange", "oid", "1536", toFileName);
        
        //OR4ToplinkHelper.outputDataToplink("BussinessGroup", "ID", "101001", toFileName);
        //OR4ToplinkHelper.outputDataToplink("BussinessGroup", "ID", "70001", toFileName);
        
        OR4ToplinkHelper.outputDataToplink("ChargeCode", "ID", "365", toFileName);  //to test many to may with this case
        
    
        long endTime = System.currentTimeMillis();
        log.debug("Done in " + (endTime - startTime) / 1000 + " seconds");
    
    }

}
