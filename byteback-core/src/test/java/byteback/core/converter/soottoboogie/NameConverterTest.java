package byteback.core.converter.soottoboogie;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootMethod;

public class NameConverterTest {

  @Test
  public void MethodName_GivenConstructorWithoutParameters_ReturnsValidName() {
    final SootMethod method = mock(SootMethod.class, Mockito.RETURNS_DEEP_STUBS);
    when(method.getSootClass().getName()).thenReturn("dummy.Test");
    when(method.getName()).thenReturn("<init>");
    when(method.getParameterTypes()).thenReturn(Collections.emptyList());
    assertEquals(NameConverter.methodName(method), "dummy.Test.$init$##");
  }

  @Test
  public void MethodName_GivenMethodWithoutParameters_ReturnsValidName() {
    final SootMethod method = mock(SootMethod.class, Mockito.RETURNS_DEEP_STUBS);
    when(method.getSootClass().getName()).thenReturn("dummy.Test");
    when(method.getName()).thenReturn("test");
    when(method.getParameterTypes()).thenReturn(Collections.emptyList());
    assertEquals(NameConverter.methodName(method), "dummy.Test.test##");
  }

  @Test
  public void MethodName_GivenMethodWithIntArrayParameter_ReturnsValidName() {
    final SootMethod method = mock(SootMethod.class, Mockito.RETURNS_DEEP_STUBS);
    final SootType type = mock(SootType.class);
    when(type.toString()).thenReturn("int[]");
    when(method.getSootClass().getName()).thenReturn("dummy.Test");
    when(method.getName()).thenReturn("test");
    when(method.getParameterTypes()).thenReturn(List.of(type));
    assertEquals(NameConverter.methodName(method), "dummy.Test.test#int?#");
  }

  @Test
  public void MethodName_GivenMethodWithIntegerParameter_ReturnsValidName() {
    final SootMethod method = mock(SootMethod.class, Mockito.RETURNS_DEEP_STUBS);
    final SootType type = mock(SootType.class);
    when(type.toString()).thenReturn("int");
    when(method.getSootClass().getName()).thenReturn("dummy.Test");
    when(method.getName()).thenReturn("test");
    when(method.getParameterTypes()).thenReturn(List.of(type));
    assertEquals(NameConverter.methodName(method), "dummy.Test.test#int#");
  }

}
