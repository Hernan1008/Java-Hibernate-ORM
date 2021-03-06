/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.annotations.reflection;

import org.hibernate.cfg.annotations.reflection.JPAOverriddenAnnotationReader;

import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

import static org.hibernate.testing.transaction.TransactionUtil.doInHibernate;
import static org.junit.Assert.assertEquals;

/**
 * Tests the legacy {@link JPAOverriddenAnnotationReader},
 * which will be replaced with {@link org.hibernate.cfg.annotations.reflection.internal.JPAXMLOverriddenAnnotationReader}.
 * {@link JPAOverriddenAnnotationReader} is still the default implementation,
 * but we want to switch to {@link org.hibernate.cfg.annotations.reflection.internal.JPAXMLOverriddenAnnotationReader}
 * as soon as it will be practical.
 *
 * @see JPAXMLOverriddenAnnotationReaderTest
 * @deprecated This test will be removed in Hibernate ORM 6, along with the legacy {@link JPAOverriddenAnnotationReader}.
 */
@Deprecated
@TestForIssue( jiraKey = "HHH-11924")
public class LegacyElementCollectionConverterTest extends BaseCoreFunctionalTestCase {

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
			Company.class,
		};
	}

	@Override
	protected String[] getXmlFiles() {
		return new String[] { "org/hibernate/test/annotations/reflection/element-collection-converter-orm.xml" };
	}


	@Test
	public void testConverterIsAppliedToElementCollection() {
		doInHibernate( this::sessionFactory, session -> {
			Company company = new Company();
			company.setId( 1L );

			Organization org1 = new Organization();
			org1.setOrganizationId( "ACME" );

			company.getOrganizations().add( org1 );

			session.persist( company );
		} );

		doInHibernate( this::sessionFactory, session -> {
			String organizationId = (String) session
					.createNativeQuery( "select organizations from Company_organizations" )
					.getSingleResult();
			assertEquals( "ORG-ACME", organizationId );

			Company company = session.find( Company.class, 1L );

			assertEquals( 1, company.getOrganizations().size() );
			assertEquals( "ACME" , company.getOrganizations().get( 0 ).getOrganizationId());
		} );
	}
}
