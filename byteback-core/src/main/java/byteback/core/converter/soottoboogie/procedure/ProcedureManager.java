package byteback.core.converter.soottoboogie.procedure;

import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.ProcedureDeclaration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProcedureManager {

	private static final ProcedureManager instance = new ProcedureManager();

	private final Map<SootMethod, ProcedureDeclaration> cache;

	public static ProcedureManager instance() {
		return instance;
	}

	private ProcedureManager() {
		this.cache = new ConcurrentHashMap<>();
	}

	public ProcedureDeclaration convert(final SootMethod method) {
		return cache.computeIfAbsent(method, ProcedureConverter.instance()::convert);
	}

}
