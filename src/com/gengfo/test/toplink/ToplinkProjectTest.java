package com.gengfo.test.toplink;

import java.util.List;
import java.util.Map;

import com.gengfo.common.PrtHelper;
import com.gengfo.exception.MultiKeyFieldException;
import com.gengfo.mapping.toplink.TableRel;
import com.gengfo.mapping.toplink.ToplinkMappingHelper;
import com.gengfo.mapping.toplink.ToplinkProjectManual;
import com.gengfo.mapping.utils.DBConstants;

import junit.framework.TestCase;
import oracle.toplink.sessions.Project;

public class ToplinkProjectTest extends TestCase {

	Project theProject = null;

	protected void setUp() {
		
		theProject = ToplinkMappingHelper.getProject();
		
	}

	public void ptestGetTableListFromProject() {

		Map descMap = theProject.getAliasDescriptors();

		System.out.println("Done!");

	}

	public void ptestGetTableKeyMap() throws MultiKeyFieldException {

		Map keyMap = ToplinkMappingHelper.getTablePKeyMapToplink();

		PrtHelper.prtHashtableKeyValue(keyMap);

	}

	public void testGetTableRels() throws MultiKeyFieldException {

		List rList = ToplinkMappingHelper.getIVOTableRels();
		System.out.println(rList.size());

		for (int i = 0; i < rList.size(); i++) {
			TableRel tr = (TableRel) rList.get(i);

			System.out.println(tr.toString());
		}

	}

	public void ptestGetTableName() {
		String r = "employee.EMP_ID";
		String t = ToplinkMappingHelper.getTableName(r);

		String f = ToplinkMappingHelper.getTableFieldName(r);

		System.out.println(t);
		System.out.println(f);

	}

	public void ptestGetTableRelManual() {

		List trList = ToplinkProjectManual.getIVOTableRelsManual();

		for (int i = 0; i < trList.size(); i++) {

			TableRel tr = (TableRel) trList.get(i);

			System.out.println(tr.toString());

		}

	}

}
