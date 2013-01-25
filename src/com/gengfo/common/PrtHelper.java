package com.gengfo.common;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PrtHelper {

	public static void prtHashtableKeyValue(Map theMap) {

		Set keySet = theMap.keySet();
		Iterator keySetIt = keySet.iterator();
		while (keySetIt.hasNext()) {
			String keyStr = (String) keySetIt.next();
			Object valueObj = (Object) theMap.get(keyStr);
			System.out.println("Key: " + keyStr +" "+ "Value: " + valueObj.toString());
		}

	}

}
