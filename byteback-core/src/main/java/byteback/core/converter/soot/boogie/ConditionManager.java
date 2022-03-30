package byteback.core.converter.soot.boogie;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import byteback.core.representation.soot.unit.SootMethodUnit;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.List;

public class ConditionManager {

  private static final ConditionManager instance = new ConditionManager();

  private final Map<SootMethodUnit, FunctionDeclaration> cache;

  public static ConditionManager instance() {
    return instance;
  }

  private ConditionManager() {
    this.cache = new ConcurrentHashMap<>();
  }

  public FunctionDeclaration convert(final SootMethodUnit methodUnit) {
    return cache.computeIfAbsent(methodUnit, FunctionConverter.instance()::convert);
  }

  public Expression inlineCondition(final SootMethodUnit condition, final List<Expression> arguments) {
    return convert(condition).getFunction().inline(arguments);
  }

}
