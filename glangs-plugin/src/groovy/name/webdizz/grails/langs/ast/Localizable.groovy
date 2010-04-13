/**
 * 
 */
package name.webdizz.grails.langs.ast

import java.lang.annotation.ElementType 
import java.lang.annotation.Retention 
import java.lang.annotation.RetentionPolicy 
import java.lang.annotation.Target 
import org.codehaus.groovy.transform.GroovyASTTransformationClass 

/**
 * @author Izzet_Mustafayev
 *
 */
@Retention (RetentionPolicy.SOURCE)
@Target ([ElementType.TYPE])
@GroovyASTTransformationClass (["name.webdizz.grails.langs.ast.LocaleTransformation"])
public @interface Localizable {

}
