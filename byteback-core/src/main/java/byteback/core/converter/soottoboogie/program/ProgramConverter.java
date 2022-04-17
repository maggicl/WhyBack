package byteback.core.converter.soottoboogie.program;

import java.util.stream.Stream;

import byteback.core.converter.soottoboogie.Annotations;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.field.FieldConverter;
import byteback.core.converter.soottoboogie.function.FunctionConverter;
import byteback.core.converter.soottoboogie.procedure.ProcedureConverter;
import byteback.core.representation.soot.unit.SootClass;
import byteback.frontend.boogie.ast.Program;

public class ProgramConverter {

  public static Program convert(final SootClass clazz) {
    final Program program = new Program();
    clazz.fields().forEach((field) -> {
      program.addDeclaration(FieldConverter.instance().convert(field));
    });
    clazz.methods().forEach((method) -> {
      if (method.getAnnotation(Annotations.PURE_ANNOTATION).isPresent()) {
        program.addDeclaration(FunctionConverter.instance().convert(method));
      } else if (!method.getAnnotation(Annotations.CONDITION_ANNOTATION).isPresent()) {
        program.addDeclaration(ProcedureConverter.instance().convert(method));
      }
    });

    return program;
  }

  public static Program convert(final Stream<SootClass> clazzes) {
    final Program program = new Program();

    clazzes.forEach((clazz) -> {
      program.merge(convert(clazz));
    });

    return Prelude.loadProgram().merge(program);
  }

}
