/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.annotations.xml.ejb3;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.hibernate.cfg.annotations.reflection.JPAOverriddenAnnotationReader;
import org.hibernate.cfg.annotations.reflection.XMLContext;

import org.hibernate.testing.boot.BootstrapContextImpl;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test superclass to provide utility methods for testing the mapping of JPA
 * XML to JPA annotations.  The configuration is built within each test, and no
 * database is used.  Thus, no schema generation or cleanup will be performed.
 * <p>
 * Equivalent to {@link org.hibernate.test.annotations.xml.ejb3.Ejb3XmlTest}
 * for the legacy {@link JPAOverriddenAnnotationReader}.
 *
 * @author Emmanuel Bernard
 * @deprecated This test will be removed in Hibernate ORM 6, along with the legacy {@link JPAOverriddenAnnotationReader}.
 */
@Deprecated
abstract class LegacyEjb3XmlTestCase extends BaseUnitTestCase {
	protected JPAOverriddenAnnotationReader reader;

	protected void assertAnnotationPresent(Class<? extends Annotation> annotationType) {
		assertTrue(
				"Expected annotation " + annotationType.getSimpleName() + " was not present",
				reader.isAnnotationPresent( annotationType )
		);
	}

	protected void assertAnnotationNotPresent(Class<? extends Annotation> annotationType) {
		assertFalse(
				"Unexpected annotation " + annotationType.getSimpleName() + " was present",
				reader.isAnnotationPresent( annotationType )
		);
	}

	protected JPAOverriddenAnnotationReader getReader(Class<?> entityClass, String fieldName, String ormResourceName)
			throws Exception {
		AnnotatedElement el = getAnnotatedElement( entityClass, fieldName );
		XMLContext xmlContext = getContext( ormResourceName );
		return new JPAOverriddenAnnotationReader( el, xmlContext, BootstrapContextImpl.INSTANCE );
	}

	protected AnnotatedElement getAnnotatedElement(Class<?> entityClass, String fieldName) throws Exception {
		return entityClass.getDeclaredField( fieldName );
	}

	protected XMLContext getContext(String resourceName) throws Exception {
		InputStream is = getClass().getResourceAsStream( resourceName );
		assertNotNull( "Could not load resource " + resourceName, is );
		return getContext( is );
	}

	protected XMLContext getContext(InputStream is) throws Exception {
		XMLContext xmlContext = new XMLContext( BootstrapContextImpl.INSTANCE );
		SAXReader reader = new SAXReader();
		reader.setFeature( "http://apache.org/xml/features/nonvalidating/load-external-dtd", false );
		reader.setFeature( "http://xml.org/sax/features/external-general-entities", false );
		reader.setFeature( "http://xml.org/sax/features/external-parameter-entities", false );
		Document doc = reader.read( is );
		xmlContext.addDocument( doc );
		return xmlContext;
	}
}
