/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.annotations.reflection;

import org.hibernate.boot.jaxb.mapping.spi.JaxbEntityMappings;
import org.hibernate.boot.jaxb.spi.XmlMappingOptions;
import org.hibernate.cfg.annotations.reflection.internal.XMLContext;
import org.hibernate.internal.util.xml.XMLMappingHelper;

import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.boot.BootstrapContextImpl;
import org.junit.Test;

/**
 * Tests the new {@link XMLContext},
 * which will be replacing {@link org.hibernate.cfg.annotations.reflection.XMLContext}.
 * {@link org.hibernate.cfg.annotations.reflection.XMLContext} is still the default implementation,
 * but we want to switch to {@link XMLContext}
 * as soon as it will be practical.
 *
 * @author Emmanuel Bernard
 */
@TestForIssue(jiraKey = "HHH-14529")
public class XMLContextTest {
	@Test
	public void testAll() throws Exception {
		XMLMappingHelper xmlHelper = new XMLMappingHelper( new XmlMappingOptions() {
			@Override
			public boolean isPreferJaxb() {
				return true;
			}
		} );
		final XMLContext context = new XMLContext( BootstrapContextImpl.INSTANCE );

		JaxbEntityMappings mappings = xmlHelper.readOrmXmlMappings( "org/hibernate/test/annotations/reflection/orm.xml" );
		context.addDocument( mappings );
	}
}
