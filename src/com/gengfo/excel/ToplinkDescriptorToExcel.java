package com.gengfo.excel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Metamodel;


import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import oracle.toplink.descriptors.RelationalDescriptor;
import oracle.toplink.internal.helper.DatabaseTable;
import oracle.toplink.mappings.DatabaseMapping;
import oracle.toplink.mappings.DirectToFieldMapping;
import oracle.toplink.mappings.ManyToManyMapping;
import oracle.toplink.mappings.OneToManyMapping;
import oracle.toplink.mappings.OneToOneMapping;
import oracle.toplink.publicinterface.Descriptor;
import oracle.toplink.sessions.Project;

import org.apache.log4j.Logger;

import com.gengfo.common.CommonConstants;
import com.gengfo.mapping.toplink.ToplinkMappingHelper;
import com.gengfo.mapping.utils.DataHolder;
import com.gengfo.mapping.utils.MappingHelper;

public class ToplinkDescriptorToExcel {

	public static Logger logger = Logger.getLogger(ToplinkDescriptorToExcel.class);

	/**
	 * descriptor structure <li>
	 * 
	 * @param wwb
	 *            TODO
	 * @param wb
	 * @param descriptor
	 */

	public static SheetContentBean toSheetContentBean(WritableWorkbook wwb,
		Descriptor desriptor) {
		SheetContentBean scBean = new SheetContentBean();

		buildHeaderCellBeans(desriptor, scBean);

		buildMappings(desriptor, wwb, scBean);

		return scBean;
	}
	
	public static SheetContentBean appendSheetContentBean(WritableWorkbook wwb,
			Descriptor desriptor, SheetContentBean scBean ) {
			//SheetContentBean scBean = new SheetContentBean();

			//buildHeaderCellBeans(desriptor, scBean);

			buildMappings(desriptor, wwb, scBean);

			return scBean;
		}
	
	
	public static SheetContentBean toSheetContentBeanWithData(
			WritableWorkbook wwb, String key) {
		SheetContentBean scBean = new SheetContentBean();

		buildHeaderCellBeansWithData(key, scBean);

		buildData(key, wwb, scBean);

		return scBean;
	}

	public static void buildHeaderCellBeans(Descriptor desriptor,
			SheetContentBean scBean) {
		// desriptor.geta
		buildHeaderJavaClassName(desriptor, scBean);
		// desriptor.getJavaClassName();

		buildHeaderTables(desriptor, scBean);
		// Vector tables = desriptor.getTables();

	}

	public static void buildHeaderCellBeansWithData(String key,
			SheetContentBean scBean) {

		scBean.setSheetName(key);

		CellBean cb = new CellBean();
		cb.setCellConent(key);
		cb.setPosition(new Position(CellConstants.DEFAULT_CLASS_CELL_POSITON_Y,
				CellConstants.DEFAULT_CLASS_CELL_POSITON_X));

		scBean.getCellBeanList().add(cb);

	}

	public static void buildHeaderTables(Descriptor desriptor,
			SheetContentBean scBean) {

		Vector tables = desriptor.getTables();
		for (int i = 0; i < tables.size(); i++) {
			DatabaseTable dt = (DatabaseTable) tables.get(i);

			CellBean cb = new CellBean();

			cb.setCellConent(dt.getName());
			cb.setPosition(new Position(
					CellConstants.DEFAULT_TABLE_CELL_POSITON_Y,
					CellConstants.DEFAULT_TABLE_CELL_POSITON_X + i));

			scBean.getCellBeanList().add(cb);
		}

	}

	public static void buildMappings(Descriptor desriptor,
			WritableWorkbook wwb, SheetContentBean scBean) {
		buildMappingHeader(scBean);
		List mList = desriptor.getMappings();
		// TODO: sort mapping list
		for (int i = 0; i < mList.size(); i++) {
			DatabaseMapping map = (DatabaseMapping) mList.get(i);

			if (map instanceof DirectToFieldMapping) {
				handleDirectToFieldMapping(map, scBean, i);
			}

			if (map instanceof OneToOneMapping) {
				handleOneToOneMapping(map, wwb, scBean, i);
			}

			if (map instanceof OneToManyMapping) {
				handleOneToManyMapping(map, wwb, scBean, i);
			}

			if (map instanceof ManyToManyMapping) {
				handleManyToManyMapping(map, wwb, scBean, i);
			}

		}
	}
	
	public static void buildData(String key, WritableWorkbook wwb,
			SheetContentBean scBean) {

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();

		String splited[] = key.split("-");
		String aliasName = splited[0];
		String tableName = ToplinkMappingHelper.getAliasTableNameMapToplink().get(
				aliasName);
		String keyField = splited[1];
		String keyValue = splited[2];

		List<String> resultList = new Vector();

		try {
			//con = MappingHelper.getConnectionIps();
			con = DataHolder.getInstance().getConnection();
		
			
			stmt = con.createStatement();

			String sqlToRun = "SELECT * FROM ";
			sqlToRun = sqlToRun + tableName;
			sqlToRun = sqlToRun + " WHERE ";
			sqlToRun = sqlToRun + keyField + " = " + keyValue;

			// to remove
			System.out.println("To run sql in showTableContent: " + sqlToRun);
			rs = stmt.executeQuery(sqlToRun);

			sb.append("\n\r");
			int row = 0;

			int initPositionLine = 2;
			int initPositionColumn = 1;

			int postionLine = initPositionLine;

			while (rs.next()) {

				row = row + 1;

				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnName = rsmd.getColumnLabel(i);

					// System.out.print("column name: ");
					// sb.append("column name: ");

					// System.out.print(columnName);
					sb.append(columnName);

					CellBean cbFieldName = new CellBean();
					cbFieldName.setCellConent(columnName);
					cbFieldName.setPosition(new Position(1,postionLine));
					scBean.getCellBeanList().add(cbFieldName);

					System.out.print(" = ");
					sb.append(" = ");

					System.out.print(rs.getString(i));
					
					String fieldValue =rs.getString(i);
					sb.append(fieldValue);

					CellBean cbFieldValue = new CellBean();
					cbFieldValue.setCellConent(fieldValue);
					cbFieldValue.setPosition(new Position( 2,postionLine));
					scBean.getCellBeanList().add(cbFieldValue);

					postionLine = postionLine + 1;

					// get return keys

					// String key = (String)
					// getTableKeyMapManual().get(tableName);
					// String key = null;
					// if (REL_USAGE.equals("TOPLINK")) {
					// key = (String) ToplinkProjectHelper.getTableKeyMap()
					// .get(tableName);
					// }
					// if (REL_USAGE.equals("MANAUL")) {
					// key = (String) ToplinkProjectManual
					// .getTableKeyMapManual().get(tableName);
					// }
					// if (columnName.equals(key)) {
					// resultList.add(rs.getString(i));
					// }

					// System.out.println();
					// sb.append("\n\r");
				}
			}
		} catch (Exception e) {

		} finally {
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
			stmt = null;
			con = null;
		}

		// to list table data

	}

	public static void handleOneToOneMapping(DatabaseMapping map,
			WritableWorkbook wwb, SheetContentBean scBean, int i) {
		OneToOneMapping theMap = (OneToOneMapping) map;

		CellBean cbType = new CellBean();
		cbType.setCellConent(CommonConstants.TYPE_ONETOONEMAPPING);
		cbType.setPosition(new Position(
				CellConstants.DEFAULT_TYPE_CELL_POSITON_Y,
				CellConstants.DEFAULT_TYPE_CELL_POSITON_X + i));
		scBean.cellsList.add(cbType);

		// TODO: apply stategy pattern
		// attribute
		CellBean cbattr = new CellBean();
		cbattr.setCellConent(theMap.getAttributeName());
		cbattr.setPosition(new Position(
				CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_Y,
				CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_X + i));
		scBean.cellsList.add(cbattr);

		// field
		// theMap.get
		CellBean cbfield = new CellBean();
		String fieldName = theMap.getSourceToTargetKeyFields().toString();
		cbfield.setCellConent(fieldName.replaceAll(
				CommonConstants.SCHEMA_HEADER, CommonConstants.EMPTY_STRING));
		cbfield.setPosition(new Position(
				CellConstants.DEFAULT_FIELD_CELL_POSITON_Y,
				CellConstants.DEFAULT_FIELD_CELL_POSITON_X + i));
		scBean.cellsList.add(cbfield);

		// refereced class
		CellBean cbref = new CellBean();
		cbref.setCellConent(getBriefClassName(theMap.getReferenceClassName()));
		cbref.setPosition(new Position(
				CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_Y,
				CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_X + i));
		scBean.cellsList.add(cbref);

		// link
		cbref.linkTo(wwb, cbref.getCellConent(), null);

	}
	
	public static void handleOneToManyMapping(DatabaseMapping map,
			WritableWorkbook wwb, SheetContentBean scBean, int i) {
		OneToManyMapping theMap = (OneToManyMapping) map;

		CellBean cbType = new CellBean();
		cbType.setCellConent(CommonConstants.TYPE_ONETOMANYMAPPING);
		cbType.setPosition(new Position(
				CellConstants.DEFAULT_TYPE_CELL_POSITON_Y,
				CellConstants.DEFAULT_TYPE_CELL_POSITON_X + i));
		scBean.cellsList.add(cbType);

		// TODO: apply stategy pattern
		// attribute
		CellBean cbattr = new CellBean();
		cbattr.setCellConent(theMap.getAttributeName());
		cbattr.setPosition(new Position(
				CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_Y,
				CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_X + i));
		scBean.cellsList.add(cbattr);

		// refereced class
		CellBean cbref = new CellBean();
		cbref.setCellConent(getBriefClassName(theMap.getReferenceClassName()));
		cbref.setPosition(new Position(
				CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_Y,
				CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_X + i));
		scBean.cellsList.add(cbref);
		// link
		cbref.linkTo(wwb, cbref.getCellConent(), null);

		// field
		CellBean cbSource = new CellBean();
		// theMap.getSourceKeyFields().toString()
		String sourceField = theMap.getSourceKeyFields().toString();
		cbSource.setCellConent(sourceField.replaceAll(
				CommonConstants.SCHEMA_HEADER, CommonConstants.EMPTY_STRING));
		cbSource.setPosition(new Position(
				CellConstants.DEFAULT_SOURCE_CELL_POSITON_Y,
				CellConstants.DEFAULT_SOURCE_CELL_POSITON_X + i));
		scBean.cellsList.add(cbSource);

		// target
		CellBean cbTarget = new CellBean();
		String targetField = theMap.getTargetForeignKeyFields().toString();
		cbTarget.setCellConent(targetField.replaceAll(
				CommonConstants.SCHEMA_HEADER, CommonConstants.EMPTY_STRING));
		cbTarget.setPosition(new Position(
				CellConstants.DEFAULT_TARGET_CELL_POSITON_Y,
				CellConstants.DEFAULT_TARGET_CELL_POSITON_X + i));
		scBean.cellsList.add(cbTarget);
	}
	
	
	public static void handleManyToManyMapping(DatabaseMapping map,
			WritableWorkbook wwb, SheetContentBean scBean, int i) {
		ManyToManyMapping theMap = (ManyToManyMapping) map;

		CellBean cbType = new CellBean();
		cbType.setCellConent(CommonConstants.TYPE_MANYTOMANYMAPPING);
		cbType.setPosition(new Position(
				CellConstants.DEFAULT_TYPE_CELL_POSITON_Y,
				CellConstants.DEFAULT_TYPE_CELL_POSITON_X + i));
		scBean.cellsList.add(cbType);
		// TODO: apply stategy pattern
		// attribute
		CellBean cbattr = new CellBean();
		cbattr.setCellConent(theMap.getAttributeName());
		cbattr.setPosition(new Position(
				CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_Y,
				CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_X + i));
		scBean.cellsList.add(cbattr);

		// refereced class
		CellBean cbref = new CellBean();
		cbref.setCellConent(getBriefClassName(theMap.getReferenceClassName()));
		cbref.setPosition(new Position(
				CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_Y,
				CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_X + i));
		scBean.cellsList.add(cbref);
		// link
		cbref.linkTo(wwb, cbref.getCellConent(), null);

		// field
		// theMap.get
		CellBean cbRelTable = new CellBean();
		cbRelTable.setCellConent(theMap.getRelationTable().getName());
		cbRelTable.setPosition(new Position(
				CellConstants.DEFAULT_RELTABLE_CELL_POSITON_Y,
				CellConstants.DEFAULT_RELTABLE_CELL_POSITON_X + i));
		scBean.cellsList.add(cbRelTable);

		CellBean cbSource = new CellBean();
		String sourceField = theMap.getSourceRelationKeyFields().toString();
		cbSource.setCellConent(sourceField.replaceAll(
				CommonConstants.SCHEMA_HEADER, CommonConstants.EMPTY_STRING));
		cbSource.setPosition(new Position(
				CellConstants.DEFAULT_SOURCE_CELL_POSITON_Y,
				CellConstants.DEFAULT_SOURCE_CELL_POSITON_X + i));
		scBean.cellsList.add(cbSource);

		CellBean cbTarget = new CellBean();
		String targetField = theMap.getTargetRelationKeyFields().toString();
		cbTarget.setCellConent(targetField.replaceAll(
				CommonConstants.SCHEMA_HEADER, CommonConstants.EMPTY_STRING));
		cbTarget.setPosition(new Position(
				CellConstants.DEFAULT_TARGET_CELL_POSITON_Y,
				CellConstants.DEFAULT_TARGET_CELL_POSITON_X + i));
		scBean.cellsList.add(cbTarget);

	}
	
	


	public static void buildMappingHeader(SheetContentBean scBean) {
		// TODO gengfo: spare one line for header -1
		// TODO gengfo: change constant definition
		CellBean cbType = new CellBean();
		cbType.setCellConent(CellConstants.DEFAULT_TYPE_LABLE);
		cbType.setPosition(new Position(
				CellConstants.DEFAULT_TYPE_CELL_POSITON_Y,
				CellConstants.DEFAULT_TYPE_CELL_POSITON_X - 1));
		scBean.cellsList.add(cbType);

		CellBean cbAttribute = new CellBean();
		cbAttribute.setCellConent(CellConstants.DEFAULT_ATTRIBUTE_LABLE);
		cbAttribute.setPosition(new Position(
				CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_Y,
				CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_X - 1));
		scBean.cellsList.add(cbAttribute);

		CellBean cbField = new CellBean();
		cbField.setCellConent(CellConstants.DEFAULT_FIELD_LABEL);
		cbField.setPosition(new Position(
				CellConstants.DEFAULT_FIELD_CELL_POSITON_Y,
				CellConstants.DEFAULT_FIELD_CELL_POSITON_X - 1));
		scBean.cellsList.add(cbField);

		CellBean cbRfClass = new CellBean();
		cbRfClass.setCellConent(CellConstants.DEFAULT_REFERENCECLASS_LABLE);
		cbRfClass.setPosition(new Position(
				CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_Y,
				CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_X - 1));
		scBean.cellsList.add(cbRfClass);

		CellBean cbRelTable = new CellBean();
		cbRelTable.setCellConent(CellConstants.DEFAULT_RELTABLE_LABLE);
		cbRelTable.setPosition(new Position(
				CellConstants.DEFAULT_RELTABLE_CELL_POSITON_Y,
				CellConstants.DEFAULT_RELTABLE_CELL_POSITON_X - 1));
		scBean.cellsList.add(cbRelTable);

		CellBean cbFKSource = new CellBean();
		cbFKSource.setCellConent(CellConstants.DEFAULT_SOURCE_LABLE);
		cbFKSource.setPosition(new Position(
				CellConstants.DEFAULT_SOURCE_CELL_POSITON_Y,
				CellConstants.DEFAULT_SOURCE_CELL_POSITON_X - 1));
		scBean.cellsList.add(cbFKSource);

		CellBean cbFKTarget = new CellBean();
		cbFKTarget.setCellConent(CellConstants.DEFAULT_TARGET_LABLE);
		cbFKTarget.setPosition(new Position(
				CellConstants.DEFAULT_TARGET_CELL_POSITON_Y,
				CellConstants.DEFAULT_TARGET_CELL_POSITON_X - 1));
		scBean.cellsList.add(cbFKTarget);

	}

	public static void handleDirectToFieldMapping(DatabaseMapping map,
			SheetContentBean scBean, int i) {
		DirectToFieldMapping dtfm = (DirectToFieldMapping) map;

		CellBean cbType = new CellBean();
		cbType.setCellConent(CommonConstants.TYPE_DIRECTTOFIELDMAPPING);
		cbType.setPosition(new Position(
				CellConstants.DEFAULT_TYPE_CELL_POSITON_Y,
				CellConstants.DEFAULT_TYPE_CELL_POSITON_X + i));
		scBean.cellsList.add(cbType);

		// attribute
		CellBean cba = new CellBean();
		cba.setCellConent(dtfm.getAttributeName());
		cba.setPosition(new Position(
				CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_Y,
				CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_X + i));
		scBean.cellsList.add(cba);

		// field
		CellBean cbf = new CellBean();
		String fieldName = dtfm.getFieldName();

		cbf.setCellConent(fieldName.replaceAll(CommonConstants.SCHEMA_HEADER,
				CommonConstants.EMPTY_STRING));
		cbf.setPosition(new Position(
				CellConstants.DEFAULT_FIELD_CELL_POSITON_Y,
				CellConstants.DEFAULT_FIELD_CELL_POSITON_X + i));
		scBean.cellsList.add(cbf);
	}

	
	public static String getBriefClassName(String fullName) {
		StringTokenizer st = new StringTokenizer(fullName, ".");
		String e = null;
		while (st.hasMoreTokens()) {
			e = (String) st.nextElement();
		}
		return e;
	}

	public static void buildHeaderJavaClassName(Descriptor desriptor,
			SheetContentBean scBean) {
		String javaClassName = getBriefClassName(desriptor.getJavaClassName());

		scBean.setSheetName(getBriefClassName(javaClassName));

		CellBean cb = new CellBean();
		cb.setCellConent(getBriefClassName(desriptor.getJavaClassName()));
		cb.setPosition(new Position(CellConstants.DEFAULT_CLASS_CELL_POSITON_Y,
				CellConstants.DEFAULT_CLASS_CELL_POSITON_X));

		scBean.getCellBeanList().add(cb);
	}

	
//	public static void toEclipselinkExcelSheets(String toFileName, EntityManagerFactory emf) {
//
//		Metamodel mm = null;
//		Object o= null;
//		mm = emf.
//		((Object) emf).getMetamodel();
//		
//		
//		WritableWorkbook wwb = null;
//
//		try {
//			wwb = WritableWorkbookFactory.getWorkBook(toFileName);
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
//
//		// Project theProject = ToplinkProjectHelper.getIvoProject();
//		//Map aliasDescMap = theProject.getAliasDescriptors();
//		
//		Set<ManagedType<?>> mts = mm.getManagedTypes();
//		
//		Set keySet = aliasDescMap.keySet();
//		Iterator keySetIt = keySet.iterator();
//
//		SheetContentBean scBeanAll = new SheetContentBean();
//		// scBeanAll
//
//		int i = 0;
//
//		int x = 1;
//		int rowCnt = 40;
//		while (keySetIt.hasNext()) {
//			Descriptor descriptor = (Descriptor) aliasDescMap.get(keySetIt
//					.next());
//			SheetContentBean scBean = DescriptorToExcel.toSheetContentBean(wwb,
//					descriptor);
//			ExlOutputSheetHelper.fillSheet(wwb, scBean);
//			logger.debug("debug");
//
//			//
//			CellBean cb = new CellBean();
//			cb.setCellConent(scBean.getSheetName());
//			cb.setPosition(new Position(x, i));
//			i = i + 1;
//
//			if ((i % 40) == 0) {
//				i = 0;
//				x = x + 2;
//			}
//
//			cb.linkTo(wwb, scBean.getSheetName(), null);
//			scBeanAll.cellsList.add(cb);
//			scBeanAll.setSheetName("AllAlias");
//
//		}
//
//		// List a = new ArrayList();
//		// Collections.sort(scBeanAll.cellsList);
//
//		ExlOutputSheetHelper.fillSheet(wwb, scBeanAll);
//
//		try {
//			wwb.write();
//			wwb.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (WriteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		logger.debug("exit toExcelSheets");
//
//	}
	
	public static void toExcelSheets(String toFileName, Project theProject) {

		logger.debug("enter toExcelSheets");

		toFileName = "DescriptorToExcelTest.xls";

		WritableWorkbook wwb = null;

		try {
			wwb = WritableWorkbookFactory.getWorkBook(toFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Project theProject = ToplinkProjectHelper.getIvoProject();
		Map aliasDescMap = theProject.getAliasDescriptors();
		Set keySet = aliasDescMap.keySet();
		Iterator keySetIt = keySet.iterator();

		SheetContentBean scBeanAll = new SheetContentBean();
		// scBeanAll

		int i = 0;

		int x = 1;
		int rowCnt = 40;
		while (keySetIt.hasNext()) {
			Descriptor descriptor = (Descriptor) aliasDescMap.get(keySetIt
					.next());
			SheetContentBean scBean = ToplinkDescriptorToExcel.toSheetContentBean(wwb,
					descriptor);
			ExlOutputSheetHelper.fillSheet(wwb, scBean);
			logger.debug("debug");

			//
			CellBean cb = new CellBean();
			cb.setCellConent(scBean.getSheetName());
			cb.setPosition(new Position(x, i));
			i = i + 1;

			if ((i % 40) == 0) {
				i = 0;
				x = x + 2;
			}

			cb.linkTo(wwb, scBean.getSheetName(), null);
			scBeanAll.cellsList.add(cb);
			scBeanAll.setSheetName("AllAlias");

		}

		// List a = new ArrayList();
		// Collections.sort(scBeanAll.cellsList);

		ExlOutputSheetHelper.fillSheet(wwb, scBeanAll);

		try {
			wwb.write();
			wwb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.debug("exit toExcelSheets");

	}

	public static void toExcelSheetsWithData(String toFileName,
			List<String> keyList) {

		WritableWorkbook wwb = null;

		try {
			wwb = WritableWorkbookFactory.getWorkBook(toFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		SheetContentBean scBeanAll = new SheetContentBean();
		// scBeanAll

		int i = 0;

		int x = 1;
		int rowCnt = 40;
		for (int s = 0; s < keyList.size(); s++) {

			String key = keyList.get(s);

			SheetContentBean scBean = ToplinkDescriptorToExcel
					.toSheetContentBeanWithData(wwb, key);
			ExlOutputSheetHelper.fillSheet(wwb, scBean);
			logger.debug("debug");

			//
			CellBean cb = new CellBean();
			cb.setCellConent(scBean.getSheetName());
			cb.setPosition(new Position(x, i));
			i = i + 1;

			if ((i % 40) == 0) {
				i = 0;
				x = x + 2;
			}

			cb.linkTo(wwb, scBean.getSheetName(), null);
			scBeanAll.cellsList.add(cb);
			scBeanAll.setSheetName("AllAlias");

		}

		// List a = new ArrayList();
		// Collections.sort(scBeanAll.cellsList);

		ExlOutputSheetHelper.fillSheet(wwb, scBeanAll);

		try {
			wwb.write();
			wwb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.debug("exit toExcelSheets");

	}

}
