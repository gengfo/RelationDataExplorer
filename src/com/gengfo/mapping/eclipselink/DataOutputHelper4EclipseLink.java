package com.gengfo.mapping.eclipselink;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.lang.StringUtils;
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

import com.gengfo.common.CommonConstants;
import com.gengfo.excel.CellBean;
import com.gengfo.excel.CellConstants;
import com.gengfo.excel.EclipselinkDescriptorToExcel;
import com.gengfo.excel.ExlOutputSheetHelper;
import com.gengfo.excel.Position;
import com.gengfo.excel.SheetContentBean;
import com.gengfo.excel.ToplinkDescriptorToExcel;
import com.gengfo.excel.WritableWorkbookFactory;
import com.gengfo.exception.MultiKeyFieldException;
import com.gengfo.mapping.toplink.TableRel;
import com.gengfo.mapping.utils.DBConstants;
import com.gengfo.mapping.utils.DataHolder;
import com.gengfo.mapping.utils.FieldPair;
import com.gengfo.mapping.utils.MappingHelper;

public class DataOutputHelper4EclipseLink {

    private static Logger logger = LogManager.getLogger(DataOutputHelper4EclipseLink.class);

    public static void outputDataEclipseLink(String aliasName, String keyFieldName, String keyFieldValue,
            String xlsFileName, String mappingType, String unitName) {

        DataOutputHelper4EclipseLink.initAliasTableNameMapEclipseLink(unitName);

        Connection con = DataHolder.getInstance().getConnection();
        Statement stam = null;
        try {
            stam = con.createStatement();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        //Set<String> set = collectAllMappingKeysEclipseLink(aliasName, keyFieldName, keyFieldValue, mappingType, con, stam);
        MappingHelper.initFirstMapping(aliasName, keyFieldName, keyFieldValue);

        collectMoreMappingKeysEclipseLink(aliasName, keyFieldName, keyFieldValue, mappingType, con, stam);

        Set<String> set = DataHolder.getInstance().getHandledStatus().keySet();
        
        try {
            con.close();
            stam.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

       // DataOutputHelper4EclipseLink.outputDataToExcelEclipseLink(set, xlsFileName);
        
        Set<String> keySet = new TreeSet<String>();
        keySet.addAll(set);

        Iterator<String> it = keySet.iterator();
        List<String> keyList = new ArrayList<String>();

        keyList.addAll(keySet);

       // DataOutputHelper4EclipseLink.toExcelSheetsWithDataEclipseLink(xlsFileName, keyList);
        
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

            SheetContentBean scBean = EclipselinkDescriptorToExcel.toSheetContentBeanWithData(wwb, key, con, stam);

            // insert mapping info
            String splited[] = key.split("-");
            String thealiasName = splited[0];
            RelationalDescriptor descriptor = DataHolder.getInstance().getAlias2EclipseLinkDescriptor()
                    .get(thealiasName);
           // appendSheetContentBeanEclipseLink(wwb, descriptor, scBean);
            buildMappingsEclipseLink(descriptor, wwb, scBean);

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

    public static Set<String> collectAllMappingKeysEclipseLink(String aliasName, String keyFieldName,
            String keyFieldValue, String mappingType, Connection con, Statement stam1) {

        MappingHelper.initFirstMapping(aliasName, keyFieldName, keyFieldValue);

        collectMoreMappingKeysEclipseLink(aliasName, keyFieldName, keyFieldValue, mappingType, con, null);

        Set<String> set = DataHolder.getInstance().getHandledStatus().keySet();

        return set;
    }

    public static void collectMoreMappingKeysEclipseLink(String aliasName, String keyFieldName,
            String keyFieldValue, String mappingType, Connection con1, Statement stam1) {
        DataHolder handledSet = DataHolder.getInstance();
        String kOfAliasKNameKValue = MappingHelper.getKey(aliasName, keyFieldName, keyFieldValue);
        
        while (null != MappingHelper.hasItemToHandle(handledSet.getHandledStatus())) {
            logger.debug("to handle-> " + kOfAliasKNameKValue);
            Boolean b = (Boolean) handledSet.getHandledStatus().get(kOfAliasKNameKValue);
            String splited[] = kOfAliasKNameKValue.split("-");
            aliasName = splited[0];
            keyFieldName = splited[1];
            keyFieldValue = splited[2];
            if (!b.booleanValue()) {
                MappingHelper.moveToHandled(aliasName, keyFieldName, keyFieldValue);
                RelationalDescriptor rd = DataHolder.getInstance().getAlias2EclipseLinkDescriptor().get(aliasName);
                List<TableRel> tableRelList = new ArrayList<TableRel>();
                List mapList = rd.getMappings();

                for (int i = 0; i < mapList.size(); i++) {
                    TableRel tr = new TableRel();
                    tr.setSourceAliasName(rd.getAlias());
                    DatabaseMapping rm = (DatabaseMapping) mapList.get(i);
                    if (rm instanceof DirectToFieldMapping) {
                    } else if (rm instanceof OneToOneMapping) {
                        OneToOneMapping oom = (OneToOneMapping) mapList.get(i);
                        Map keysMap = oom.getSourceToTargetKeyFields();
                        Set keyset = keysMap.keySet();
                        if (keyset.size() > 0) {
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

                for (TableRel tableRel : tableRelList) {
                    if (!aliasName.contains("ARP_") && !aliasName.contains("SPS_")) {
                        continue;
                    }

                    String whereClause = "";
                    if (StringUtils.isEmpty(keyFieldValue)) {
                        whereClause = "";
                    }

                    String srcTableName = tableRel.getSourceTbName();

                    if (!StringUtils.isEmpty(MappingHelper.schemaNameArp)) {
                        srcTableName = MappingHelper.schemaNameArp + "." + srcTableName;
                    }

                    FieldPair[] fps = tableRel.getFieldPairs();

                    for (int i = 0; i < fps.length; i++) {
                        //TODO
                        if (i != 0) {
                            whereClause = whereClause + " " + "and" + " ";
                        }
                        FieldPair fp = fps[i];
                        //String fk = MappingHelper.getTableFiledValue(srcTableName, fp.getSourceFd(), keyFieldValue);
                        String fk = "";
                        try {

                            String tablePkName = MappingHelper.getTableKeyField(srcTableName);
                            String filedName = fp.getSourceFd();
                            //String tableName = srcTableName;
                          //  fk = MappingHelper.getDBFiledValueByOid(srcTableName, fp.getSourceFd(), tablePkName,
                          //          keyFieldValue);
                            //String fieldValue = "";
                            //Connection con = null;
                            //Statement stmt = null;
                            ResultSet rs = null;

                            //con = DataHolder.getInstance().getConnection();

                           // try {
                                //stmt = con1.createStatement();
                          //  } catch (SQLException e) {
                            //    e.printStackTrace();
                          //  }

                            String sqlToRun = "SELECT ";
                            sqlToRun = sqlToRun + filedName;
                            sqlToRun = sqlToRun + " FROM ";
                            sqlToRun = sqlToRun + srcTableName;
                            sqlToRun = sqlToRun + " a WHERE ";
                            // sqlToRun = sqlToRun + " a.OID = ";
                            sqlToRun = sqlToRun + tablePkName;
                            sqlToRun = sqlToRun + " = ";
                            sqlToRun = sqlToRun + keyFieldValue;

                            // to keep
                            // System.out.println("sqlToRun in getDBFiledValueByOid: " + sqlToRun);

                            try {
                                rs = stam1.executeQuery(sqlToRun);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            try {
                                // /while (rsnext()) {
                                if (rs.next()) {
                                    fk = rs.getString(1);
                                }

                                // }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

//                            try {
//                               // stmt.close();
//                                //con.close();
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//                            }

                            
                            ////

                        } catch (MultiKeyFieldException e) {
                            e.printStackTrace();
                        }
                        
                        ///
                        if (!StringUtils.isEmpty(fk)) {
                            whereClause = whereClause + fp.getDestinationFd() + "=" + fk;
                        }
                    }

                   // List<String> fkValueList = MappingHelper.getTableContent(tableRel.getDestTbName(), whereClause);
                    List<String> fkValueList = new Vector();

                    try {
                        //con = DataHolder.getInstance().getConnection();

                        //stmt = con.createStatement();

                        String sqlToRun = "SELECT * FROM ";
                        sqlToRun = sqlToRun + tableRel.getDestTbName();
                        sqlToRun = sqlToRun + " WHERE ";
                        sqlToRun = sqlToRun + whereClause;

                        logger.debug("to get relations->" + sqlToRun);

                        ResultSet rs = stam1.executeQuery(sqlToRun);
                        int row = 0;
                        while (rs.next()) {
                            row = row + 1;
                            ResultSetMetaData rsmd = rs.getMetaData();

                            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                                String columnName = rsmd.getColumnLabel(i);
                                String key = DataHolder.getInstance().getTablePkMap().get(tableRel.getDestTbName());
                                if (columnName.equals(key)) {
                                    fkValueList.add(rs.getString(i));
                                }

                            }
                        }
                    } catch (Exception e) {

                    } finally {
//                        try {
//                            stmt.close();
//
//                            con.close();
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
                    }
                    
                    ///

                    if (null != fkValueList && fkValueList.size() > 0) {
                        for (String akey : fkValueList) {
                            String tmpAlias = "";
                            Map tm = DataHolder.getInstance().getTable2AliasMap();
                            tmpAlias = (String) tm.get(tableRel.getDestTbName());
                            String keyOfAliasKfNameKfValue = MappingHelper.getKey(tmpAlias,
                                    tableRel.getFieldPairs()[0].getDestinationFd(), akey);
                            boolean hasflag = false;
                            Map mapOfStatus = DataHolder.getInstance().getHandledStatus();
                            Set<String> keySet = mapOfStatus.keySet();
                            Iterator<String> it = keySet.iterator();
                            while (it.hasNext()) {
                                String tmpKey = it.next();
                                if (tmpKey.equals(keyOfAliasKfNameKfValue)) {
                                    hasflag = true;
                                    break;
                                }
                            }
                            if (!hasflag) {
                                DataHolder.getInstance().getHandledStatus()
                                        .put(keyOfAliasKfNameKfValue, Boolean.FALSE);
                                logger.debug("find new one to handle -> " + keyOfAliasKfNameKfValue);
                            }
                        }
                    } else {
                        // System.out.println("no result");
                    }

                    // //

                }

            } else {
            }

            String asetky = MappingHelper.hasItemToHandle(handledSet.getHandledStatus());
            if (null != asetky) {
                kOfAliasKNameKValue = asetky;
            }
        }

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

    public static void initAliasTableNameMapEclipseLink(String unitName) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(unitName);

        EntityManager em = emf.createEntityManager();
        JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);

        Session activeSession = jpaEntityManager.getActiveSession();
        Connection connection = (Connection) activeSession.getDatasourceLogin().connectToDatasource(null,
                activeSession);

        Metamodel mm = emf.getMetamodel();

        Set<ManagedType<?>> mts = mm.getManagedTypes();

        for (ManagedType mt : mts) {
            if (mt instanceof EntityTypeImpl) {

                EntityTypeImpl a = (EntityTypeImpl) mt;
                RelationalDescriptor rd = a.getDescriptor();

                List pkList = rd.getPrimaryKeyFields();
                if (pkList.size() == 1) {
                    DatabaseField pk = (DatabaseField) pkList.get(0);

                    DataHolder.getInstance().getAlias2TableMap().put(rd.getAlias(), rd.getTableName());

                    logger.debug(rd.getAlias() + "  ->  " + pk.getTableName());

                    DataHolder.getInstance().getTable2AliasMap().put(rd.getTableName(), rd.getAlias());

                    DataHolder.getInstance().getAlias2EclipseLinkDescriptor().put(rd.getAlias(), rd);

                    DataHolder.getInstance().getTablePkMap().put(rd.getTableName(), pk.getName());

                    logger.debug(rd.getTableName() + " PKName ->  " + pk.getName());

                } else {
                    // throw (new MultiKeyFieldException());
                }

            }

        }

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

        buildMappingsEclipseLink(desriptor, wwb, scBean);

        return scBean;
    }

    public static void buildMappingsEclipseLink(RelationalDescriptor desriptor, WritableWorkbook wwb,
            SheetContentBean scBean) {
        ToplinkDescriptorToExcel.buildMappingHeader(scBean);
        List mList = desriptor.getMappings();

        for (int i = 0; i < mList.size(); i++) {
            DatabaseMapping map = (DatabaseMapping) mList.get(i);

            if (map instanceof DirectToFieldMapping) {
                handleDirectToFieldMappingEclipseLink(map, scBean, i);
            }

            if (map instanceof OneToOneMapping) {
                handleOneToOneMappingEclipseLink(map, wwb, scBean, i);
            }

            if (map instanceof OneToManyMapping) {
                handleOneToManyMappingEclipseLink(map, wwb, scBean, i);
            }

            if (map instanceof ManyToManyMapping) {
                handleManyToManyMappingEclipseLink(map, wwb, scBean, i);
            }

        }
    }

    public static void handleDirectToFieldMappingEclipseLink(DatabaseMapping map, SheetContentBean scBean, int i) {
        DirectToFieldMapping dtfm = (DirectToFieldMapping) map;

        CellBean cbType = new CellBean();
        cbType.setCellConent(CommonConstants.TYPE_DIRECTTOFIELDMAPPING);
        cbType.setPosition(new Position(CellConstants.DEFAULT_TYPE_CELL_POSITON_Y,
                CellConstants.DEFAULT_TYPE_CELL_POSITON_X + i));
        scBean.cellsList.add(cbType);

        // attribute
        CellBean cba = new CellBean();
        cba.setCellConent(dtfm.getAttributeName());
        cba.setPosition(new Position(CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_Y,
                CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_X + i));
        scBean.cellsList.add(cba);

        // field
        CellBean cbf = new CellBean();
        String fieldName = dtfm.getFieldName();

        String tmp = fieldName.replaceAll(CommonConstants.SCHEMA_HEADER, CommonConstants.EMPTY_STRING);
        tmp = tmp.substring(tmp.lastIndexOf(".") + 1, tmp.length());
        cbf.setCellConent(tmp);

        // cbf.setCellConent(fieldName.replaceAll(CommonConstants.SCHEMA_HEADER,
        // CommonConstants.EMPTY_STRING));

        cbf.setPosition(new Position(CellConstants.DEFAULT_FIELD_CELL_POSITON_Y,
                CellConstants.DEFAULT_FIELD_CELL_POSITON_X + i));
        scBean.cellsList.add(cbf);
    }

    public static void handleOneToOneMappingEclipseLink(DatabaseMapping map, WritableWorkbook wwb,
            SheetContentBean scBean, int i) {
        OneToOneMapping theMap = (OneToOneMapping) map;

        CellBean cbType = new CellBean();
        cbType.setCellConent(CommonConstants.TYPE_ONETOONEMAPPING);
        cbType.setPosition(new Position(CellConstants.DEFAULT_TYPE_CELL_POSITON_Y,
                CellConstants.DEFAULT_TYPE_CELL_POSITON_X + i));
        scBean.cellsList.add(cbType);

        // TODO: apply stategy pattern
        // attribute
        CellBean cbattr = new CellBean();
        cbattr.setCellConent(theMap.getAttributeName());
        cbattr.setPosition(new Position(CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_Y,
                CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_X + i));
        scBean.cellsList.add(cbattr);

        // field
        // theMap.get
        CellBean cbfield = new CellBean();
        String fieldName = theMap.getSourceToTargetKeyFields().toString();
        cbfield.setCellConent(fieldName.replaceAll(CommonConstants.SCHEMA_HEADER, CommonConstants.EMPTY_STRING));
        cbfield.setPosition(new Position(CellConstants.DEFAULT_FIELD_CELL_POSITON_Y,
                CellConstants.DEFAULT_FIELD_CELL_POSITON_X + i));
        scBean.cellsList.add(cbfield);

        // refereced class
        CellBean cbref = new CellBean();
        cbref.setCellConent(ToplinkDescriptorToExcel.getBriefClassName(theMap.getReferenceClassName()));
        cbref.setPosition(new Position(CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_Y,
                CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_X + i));
        scBean.cellsList.add(cbref);

        // link
        cbref.linkTo(wwb, cbref.getCellConent(), null);

    }

    public static void handleOneToManyMappingEclipseLink(DatabaseMapping map, WritableWorkbook wwb,
            SheetContentBean scBean, int i) {
        OneToManyMapping theMap = (OneToManyMapping) map;

        CellBean cbType = new CellBean();
        cbType.setCellConent(CommonConstants.TYPE_ONETOMANYMAPPING);
        cbType.setPosition(new Position(CellConstants.DEFAULT_TYPE_CELL_POSITON_Y,
                CellConstants.DEFAULT_TYPE_CELL_POSITON_X + i));
        scBean.cellsList.add(cbType);

        // TODO: apply stategy pattern
        // attribute
        CellBean cbattr = new CellBean();
        cbattr.setCellConent(theMap.getAttributeName());
        cbattr.setPosition(new Position(CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_Y,
                CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_X + i));
        scBean.cellsList.add(cbattr);

        // refereced class
        CellBean cbref = new CellBean();
        cbref.setCellConent(ToplinkDescriptorToExcel.getBriefClassName(theMap.getReferenceClassName()));
        cbref.setPosition(new Position(CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_Y,
                CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_X + i));
        scBean.cellsList.add(cbref);
        // link
        cbref.linkTo(wwb, cbref.getCellConent(), null);

        // field
        CellBean cbSource = new CellBean();
        // theMap.getSourceKeyFields().toString()
        String sourceField = theMap.getSourceKeyFields().toString();
        cbSource.setCellConent(sourceField.replaceAll(CommonConstants.SCHEMA_HEADER, CommonConstants.EMPTY_STRING));
        cbSource.setPosition(new Position(CellConstants.DEFAULT_SOURCE_CELL_POSITON_Y,
                CellConstants.DEFAULT_SOURCE_CELL_POSITON_X + i));
        scBean.cellsList.add(cbSource);

        // target
        CellBean cbTarget = new CellBean();
        String targetField = theMap.getTargetForeignKeyFields().toString();
        cbTarget.setCellConent(targetField.replaceAll(CommonConstants.SCHEMA_HEADER, CommonConstants.EMPTY_STRING));
        cbTarget.setPosition(new Position(CellConstants.DEFAULT_TARGET_CELL_POSITON_Y,
                CellConstants.DEFAULT_TARGET_CELL_POSITON_X + i));
        scBean.cellsList.add(cbTarget);
    }

    public static void handleManyToManyMappingEclipseLink(DatabaseMapping map, WritableWorkbook wwb,
            SheetContentBean scBean, int i) {
        ManyToManyMapping theMap = (ManyToManyMapping) map;

        CellBean cbType = new CellBean();
        cbType.setCellConent(CommonConstants.TYPE_MANYTOMANYMAPPING);
        cbType.setPosition(new Position(CellConstants.DEFAULT_TYPE_CELL_POSITON_Y,
                CellConstants.DEFAULT_TYPE_CELL_POSITON_X + i));
        scBean.cellsList.add(cbType);
        // TODO: apply stategy pattern
        // attribute
        CellBean cbattr = new CellBean();
        cbattr.setCellConent(theMap.getAttributeName());
        cbattr.setPosition(new Position(CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_Y,
                CellConstants.DEFAULT_ATTRIBUTE_CELL_POSITON_X + i));
        scBean.cellsList.add(cbattr);

        // refereced class
        CellBean cbref = new CellBean();
        cbref.setCellConent(ToplinkDescriptorToExcel.getBriefClassName(theMap.getReferenceClassName()));
        cbref.setPosition(new Position(CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_Y,
                CellConstants.DEFAULT_REFERENCECLASS_CELL_POSITON_X + i));
        scBean.cellsList.add(cbref);
        // link
        cbref.linkTo(wwb, cbref.getCellConent(), null);

        // field
        // theMap.get
        CellBean cbRelTable = new CellBean();
        cbRelTable.setCellConent(theMap.getRelationTable().getName());
        cbRelTable.setPosition(new Position(CellConstants.DEFAULT_RELTABLE_CELL_POSITON_Y,
                CellConstants.DEFAULT_RELTABLE_CELL_POSITON_X + i));
        scBean.cellsList.add(cbRelTable);

        CellBean cbSource = new CellBean();
        String sourceField = theMap.getSourceRelationKeyFields().toString();
        cbSource.setCellConent(sourceField.replaceAll(CommonConstants.SCHEMA_HEADER, CommonConstants.EMPTY_STRING));
        cbSource.setPosition(new Position(CellConstants.DEFAULT_SOURCE_CELL_POSITON_Y,
                CellConstants.DEFAULT_SOURCE_CELL_POSITON_X + i));
        scBean.cellsList.add(cbSource);

        CellBean cbTarget = new CellBean();
        String targetField = theMap.getTargetRelationKeyFields().toString();
        cbTarget.setCellConent(targetField.replaceAll(CommonConstants.SCHEMA_HEADER, CommonConstants.EMPTY_STRING));
        cbTarget.setPosition(new Position(CellConstants.DEFAULT_TARGET_CELL_POSITON_Y,
                CellConstants.DEFAULT_TARGET_CELL_POSITON_X + i));
        scBean.cellsList.add(cbTarget);

    }

}
