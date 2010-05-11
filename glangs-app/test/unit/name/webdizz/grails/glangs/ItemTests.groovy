package name.webdizz.grails.glangs

import org.junit.Test

import grails.test.*
import static grails.test.MockUtils.mockDomain
import static org.junit.Assert.assertNotNull

class ItemTests {
    
	@Test
    void testSomething() {
		mockDomain(Item, [:])
    	def item = new Item()
		item.nameRu = ''
		assertNotNull(item.nameRu)
    }
}
