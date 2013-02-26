package com.gengfo.mapping.eclipselink;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.persistence.descriptors.RelationalDescriptor;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.jpa.metamodel.EntityTypeImpl;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.ManyToManyMapping;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;
import org.eclipse.persistence.sessions.Session;

import com.gengfo.excel.CellBean;
import com.gengfo.excel.EclipselinkDescriptorToExcel;
import com.gengfo.excel.Position;
import com.gengfo.excel.SheetContentBean;
import com.gengfo.mapping.toplink.TableRel;
import com.gengfo.mapping.utils.DBConstants;
import com.gengfo.mapping.utils.FieldPair;
import com.gengfo.mapping.utils.MappingHelper;
import com.gengfo.or.OR4EclipselinkHelper;
import com.gengfo.or.common.DataHolder;
import com.gengfo.or.common.ExlOutputSheetHelper;
import com.gengfo.or.common.WritableWorkbookFactory;

public class DataOutputHelper4EclipseLink {

    public static Logger logger = LogManager.getLogger(DataOutputHelper4EclipseLink.class);

    public static Set<String> collectAllMappingKeysEclipseLink(String aliasName, String keyFieldName,
            String keyFieldValue, String mappingType, Connection con, Statement stam1) {

        MappingHelper.initFirstMapping(aliasName, keyFieldName, keyFieldValue);

        OR4EclipselinkHelper.collectMoreMappingKeysEclipseLink(aliasName, keyFieldName, keyFieldValue, mappingType, con, null);

        Set<String> set = DataHolder.getInstance().getHandledStatus().keySet();

        return set;
    }

    public static List<TableRel> getTabbleRelsEclipseLink(String alias) {
        RelationalDescriptor rd = DataHolder.getInstance().getAlias2EclipseLinkDescriptor().get(alias);
        List<TableRel> tableRelList = new ArrayList<TableRel>();
        List mapList = rd.getMappings();
        for (int i = 0; i < mapList.size(); i++) {
            TableRel tr = new TableRel();
            tr.setSourceAliasName(rd.getAlias());

            DatabaseMapping rm = (DatabaseMapping) mapList.get(i);

            if (rm instanceof DirectToFieldMapping) {

                // logger.debug("DirectToFieldMapping");

            } else if (rm instanceof OneToOneMapping) {

                OneToOneMapping oom = (OneToOneMapping) mapList.get(i);

                Map keysMap = oom.getSourceToTargetKeyFields();

                Set keyset = keysMap.keySet();
                if (keyset.size() > 0) {
                    // throw exception
                }
                Iterator it = keyset.iterator();
                if (it.hasNext()) {
                    DatabaseField keyField = (DatabaseField) it.next();
                    DatabaseField valueField = (DatabaseField) keysMap.get(keyField);

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
                    DatabaseField valueField = (DatabaseField) destKeyList.get(0);

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
                }

            } else if (rm instanceof ManyToManyMapping) {
                logger.debug("many to many");
            }
        }

        return tableRelList;

    }

    

    public static void toExcelSheetsWithDataEclipseLink(String xlsFileName, List<String> keyList) {

        WritableWorkbook wwb = null;

        try {
            wwb = WritableWorkbookFactory.getWorkBook(xlsFileName);
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

            SheetContentBean scBean = EclipselinkDescriptorToExcel.toSheetContentBeanWithData(wwb, key, null, null);

            // insert mapping info
            String splited[] = key.split("-");
            String aliasName = splited[0];
            RelationalDescriptor descriptor = DataHolder.getInstance().getAlias2EclipseLinkDescriptor()
                    .get(aliasName);
            appendSheetContentBeanEclipseLink(wwb, descriptor, scBean);

            // // insert mapping info end

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

    public static void outputDataToExcelEclipseLink(Set<String> set, String xlsFileName) {

        Set<String> keySet = new TreeSet<String>();
        keySet.addAll(set);

        Iterator<String> it = keySet.iterator();
        List<String> keyList = new ArrayList<String>();

        keyList.addAll(keySet);

        DataOutputHelper4EclipseLink.toExcelSheetsWithDataEclipseLink(xlsFileName, keyList);
    }

    public static SheetContentBean appendSheetContentBeanEclipseLink(WritableWorkbook wwb,
            RelationalDescriptor desriptor, SheetContentBean scBean) {
        // SheetContentBean scBean = new SheetContentBean();

        // buildHeaderCellBeans(desriptor, scBean);

        OR4EclipselinkHelper.buildMappingsEclipseLink(desriptor, wwb, scBean);

        return scBean;
    }

}
