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
import name.webdizz.grails.glint.Localizable;

import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty;

class GlintGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [hibernate:"1.1 > *"]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Izzet Mustafayev"
    def authorEmail = "webdizzgmail.com"
    def title = "The Grails domain's i18n plugin."
    def description = '''\\

'''

    // URL to the plugin's documentation
    def documentation = "http://webdizz.name/tags/glint"

    def doWithSpring = {
		def config = application.config		
		if(!config.grails.glint.evaluator) {
			config.grails.glint.evaluator = { request.user }
		}
		if (!config.grails.glint.locale) {
			config.grails.glint.locale = 'en';
		}
		if (!config.grails.glint.locales) {
			config.grails.glint.locales = ['ru', 'en'];
		}
    }

    def doWithDynamicMethods = { ctx ->
		//get all domain classes
		for(GrailsDomainClass domainClass in application.domainClasses) {
			if (Localizable.class.isAssignableFrom(domainClass.clazz)) {
				
			}
		}
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }
}
