package com.gengfo.or.common;

import com.gengfo.mapping.utils.MappingHelper;







public class CommonUtils {

    public static String getKey(String aliasName, String mappingKey, String keyValue) {
        return aliasName + "-" + mappingKey.toUpperCase() + "-" + keyValue;
    }

    public static void moveToHandled(String aliasName, String mappingKey, String oid) {
    
        String key = MappingHelper.getKey(aliasName, mappingKey, oid);
    
        // DataHolder.getInstance().getToHandledMappings().remove(key);
        // DataHolder.getInstance().getHandledMappings()
        // .add(getKey(aliasName, mappingKey, oid));
    
        DataHolder.getInstance().getHandledStatus().put(MappingHelper.getKey(aliasName, mappingKey, oid), Boolean.TRUE);
    
    }
    
    

}
