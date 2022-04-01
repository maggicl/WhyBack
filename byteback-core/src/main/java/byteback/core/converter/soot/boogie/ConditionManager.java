package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConditionManager {

	private static final ConditionManager instance = new ConditionManager();

	private final Map<SootMethod, FunctionDeclaration> cache;

	public static ConditionManager instance() {
		return instance;
	}

	private ConditionManager() {
		this.cache = new ConcurrentHashMap<>();
	}

	public FunctionDeclaration convert(final SootMethod method) {
		return cache.computeIfAbsent(method, FunctionConverter.instance()::convert);
	}

}
