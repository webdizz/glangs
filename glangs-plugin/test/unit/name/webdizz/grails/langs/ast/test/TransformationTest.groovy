package name.webdizz.grails.langs.ast.test


import static org.junit.Assert.*

import groovy.lang.MetaClass;
import name.webdizz.grails.glint.Article;
import name.webdizz.grails.langs.ast.LocaleTransformation 

import org.codehaus.groovy.ast.builder.TranformTestHelper
import org.codehaus.groovy.control.CompilePhase 
//import org.codehaus.groovy.tools.ast.TranformTestHelper
import org.junit.Test
import groovy.util.ConfigObject

import org.codehaus.groovy.grails.commons.ApplicationHolder;
import org.codehaus.groovy.grails.commons.cfg.ConfigurationHelper

public class TransformationTest {

	@Test
	public void testLocalizableTransformation() {
		def article = new Article()
		int before = article.metaClass.properties.size()
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
        int after = mc.properties.size()
        assertTrue("Amount of properties is greater then should be.", after-before==2)
	}

}
