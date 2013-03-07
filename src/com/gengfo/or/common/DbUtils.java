package com.gengfo.or.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

public class DbUtils {

    public static String getDBFiledValueByKey(String srcTableName, String srcTableMappedFiledName, String srcTablePkName,
            String tablePkValue, Connection con) {
        
        
        String fieldValue = "";
        //Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
    
        //con = DataHolder.getInstance().getConnection();
    
        try {
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
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

}
