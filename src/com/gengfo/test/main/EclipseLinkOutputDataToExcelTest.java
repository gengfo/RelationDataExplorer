package com.gengfo.test.main;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gengfo.common.CommonConstants;
import com.gengfo.mapping.eclipselink.DataOutputHelper4EclipseLink;

public class EclipseLinkOutputDataToExcelTest {
	
	private static Logger log = LogManager
			.getLogger(DataOutputHelper4EclipseLink.class);


	public static void main(String[] args) {

		String toFileName = "arpToExcelSheets.xls";

		String persistentUnitName = "ARPUT";

		String mappingType = CommonConstants.MAPPING_TYPE_ECLIPSELINK;

		//DataOutputHelper4EclipseLink.outputDataEclipseLink("ARP_Invoice",
		//		"INV_OID", "139", toFileName, mappingType, persistentUnitName);
		
		DataOutputHelper4EclipseLink.outputDataEclipseLink("ARP_Payment",
				"pmt_oid", "1", toFileName, mappingType, persistentUnitName);

		
		

		log.debug("Done");

	}

}
