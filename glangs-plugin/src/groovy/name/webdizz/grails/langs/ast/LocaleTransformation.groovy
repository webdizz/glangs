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

package name.webdizz.grails.langs.ast

import java.lang.reflect.Modifier
import java.net.URL
import java.util.List
import groovy.util.ConfigObject
import groovy.util.ConfigSlurper
import org.codehaus.groovy.control.CompilePhase 
import org.codehaus.groovy.transform.ASTTransformation 
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.ast.ASTNode 
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode 
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.control.CompilePhase 
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import java.io.File

/**
 * Read defined locales from configuration file and inject appropriate properties 
 * for each marked with {@link name.webdizz.grails.langs.ast.Localizable} annotation domain classes.
 * 
 * @author Izzet_Mustafayev
 *
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class LocaleTransformation implements ASTTransformation {
	
	/**
	 * The Grails app locales list.
	 */
	private static final String LOCALES = 'grails.langs.locales'
	
	/**
	 * The Grails app default locale.
	 */
	private static final String LOCALE = 'grails.langs.locale'
	
	/**
	 * Load and parse Grails config.
	 */
	private static ConfigObject config = new ConfigSlurper().parse(new File('grails-app/conf/Config.groovy').toURI().toURL())
	
	void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
		
		// use guard clauses as a form of defensive programming. 
		if (!sourceUnit) return 
			if (!astNodes) return 
			if (!astNodes[0]) return 
			if (!astNodes[1]) return 
			if (!(astNodes[0] instanceof AnnotationNode)) return
			if (astNodes[0].classNode?.name != Localizable.class.getName()) return
			if (!(astNodes[1] instanceof ClassNode)) return 
			def classNode = astNodes[1]
		if (!classNode) return
			injectLocalizableProperties(classNode)
	}
	
	private List getLocales(){
		List locales = []
		if (!config) {
			throw new LocalizationException("Unable to read Grails configuration file.")
		}
		Map properties = config.flatten()	
		if(properties.containsKey(LOCALES)){
			locales = properties.get(LOCALES) as List
		}
		locales 
	}
	
	private void injectLocalizableProperties(ClassNode classNode) {
		List<FieldNode> fields = classNode.getFields()
		List<String> localeSuffixes = getLocales()
		if (!localeSuffixes || localeSuffixes.isEmpty()) {
			throw new LocalizationException("There are no configured locales.")
		}
		List<String> properties = []
		//gather properties to inject
		if(!fields.isEmpty()){
			fields.each{ FieldNode field ->
				if (String.class.getName() == field.getType().getName()) {
					localeSuffixes.each{ String suffix ->
						String propName = field.getName()+suffix
						if(!classNode.hasProperty(propName)){
							properties.add(propName)
						}
					}
				} else {
					println 'Bad'
				}
			}
		}
		//inject properties
		if(!properties.isEmpty()){
			properties.each{ String prop ->
				classNode.addProperty(prop, Modifier.PUBLIC, new ClassNode(String.class), null, null, null)
			}
		}
	}
	
}
