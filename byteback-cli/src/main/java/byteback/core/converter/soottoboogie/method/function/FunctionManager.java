package byteback.core.converter.soottoboogie.method.function;

import byteback.frontend.boogie.ast.FunctionDeclaration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import soot.SootMethod;

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
		return cache.computeIfAbsent(method, FunctionConverter.v()::convert);
	}

}
