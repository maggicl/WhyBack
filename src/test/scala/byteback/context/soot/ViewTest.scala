package byteback.context.soot 

import munit.FunSuite
import sootup.java.core.JavaProject
import sootup.java.core.language.JavaLanguage
import sootup.java.bytecode.inputlocation.JavaClassPathAnalysisInputLocation
import scala.collection.JavaConverters._

import sootup.java.core.JavaSootClass
import java.util.Optional

class ViewTest extends FunSuite {

  test("Instantiate view") {

    val jarPath = getClass
      .getClassLoader
      .getResource("./java-8/test-java-8.jar")
      .getPath

    val inputLocation = new JavaClassPathAnalysisInputLocation(jarPath)

    val project = JavaProject.builder(new JavaLanguage(8))
      .addInputLocation(inputLocation)
      .build()

    val view = project.createOnDemandView()

    val classType = project.getIdentifierFactory().getClassType("byteback.test.algorithm.IntegerInsertionSort");

    val javaClass = view.getClass(classType).get()

    for (method <- javaClass.getMethods().asScala) {
      println(method.getBody())
    }

  }

}
