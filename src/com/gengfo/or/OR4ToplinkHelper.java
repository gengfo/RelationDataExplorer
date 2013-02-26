package com.gengfo.or;

import java.util.Map;
import java.util.Set;

import oracle.toplink.publicinterface.Descriptor;

import com.gengfo.common.CommonConstants;
import com.gengfo.mapping.toplink.ToplinkMappingHelper;
import com.gengfo.mapping.utils.MappingHelper;
import com.gengfo.or.common.DataHolder;


public class OR4ToplinkHelper {

    public static void outputDataToplink(String aliasName, String key, String oid, String xlsFileName) {
    
        OR4ToplinkHelper.initDataHoderToplink();
    
        Set<String> set = MappingHelper.collectAllMappingKeys(aliasName, key, oid);
    
        MappingHelper.outputDataToExcel(set, xlsFileName);
    
    }

    public static void initDataHoderToplink() {
    
    	Map<String, String> alias2TableMap = ToplinkMappingHelper.getAliasTableNameMapToplink();
    	DataHolder.getInstance().getAlias2TableMap().putAll(alias2TableMap);
    
    	Map<String, String> table2AliasMap = ToplinkMappingHelper.getTableNameAliasMapToplink();
    	DataHolder.getInstance().getTable2AliasMap().putAll(table2AliasMap);
    
    	Map<String, Descriptor> alias2Descriptor = ToplinkMappingHelper.getAliasDescriptorMapToplink();
    	DataHolder.getInstance().getAlias2Descriptor().putAll(alias2Descriptor);
    
    	Map<String, String> table2Pk = ToplinkMappingHelper.getTablePKeyMapToplink();
    	DataHolder.getInstance().getTablePkMap().putAll(table2Pk);
    	
    	DataHolder.getInstance().setMappingType(CommonConstants.MAPPING_TYPE_TOPLINK);
    
    
    }
    
    

}
