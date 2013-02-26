package com.gengfo.or.test;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gengfo.mapping.utils.DBConstants;
import com.gengfo.mapping.utils.MappingHelper;
import com.gengfo.or.OR4ToplinkHelper;


public class OR4ToplinkTest {
    
    public static Logger log = LogManager.getLogger(OR4ToplinkTest.class);

    public static void main(String args[]) {
        
        
        
        long startTime = System.currentTimeMillis();
    
        MappingHelper.dbEnv = DBConstants.DB_DEV;
    
        // MappingHelper.mappingType = CommonConstants.MAPPING_TYPE_TOPLINK;
    
        String toFileName = "ipsToExcelSheets.xls";
        OR4ToplinkHelper.outputDataToplink("ARNote", "oid", "1536", toFileName);
    
        long endTime = System.currentTimeMillis();
        log.debug("Done in " + (endTime - startTime) / 1000 + " seconds");
    
    }

}
