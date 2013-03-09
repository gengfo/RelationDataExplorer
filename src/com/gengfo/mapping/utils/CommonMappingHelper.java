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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gengfo.excel.EclipselinkDescriptorToExcel;
import com.gengfo.mapping.eclipselink.DataOutputHelper4EclipseLink;
import com.gengfo.mapping.toplink.TableRel;
import com.gengfo.mapping.toplink.ToplinkMappingHelper;
import com.gengfo.mapping.toplink.ToplinkProjectManual;
import com.gengfo.or.OR4ToplinkHelper;
import com.gengfo.or.common.DataHolder;
import com.gengfo.or.common.DbUtils;

public class CommonMappingHelper {

    public static Logger logger = LogManager.getLogger(CommonMappingHelper.class);

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

    public static void retrieveSingleTableContent(String aliasName, String mappingKey, String oid) {

        String tab = DataHolder.getInstance().getAlias2TableMap().get(aliasName);

        List rList = new Vector();
        DbUtils.showTableContent(tab, mappingKey, oid);
    }

    public static String getTableName(String desAlias) {

        RelationalDescriptor rd = (RelationalDescriptor) DataHolder.getInstance().getProject()
                .getAliasDescriptors().get(desAlias);

        DatabaseTable dt = (DatabaseTable) rd.getTables().get(0);

        return dt.getName();

    }

    public static String getKey(String aliasName, String pkName, String pkValue) {
        return aliasName + "-" + pkName.toUpperCase() + "-" + pkValue;
    }

    public static void initFirstMapping(String aliasName, String pkName, String pkValue) {
        
        //TODO GENGFO TO EXTACT SET AS PARAMETER

        DataHolder handledSet = DataHolder.getInstance();
        
        String mappedKeyStr = CommonMappingHelper.getKey(aliasName, pkName, pkValue);

        handledSet.getInstance().getHandledStatus().put(mappedKeyStr, Boolean.FALSE);

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

    public static void outputDataToExcel(Set<String> set, String xlsFileName, Connection con) {

        Set<String> keySet = new TreeSet<String>();
        keySet.addAll(set);

        Iterator<String> it = keySet.iterator();
        List<String> keyList = new ArrayList<String>();

        keyList.addAll(keySet);

        EclipselinkDescriptorToExcel.toExcelSheetsWithData(xlsFileName, keyList, con);
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
        CommonMappingHelper.retrieveSingleTableContent(tableName, null, tbOid);
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
