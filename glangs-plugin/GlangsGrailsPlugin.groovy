/* Copyright 2010 Izzet Mustafaiev
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

import org.springframework.context.ApplicationContext
import name.webdizz.grails.langs.support.GlangsPluginSupport

class GlangsGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2.0 > *"
    // the other plugins this plugin depends on
    def dependsOn = [hibernate:"1.1 > *"]
   
	def loadAfter = ['controllers', 'domainClass']
	                 
    def loadBefore = ['hibernate']
	                 
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "grails-app/domain/*.groovy",
    	    "grails-app/controllers/*.groovy"
    ]

    // TODO Fill in these fields
    def author = "Izzet Mustafaiev"
    def authorEmail = "webdizzgmail.com"
    def title = "The Grails domain's i18n plugin."
    def description = '''\\

'''

    // URL to the plugin's documentation
    def documentation = "http://webdizz.name/tags/glangs"

    def doWithSpring = {
		def config = application.config		
		if(!config.grails.glint.evaluator) {
			config.grails.glint.evaluator = { request.user }
		}
		if (!config.grails.langs.locale) {
			config.grails.langs.locale = 'en';
		}
		if (!config.grails.langs.locales) {
			config.grails.langs.locales = ['Ru', 'En'];
		}
    }

    def doWithDynamicMethods = {ApplicationContext ctx ->
    	def dynamicMethods = GlangsPluginSupport.doWithDynamicMethods
    	dynamicMethods.delegate = delegate
		dynamicMethods.call(ctx)
    	// copied from HibernateGrailsPlugin.groovy file
    	// aids in generating appropriate documentation in plugin.xml since 
    	// domain class methods are lazily loaded we initialize them here
    	if(plugin.basePlugin) {
			try {
				def clz = application.classLoader.loadClass("org.grails.Behavior")
				clz.count()				
			} catch(e) {
				// ignore
			}
    	}
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }
}
