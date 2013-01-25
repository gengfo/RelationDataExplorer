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
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import oracle.toplink.internal.helper.DatabaseTable;
//import oracle.toplink.mappings.DatabaseMapping;

//import oracle.toplink.mappings.DirectToFieldMapping;
//import oracle.toplink.mappings.ManyToManyMapping;
//import oracle.toplink.mappings.OneToManyMapping;
//import oracle.toplink.mappings.OneToOneMapping;
import oracle.toplink.publicinterface.Descriptor;
import oracle.toplink.sessions.Project;

import org.apache.log4j.Logger;
import org.eclipse.persistence.descriptors.RelationalDescriptor;
import org.eclipse.persistence.internal.jpa.metamodel.EntityTypeImpl;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.ManyToManyMapping;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;

import com.gengfo.common.CommonConstants;
import com.gengfo.mapping.toplink.ToplinkMappingHelper;
import com.gengfo.mapping.utils.DataHolder;
import com.gengfo.mapping.utils.MappingHelper;

public class EclipselinkDescriptorToExcel {

	public static Logger logger = Logger.getLogger(EclipselinkDescriptorToExcel.class);

	/**
	 * descriptor structure <li>
	 * 
	 * @param wwb
	 * 
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

	public static SheetContentBean toSheetContentBean4EclipseLink(
			WritableWorkbook wwb, RelationalDescriptor desriptor) {
		SheetContentBean scBean = new SheetContentBean();

		buildHeaderCellBeans4EclipseLink(desriptor, scBean);

		 buildMappings4EclipseLink(desriptor, wwb, scBean);

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

	public static void buildHeaderCellBeans4EclipseLink(
			RelationalDescriptor desriptor, SheetContentBean scBean) {
		// desriptor.geta
		buildHeaderJavaClassName4EclipesLink(desriptor, scBean);
		// desriptor.getJavaClassName();

		buildHeaderTables4EclipseLink(desriptor, scBean);
		// Vector tables = desriptor.getTables();

	}

	public static void buildHeaderCellBeans4EclipseLink(Descriptor desriptor,
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

	public static void buildHeaderTables4EclipseLink(
			RelationalDescriptor desriptor, SheetContentBean scBean) {

		Vector tables = desriptor.getTables();

		for (int i = 0; i < tables.size(); i++) {
			org.eclipse.persistence.internal.helper.DatabaseTable dt = (org.eclipse.persistence.internal.helper.DatabaseTable) tables
					.get(i);

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


//			if (map instanceof DirectToFieldMapping) {
//				handleDirectToFieldMapping(map, scBean, i);
//			}
//
//			if (map instanceof OneToOneMapping) {
//				handleOneToOneMapping(map, wwb, scBean, i);
//			}
//
//			if (map instanceof OneToManyMapping) {
//				handleOneToManyMapping(map, wwb, scBean, i);
//			}
//
//			if (map instanceof ManyToManyMapping) {
//				handleManyToManyMapping(map, wwb, scBean, i);
//			}

		}
	}
	
	public static void buildMappings4EclipseLink(RelationalDescriptor desriptor,
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
		//StringBuffer sb = new StringBuffer();

		String splited[] = key.split("-");
		String aliasName = splited[0];
		//String tableName = ToplinkProjectHelper.getAliasTableNameMap().get(
		//		aliasName);
		String tableName = DataHolder.getInstance().getAlias2TableMap().get(
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

			
			int row = 0;

			int initPositionLine = 5;
			int initPositionColumn = 8;

			int postionLine = initPositionLine;

			while (rs.next()) {

				row = row + 1;

				

				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnName = rsmd.getColumnLabel(i);

					CellBean cbFieldName = new CellBean();
					cbFieldName.setCellConent(columnName);
					cbFieldName.setPosition(new Position(initPositionColumn, postionLine));
					scBean.getCellBeanList().add(cbFieldName);

					String fieldValue = rs.getString(i);

					CellBean cbFieldValue = new CellBean();
					cbFieldValue.setCellConent(fieldValue);
					cbFieldValue.setPosition(new Position(initPositionColumn+1, postionLine));
					scBean.getCellBeanList().add(cbFieldValue);

					postionLine = postionLine + 1;

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

	// public static void addReferenceClass(DatabaseMapping theMap,
	// SheetContentBean scBean){
	// // CellBean cbref = new CellBean();
	// //refereced class
	// if (){
	//
	// cbref.setCellConent(getBriefClassName(theMap.getr()));
	// cbref.setPosition(new Position(
	// CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_Y,
	// CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_X + i));
	// scBean.cellsList.add(cbref);
	// }
	//
	// }

	public static void buildMappingHeader(SheetContentBean scBean) {

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

		String tmp = fieldName.replaceAll(CommonConstants.SCHEMA_HEADER,
				CommonConstants.EMPTY_STRING);
		tmp.substring(tmp.length()- tmp.lastIndexOf("."));
		cbf.setCellConent(tmp);
		
		
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

	public static void buildHeaderJavaClassName4EclipesLink(
			RelationalDescriptor desriptor, SheetContentBean scBean) {
		String javaClassName = getBriefClassName(desriptor.getJavaClassName());

		scBean.setSheetName(getBriefClassName(javaClassName));

		CellBean cb = new CellBean();
		cb.setCellConent(getBriefClassName(desriptor.getJavaClassName()));
		cb.setPosition(new Position(CellConstants.DEFAULT_CLASS_CELL_POSITON_Y,
				CellConstants.DEFAULT_CLASS_CELL_POSITON_X));

		scBean.getCellBeanList().add(cb);
	}

	// public static void
	// buildHeaderJavaClassName4EclipseLink(RelationalDescriptor desriptor,
	// SheetContentBean scBean) {
	// String javaClassName = getBriefClassName(desriptor.getJavaClassName());
	//
	// scBean.setSheetName(getBriefClassName(javaClassName));
	//
	// CellBean cb = new CellBean();
	// cb.setCellConent(getBriefClassName(desriptor.getJavaClassName()));
	// cb.setPosition(new Position(CellConstants.DEFAULT_CLASS_CELL_POSITON_Y,
	// CellConstants.DEFAULT_CLASS_CELL_POSITON_X));
	//
	// scBean.getCellBeanList().add(cb);
	// }

	public static void buildHeaderJavaClassName4EclipseLink(
			RelationalDescriptor desriptor, SheetContentBean scBean) {
		String javaClassName = getBriefClassName(desriptor.getJavaClassName());

		scBean.setSheetName(getBriefClassName(javaClassName));

		CellBean cb = new CellBean();
		cb.setCellConent(getBriefClassName(desriptor.getJavaClassName()));
		cb.setPosition(new Position(CellConstants.DEFAULT_CLASS_CELL_POSITON_Y,
				CellConstants.DEFAULT_CLASS_CELL_POSITON_X));

		scBean.getCellBeanList().add(cb);
	}

	public static void toEclipselinkExcelSheets(String toFileName,
			EntityManagerFactory emf) {

		Metamodel mm = emf.getMetamodel();

		WritableWorkbook wwb = null;

		try {
			wwb = WritableWorkbookFactory.getWorkBook(toFileName);
		} catch (IOException e) {

			e.printStackTrace();
		}

		Set<ManagedType<?>> mts = mm.getManagedTypes();
		// for (ManagedType mt : mts) {

		// if (mt instanceof EntityTypeImpl) {

		SheetContentBean scBeanAll = new SheetContentBean();
		// scBeanAll

		int i = 0;

		int x = 1;
		int rowCnt = 40;
		// while (keySetIt.hasNext()) {

		for (ManagedType mt : mts) {

			if (mt instanceof EntityTypeImpl) {

				EntityTypeImpl a = (EntityTypeImpl) mt;

				RelationalDescriptor descriptor = a.getDescriptor();

				// Descriptor descriptor = (Descriptor) aliasDescMap
				// .get(keySetIt.next());
				SheetContentBean scBean = EclipselinkDescriptorToExcel
						.toSheetContentBean4EclipseLink(wwb, descriptor);
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
		}
		// List a = new ArrayList();
		// Collections.sort(scBeanAll.cellsList);

		ExlOutputSheetHelper.fillSheet(wwb, scBeanAll);

		try {
			wwb.write();
			wwb.close();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (WriteException e) {

			e.printStackTrace();
		}

		logger.debug("exit toExcelSheets");

	}

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
			SheetContentBean scBean = EclipselinkDescriptorToExcel.toSheetContentBean(wwb,
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

			e.printStackTrace();
		} catch (WriteException e) {

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

			SheetContentBean scBean = EclipselinkDescriptorToExcel
					.toSheetContentBeanWithData(wwb, key);
			
			// insert mapping info
			String splited[] = key.split("-");
			String aliasName = splited[0];
			Descriptor descriptor = DataHolder.getInstance().getAlias2Descriptor().get(aliasName);
			ToplinkDescriptorToExcel.appendSheetContentBean(wwb,
					descriptor,scBean);
			
			//			// insert mapping info end 
			
			ExlOutputSheetHelper.fillSheet(wwb, scBean);
			

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

		ExlOutputSheetHelper.fillSheet(wwb, scBeanAll);

		try {
			wwb.write();
			wwb.close();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (WriteException e) {

			e.printStackTrace();
		}


	}

}
