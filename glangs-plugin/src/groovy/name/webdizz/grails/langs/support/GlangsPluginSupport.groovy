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
package name.webdizz.grails.langs.support

import java.util.List;

import groovy.lang.MetaClass
import groovy.lang.MetaProperty;
import groovy.lang.MissingMethodException

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.commons.metaclass.StaticMethodInvocation
import org.codehaus.groovy.grails.orm.hibernate.metaclass.CountByPersistentMethod 
import org.codehaus.groovy.grails.orm.hibernate.metaclass.FindAllByBooleanPropertyPersistentMethod
import org.codehaus.groovy.grails.orm.hibernate.metaclass.FindAllByPersistentMethod
import org.codehaus.groovy.grails.orm.hibernate.metaclass.FindByBooleanPropertyPersistentMethod 
import org.codehaus.groovy.grails.orm.hibernate.metaclass.FindByPersistentMethod 
import org.codehaus.groovy.grails.orm.hibernate.metaclass.ListOrderByPersistentMethod 
import org.springframework.context.ApplicationContext

/**
 * @author Izzet Mustafaiev
 *
 */
class GlangsPluginSupport {
	
	/**
	 * 
	 */
	static final doWithDynamicMethods = { ApplicationContext ctx ->
		for (GrailsDomainClass dc in application.domainClasses) {
			registerDynamicMethods(dc, application, ctx)
		}
	}
	
	private static registerDynamicMethods(GrailsDomainClass dc, GrailsApplication application, ApplicationContext ctx) {
		dc.metaClass.methodMissing = { String name, args ->
			throw new MissingMethodException(name, dc.clazz, args, true)
		}
		addDynamicFinderSupport(dc, application, ctx)
	}
	
	private static addDynamicFinderSupport(GrailsDomainClass dc, GrailsApplication application, ApplicationContext ctx) {
		def mc = dc.metaClass
		ClassLoader classLoader = application.classLoader
		def sessionFactory = ctx.getBean('sessionFactory')
		
		def dynamicMethods = [new FindAllByPersistentMethod(application, sessionFactory, classLoader),
                              new FindAllByBooleanPropertyPersistentMethod(application, sessionFactory, classLoader),
                              new FindByPersistentMethod(application, sessionFactory, classLoader),
                              new FindByBooleanPropertyPersistentMethod(application, sessionFactory, classLoader),
                              new CountByPersistentMethod(application, sessionFactory, classLoader),
                              new ListOrderByPersistentMethod(sessionFactory, classLoader)]
		
		// This is the code that deals with dynamic finders. It looks up a static method, if it exists it invokes it
		// otherwise it trys to match the method invocation to one of the dynamic methods. If it matches it will
		// register a new method with the ExpandoMetaClass so the next time it is invoked it doesn't have this overhead.
		mc.static.methodMissing = {String methodName, args ->
			def result = null
			StaticMethodInvocation method = dynamicMethods.find {it.isMethodMatch(methodName)}
			if (method){
				methodName = resolveLocalizedFinder(methodName, dc, application)
			}
			// check whether we able to call localized dynamic finder
			method = dynamicMethods.find {it.isMethodMatch(methodName)}
			if (method) {
				// register the method invocation for next time
				synchronized(this) {
					mc.static."$methodName" = {List varArgs ->
						method.invoke(dc.clazz, methodName, varArgs)
					}
				}
				result = method.invoke(dc.clazz, methodName, args)
			}
			else {
				throw new MissingMethodException(methodName, delegate, args)
			}
			result
		}
	}
	
	private String resolveLocalizedFinder(String methodName, GrailsDomainClass dc, GrailsApplication application){
		List<String> locales = application.config.grails.langs.locales
		String defLocale = application.config.grails.langs.locale
		String currentLocale = 'Ru' //TODO: should be resolved runtime
		String localizedMethodName = methodName
		// check localizable properties
		MetaClass mc = dc.metaClass
		List<MetaProperty> properties = mc.properties
		List<String> localizableProperties = []
		if (properties) {
			properties.each{ MetaProperty prop ->
				if (String.class.getName() == prop.getType().getName()) {
					localizableProperties.add(prop.name)
				}
			}
		}
		
		localizedMethodName
	}
}
