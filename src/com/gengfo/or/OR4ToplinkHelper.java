package com.gengfo.or;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.toplink.descriptors.RelationalDescriptor;
import oracle.toplink.internal.helper.DatabaseField;
import oracle.toplink.internal.helper.DatabaseTable;
import oracle.toplink.mappings.ManyToManyMapping;
import oracle.toplink.publicinterface.Descriptor;
import oracle.toplink.sessions.Project;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gengfo.common.CommonConstants;
import com.gengfo.exception.MultiKeyFieldException;
import com.gengfo.mapping.toplink.TableRel;
import com.gengfo.mapping.toplink.ToplinkMappingHelper;
import com.gengfo.mapping.utils.CommonMappingHelper;
import com.gengfo.mapping.utils.DBConstants;
import com.gengfo.mapping.utils.FieldPair;
import com.gengfo.or.common.CommonUtils;
import com.gengfo.or.common.DataHolder;
import com.gengfo.or.common.DbUtils;
import com.oocl.ivo.domain.mapping.IVOProject;

public class OR4ToplinkHelper {

    public static Logger logger = LogManager.getLogger(OR4ToplinkHelper.class);
    
    public static void outputDataToplink(String aliasName, String key, String oid, String xlsFileName) {

        OR4ToplinkHelper.initDataHoderToplink();

        Connection con = CommonMappingHelper.getConnectionIps();
        try {
            Statement stmt = con.createStatement();
       

        Set<String> set = OR4ToplinkHelper.collectAllMappingKeysToplink(aliasName, key, oid, con, stmt);

        CommonMappingHelper.outputDataToExcel(set, xlsFileName, con);
        
        try {
            stmt.close();
            con.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


    }

    public static void initDataHoderToplink() {

        Map<String, String> alias2TableMap = OR4ToplinkHelper.getToplinkAliasTableNameMaps();
        DataHolder.getInstance().getAlias2TableMap().putAll(alias2TableMap);

        Map<String, String> table2AliasMap = ToplinkMappingHelper.getTableNameAliasMapToplink();
        DataHolder.getInstance().getTable2AliasMap().putAll(table2AliasMap);

        Map<String, Descriptor> alias2Descriptor = ToplinkMappingHelper.getAliasDescriptorMapToplink();
        DataHolder.getInstance().getAlias2Descriptor().putAll(alias2Descriptor);

        Map<String, String> table2Pk = ToplinkMappingHelper.getTablePKeyMapToplink();
        DataHolder.getInstance().getTablePkMap().putAll(table2Pk);

        DataHolder.getInstance().setMappingType(CommonConstants.MAPPING_TYPE_TOPLINK);

    }

    public static Map<String, String> getToplinkAliasTableNameMaps() {
        Project theProject = OR4ToplinkHelper.getIVOProject();

        Map<String, String> tableKeyMap = OR4ToplinkHelper.getAliasTableNameMapToplink(theProject);

        // tableKeyMap

        // Project theProject = getProject();
        //
        // Map aliasDescMap = theProject.getAliasDescriptors();
        // Set keySet = aliasDescMap.keySet();
        // Iterator keySetIt = keySet.iterator();
        //
        // while (keySetIt.hasNext()) {
        // String tableName = (String) keySetIt.next();
        // RelationalDescriptor rd = (RelationalDescriptor) aliasDescMap
        // .get(tableName);
        //
        // List pkList = rd.getPrimaryKeyFields();
        // if (pkList.size() == 1) {
        // DatabaseField pk = (DatabaseField) pkList.get(0);
        //
        // tableKeyMap.put(rd.getAlias(), rd.getTableName());
        // } else {
        // }
        //
        // }

        return tableKeyMap;
    }

    public static Map<String, String> getAliasTableNameMapToplink(Project theProject) {
        Map<String, String> tableKeyMap = new Hashtable();

        // Project theProject = getProject();

        Map aliasDescMap = theProject.getAliasDescriptors();
        Set keySet = aliasDescMap.keySet();
        Iterator keySetIt = keySet.iterator();

        while (keySetIt.hasNext()) {
            String tableName = (String) keySetIt.next();
            RelationalDescriptor rd = (RelationalDescriptor) aliasDescMap.get(tableName);

            List pkList = rd.getPrimaryKeyFields();
            if (pkList.size() == 1) {
                DatabaseField pk = (DatabaseField) pkList.get(0);

                tableKeyMap.put(rd.getAlias(), rd.getTableName());
            } else {
            }

        }

        return tableKeyMap;
    }

    public static Project getIVOProject() {

        return new IVOProject();

    }

    public static Project getNumberRangeProject() {

        return new IVOProject();

    }

    public static void collectMoreMappingKeysToplink(String aliasName, String origPkName,
            String srcTableKeyFieldValue, Connection con, Statement stat1) {

        DataHolder handledSet = DataHolder.getInstance();

        String mappedKeyStr = CommonMappingHelper.getKey(aliasName, origPkName, srcTableKeyFieldValue);
        
       
        while (null != CommonMappingHelper.hasItemToHandle(handledSet.getHandledStatus())) {

            Boolean b = (Boolean) handledSet.getHandledStatus().get(mappedKeyStr);

            String splited[] = mappedKeyStr.split("-");
            aliasName = splited[0];
            origPkName = splited[1];
            srcTableKeyFieldValue = splited[2];
            
           


            if (!b.booleanValue()) {
                logger.debug("handling -> "+ mappedKeyStr);
                CommonUtils.moveToHandled(aliasName, origPkName, srcTableKeyFieldValue);
                
                

                List<TableRel> tableOwnList = ToplinkMappingHelper
                        .getTabbleRels(handledSet.getProject(), aliasName);

                // TableRel example: according to tableRel.getDestTbName() with srcKeyFieldValue

                // result to put aliasName FieldName fieldValue to search the table content

                for (TableRel tableRel : tableOwnList) {

                    if (!DBConstants.MAPPING_TYPE_MANYTOMANY.equals(tableRel.getRelType())) {

                        String dbSchema = CommonMappingHelper.schemaNameIps;
                        // OR4ToplinkHelper.findMoreToHandle(aliasName, keyFieldName, keyFieldValue, tableRel,
                        // dbSchema);

                        String whereClause = "";
                        // String whereClause = OR4ToplinkHelper.getTableRelClause(tableRel, srcKeyFieldValue,
                        // dbSchema);
                        if (StringUtils.isEmpty(srcTableKeyFieldValue)) {
                            whereClause = "";
                        } else {
                            String srcTableName = tableRel.getSourceTbName();

                            if (!StringUtils.isEmpty(dbSchema)) {
                                srcTableName = dbSchema + "." + srcTableName;
                            }

                            FieldPair[] mkps = tableRel.getFieldPairs();

                            for (int i = 0; i < mkps.length; i++) {
                                if (i != 0) {
                                    whereClause = whereClause + " " + "and" + " ";
                                }
                                FieldPair mappingKeyPair = mkps[i];

                                String srcTableMappedFiledName = mappingKeyPair.getSourceFd();

                                // String fk = OR4ToplinkHelper.getTableFiledValue(srcTableName, tableFiledName,
                                // srcKeyFieldValue);

                                String srcTableMappedFiledValue = "";
                                try {

                                    String srcTablePkName = CommonUtils.getTableKeyField(srcTableName);

                                    srcTableMappedFiledValue = DbUtils.getDBFiledValueByKey(srcTableName,
                                            srcTableMappedFiledName, srcTablePkName, srcTableKeyFieldValue, con, stat1);

                                } catch (MultiKeyFieldException e) {
                                    e.printStackTrace();
                                }
                                // --
                                String desTableMappedFiledName = mappingKeyPair.getDestinationFd();

                                if (!StringUtils.isEmpty(srcTableMappedFiledValue)) {
                                    whereClause = whereClause + desTableMappedFiledName + "="
                                            + srcTableMappedFiledValue;
                                }
                            }
                        }

                        //
                        String desTableName = tableRel.getDestTbName();

                        List<String> fkValueList = DbUtils.getTableContentDirectly(desTableName,
                                whereClause, con, stat1);

                        if (null != fkValueList && fkValueList.size() > 0) {
                            for (String akey : fkValueList) {
                                String tmpAlias = "";
                                Map tm = DataHolder.getInstance().getTable2AliasMap();
                                tmpAlias = (String) tm.get(tableRel.getDestTbName());

                                String keyOfAliasKfNameKfValue = CommonMappingHelper.getKey(tmpAlias,
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
                                    CommonMappingHelper.logger.debug("find new one to handle -> "
                                            + keyOfAliasKfNameKfValue);
                                }
                            }
                        } else {
                            // System.out.println("no result");
                        }

                        // --else

                    } else if (DBConstants.MAPPING_TYPE_MANYTOMANY.equals(tableRel.getRelType())) {
                        // target: many to many to put keyOfAliasKfNameKfValue to
                        // DataHolder.getInstance().getHandledStatus()
                        // input: current -> aliasName, origPkName, srcTableKeyFieldValue
                        // output: new -> input aliasName, origPkName, srcTableKeyFieldValue
                        // --
                        // select code from supp_chgcode where id = 365;
                        // select bizType from SUPP_CHGCODE_BIZTYPE where code = 365
                        // select * from supp_data where id in (70000,70001,70019)
                        // --

                        // get src table src table key src table key value
                        //String mmSrcTableName = "";
                        //String mmSrcTablePKName = "";
                        //String mmSrcTablePKValue = "";

                        // String mmMiddleTableName= "";
                        // String mmMiddleTableName= "";
                        //
                        //
                        ManyToManyMapping omm = tableRel.getToplinkMMDm();
                        //
                         List srcKeysList = omm.getSourceKeyFields();
                         List destKeyList = omm.getTargetKeyFields();
                         // check the second key
                         if (srcKeysList.size() == 1 && destKeyList.size() == 1) {
                         DatabaseField srcKeyField = (DatabaseField) srcKeysList.get(0);
                         DatabaseField desKeyField = (DatabaseField) destKeyList.get(0);
                        
                         FieldPair[] fps = new FieldPair[1];
                         FieldPair fp = new FieldPair();
                         fps[0] = fp;
                         //tr.setFieldPairs(fps);
                        
                         String srcTableName = srcKeyField.getTableName();   //supp_chargecode
                         String srcTableKeyFieldName = srcKeyField.getName();    //id
                         
                         
                         //DatabaseTable omm.get
                         DatabaseTable dt = omm.getRelationTable();
                         String midTableName =  dt.getName(); //SUPP_CHGCODE_BIZTYPE
                         
                         
                         String destTableName = desKeyField.getTableName(); //supp_data
                         String destTableFieldName = desKeyField.getName(); //id
                         
                         List midSrcKeysList = omm.getSourceRelationKeyFields();
                         List midDesKeysList  = omm.getTargetRelationKeyFields();
                         
                         DatabaseField midSrcKeyDf = (DatabaseField) midSrcKeysList.get(0);
                         DatabaseField midDesKeyDf = (DatabaseField) midDesKeysList.get(0);
                         
                         String midTableSrcKeyName = midSrcKeyDf.getName(); //code 
                         String midTableDesKeyName = midDesKeyDf.getName(); //biz_type
                         
                         //
                        List<String> mappedKeyValues= DbUtils.getDBFiledValueByKeys(midTableName, midTableDesKeyName, midTableSrcKeyName,srcTableKeyFieldValue,con, stat1);
                         
                        for (String desKey: mappedKeyValues){
                            //DbUtils.getDBFiledValueByKeys(destTableName,destTableFieldName,desKey);
                            
                            Map tm = DataHolder.getInstance().getTable2AliasMap();
                            String tmpAlias = (String) tm.get(destTableName);

                            String keyOfAliasKfNameKfValue = CommonMappingHelper.getKey(tmpAlias,
                                    destTableFieldName , desKey);
                            
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
                                CommonMappingHelper.logger.debug("find new one to handle -> "
                                        + keyOfAliasKfNameKfValue);
                            }

                                
                        }
                         //tr.setSourceTbName(srcTableName);
                         //tr.setDestTbName(destTableName);
                        // tr.setRelType(DBConstants.MAPPING_TYPE_ONETOMANY);
                        
                         //fp.setSourceFd(srcTableKeyFieldName);
                         //fp.setDestinationFd(destTableFieldName);
                        
                         // tableRelList.add(tr);
                         } else {
                         // TODOgengfo throw MultiKeyFieldException
                         }

                        // get mid table
                        // get

                    }
                }

            } else {
            }

            String asetky = CommonMappingHelper.hasItemToHandle(handledSet.getHandledStatus());
            if (null != asetky) {
                mappedKeyStr = asetky;
            }
        }

    }

    public static Set<String> collectAllMappingKeysToplink(String aliasName, String pkName, String pkValue,
            Connection con, Statement stmt) {

        
        // CommonMappingHelper.initFirstMapping(aliasName, pkName, pkValue);
        DataHolder handledSet = DataHolder.getInstance();
        String mappedKeyStr = CommonMappingHelper.getKey(aliasName, pkName, pkValue);
        handledSet.getInstance().getHandledStatus().put(mappedKeyStr, Boolean.FALSE);

        
        // --
        // connection =
        //

        collectMoreMappingKeysToplink(aliasName, pkName, pkValue, con, stmt);

        Set<String> set = DataHolder.getInstance().getHandledStatus().keySet();

        return set;
    }

    // public static void findMoreToHandle(String aliasName, String keyFieldName, String keyFieldValue,
    // TableRel tableRel, String dbSchema) {
    //
    // String whereClause = OR4ToplinkHelper.getTableRelClause(tableRel, keyFieldValue, dbSchema);
    //
    // List<String> fkValueList = CommonMappingHelper.getTableContent(tableRel.getDestTbName(), whereClause);
    // if (null != fkValueList && fkValueList.size() > 0) {
    // for (String akey : fkValueList) {
    // String tmpAlias = "";
    // Map tm = DataHolder.getInstance().getTable2AliasMap();
    // tmpAlias = (String) tm.get(tableRel.getDestTbName());
    // String keyOfAliasKfNameKfValue = CommonMappingHelper.getKey(tmpAlias,
    // tableRel.getFieldPairs()[0].getDestinationFd(), akey);
    // boolean hasflag = false;
    // Map mapOfStatus = DataHolder.getInstance().getHandledStatus();
    // Set<String> keySet = mapOfStatus.keySet();
    // Iterator<String> it = keySet.iterator();
    // while (it.hasNext()) {
    // String tmpKey = it.next();
    // if (tmpKey.equals(keyOfAliasKfNameKfValue)) {
    // hasflag = true;
    // break;
    // }
    // }
    // if (!hasflag) {
    // DataHolder.getInstance().getHandledStatus().put(keyOfAliasKfNameKfValue, Boolean.FALSE);
    // CommonMappingHelper.logger.debug("find new one to handle -> " + keyOfAliasKfNameKfValue);
    // }
    // }
    // } else {
    // // System.out.println("no result");
    // }
    //
    // }

    public static String getTableRelClause(TableRel tableRel, String srcKeyFieldValue, String dbSchema) {

        if (StringUtils.isEmpty(srcKeyFieldValue)) {
            return "";
        }

        // sample SQL
        //

        String srcTableName = tableRel.getSourceTbName();

        if (!StringUtils.isEmpty(dbSchema)) {
            srcTableName = dbSchema + "." + srcTableName;
        }

        FieldPair[] fps = tableRel.getFieldPairs();
        String whereClause = "";

        for (int i = 0; i < fps.length; i++) {
            if (i != 0) {
                whereClause = whereClause + " " + "and" + " ";
            }
            FieldPair fp = fps[i];
            String fk = OR4ToplinkHelper.getTableFiledValue(srcTableName, fp.getSourceFd(), srcKeyFieldValue);
            if (!StringUtils.isEmpty(fk)) {
                whereClause = whereClause + fp.getDestinationFd() + "=" + fk;
            }
        }

        return whereClause;
    }

    public static String getTableFiledValue(String srcTableName, String tableFiledName, String srcKeyFieldValue) {
        String fk = "";
        try {

            String tablePkName = CommonUtils.getTableKeyField(srcTableName);

            fk = DbUtils.getDBFiledValueByKey(srcTableName, tableFiledName, tablePkName, srcKeyFieldValue, null, null);

        } catch (MultiKeyFieldException e) {
            e.printStackTrace();
        }
        return fk;
    }

}
