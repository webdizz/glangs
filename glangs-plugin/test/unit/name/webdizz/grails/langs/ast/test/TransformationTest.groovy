package name.webdizz.grails.langs.ast.test


import static org.junit.Assert.*

import name.webdizz.grails.langs.ast.LocaleTransformation 

import org.codehaus.groovy.ast.builder.TranformTestHelper
import org.codehaus.groovy.control.CompilePhase 
//import org.codehaus.groovy.tools.ast.TranformTestHelper
import org.junit.Test;

public class TransformationTest {

	@Test
	public void testVisit() {
        def invoker = new TranformTestHelper(new LocaleTransformation(), CompilePhase.CANONICALIZATION)

        def file = new File("./grails-app/domain/name/webdizz/grails/glint/Article.groovy")
        assert file.exists()

        def clazz = invoker.parse(file)
        def tester = clazz.newInstance()
        tester.nameRu = ""
        assertNotNull(tester.nameRu)
        tester.nameGb = ""
        assertNotNull(tester.nameGb)
	}

}
