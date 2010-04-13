package name.webdizz.grails.langs.ast


import java.lang.reflect.Modifier;

import org.codehaus.groovy.ast.ASTNode 
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode 
import org.codehaus.groovy.control.CompilePhase 
import org.codehaus.groovy.control.SourceUnit 
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation 

@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
class LocaleTransformation implements ASTTransformation {
	
	static int PUBLIC = 1
	static int STATIC = 8
	
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
		String propName = "nameRu"
		if(!classNode.hasProperty(propName)){
			classNode.addProperty(propName, Modifier.PUBLIC, new ClassNode(String.class), null, null, null)
		}
	}
	
}
