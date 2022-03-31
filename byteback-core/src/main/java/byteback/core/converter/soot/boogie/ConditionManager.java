package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.unit.SootMethodUnit;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

}
