package com.gengfo.test.eclipselink;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.gengfo.excel.EclipselinkDescriptorToExcel;

public class EclipseLinkMapping2ExcelTest {
	
	public static void main(String[] args){
	
	String toFileName = "arpToExcelSheets.xls";
	
	EntityManagerFactory emf = Persistence
			.createEntityManagerFactory("ARPUT");
	
	
		EclipselinkDescriptorToExcel.toEclipselinkExcelSheets(toFileName,emf);
		
		System.out.println("Done");
	
	}
	

}
