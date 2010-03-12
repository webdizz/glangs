/* Copyright 2010 Izzet Mustafayev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package name.webdizz.grails.glint.support

import java.util.List
import grails.test.*
import name.webdizz.grails.glint.Article
import name.webdizz.grails.glint.Localizable
import name.webdizz.grails.glint.test.ArticleController

import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty;

class LocalizationSupportIntegrationTests extends GrailsUnitTestCase {
	
	protected void setUp() {
		super.setUp()
	}
	
	protected void tearDown() {
		super.tearDown()
	}
	
	void testHasLocalizableFields() {
		def controller = new ArticleController()
		assertNotNull "Application context doesn't exist.", controller.grailsApplication
		
		def application = controller.grailsApplication
		
		application.getConfig().put("config.grails.glint.locale", 'en');
		application.getConfig().put("config.grails.glint.locales", ['ru', 'en']);
		
		def domainClass = null
		for(dc in application.domainClasses) {
			if (Localizable.class.isAssignableFrom(dc.clazz)) {
				assertNotNull "Domain class doesn't localizable", dc
				domainClass = dc
				break;
			}
		}
		boolean wasLocalised = LocalizationSupport.localizeDomain(domainClass, application)
		assertTrue("Domain class was localized", wasLocalised)
		def ctx = application.mainContext
		def article = new Article(name:'name')
		article.save()
	}
}
