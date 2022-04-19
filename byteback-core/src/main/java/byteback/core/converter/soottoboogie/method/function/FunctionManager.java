package byteback.core.converter.soottoboogie.method.function;

import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FunctionManager {

	private static final FunctionManager instance = new FunctionManager();

	private final Map<SootMethod, FunctionDeclaration> cache;

	public static FunctionManager instance() {
		return instance;
	}

	private FunctionManager() {
		this.cache = new ConcurrentHashMap<>();
	}

	public FunctionDeclaration convert(final SootMethod method) {
		return cache.computeIfAbsent(method, FunctionConverter.instance()::convert);
	}

}
