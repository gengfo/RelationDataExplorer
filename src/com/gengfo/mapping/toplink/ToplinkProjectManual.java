package com.gengfo.mapping.toplink;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.toplink.descriptors.RelationalDescriptor;
import oracle.toplink.internal.helper.DatabaseField;
import oracle.toplink.mappings.DatabaseMapping;
import oracle.toplink.mappings.OneToManyMapping;
import oracle.toplink.mappings.OneToOneMapping;
import oracle.toplink.sessions.Project;
import oracle.toplink.tools.workbench.XMLProjectReader;

import com.gengfo.exception.MultiKeyFieldException;
import com.gengfo.mapping.utils.DBConstants;
import com.gengfo.mapping.utils.FieldPair;
import com.oocl.ivo.domain.mapping.IVOProject;

public class ToplinkProjectManual {

	public static Project getProject(String projectXml) {

		return XMLProjectReader.read(projectXml);

	}

	public static Project getProject() {

		return new IVOProject();

	}

	/**
	 * the map cotains:
	 * <li>key: tableName
	 * <li>primary key field
	 * 
	 * @return
	 * @throws MultiKeyFieldException
	 */

	public static String getTableName(String key) {
		return (key.substring(0, key.indexOf(".") - 1));
	}

	public static String getTableFieldName(String key) {
		return (key.substring(key.indexOf(".") + 1, key.length()));
	}

	// --------------------
	// --------------------
	public static Map getTableKeyMapManual() {
		Map keyMap = new Hashtable();
		// Repository Charge Related
		keyMap.put(DBConstants.TB_AR_INV_CHG_POOL,
				DBConstants.TB_AR_INV_CHG_POOL_KEY);

		// Invoice Related

		keyMap.put(DBConstants.TB_AR_INV_NOTE, DBConstants.TB_AR_INV_NOTE_KEY);
		keyMap.put(DBConstants.TB_AR_INV_REF_SMMA, DBConstants.TB_AR_INV_REF_SMMA_KEY);
		
		// commons
		keyMap.put(DBConstants.TB_AR_INV_REF_SMMC,
				DBConstants.TB_AR_INV_REF_SMMC_KEY);

		keyMap.put(DBConstants.TB_AR_INV_REF_SHIPMENT,
				DBConstants.TB_AR_INV_REF_SHIPMENT_KEY);

		keyMap.put(DBConstants.TB_AR_INV_ARCHARGE,
				DBConstants.TB_AR_INV_ARCHARGE_KEY);

		keyMap.put(DBConstants.TB_AR_INV_CHG_POOL,
				DBConstants.TB_AR_INV_CHG_POOL_KEY);

		keyMap.put(DBConstants.TB_SUPP_DATA, DBConstants.TB_SUPP_DATA_KEY);

		keyMap
				.put(DBConstants.TB_SUPP_CHGCODE,
						DBConstants.TB_SUPP_CHGCODE_KEY);

		keyMap.put(DBConstants.TB_PF_INVOICEE, DBConstants.TB_PF_INVOICEE_KEY);

		//
		keyMap.put(DBConstants.TB_PF_SERVICE_OFFICE,
				DBConstants.TB_PF_SERVICE_OFFICE_KEY);

		keyMap.put(DBConstants.TB_AR_INV_REF_TPMC,
				DBConstants.TB_AR_INV_REF_TPMC_KEY);

		keyMap.put(DBConstants.TB_AR_INV_REF_TPMA, DBConstants.TB_AR_INV_REF_TPMA_KEY);
		keyMap.put(DBConstants.TB_AR_INV_REF_TRANSPORT, DBConstants.TB_AR_INV_REF_TRANSPORT_KEY);
		
		
		
		
		return keyMap;

	}

	public static List getIVOTableRelsManual() {
		List tableRelList = new ArrayList();

		/**
		 * <p>
		 * AR_INV_CHG_POOL
		 * <li>1. AR_INV_CHG_POOL --> AR_INV_REF_SMMC
		 * <li>2. AR_INV_REF_SMMC --> AR_INV_REF_SHIPMENT
		 * <li>3. AR_INV_CHG_POOL --> AR_INV_REF_TPMC
		 * <li>4. AR_INV_REF_TPMC --> AR_INV_REF_TRANSPORT
		 * <li>5. AR_INV_CHG_POOL.BIZ_TYPE -->SUPP_DATA.ID
		 * <li>6. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.AR_INV_CHG_POOL.CHARGE_CODE
		 * -->SUPP_CHGCODE.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 */

		// AR_INV_CHG_POOL------------------------------------ start
		// AR_INV_CHG_POOL
		// 1
		tableRelList.add(getAR_INV_CHG_POOLAndAR_INV_REF_SMMC());
		// 2
		tableRelList.add(getAR_INV_REF_SMMCAndAR_INV_REF_SHIPMENT());
		// 3
		tableRelList.add(getAR_INV_CHG_POOLAndAR_INV_REF_TPMC());
		// 4
		tableRelList.add(getAR_INV_REF_TPMCAndAR_INV_REF_TRANSPORT());
		// 5
		tableRelList.add(getAR_INV_CHG_POOLAndSUPP_DATA_BizType());
		
		
		// tableRelList.add(getAR_INV_CHG_POOLAndSUPP_DATA_CargoNature());
		// tableRelList.add(getAR_INV_CHG_POOLAndSUPP_CHGCODE());
		// tableRelList.add(getAR_INV_CHG_POOLAndPF_INVOICEE());
		// tableRelList.add(getAR_INV_CHG_POOLAndSUPP_DATA_PatternOption());
		// issuer
		// refCurrency
		// revOffice
		// tableRelList.add(getAR_INV_CHG_POOLAndPF_SERVICE_OFFICE_RevOffice());
		// tableRelList.add(getAR_INV_CHG_POOLAndCM_UNIT());
		// --
		// archarge
		// tableRelList.add(getAR_INV_CHG_POOLAndAR_INV_ARCHARGE());

		// mmValues
		// allSrs

		// allTrs
		// equipmentRef

		// AR_INV_CHG_POOL------------------------------------end
		/**
		 * <p>
		 * AR_INV_CHG_POOL
		 * <li>1. AR_INV_CHG_POOL --> AR_INV_REF_SMMC
		 * <li>2. AR_INV_REF_SMMC --> AR_INV_REF_SHIPMENT
		 * <li>3. AR_INV_CHG_POOL --> AR_INV_REF_TPMC
		 * <li>4. AR_INV_REF_TPMC --> AR_INV_REF_TRANSPORT
		 * <li>5. AR_INV_CHG_POOL.BIZ_TYPE -->SUPP_DATA.ID
		 * <li>6. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.AR_INV_CHG_POOL.CHARGE_CODE
		 * -->SUPP_CHGCODE.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 * <li>5. AR_INV_CHG_POOL.CARGO_NATURE -->SUPP_DATA.ID
		 */

		/**
		 * <p>
		 * AR_INV_NOTE
		 * <li>2. AR_INV_NOTE --> AR_INV_REF_SMMA
		 * <li>2. AR_INV_REF_SMMA --> AR_INV_REF_SHIPMENT
		 * <li>2. AR_INV_NOTE --> AR_INV_REF_TPMA
		 * <li>2. AR_INV_NOTE --> AR_INV_ARCHARGE
		 * 
		 * <li>1. AR_INV_NOTE --> AR_INV_NOTE CNDN
		 */
		// AR_INV_NOTE------------------------------------invoice start
		
		tableRelList.add(getAR_INV_NOTEAndAR_INV_REF_SMMA());
		tableRelList.add(getAR_INV_REF_SMMAAndAR_INV_REF_SHIPMENT());
		tableRelList.add(getAR_INV_NOTEAndAR_INV_REF_TPMA());
		tableRelList.add(getAR_INV_REF_TPMAAndAR_INV_REF_TRANSPORT());
		tableRelList.add(getAR_INV_NOTEAndAR_INV_ARCHARGE());
		//AR_INV_NOTE AR_INV_REF_SHORTCUT 
		tableRelList.add(getAR_INV_NOTEAndAR_INV_REF_SHORTCUT());
		//tableRelList.add(getAR_INV_NOTEAndAR_INV_NOTE_BalanceOfCnDn());
		//tableRelList.add(getAR_INV_NOTEAndAR_INV_NOTE_IdnsAsTo());
		//tableRelList.add(getAR_INV_NOTEAndAR_INV_NOTE_IdnsAsFrom());
		
		// tableRelList.add(getAR_INV_NOTEAndAR_INV_REF_SHIPMENT());
		// tableRelList.add(getAR_INV_NOTEAndPF_INVOICEE());
		// tableRelList.add(getAR_INV_NOTEAndPF_SERVICE_OFFICEServiceOffice());
		
		
		// tableRelList.add(getAR_INV_NOTEAndAR_INV_NOTE_IssuedNotes());

		// AR_INV_NOTE------------------------------------invoice end
		
		// TB_PF_SERVICE_OFFICE------------------------------------rule start
		tableRelList.add(getPF_SERVICE_OFFICEAndSUPP_RULE());
		tableRelList.add(getSUPP_RULEAndSUPP_DATA());
		
		
		
		// TB_PF_SERVICE_OFFICE------------------------------------rule end

		return tableRelList;
	}

	// ------------------------------
	// ------------------------------
	// RepositoryCharge: AR_INV_CHG_POOL start
	// ------------------------------
	// ------------------------------

	private static TableRel getAR_INV_CHG_POOLAndAR_INV_REF_TPMC() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_CHG_POOL);
		tr.setDestTbName(DBConstants.TB_AR_INV_REF_TPMC);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("OID");
		fp.setDestinationFd("OWNER");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_CHG_POOLAndSUPP_DATA_BizType() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_CHG_POOL);
		tr.setDestTbName(DBConstants.TB_SUPP_DATA);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("BIZ_TYPE");
		fp.setDestinationFd("ID");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_CHG_POOLAndSUPP_DATA_CargoNature() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_CHG_POOL);
		tr.setDestTbName(DBConstants.TB_SUPP_DATA);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("CARGO_NATURE");
		fp.setDestinationFd("ID");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_CHG_POOLAndSUPP_CHGCODE() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_CHG_POOL);
		tr.setDestTbName(DBConstants.TB_SUPP_CHGCODE);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("CHARGE_CODE");
		fp.setDestinationFd("ID");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_CHG_POOLAndPF_INVOICEE() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_CHG_POOL);
		tr.setDestTbName(DBConstants.TB_PF_INVOICEE);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("INVOICEE");
		fp.setDestinationFd("OID");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_CHG_POOLAndSUPP_DATA_PatternOption() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_CHG_POOL);
		tr.setDestTbName(DBConstants.TB_SUPP_DATA);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("INV_PATTERN_OPTION");
		fp.setDestinationFd("ID");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_CHG_POOLAndPF_SERVICE_OFFICE_RevOffice() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_CHG_POOL);
		tr.setDestTbName(DBConstants.TB_PF_SERVICE_OFFICE);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("REV_OFFICE");
		fp.setDestinationFd("OID");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_CHG_POOLAndCM_UNIT() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_CHG_POOL);
		tr.setDestTbName(DBConstants.TB_CM_UNIT);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("UNIT");
		fp.setDestinationFd("OID");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_CHG_POOLAndAR_INV_ARCHARGE() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_CHG_POOL);
		tr.setDestTbName(DBConstants.TB_AR_INV_ARCHARGE);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("OID");
		fp.setDestinationFd("POOL_CHARGE");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_CHG_POOLAndAR_INV_REF_SMMC() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_CHG_POOL);
		tr.setDestTbName(DBConstants.TB_AR_INV_REF_SMMC);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("OID");
		fp.setDestinationFd("OWNER");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_REF_SMMCAndAR_INV_REF_SHIPMENT() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_REF_SMMC);
		tr.setDestTbName(DBConstants.TB_AR_INV_REF_SHIPMENT);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("REF");
		fp.setDestinationFd("OID");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_REF_TPMCAndAR_INV_REF_TRANSPORT() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_REF_TPMC);
		tr.setDestTbName(DBConstants.TB_AR_INV_REF_TRANSPORT);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("REF");
		fp.setDestinationFd("OID");

		tr.setFieldPairs(fps);
		return tr;

	}
	// ----------------------------
	// PF_SERVICE_OFFICEAndSUPP_RULE start
	// ----------------------------
	private static TableRel getPF_SERVICE_OFFICEAndSUPP_RULE() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_PF_SERVICE_OFFICE);
		tr.setDestTbName(DBConstants.TB_SUPP_RULE);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("OID");
		fp.setDestinationFd("OWNER_OID");

		tr.setFieldPairs(fps);
		return tr;

	}
	
	
	private static TableRel getSUPP_RULEAndSUPP_DATA() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_SUPP_RULE);
		tr.setDestTbName(DBConstants.TB_SUPP_DATA);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("RULE_DEF");
		fp.setDestinationFd("ID");

		tr.setFieldPairs(fps);
		return tr;

	}
	// ----------------------------
	// PF_SERVICE_OFFICEAndSUPP_RULE end
	// ----------------------------

	
	
	// ----------------------------
	// AR_INV_CHG_POOL end
	// ----------------------------

	// ----------------------------
	// AR_INV_NOTE start
	// ----------------------------

	private static TableRel getAR_INV_NOTEAndPF_SERVICE_OFFICEServiceOffice() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_NOTE);
		tr.setDestTbName(DBConstants.TB_PF_SERVICE_OFFICE);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("COL_OFFICE");
		fp.setDestinationFd("OID");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_NOTEAndPF_INVOICEE() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_NOTE);
		tr.setDestTbName(DBConstants.TB_PF_INVOICEE);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("INVOICEE");
		fp.setDestinationFd("OID");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_NOTEAndAR_INV_NOTE_IdnsAsTo() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_NOTE);
		tr.setDestTbName(DBConstants.TB_AR_INV_NOTE);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("OID");
		fp.setDestinationFd("IDN_TO");

		tr.setFieldPairs(fps);
		return tr;
	}

	// private static TableRel getAR_INV_NOTEAndAR_INV_ARCHARGE() {
	// TableRel tr = new TableRel();
	// tr.setSourceTbName(DBConstants.TB_AR_INV_NOTE);
	// tr.setDestTbName(DBConstants.TB_AR_INV_ARCHARGE);
	//
	// FieldPair[] fps = new FieldPair[1];
	// FieldPair fp = new FieldPair();
	// fps[0] = fp;
	//
	// fp.setSourceFd("OID");
	// fp.setDestinationFd("NOTE");
	//
	// tr.setFieldPairs(fps);
	// return tr;
	// }

	private static TableRel getAR_INV_NOTEAndAR_INV_NOTE_BalanceOfCnDn() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_NOTE);
		tr.setDestTbName(DBConstants.TB_AR_INV_NOTE);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("OID");
		fp.setDestinationFd("INV_CNDN");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_NOTEAndAR_INV_NOTE_IdnsAsFrom() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_NOTE);
		tr.setDestTbName(DBConstants.TB_AR_INV_NOTE);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("OID");
		fp.setDestinationFd("IDN_FROM");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_NOTEAndAR_INV_NOTE_IssuedNotes() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_NOTE);
		tr.setDestTbName(DBConstants.TB_AR_INV_NOTE);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("INV_CNDN");
		fp.setDestinationFd("OID");

		tr.setFieldPairs(fps);
		return tr;
	}
	
	
	private static TableRel getAR_INV_REF_SMMAAndAR_INV_REF_SHIPMENT() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_REF_SMMA);
		tr.setDestTbName(DBConstants.TB_AR_INV_REF_SHIPMENT);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("REF");
		fp.setDestinationFd("OID");

		tr.setFieldPairs(fps);
		return tr;
	}

	
	private static TableRel getAR_INV_NOTEAndAR_INV_REF_TPMA() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_NOTE);
		tr.setDestTbName(DBConstants.TB_AR_INV_REF_TPMA);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("OID");
		fp.setDestinationFd("OWNER");

		tr.setFieldPairs(fps);
		return tr;
	}
	
	
	private static TableRel getAR_INV_REF_TPMAAndAR_INV_REF_TRANSPORT() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_REF_TPMA);
		tr.setDestTbName(DBConstants.TB_AR_INV_REF_TRANSPORT);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("REF");
		fp.setDestinationFd("OID");

		tr.setFieldPairs(fps);
		return tr;
	}
	
	private static TableRel getAR_INV_NOTEAndAR_INV_REF_SHORTCUT() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_NOTE);
		tr.setDestTbName(DBConstants.TB_AR_INV_REF_SHORTCUT);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("OID");
		fp.setDestinationFd("INVOICE");

		tr.setFieldPairs(fps);
		return tr;
	}


	private static TableRel getAR_INV_NOTEAndAR_INV_ARCHARGE() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_NOTE);
		tr.setDestTbName(DBConstants.TB_AR_INV_ARCHARGE);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("OID");
		fp.setDestinationFd("NOTE");

		tr.setFieldPairs(fps);
		return tr;
	}
	
	
	private static TableRel getAR_INV_NOTEAndAR_INV_REF_SMMA() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_NOTE);
		tr.setDestTbName(DBConstants.TB_AR_INV_REF_SMMA);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("OID");
		fp.setDestinationFd("OWNER");

		tr.setFieldPairs(fps);
		return tr;
	}
	

	private static TableRel getAR_INV_NOTEAndAR_INV_REF_SHIPMENT() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_NOTE);
		tr.setDestTbName(DBConstants.TB_AR_INV_REF_SHIPMENT);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("OID");
		fp.setDestinationFd("OWNER");

		tr.setFieldPairs(fps);
		return tr;
	}

	private static TableRel getAR_INV_CHG_POOLAndAR_INV_REF_SMMA() {
		TableRel tr = new TableRel();
		tr.setSourceTbName(DBConstants.TB_AR_INV_CHG_POOL);
		tr.setDestTbName(DBConstants.TB_AR_INV_REF_SMMA);

		FieldPair[] fps = new FieldPair[1];
		FieldPair fp = new FieldPair();
		fps[0] = fp;

		fp.setSourceFd("OID");
		fp.setDestinationFd("OWNER");

		tr.setFieldPairs(fps);
		return tr;
	}

	// ----------------------------
	// AR_INV_NOTE end
	// ----------------------------

}
