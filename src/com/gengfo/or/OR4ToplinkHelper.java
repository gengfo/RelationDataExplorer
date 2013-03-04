package com.gengfo.or;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.toplink.descriptors.RelationalDescriptor;
import oracle.toplink.internal.helper.DatabaseField;
import oracle.toplink.publicinterface.Descriptor;
import oracle.toplink.sessions.Project;

import com.gengfo.common.CommonConstants;
import com.gengfo.mapping.toplink.ToplinkMappingHelper;
import com.gengfo.mapping.utils.MappingHelper;
import com.gengfo.or.common.DataHolder;
import com.oocl.ivo.domain.mapping.IVOProject;


public class OR4ToplinkHelper {

    public static void outputDataToplink(String aliasName, String key, String oid, String xlsFileName) {
    
        OR4ToplinkHelper.initDataHoderToplink();
    
        Set<String> set = MappingHelper.collectAllMappingKeys(aliasName, key, oid);
    
        MappingHelper.outputDataToExcel(set, xlsFileName);
    
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
    		
    		//tableKeyMap
    		 
    
    //		Project theProject = getProject();
    //
    //		Map aliasDescMap = theProject.getAliasDescriptors();
    //		Set keySet = aliasDescMap.keySet();
    //		Iterator keySetIt = keySet.iterator();
    //
    //		while (keySetIt.hasNext()) {
    //			String tableName = (String) keySetIt.next();
    //			RelationalDescriptor rd = (RelationalDescriptor) aliasDescMap
    //					.get(tableName);
    //
    //			List pkList = rd.getPrimaryKeyFields();
    //			if (pkList.size() == 1) {
    //				DatabaseField pk = (DatabaseField) pkList.get(0);
    //
    //				tableKeyMap.put(rd.getAlias(), rd.getTableName());
    //			} else {
    //			}
    //
    //		}
    
    		return tableKeyMap;
    	}

    public static Map<String, String> getAliasTableNameMapToplink( Project theProject) {
        Map<String, String> tableKeyMap = new Hashtable();
    
        //Project theProject = getProject();
    
        Map aliasDescMap = theProject.getAliasDescriptors();
        Set keySet = aliasDescMap.keySet();
        Iterator keySetIt = keySet.iterator();
    
        while (keySetIt.hasNext()) {
            String tableName = (String) keySetIt.next();
            RelationalDescriptor rd = (RelationalDescriptor) aliasDescMap
                    .get(tableName);
    
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
    
    

}
