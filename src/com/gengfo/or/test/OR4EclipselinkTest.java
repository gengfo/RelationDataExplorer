package com.gengfo.or.test;

import com.gengfo.common.CommonConstants;
import com.gengfo.or.OR4EclipselinkHelper;
import com.gengfo.test.main.EclipseLinkOutputDataToExcelTest;

public class OR4EclipselinkTest {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        String toFileName = "arpToExcelSheets.xls";

        String persistentUnitName = "ARPUT";

        String mappingType = CommonConstants.MAPPING_TYPE_ECLIPSELINK;

        // DataOutputHelper4EclipseLink.outputDataEclipseLink("ARP_Invoice",
        // "INV_OID", "139", toFileName, mappingType, persistentUnitName);

        // DataOutputHelper4EclipseLink.outputDataEclipseLink("ARP_Payment", "pmt_oid", "1", toFileName, mappingType,
        // persistentUnitName);

        // DataOutputHelper4EclipseLink.outputDataEclipseLink("SPS_JobOrder",
        // "JOB_ORDER_ID", "100004123", toFileName, mappingType, persistentUnitName);

        // SPS_JobCostItem
        // JOB_COST_ITEM_ID
        // DataOutputHelper4EclipseLink.outputDataEclipseLink("SPS_JobCostItem",
        // "JOB_COST_ITEM_ID", "100000037", toFileName, mappingType, persistentUnitName);

        OR4EclipselinkHelper.outputDataEclipseLink("ARP_InvoiceMatchItem", "MATCH_ITEM_OID", "5461", toFileName,
                mappingType, persistentUnitName);

        long endTime = System.currentTimeMillis();
        EclipseLinkOutputDataToExcelTest.log.debug("Done in " + (endTime - startTime) / 1000 + " seconds");

    }

}
