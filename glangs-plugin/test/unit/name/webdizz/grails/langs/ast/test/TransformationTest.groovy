package name.webdizz.grails.langs.ast.test


import static org.junit.Assert.*

import groovy.lang.MetaClass;
import name.webdizz.grails.langs.ast.LocaleTransformation 

import org.codehaus.groovy.ast.builder.TranformTestHelper
import org.codehaus.groovy.control.CompilePhase 
//import org.codehaus.groovy.tools.ast.TranformTestHelper
import org.junit.Test;

public class TransformationTest {

	@Test
	public void testLocalizableTransformation() {
        def invoker = new TranformTestHelper(new LocaleTransformation(), CompilePhase.CANONICALIZATION)

        def file = new File("./grails-app/domain/name/webdizz/grails/glint/Article.groovy")
        assert file.exists()

        def clazz = invoker.parse(file)
        def tester = clazz.newInstance()
        tester.nameRu = ""
        assertNotNull("There is no property nameRu", tester.nameRu)
        tester.nameEn = ""
        assertNotNull("There is no property nameEn", tester.nameEn)
        MetaClass mc = tester.metaClass
        assertTrue("Amount of properties is greater then should be.", mc.properties.size()==8)
        
	}

}
