package com.gengfo.excel.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import oracle.toplink.publicinterface.Descriptor;
import oracle.toplink.sessions.Project;

import org.apache.log4j.Logger;

import com.gengfo.excel.CellBean;
import com.gengfo.excel.ToplinkDescriptorToExcel;
import com.gengfo.excel.EclipselinkDescriptorToExcel;
import com.gengfo.excel.Position;
import com.gengfo.excel.SheetBaseTestCase;
import com.gengfo.excel.SheetContentBean;
import com.gengfo.mapping.toplink.ToplinkMappingHelper;
import com.gengfo.or.common.ExlOutputSheetHelper;
import com.gengfo.or.common.WritableWorkbookFactory;


public class DescriptorToExcelTest extends SheetBaseTestCase {

	static Logger logger = Logger.getLogger(DescriptorToExcelTest.class);

	public void ptestToSheetContentBeanUser() throws IOException,
			WriteException {

		logger.debug("enter DescriptorToExcelTest");

		toFileName = "DescriptorToExcelTest.xls";

		WritableWorkbook wwb = WritableWorkbookFactory.getWorkBook(toFileName);

		Descriptor userDesc = ToplinkMappingHelper.getDescriptor("User");

		SheetContentBean scBean = EclipselinkDescriptorToExcel.toSheetContentBean(wwb,
				userDesc);

		ExlOutputSheetHelper.fillSheet(wwb, scBean);

		wwb.write();
		wwb.close();

		logger.debug("exit DescriptorToExcelTest");

	}

	public void ptestToSheetContentBeanForAlias() throws IOException, WriteException {

		logger.debug("enter DescriptorToExcelTest");

		String aliasName = "RepositoryCharge";
		
		toFileName = "DescriptorToExcelTest.xls";

		WritableWorkbook wwb = WritableWorkbookFactory.getWorkBook(toFileName);

		// /
		Project theProject = ToplinkMappingHelper.getIvoProject();
		Map aliasDescMap = theProject.getAliasDescriptors();
		Set keySet = aliasDescMap.keySet();
		Iterator keySetIt = keySet.iterator();

		SheetContentBean scBeanAll = new SheetContentBean();
		// scBeanAll
		
		int i = 0;
		//while (keySetIt.hasNext()) {
			Descriptor descriptor = (Descriptor) aliasDescMap.get(aliasName);
			SheetContentBean scBean = EclipselinkDescriptorToExcel.toSheetContentBean(wwb,
					descriptor);
			ExlOutputSheetHelper.fillSheet(wwb, scBean);
			logger.debug("debug");

			CellBean cb = new CellBean();
			cb.setCellConent(scBean.getSheetName());
			cb.setPosition(new Position(1, i));
			i = i + 1;

			cb.linkTo(wwb, scBean.getSheetName(), null);
			scBeanAll.cellsList.add(cb);
			scBeanAll.setSheetName("AllAlias");
			// scBeanAll
		//}

		ExlOutputSheetHelper.fillSheet(wwb, scBeanAll);
		

		wwb.write();
		wwb.close();

		logger.debug("exit DescriptorToExcelTest");

	}
	
	public void testToExcelSheets(){
		logger.debug("enter testToExcelSheets"); 
		
		toFileName = "testToExcelSheets.xls";
		Project theProject = ToplinkMappingHelper.getIvoProject();
		ToplinkDescriptorToExcel.toExcelSheets(toFileName,theProject);
		
		logger.debug("exit testToExcelSheets");
		
	}

}
