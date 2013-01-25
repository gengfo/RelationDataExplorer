package com.gengfo.common;

/**
 * The helper class to do some misc operations
 * @author Will Li(ISDC-OCHL/SHA)
 * @since 1.3 Created on 2006-2-24
 */

public class MiscUtil {
    
    private MiscUtil(){}

    /**
     * If the value is null, return the defaultvalue. Otherwise return this value.
     * @param value
     * @param defaultValue
     * @return
     */
    public static Object getValueWithDefault(Object value, Object defaultValue)
    {
        return null==value?defaultValue:value;
    }
    
    /**
     * Get the class name without package names.
     * @param obj
     * @return
     */
    public static String getClassName(Object obj)
    {
        String name = obj.getClass().getName();
        int index = name.lastIndexOf('.');
        return name.substring(index+1);
    }

}
