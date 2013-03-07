package com.gengfo.or.common;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.descriptors.RelationalDescriptor;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.jpa.JpaHelper;
import org.eclipse.persistence.sessions.Session;

import oracle.toplink.publicinterface.Descriptor;
import oracle.toplink.sessions.Project;

import com.gengfo.common.CommonConstants;
import com.gengfo.mapping.utils.CommonMappingHelper;
import com.oocl.ivo.domain.mapping.IVOProject;

public class DataHolder {

	// key already handled
	private Map<String, String> alias2TableMap = new HashMap<String, String>();
	
	private Map<String, String> class2AliasMap = new HashMap<String, String>();

	private Map<String, String> table2AliasMap = new HashMap<String, String>();
	
	

	private Map<String, String> tablePkMap = new HashMap<String, String>();
	
	private String mappingType;

	public String getMappingType() {
		return mappingType;
	}

	public void setMappingType(String mappingType) {
		this.mappingType = mappingType;
	}

	private Connection connection = null;

	public Connection getConnection() {

		if (CommonConstants.MAPPING_TYPE_TOPLINK.equals(mappingType)) {
			Connection connection = null;
			// connection = DataHolder.getInstance().getConnection();
			connection = CommonMappingHelper.getConnectionIps();
			DataHolder.getInstance().setConnection(connection);
			
			return connection;
		} else {
			EntityManagerFactory emf = Persistence
					.createEntityManagerFactory("ARPUT");

			EntityManager em = emf.createEntityManager();
			JpaEntityManager jpaEntityManager = JpaHelper.getEntityManager(em);

			Session activeSession = jpaEntityManager.getActiveSession();
			Connection connection = (Connection) activeSession
					.getDatasourceLogin().connectToDatasource(null,
							activeSession);
			DataHolder.getInstance().setConnection(connection);
			return connection;
		}

	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Map<String, String> getTablePkMap() {
		return tablePkMap;
	}

	private Map<String, Descriptor> alias2Descriptor = new HashMap<String, Descriptor>();

	private Map<String, RelationalDescriptor> alias2EclipseLinkDescriptor = new HashMap<String, RelationalDescriptor>();

	public Map<String, RelationalDescriptor> getAlias2EclipseLinkDescriptor() {
		return alias2EclipseLinkDescriptor;
	}

	public Map<String, Descriptor> getAlias2Descriptor() {
		return alias2Descriptor;
	}

	public Map<String, String> getTable2AliasMap() {
		return table2AliasMap;
	}

	public Map<String, String> getAlias2TableMap() {
		return alias2TableMap;
	}

	private Hashtable<String, Boolean> mappingHandleStatus = new Hashtable<String, Boolean>();

	public Hashtable<String, Boolean> getHandledStatus() {
		return mappingHandleStatus;
	}

	public void setHandledStatus(Hashtable<String, Boolean> settings) {
		this.mappingHandleStatus = settings;
	}

	private Project project = new IVOProject();

	public Project getProject() {
		return project;
	}

	private static DataHolder instance = new DataHolder();

	public static DataHolder getInstance() {
		return instance;
	}

}
