package com.gengfo.mapping.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import oracle.toplink.descriptors.RelationalDescriptor;
import oracle.toplink.internal.helper.DatabaseTable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gengfo.excel.EclipselinkDescriptorToExcel;
import com.gengfo.exception.MultiKeyFieldException;
import com.gengfo.mapping.eclipselink.DataOutputHelper4EclipseLink;
import com.gengfo.mapping.toplink.TableRel;
import com.gengfo.mapping.toplink.ToplinkMappingHelper;
import com.gengfo.mapping.toplink.ToplinkProjectManual;
import com.gengfo.or.OR4ToplinkHelper;
import com.gengfo.or.common.CommonUtils;
import com.gengfo.or.common.DataHolder;

public class MappingHelper {

    private static Logger logger = LogManager.getLogger(MappingHelper.class);

    private static final long serialVersionUID = 1L;

    private static final String REL_USAGE = "TOPLINK";

    // private static final String REL_USAGE = "MANAUL";

    public static String dbEnv = "DEV"; // SIT,DEV,PRE,PRD,UAT

    public static String mappingType = ""; // SIT,DEV,PRE,PRD,UAT

    public static String schemaNameIps = "IVO_AEOWNER"; // SIT,DEV,PRE,PRD,UAT

    // public static String schemaNameArp = "ARPOWNER"; // SIT,DEV,PRE,PRD,UAT
    public static String schemaNameArp = ""; // SIT,DEV,PRE,PRD,UAT
    
    public static String schemaNameIr4 = "IR4_IN"; // SIT,DEV,PRE,PRD,UAT
    
    public static String schemaNameSps = "SPS_IN"; // SIT,DEV,PRE,PRD,UAT
    

    // public static String tabelePreIps = schemaNameIps + "."; //
    // SIT,DEV,PRE,PRD,UAT

    // public static String tabelePreArp = schemaNameArp + "."; //
    // SIT,DEV,PRE,PRD,UAT

    private static Map getEnvMap() {
        Map envMap = new Hashtable();

        return envMap;
    }

    public static Connection getConnectionIps() {
        String url = null;
        String username = null;
        String password = null;
        String drivers = "oracle.jdbc.driver.OracleDriver";
        if (drivers != null)
            System.setProperty("jdbc.drivers", drivers);

        if (DBConstants.DB_DEV.equals(dbEnv)) {
            url = "jdbc:oracle:thin:@shasudv7:1521:ivodb"; // dev
            username = "ivo_aeowner";
            password = "ivoaeowner";
        }

        if (DBConstants.DB_SIT.equals(dbEnv)) {
            url = "jdbc:oracle:thin:@shasudv5:1521:ivodb"; // sit
            username = "ivo_aeowner";
            password = "ivoaeowner";
        }

        if (DBConstants.DB_UAT.equals(dbEnv)) {
            url = "jdbc:oracle:thin:@hkhpdv18:1521:ivouat"; // uat
            username = "ivo_user";
            password = "ivouser";
        }

        if (DBConstants.DB_PRD.equals(dbEnv)) {
            url = "jdbc:oracle:thin:@ivodbprd:1521:ivoprd"; // prd
            username = "ivo_aesupp";
            password = "ivoaesupp";
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getConnectionArp() throws SQLException, IOException {
        String url = null;
        String username = null;
        String password = null;
        String drivers = "oracle.jdbc.driver.OracleDriver";
        if (drivers != null)
            System.setProperty("jdbc.drivers", drivers);

        if (DBConstants.DB_DEV.equals(dbEnv)) {
            url = "jdbc:oracle:thin:@shasudv7:1521:ivodb"; // dev
            username = "ivo_aeowner";
            password = "ivoaeowner";
        }

        if (DBConstants.DB_SIT.equals(dbEnv)) {
            url = "jdbc:oracle:thin:@shasudv5:1521:ivodb"; // sit
            username = "ivo_aeowner";
            password = "ivoaeowner";
        }

        if (DBConstants.DB_UAT.equals(dbEnv)) {
            url = "jdbc:oracle:thin:@hkhpdv18:1521:ivouat"; // uat
            username = "ivo_user";
            password = "ivouser";
        }

        if (DBConstants.DB_PRD.equals(dbEnv)) {
            url = "jdbc:oracle:thin:@ivodbprd:1521:ivoprd"; // prd
            username = "ivo_aesupp";
            password = "ivoaesupp";
        }

        return DriverManager.getConnection(url, username, password);
    }

    public static String getTableKeyField(String tableName) throws MultiKeyFieldException {

        String atableName = "";
        if (tableName.contains(".")) {
            atableName = tableName.substring(tableName.indexOf(".") + 1, tableName.length());
        } else {
            atableName = tableName;
        }

        String tablePkName = DataHolder.getInstance().getTablePkMap().get(atableName);
        return tablePkName;

    }

    public static List<String> getTableContent(String tableName, String whereClause) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        List<String> fkValueList = new Vector();

        try {
            con = DataHolder.getInstance().getConnection();

            stmt = con.createStatement();

            String sqlToRun = "SELECT * FROM ";
            sqlToRun = sqlToRun + tableName;
            sqlToRun = sqlToRun + " WHERE ";
            sqlToRun = sqlToRun + whereClause;

            logger.debug("to get relations->" + sqlToRun);

            rs = stmt.executeQuery(sqlToRun);
            int row = 0;
            while (rs.next()) {
                row = row + 1;
                ResultSetMetaData rsmd = rs.getMetaData();

                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String columnName = rsmd.getColumnLabel(i);
                    String key = DataHolder.getInstance().getTablePkMap().get(tableName);
                    if (columnName.equals(key)) {
                        fkValueList.add(rs.getString(i));
                    }

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
        }
        return fkValueList;
    }

    public static List<String> showTableContent(String tableName, String whereClause) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer();

        List<String> resultList = new Vector();

        try {
            // con = getConnectionIps();
            con = DataHolder.getInstance().getConnection();

            stmt = con.createStatement();

            String sqlToRun = "SELECT * FROM ";
            sqlToRun = sqlToRun + tableName;
            sqlToRun = sqlToRun + " WHERE ";
            sqlToRun = sqlToRun + whereClause;

            // to remove
            System.out.println("To run sql in showTableContent: " + sqlToRun);
            rs = stmt.executeQuery(sqlToRun);

            sb.append("\n\r");
            int row = 0;
            while (rs.next()) {
                row = row + 1;

                System.out.print(" === row ");
                sb.append(" === row ");

                System.out.print(row);
                sb.append(row);

                System.out.print(" ");
                sb.append(" ");

                System.out.print(tableName);
                sb.append(tableName);

                System.out.print(" === ");
                sb.append(" === ");

                System.out.println();
                sb.append("\n\r");

                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String columnName = rsmd.getColumnLabel(i);

                    System.out.print("column name: ");
                    sb.append("column name: ");

                    System.out.print(columnName);
                    sb.append(columnName);

                    System.out.print(" = ");
                    sb.append(" = ");

                    System.out.print(rs.getString(i));
                    sb.append(rs.getString(i));

                    // get return keys

                    // String key = (String)
                    // getTableKeyMapManual().get(tableName);
                    String key = null;
                    if (REL_USAGE.equals("TOPLINK")) {
                        key = (String) ToplinkMappingHelper.getTablePKeyMapToplink().get(tableName);
                    }
                    if (REL_USAGE.equals("MANAUL")) {
                        key = (String) ToplinkProjectManual.getTableKeyMapManual().get(tableName);
                    }
                    if (columnName.equals(key)) {
                        resultList.add(rs.getString(i));
                    }

                    System.out.println();
                    sb.append("\n\r");
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
        }
        return resultList;
    }

    public static void showTableContent(String tableName, String mappingKey, String oid) {

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        // StringBuffer sb = new StringBuffer();

        List<String> resultList = new Vector();

        try {
            // con = getConnectionIps();
            con = DataHolder.getInstance().getConnection();

            stmt = con.createStatement();

            String sqlToRun = "SELECT * FROM ";
            sqlToRun = sqlToRun + tableName;
            sqlToRun = sqlToRun + " WHERE ";
            sqlToRun = sqlToRun + mappingKey + " = " + oid;

            // to remove
            System.out.println("To run sql in showTableContent: " + sqlToRun);
            rs = stmt.executeQuery(sqlToRun);

            // sb.append("\n\r");
            int row = 0;
            while (rs.next()) {
                row = row + 1;

                System.out.print(" === row ");
                // sb.append(" === row ");

                System.out.print(row);
                // sb.append(row);

                System.out.print(" ");
                // sb.append(" ");

                System.out.print(tableName);
                // sb.append(tableName);

                System.out.print(" === ");
                // sb.append(" === ");

                System.out.println();
                // sb.append("\n\r");

                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String columnName = rsmd.getColumnLabel(i);

                    // System.out.print("column name: ");
                    // sb.append("column name: ");

                    System.out.print(columnName);
                    // sb.append(columnName);

                    System.out.print(" = ");
                    // sb.append(" = ");

                    System.out.print(rs.getString(i));
                    // sb.append(rs.getString(i));

                    // get return keys

                    // String key = (String)
                    // // getTableKeyMapManual().get(tableName);
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
                    //
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

        }
        // return resultList;
    }

    public static String showTableContent(String tableName) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuffer sb = new StringBuffer();

        try {

            // con = getConnectionIps();
            con = DataHolder.getInstance().getConnection();

            stmt = con.createStatement();

            String sqlToRun = "SELECT * FROM ";
            sqlToRun = sqlToRun + tableName;

            // to remove print
            System.out.println("Torun sql in showTableContent: " + sqlToRun);

            rs = stmt.executeQuery(sqlToRun);

            int row = 0;
            if (rs.getRow() == 0) {
                System.out.println(" ======zero=======");
                return sb.toString();
            }

            while (rs.next()) {
                row = row + 1;

                System.out.print(" === row ");
                sb.append(" === row ");

                System.out.print(row);
                sb.append(row);

                System.out.print(" ");
                sb.append(" ");

                System.out.print(tableName);
                sb.append(tableName);

                System.out.print(" === ");
                sb.append(" === ");

                System.out.println();
                sb.append("\n\r");

                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String columnName = rsmd.getColumnLabel(i);

                    System.out.print("column name: ");
                    sb.append("column name: ");

                    System.out.print(columnName);
                    sb.append(columnName);

                    System.out.print(" = ");
                    sb.append(" = ");

                    System.out.print(rs.getString(i));
                    sb.append(rs.getString(i));

                    System.out.println();
                    sb.append("\n\r");
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

        }

        return sb.toString();
    }

    public static void dispose() {

    }

    public static String getDBFiledValueByOid(String tableName, String filedName, String tablePkName,
            String tablePkValue) {
        String fieldValue = "";
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        con = DataHolder.getInstance().getConnection();

        try {
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sqlToRun = "SELECT ";
        sqlToRun = sqlToRun + filedName;
        sqlToRun = sqlToRun + " FROM ";
        sqlToRun = sqlToRun + tableName;
        sqlToRun = sqlToRun + " a WHERE ";
        // sqlToRun = sqlToRun + " a.OID = ";
        sqlToRun = sqlToRun + tablePkName;
        sqlToRun = sqlToRun + " = ";
        sqlToRun = sqlToRun + tablePkValue;

        // to keep
        // System.out.println("sqlToRun in getDBFiledValueByOid: " + sqlToRun);

        try {
            rs = stmt.executeQuery(sqlToRun);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // /while (rsnext()) {
            if (rs.next()) {
                fieldValue = rs.getString(1);
            }

            // }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // rs = null;
        // stmt = null;
        // con = null;

        return fieldValue;
    }

    public static String getTableFiledValue(String srcTableName, String tableFiledName, String tableFieldValue) {
        String fieldValue = "";
        try {

            String tablePkName = getTableKeyField(srcTableName);

            fieldValue = MappingHelper.getDBFiledValueByOid(srcTableName, tableFiledName, tablePkName,
                    tableFieldValue);

        } catch (MultiKeyFieldException e) {
            e.printStackTrace();
        }
        return fieldValue;
    }

    public static void retrieveSingleTableContent(String aliasName, String mappingKey, String oid) {

        String tab = DataHolder.getInstance().getAlias2TableMap().get(aliasName);

        List rList = new Vector();
        MappingHelper.showTableContent(tab, mappingKey, oid);
    }

    public static String getTableName(String desAlias) {

        RelationalDescriptor rd = (RelationalDescriptor) DataHolder.getInstance().getProject()
                .getAliasDescriptors().get(desAlias);

        DatabaseTable dt = (DatabaseTable) rd.getTables().get(0);

        return dt.getName();

    }

    public static String getKey(String aliasName, String mappingKey, String keyValue) {
        return aliasName + "-" + mappingKey.toUpperCase() + "-" + keyValue;
    }

    public static void initFirstMapping(String aliasName, String mappingKey, String oid) {

        DataHolder handledSet = DataHolder.getInstance();

        handledSet.getInstance().getHandledStatus().put(getKey(aliasName, mappingKey, oid), Boolean.FALSE);

    }

    public static void inertToToHandleAsTrue(String aliasName, String mappingKey, String oid) {

        DataHolder handledSet = DataHolder.getInstance();

        handledSet.getInstance().getHandledStatus().put(getKey(aliasName, mappingKey, oid), Boolean.TRUE);

    }

    // /**
    // *
    // * @param srcTableName
    // * @param srcOid
    // * @param trList
    // * @param dbSchema TODO
    // * @return
    // * @throws MultiKeyFieldException
    // */
    // public static String getTableRelatedFromDB(String srcTableName,
    // String srcOid, List trList, String dbSchema) throws
    // MultiKeyFieldException {
    //
    // StringBuffer sb = new StringBuffer();
    // // sb.append(DBHelper.getTableValue(tableName,invOid));
    // List rList = new Vector();
    // if (null == trList || trList.size() == 0) {
    // return "";
    // }
    //
    // for (int i = 0; i < trList.size(); i++) {
    // TableRel tr = (TableRel) trList.get(i);
    //
    // String whereClause = MappingHelper.getTableRelClause(tr, srcOid);
    //
    // System.out.println();
    // System.out.println("-------------- to show rel: --------------");
    //
    // String srcTbName = tr.getSourceTbName();
    // srcTbName = tabelePre + srcTbName;
    // String descTbName = tr.getDestTbName();
    // descTbName = tabelePre + descTbName;
    // String srcTbFieldName = tr.getFieldPairs()[0].getSourceFd();
    // String descTbFieldName = tr.getFieldPairs()[0].getDestinationFd();
    //
    // System.out.println(srcTbName + " ---> " + descTbName);
    //
    // System.out.println("rel: " + srcTbFieldName + " and "
    // + descTbFieldName);
    //
    // // sb.append(DBHelperR0202.showTableContent(descTbName,
    // // whereClause, rList));
    // rList = MappingHelper.getTableContent(descTbName, whereClause);
    //
    // // show deep level,recurse to find sub table
    // for (int j = 0; j < rList.size(); j++) {
    // String destOid = (String) rList.get(j);
    //
    // String destKeyValue = getTableFiledValue(descTbName,
    // getTableKeyField(descTbName), destOid);
    //
    // outputDataToplink(descTbName, null, destKeyValue, null);
    // }
    // rList = null;
    // }
    //
    // return sb.toString();
    // }

    public static String getTableRelClause(TableRel tr, String tableKeyValue, String dbSchema) {

        if (StringUtils.isEmpty(tableKeyValue)) {
            return "";
        }

        // sample SQL
        //

        String srcTableName = tr.getSourceTbName();

        if (!StringUtils.isEmpty(dbSchema)) {
            srcTableName = dbSchema + "." + srcTableName;
        }

        FieldPair[] fps = tr.getFieldPairs();
        String whereClause = "";

        for (int i = 0; i < fps.length; i++) {
            if (i != 0) {
                whereClause = whereClause + " " + "and" + " ";
            }
            FieldPair fp = fps[i];
            String fk = MappingHelper.getTableFiledValue(srcTableName, fp.getSourceFd(), tableKeyValue);
            if (!StringUtils.isEmpty(fk)) {
                whereClause = whereClause + fp.getDestinationFd() + "=" + fk;
            }
        }

        return whereClause;
    }

    public static void outputDataToExcel(Set<String> set, String xlsFileName) {

        Set<String> keySet = new TreeSet<String>();
        keySet.addAll(set);

        Iterator<String> it = keySet.iterator();
        List<String> keyList = new ArrayList<String>();

        keyList.addAll(keySet);

        EclipselinkDescriptorToExcel.toExcelSheetsWithData(xlsFileName, keyList);
    }

    public static Set<String> collectAllMappingKeys(String aliasName, String key, String oid) {

        initFirstMapping(aliasName, key, oid);

        collectMoreMappingKeys(aliasName, key, oid);

        Set<String> set = DataHolder.getInstance().getHandledStatus().keySet();

        return set;
    }

    public static void collectMoreMappingKeys(String aliasName, String key, String oid) {

        DataHolder handledSet = DataHolder.getInstance();

        String setKey = getKey(aliasName, key, oid);

        while (null != hasItemToHandle(handledSet.getHandledStatus())) {

            Boolean b = (Boolean) handledSet.getHandledStatus().get(setKey);

            String splited[] = setKey.split("-");
            aliasName = splited[0];
            key = splited[1];
            oid = splited[2];

            if (!b.booleanValue()) {

                CommonUtils.moveToHandled(aliasName, key, oid);

                List<TableRel> tableOwnList = ToplinkMappingHelper
                        .getTabbleRels(handledSet.getProject(), aliasName);

                for (TableRel tr : tableOwnList) {

                    findMoreToHandle(aliasName, key, oid, tr, MappingHelper.schemaNameIps);

                }

            } else {
            }

            String asetky = hasItemToHandle(handledSet.getHandledStatus());
            if (null != asetky) {
                setKey = asetky;
            }
        }

    }

    public static String hasItemToHandle(Map<String, Boolean> theMap) {
        Set<String> set = theMap.keySet();

        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            Boolean b = theMap.get(key);
            if (!b.booleanValue()) {
                return key;
            }
        }

        return null;

    }

    public static void findMoreToHandle(String aliasName, String keyFieldName, String keyFieldValue,
            TableRel tableRel, String dbSchema) {

        String whereClause = MappingHelper.getTableRelClause(tableRel, keyFieldValue, dbSchema);
        List<String> fkValueList = MappingHelper.getTableContent(tableRel.getDestTbName(), whereClause);
        if (null != fkValueList && fkValueList.size() > 0) {
            for (String akey : fkValueList) {
                String tmpAlias = "";
                Map tm = DataHolder.getInstance().getTable2AliasMap();
                tmpAlias = (String) tm.get(tableRel.getDestTbName());
                String keyOfAliasKfNameKfValue = getKey(tmpAlias, tableRel.getFieldPairs()[0].getDestinationFd(),
                        akey);
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
                    DataHolder.getInstance().getHandledStatus().put(keyOfAliasKfNameKfValue, Boolean.FALSE);
                    logger.debug("find new one to handle -> " + keyOfAliasKfNameKfValue);
                }
            }
        } else {
            // System.out.println("no result");
        }

    }

    public static List filterTableRel(String tableName, List relList) {
        List rList = new ArrayList();
        for (int i = 0; i < relList.size(); i++) {
            TableRel tr = (TableRel) relList.get(i);
            if (tableName.equals(tr.getSourceTbName())) {
                System.out.println(tr.toString());
                rList.add(tr);
            }
        }
        return rList;
    }

    /**
     * @deprecated
     * @return
     */
    private static TableRel getAR_INV_CHG_POOLAndAR_INV_REF_SHIPMENT() {
        TableRel tr = new TableRel();
        tr.setSourceTbName(DBConstants.TB_AR_INV_CHG_POOL);
        tr.setDestTbName(DBConstants.TB_AR_INV_REF_SHIPMENT);

        FieldPair[] fps = new FieldPair[1];
        FieldPair fp = new FieldPair();
        fps[0] = fp;

        fp.setSourceFd("OID");
        fp.setDestinationFd("OWNER");

        tr.setFieldPairs(fps);
        return tr;
    }

    /**
     * Sample usage
     * 
     */

    public static void main(String args[]) {

        String tableName = DBConstants.TB_AR_INV_CHG_POOL;
        String tbOid = "20218";
        MappingHelper.retrieveSingleTableContent(tableName, null, tbOid);
        OR4ToplinkHelper.outputDataToplink(tableName, null, tbOid, null);

        System.out.println("Done!");

    }

    public String getDbEnv() {
        return dbEnv;
    }

    public void setDbEnv(String dbEnv) {
        this.dbEnv = dbEnv;
    }

}
