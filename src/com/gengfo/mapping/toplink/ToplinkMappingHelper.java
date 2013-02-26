package com.gengfo.mapping.toplink;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import org.eclipse.persistence.internal.jpa.metamodel.EntityTypeImpl;

import oracle.toplink.descriptors.RelationalDescriptor;
import oracle.toplink.internal.helper.DatabaseField;
import oracle.toplink.mappings.DatabaseMapping;
import oracle.toplink.mappings.OneToManyMapping;
import oracle.toplink.mappings.OneToOneMapping;
import oracle.toplink.publicinterface.Descriptor;
import oracle.toplink.sessions.Project;
import oracle.toplink.tools.workbench.XMLProjectReader;

import com.gengfo.exception.MultiKeyFieldException;
import com.gengfo.mapping.eclipselink.EclipseLinkMappingHelper;
import com.gengfo.mapping.utils.DBConstants;
import com.gengfo.mapping.utils.FieldPair;
import com.gengfo.mapping.utils.MappingHelper;
import com.gengfo.or.common.DataHolder;
import com.oocl.ivo.domain.mapping.IVOProject;

public class ToplinkMappingHelper {

	public static Project getProject(String projectXml) {

		return XMLProjectReader.read(projectXml);

	}

	public static Project getProject() {

		return new IVOProject();

	}

	public static Descriptor getDescriptor(String aliasName) {

		Project ivoProject = ToplinkMappingHelper.getIvoProject();

		Descriptor descriptor = (RelationalDescriptor) ivoProject
				.getAliasDescriptors().get(aliasName);

		return descriptor;
	}

	public static Project getIvoProject() {

		return new IVOProject();

	}

	/**
	 * the map cotains: <li>key: tableName <li>primary key field
	 * 
	 * @return
	 * @throws MultiKeyFieldException
	 */
	public static Map<String, String> getTablePKeyMapToplink() {

		Map<String, String> tableKeyMap = new HashMap();

		// Project theProject = getProject(DBConstants.PRJ_CONF_XML);
		Project theProject = getProject();

		Map aliasDescMap = theProject.getAliasDescriptors();
		Set keySet = aliasDescMap.keySet();
		Iterator keySetIt = keySet.iterator();

		while (keySetIt.hasNext()) {
			String tableName = (String) keySetIt.next();
			RelationalDescriptor rd = (RelationalDescriptor) aliasDescMap
					.get(tableName);

			List pkList = rd.getPrimaryKeyFields();
			if (pkList.size() == 1) {
				DatabaseField pk = (DatabaseField) pkList.get(0);

				tableKeyMap.put(rd.getTableName(), pk.getName());
			} else {
				// throw (new MultiKeyFieldException());
			}

		}

		return tableKeyMap;
	}

	public static Map<String, String> getTableNameAliasMapToplink() {
		Map<String, String> tableKeyMap = new Hashtable();

		// Project theProject = getProject(DBConstants.PRJ_CONF_XML);
		Project theProject = getProject();

		Map aliasDescMap = theProject.getAliasDescriptors();
		Set keySet = aliasDescMap.keySet();
		Iterator keySetIt = keySet.iterator();

		while (keySetIt.hasNext()) {
			String tableName = (String) keySetIt.next();
			RelationalDescriptor rd = (RelationalDescriptor) aliasDescMap
					.get(tableName);

			List pkList = rd.getPrimaryKeyFields();
			if (pkList.size() == 1) {
				DatabaseField pk = (DatabaseField) pkList.get(0);

				tableKeyMap.put(rd.getTableName(), rd.getAlias());
			} else {
				// throw (new MultiKeyFieldException());
			}

		}

		return tableKeyMap;
	}

	public static Map<String, Descriptor> getAliasDescriptorMapToplink() {

		Project theProject = DataHolder.getInstance().getProject();

		Map aliasDescMap = theProject.getAliasDescriptors();
		// DataHolder.getInstance().getAlias2Descriptor().putAll(aliasDescMap);

		return aliasDescMap;
	}

	public static Map<String, Descriptor> getAliasDescriptorMapElipseLink() {

		Project theProject = DataHolder.getInstance().getProject();

		Map aliasDescMap = theProject.getAliasDescriptors();
		// DataHolder.getInstance().getAlias2Descriptor().putAll(aliasDescMap);

		return aliasDescMap;
	}

	public static Map<String, String> getAliasTableNameMapToplink() {
		Map<String, String> tableKeyMap = new Hashtable();

		Project theProject = getProject();

		Map aliasDescMap = theProject.getAliasDescriptors();
		Set keySet = aliasDescMap.keySet();
		Iterator keySetIt = keySet.iterator();

		while (keySetIt.hasNext()) {
			String tableName = (String) keySetIt.next();
			RelationalDescriptor rd = (RelationalDescriptor) aliasDescMap
					.get(tableName);

			List pkList = rd.getPrimaryKeyFields();
			if (pkList.size() == 1) {
				DatabaseField pk = (DatabaseField) pkList.get(0);

				tableKeyMap.put(rd.getAlias(), rd.getTableName());
			} else {
				// throw (new MultiKeyFieldException());
			}

		}

		return tableKeyMap;
	}

	public static RelationalDescriptor filterRelationalDescriptor(
			Project theProject, String tableName) {

		Map aliasDescMap = theProject.getAliasDescriptors();
		Set keySet = aliasDescMap.keySet();
		Iterator keySetIt = keySet.iterator();

		RelationalDescriptor rd = (RelationalDescriptor) aliasDescMap
				.get(tableName);

		return rd;

	}

	public static List<TableRel> getTabbleRels(Project theProject, String alias) {

		RelationalDescriptor rd = filterRelationalDescriptor(theProject, alias);

		List<TableRel> tableRelList = getTabbleRels(rd);

		return tableRelList;

	}

	public static List<TableRel> getTabbleRels(RelationalDescriptor rd) {

		List<TableRel> tableRelList = new ArrayList<TableRel>();

		List mapList = rd.getMappings();
		// for each one to one mapping or oneto many maaping
		for (int i = 0; i < mapList.size(); i++) {
			TableRel tr = new TableRel();

			tr.setSourceAliasName(rd.getAlias());

			DatabaseMapping rm = (DatabaseMapping) mapList.get(i);

			if (rm instanceof OneToOneMapping) {

				OneToOneMapping oom = (OneToOneMapping) mapList.get(i);

				// oom.get

				Map keysMap = oom.getSourceToTargetKeyFields();

				Set keyset = keysMap.keySet();
				if (keyset.size() > 0) {
					// throw exception
				}
				Iterator it = keyset.iterator();
				if (it.hasNext()) {
					DatabaseField keyField = (DatabaseField) it.next();
					DatabaseField valueField = (DatabaseField) keysMap
							.get(keyField);

					FieldPair[] fps = new FieldPair[1];
					FieldPair fp = new FieldPair();
					fps[0] = fp;
					tr.setFieldPairs(fps);

					String srcTableName = keyField.getTableName();
					String destTableName = valueField.getTableName();
					String srcTableFieldName = keyField.getName();
					String destTableFieldName = valueField.getName();

					tr.setSourceTbName(srcTableName);

					tr.setDestTbName(destTableName);

					tr.setRelType(DBConstants.MAPPING_TYPE_ONETOONE);
					fp.setSourceFd(srcTableFieldName);
					fp.setDestinationFd(destTableFieldName);
				}
				tableRelList.add(tr);
			} else if (rm instanceof OneToManyMapping) {

				OneToManyMapping omm = (OneToManyMapping) mapList.get(i);
				List srcKeysList = omm.getSourceKeyFields();
				List destKeyList = omm.getTargetForeignKeyFields();
				// check the second key
				if (srcKeysList.size() == 1 && destKeyList.size() == 1) {
					DatabaseField keyField = (DatabaseField) srcKeysList.get(0);
					DatabaseField valueField = (DatabaseField) destKeyList
							.get(0);

					FieldPair[] fps = new FieldPair[1];
					FieldPair fp = new FieldPair();
					fps[0] = fp;
					tr.setFieldPairs(fps);

					String srcTableName = keyField.getTableName();
					String destTableName = valueField.getTableName();
					String srcTableFieldName = keyField.getName();
					String destTableFieldName = valueField.getName();

					tr.setSourceTbName(srcTableName);
					tr.setDestTbName(destTableName);
					tr.setRelType(DBConstants.MAPPING_TYPE_ONETOMANY);

					fp.setSourceFd(srcTableFieldName);
					fp.setDestinationFd(destTableFieldName);

					tableRelList.add(tr);
				} else {
					// TODOgengfo throw MultiKeyFieldException
				}

			}

		}

		return tableRelList;
	}

	public static List getIVOTableRels() {
		List<TableRel> tableRelList = new ArrayList<TableRel>();

		// --
		// Project theProject = getProject(DBConstants.PRJ_CONF_XML);
		Project theProject = getProject();

		Map aliasDescMap = theProject.getAliasDescriptors();
		Set keySet = aliasDescMap.keySet();
		Iterator keySetIt = keySet.iterator();

		// For each descriptor
		while (keySetIt.hasNext()) {
			String tableName = (String) keySetIt.next();
			// remove comments
			// if (null != DBHelperR0202.getTableKeyField(tableName)) {
			RelationalDescriptor rd = (RelationalDescriptor) aliasDescMap
					.get(tableName);

			System.out.println("---------------- " + tableName
					+ "------------------------------");

			List mapList = rd.getMappings();
			// for each one to one mapping or oneto many maaping
			for (int i = 0; i < mapList.size(); i++) {
				TableRel tr = new TableRel();

				DatabaseMapping rm = (DatabaseMapping) mapList.get(i);

				if (rm instanceof OneToOneMapping) {

					OneToOneMapping oom = (OneToOneMapping) mapList.get(i);

					Map keysMap = oom.getSourceToTargetKeyFields();

					Set keyset = keysMap.keySet();
					if (keyset.size() > 0) {
						// throw exception
					}
					Iterator it = keyset.iterator();
					if (it.hasNext()) {
						DatabaseField keyField = (DatabaseField) it.next();
						DatabaseField valueField = (DatabaseField) keysMap
								.get(keyField);

						FieldPair[] fps = new FieldPair[1];
						FieldPair fp = new FieldPair();
						fps[0] = fp;
						tr.setFieldPairs(fps);

						String srcTableName = keyField.getTableName();
						String destTableName = valueField.getTableName();
						String srcTableFieldName = keyField.getName();
						String destTableFieldName = valueField.getName();

						tr.setSourceTbName(srcTableName);
						tr.setDestTbName(destTableName);
						tr.setRelType(DBConstants.MAPPING_TYPE_ONETOONE);
						fp.setSourceFd(srcTableFieldName);
						fp.setDestinationFd(destTableFieldName);
					}
					tableRelList.add(tr);
				} else if (rm instanceof OneToManyMapping) {

					OneToManyMapping omm = (OneToManyMapping) mapList.get(i);
					List srcKeysList = omm.getSourceKeyFields();
					List destKeyList = omm.getTargetForeignKeyFields();
					// check the second key
					if (srcKeysList.size() == 1 && destKeyList.size() == 1) {
						DatabaseField keyField = (DatabaseField) srcKeysList
								.get(0);
						DatabaseField valueField = (DatabaseField) destKeyList
								.get(0);

						FieldPair[] fps = new FieldPair[1];
						FieldPair fp = new FieldPair();
						fps[0] = fp;
						tr.setFieldPairs(fps);

						String srcTableName = keyField.getTableName();
						String destTableName = valueField.getTableName();
						String srcTableFieldName = keyField.getName();
						String destTableFieldName = valueField.getName();

						tr.setSourceTbName(srcTableName);
						tr.setDestTbName(destTableName);
						tr.setRelType(DBConstants.MAPPING_TYPE_ONETOMANY);

						fp.setSourceFd(srcTableFieldName);
						fp.setDestinationFd(destTableFieldName);

						tableRelList.add(tr);
					} else {
						// TODOgengfo throw MultiKeyFieldException
					}

				}

			}
			// to remove comments
			// }
		}

		return tableRelList;
	}

	public static String getTableName(String key) {
		// sample: tableName.tablefiled

		return (key.substring(0, key.indexOf(".") - 1));
	}

	public static String getTableFieldName(String key) {
		return (key.substring(key.indexOf(".") + 1, key.length()));
	}

	public static void showTableList(List<TableRel> trList) {

		if (null == trList || trList.size() == 0) {
			return;
		}

		StringBuffer sb = new StringBuffer();
		for (TableRel tr : trList) {

			sb.append(tr.getDestTbName());
			sb.append("  ");
			sb.append(tr.getRelType());
			sb.append("\n");

		}

		System.out.println(sb.toString());

	}

}
