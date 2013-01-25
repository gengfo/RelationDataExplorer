package com.gengfo.common;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * The helper class to do some string format operations.
 *
 * @author Will Li(ISDC-OCHL/SHA)
 * @since 1.3 Created on 2006-2-15
 */
public class Format {
    private Format() {
    }

    /**
     * Format the string according to the parameters.<p>
     * 
     * Format.sprintf takes a list of objects, formats them, then inserts the formatted strings into the pattern at the appropriate places.
     * <h4><a name="patterns">Patterns and Their Interpretation</a></h4>
	 *
	 * <code>Format</code> uses patterns of the following form:
	 * <blockquote><pre>
	 * <i>FormatPattern:</i>
	 *         <i>String</i>
	 *         <i>FormatPattern</i> <i>FormatElement</i> <i>String</i>
	 *
	 * <i>FormatElement:</i>
	 *         { <i>ArgumentIndex</i> }
	 *         { <i>ArgumentIndex</i> : <i>FormatType</i> }
	 *         { <i>ArgumentIndex</i> : <i>FormatType</i> , <i>FormatStyle</i> }
	 *
	 * </pre></blockquote>
	 * <strong>Note:</strong> This version will <strong>ignore</strong> FormatType and FormatStyle and 
	 * only renturn <code>Format.objectToString(obj)</code>(see 
	 * {@link Format#objectToString(Object)}
	 *  for more information). 
	 * 
	 * <p>
	 * Within a <i>String</i>, <code>"{{"</code> represents a single "{".
	 * 
	 * <h4>Usage Information</h4>
	 *
	 * <p>
	 * Example:
	 * <blockquote>
	 * <pre>
	 *        List params = new ArrayList();
	 *        Integer i = new Integer(5);
	 *        int[] intArray = new int[5]{1,2,3,4,5};
	 *        Boolean bValue = Boolean.TRUE;
	 *        params.add(i);
	 *        params.add(intArray);
	 *        params.add(bValue);
	 *        
	 *        String pattern1 = "Integer:{0}  intArray:{1}   Boolean:{2}";
	 *        String pattern2 = "Integer:{{0}  intArray:{{1}   Boolean:{2}"
	 *        String result = Format.sprintf(pattern1, params);
	 * 
	 *        System.out.println(result);
	 *        System.out.println(Format.sprintf(pattern2, params));
	 * 
	 * 
	 *        output: Integer:5  intArray:{1,2,3,4,5}   Boolean:true
	 *                Integer:{0}  intArray:{1}   Boolean:true
	 * </pre>
	 * 
	 *  
     * @throws FormatException
     */
    public static String sprintf(String pattern, List paras)
        throws FormatException {
        if (null == pattern) {
            return null;
        }

        if ((null == paras) || (paras.size() == 0)) {
            return pattern;
        }

        StringBuffer result = new StringBuffer(pattern.length() * 2);

        for (int i = 0; i < pattern.length(); i++) {
            char current = pattern.charAt(i);

            if (current == '{') {
                try {
                    char next = pattern.charAt(++i);

                    if (next == '{') {
                        result.append('{');

                        continue;
                    }

                    i = inFormatString(pattern, i, paras, result);
                } catch (IndexOutOfBoundsException ex) {
                    throw new FormatException(
                        "There is an extra '{' in format string \"" + pattern +
                        '"', Format.class, ex);
                }
            } else {
                result.append(current);
            }
        }

        return result.toString();
    }

    /**
     * Format the string according to the parameters.
	 * <h4>Usage Information</h4>
	 *
	 * <p>
	 * Example:
	 * <blockquote>
	 * <pre>
	 *        Parameters params = new Parameters().add(5).add(new int[5]{1,2,3,4,5}).add(true);
	 *        
	 *        String pattern1 = "Integer:{0}  intArray:{1}   Boolean:{2}";
	 *        String result = Format.sprintf(pattern1, params);
	 * 
	 *        System.out.println(result);
	 * 
	 *        output: Integer:5  intArray:{1,2,3,4,5}   Boolean:true
	 * </pre>
     * @throws FormatException
     * @see Format#sprintf(String, List)
     */
    public static String sprintf(String pattern, Parameters paras) {
        if (null == paras) {
            return pattern;
        }

        return sprintf(pattern, paras.getParameterList());
    }

    /**
     * Print the formatted string to the console.
     * @param pattern
     * @param paras
     */
    public static void printf(String pattern, Parameters paras) {
        System.out.println(sprintf(pattern, paras));
    }

    /**
     * Print the string to the console.
     * It is the same with <code>System.out.println(string)</code>
     * @param string
     */
    public static void printf(String string) {
        System.out.println(string);
    }
    
    /**
     * <pre>
     *   - If object is Array, return "{a1,a2,a3}"
     *   - If object is Collection, return "(a1,a2,a3)"
     *   - else return obj.toString();
     * </pre>
     */
    public static String objectToString(Object obj) {
        if (null == obj) {
            return "null";
        }

        if(obj instanceof Collection){
            StringBuffer sb = new StringBuffer();
            collectionToString((Collection)obj,sb);
            return sb.toString();
        } 

        if (obj.getClass().isArray()) {
            StringBuffer sb = new StringBuffer();
            arrayToString(obj, sb);
            return sb.toString();
        }

        return obj.toString();
        
    }

    /**
     * Add the string which represents the Object:obj to the end of StringBuffer
     * <P>
     * <pre>
     *   - If object is Array, return "{a1,a2,a3}"
     *   - If object is Collection, return "(a1,a2,a3)"
     *   - else return obj.toString();
     * </pre>
     */
    public static void objectToString(Object obj, StringBuffer sb) {
    	
        if (null == obj) {
            sb.append("null");
        } else if (obj.getClass().isArray()) {
            arrayToString(obj, sb);
        } else if (obj instanceof Collection) {
            collectionToString((Collection)obj, sb);
        } else {
            sb.append(obj.toString());
        	
        	//sb.append(ToStringBuilder.toString(obj));
        	//objectToString(obj,sb);
        }
    }
    
    public static void arrayObjectToString(Object obj, StringBuffer sb) {
    	
        if (null == obj) {
            sb.append("null");
        } else if (obj.getClass().isArray()) {
            arrayToString(obj, sb);
        } else if (obj instanceof Collection) {
            collectionToString((Collection)obj, sb);
        } else {
        	sb.append(ToStringBuilder.toString(obj));
            //sb.append( );
        	//objectToString(obj,sb);
        }
    }

    static void collectionToString(Collection collection, StringBuffer sb) {
        
        if (collection.isEmpty()) {
            sb.append("()");

            return;
        }

        sb.append('(');

        Iterator ite = collection.iterator();

        while (ite.hasNext()) {
            objectToString(ite.next(), sb);
            sb.append(',');
        }

        sb.setCharAt(sb.length() - 1, ')');
    }

    static void arrayToString(Object obj, StringBuffer sb) {
        int length = Array.getLength(obj);

        if (length == 0) {
            sb.append("{}");

            return;
        }

        sb.append('{').append(ToStringBuilder.LINE_SEPARATOR);

        for (int i = 0; i < length; i++) {
            Object tmp = Array.get(obj, i);
            //just here
            sb.append(ToStringBuilder.toString(tmp));
            //objectToString(tmp, sb);
            sb.append(',').append(ToStringBuilder.LINE_SEPARATOR);
        }

        sb.setCharAt(sb.length() - 1, '}');
    }

    private static int inFormatString(String pattern, int position, List paras,
        StringBuffer result) throws FormatException, IndexOutOfBoundsException {
        char nChar = pattern.charAt(position);
        int index = nChar - '0';

        if ((0 > index) || (9 < index)) {
            throw new FormatException("The format string before ':' must be a number to " +
            		"indicate the parameter index. \"" +
                    pattern.substring(0, position + 1) + "^\"", Format.class);
        }

        nChar = pattern.charAt(++position);

        while (':' != nChar) {
            int tmp = nChar - '0';

            if ((0 > tmp) || (9 < tmp)) {
                if ('}' == nChar) {
                    objectToString(getParameter(paras, index, pattern), result);

                    return position;
                } else {
                    throw new FormatException("The format string before ':' must be a number" +
                    		" to indicate the parameter index. \"" +
                            pattern.substring(0, position + 1) + "^\"", Format.class);
                }
            }

            index = (index * 10) + tmp;
            nChar = pattern.charAt(++position);
        }

        return transform(getParameter(paras, index, pattern), pattern,
            position + 1, result);
    }

    private static Object getParameter(List paras, int index, String fmt) {
        try {
            return paras.get(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new FormatException("Parameter " + index +
                " does not exist for format string \"" + fmt + '"',
                Format.class, ex);
        }
    }

    /**
     * First format the obj according to the type which start from fmt.charAt(position) and end with '}'.
     * then add the formatted string to result;
     *
     * this version will ignore type and only renturn Format.objectToString(obj).
     * @return end position of format string.(the position of next '}')
     */
    private static int transform(Object obj, String fmt, int position,
        StringBuffer result) throws IndexOutOfBoundsException {
        //TODO format the obj to the correct string accroding to type.
        //the following shows how to get the type string.
        //this function can ignor IndexOutOfBoundsException throwed by fmt.charAt() 
        //, which had been handled by parent method.
        //
        //        StringBuffer format = new StringBuffer();
        //        char nChar = fmt.charAt(position);
        //        while ('}' != nChar)
        //        {
        //            format.append(nChar);
        //            nChar = fmt.charAt(++position);
        //        }
        //        
        //        String type = format.toString();

        while ('}' != fmt.charAt(position++));

        objectToString(obj, result);

        return position-1;
    }
}
