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

import grails.util.GrailsConfig;
import groovy.lang.ExpandoMetaClass

import java.util.ArrayList;
import java.util.List

import name.webdizz.grails.glint.LocalizationException;

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty;

/**
 * @author Izzet_Mustafayev
 *
 */
class LocalizationSupport {
	
	static boolean localizeDomain(GrailsDomainClass gdc, GrailsApplication application){
		boolean result = false;
		def config = application.getConfig()
		def defLocale = config.get("config.grails.glint.locale");
		def locales = config.get("config.grails.glint.locales");
		if (!locales || (locales && locales.size()==0)) {
			throw new LocalizationException("Application has no defined locales.");
		}
		//set default locale the first locale in the locales if not set
		if (locales && !defLocale) {
			defLocale = locales[0];
		}
		def propertiesToLocalize = [];
		//gather properties applicable for localization
		GrailsDomainClassProperty[] propoerties = gdc.persistentProperties
		for(GrailsDomainClassProperty prop in propoerties){
			if(String.class == prop.getType()){
				propertiesToLocalize.add(prop)
			}
		}
		// localize propeties
		if (!propertiesToLocalize.isEmpty()) {
			for(GrailsDomainClassProperty prop in propertiesToLocalize){
				def domainMC = gdc.getMetaClass()
			}
			result = true
		}
	}
}
