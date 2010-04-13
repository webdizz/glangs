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
import java.util.List

import org.codehaus.groovy.ast.ASTNode 
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode 
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.control.CompilePhase 
import org.codehaus.groovy.control.SourceUnit 
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation 

/**
 * @author Izzet_Mustafayev
 *
 */
@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
class LocaleTransformation implements ASTTransformation {
	
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
	
	private void injectLocalizableProperties(ClassNode classNode) {
		List<FieldNode> fields = classNode.getFields()
		List<String> localeSuffixes = ['Ru']
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
