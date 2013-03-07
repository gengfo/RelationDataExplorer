package com.gengfo.or.common;

import com.gengfo.exception.MultiKeyFieldException;
import com.gengfo.mapping.utils.CommonMappingHelper;







public class CommonUtils {

    public static String getKey(String aliasName, String mappingKey, String keyValue) {
        return aliasName + "-" + mappingKey.toUpperCase() + "-" + keyValue;
    }

    public static void moveToHandled(String aliasName, String mappingKey, String oid) {
    
        String key = CommonMappingHelper.getKey(aliasName, mappingKey, oid);
    
        DataHolder.getInstance().getHandledStatus().put(CommonMappingHelper.getKey(aliasName, mappingKey, oid), Boolean.TRUE);
    
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
    
    

}
