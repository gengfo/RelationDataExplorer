package com.gengfo.or.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gengfo.mapping.utils.CommonMappingHelper;

public class DbUtils {

    public static Logger logger = LogManager.getLogger(DbUtils.class);

    public static String getDBFiledValueByKey(String srcTableName, String srcTableMappedFiledName,
            String srcTablePkName, String tablePkValue, Connection con, Statement stmt) {

        String fieldValue = "";
        // Connection con = null;
        //Statement stmt = null;
        ResultSet rs = null;

        // con = DataHolder.getInstance().getConnection();

     

        String sqlToRun = "SELECT ";
        sqlToRun = sqlToRun + srcTableMappedFiledName;
        sqlToRun = sqlToRun + " FROM ";
        sqlToRun = sqlToRun + srcTableName;
        sqlToRun = sqlToRun + " a WHERE ";
        // sqlToRun = sqlToRun + " a.OID = ";
        sqlToRun = sqlToRun + srcTablePkName;
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

       
        // rs = null;
        // stmt = null;
        // con = null;

        return fieldValue;
    }

    public static List<String> getDBFiledValueByKeys(String srcTableName, String srcTableMappedFiledName,
            String srcTablePkName, String tablePkValue, Connection con, Statement stmt) {

        List<String> resultList = new ArrayList<String>();

        String fieldValue = "";
        // Connection con = null;
        //Statement stmt = null;
        ResultSet rs = null;

        // con = DataHolder.getInstance().getConnection();

     

        String sqlToRun = "SELECT ";
        sqlToRun = sqlToRun + srcTableMappedFiledName;
        sqlToRun = sqlToRun + " FROM ";
        sqlToRun = sqlToRun + srcTableName;
        sqlToRun = sqlToRun + " a WHERE ";
        // sqlToRun = sqlToRun + " a.OID = ";
        sqlToRun = sqlToRun + srcTablePkName;
        sqlToRun = sqlToRun + " = ";
        sqlToRun = sqlToRun + tablePkValue;

        // to keep
        // System.out.println("sqlToRun in getDBFiledValueByOid: " + sqlToRun);

        try {

            logger.debug("to fetch-> " + sqlToRun);
            rs = stmt.executeQuery(sqlToRun);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // /while (rsnext()) {
            if (rs.next()) {
                fieldValue = rs.getString(1);
                resultList.add(fieldValue);
            }

            // }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // rs = null;
        // stmt = null;
        // con = null;

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

//            try {
//                stmt.close();
//                con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }

        }
        // return resultList;
    }

    public static List<String> getTableContentDirectly(String tableName, String whereClause, Connection con, Statement stmt) {
        //Connection con = null;
        //Statement stmt = null;
        ResultSet rs = null;
    
        List<String> fkValueList = new Vector();
    
        try {
            //con = DataHolder.getInstance().getConnection();
    
            //stmt = con.createStatement();
    
            String sqlToRun = "SELECT * FROM ";
            sqlToRun = sqlToRun + tableName;
            sqlToRun = sqlToRun + " WHERE ";
            sqlToRun = sqlToRun + whereClause;
    
            CommonMappingHelper.logger.debug("to get relations->" + sqlToRun);
    
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
           
        }
        return fkValueList;
    }

}
