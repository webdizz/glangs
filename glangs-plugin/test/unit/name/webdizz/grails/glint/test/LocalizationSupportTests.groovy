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
package name.webdizz.grails.glint.test

import grails.test.*
import name.webdizz.grails.glint.Article
import name.webdizz.grails.glint.LocalizationException
import name.webdizz.grails.glint.support.LocalizationSupport

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass;
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsDomainClass;

class LocalizationSupportTests extends GrailsUnitTestCase {
	
	protected void setUp() {
		super.setUp()
	}
	
	protected void tearDown() {
		super.tearDown()
	}
	
	private Map fullConfig = [
	'config.grails.glint.locale' : 'en',
	'config.grails.glint.locales' : ['ru', 'en']
	]
	private Map config = [:]
	
	def get(String key){
		config.get(key)
	}
	
	void testShouldFailOnConfigurationAbsence() {
		def appMock = mockFor(GrailsApplication)
		appMock.demand.getConfig(){ -> config}
		
		def application = appMock.createMock()
		def domainMock = mockDomain(Article)
		boolean wasLocalized = false
		try {
			LocalizationSupport.localizeDomain(domainMock, application)
		} catch (LocalizationException exc) {
			wasLocalized = true
		}
		assertTrue(wasLocalized)
	}
	
	void testShouldFailOnConfigurat() {
		def appMock = mockFor(GrailsApplication)
		config = fullConfig
		appMock.demand.getConfig(){ -> config}
		
		def tmp = new Article(nameRu: 'sergwer')
		assertEquals('sergwer', tmp.nameRu)
		
		def application = appMock.createMock()
		def article = new DefaultGrailsDomainClass(Article)
		
		boolean wasLocalized = LocalizationSupport.localizeDomain(article, application)
		assertTrue(wasLocalized)
	}
	
	
}
