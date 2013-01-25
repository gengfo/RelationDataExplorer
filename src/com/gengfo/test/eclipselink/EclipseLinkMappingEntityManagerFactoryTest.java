package com.gengfo.test.eclipselink;

import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import org.eclipse.persistence.descriptors.RelationalDescriptor;
import org.eclipse.persistence.internal.jpa.metamodel.EntityTypeImpl;

public class EclipseLinkMappingEntityManagerFactoryTest {

	public static void main(String args[]) {

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ARPUT");
		// Creating an EntityManager will trigger database login
		// and schema generation (because of the properties passed above)
		// EntityManager em = emf.createEntityManager();
		Metamodel mm = emf.getMetamodel();
		// emf.getm

		Set<ManagedType<?>> mts = mm.getManagedTypes();
		System.out
				.println("======================================================");
		for (ManagedType mt : mts) {
			System.out.println(mt);

			// Set dtSet = mt.getDeclaredAttributes();

			if (mt instanceof EntityTypeImpl) {

				EntityTypeImpl a = (EntityTypeImpl) mt;

				RelationalDescriptor b = a.getDescriptor();

				System.out.println("RelationalDescriptor ----------------------------> " + b);

			}

		}

		// Set<EntityType<?>> eSet = mm.getEntities();
		//
		// for (EntityType et: eSet){
		// System.out.println("et.getName()->" +et.getName());
		//
		// //Entity e = (Entity)et;
		//
		// System.out.println("et.getJavaType()->" +et.getJavaType());
		//
		//
		// //System.out.println(et);
		// }
		//
		//

		System.out.println("Done");

	}

}
