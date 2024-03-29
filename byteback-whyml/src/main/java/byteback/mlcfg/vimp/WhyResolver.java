package byteback.mlcfg.vimp;

import byteback.mlcfg.WhyFunctionSCC;
import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.WhyClass;
import byteback.mlcfg.syntax.WhyFunction;
import byteback.mlcfg.syntax.WhyFunctionSignature;
import byteback.mlcfg.syntax.expr.transformer.CallDependenceVisitor;
import byteback.mlcfg.syntax.types.ReferenceVisitor;
import byteback.mlcfg.syntax.types.WhyType;
import byteback.mlcfg.vimp.graph.PostOrder;
import byteback.mlcfg.vimp.graph.Tarjan;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WhyResolver {
	private final Map<Identifier.FQDN, WhyClass> classes = new HashMap<>();
	private final Map<Identifier.FQDN, List<WhyFunctionSignature>> methods = new HashMap<>();
	private final Map<Identifier.FQDN, Map<WhyFunctionSignature, WhyFunction>> functions = new HashMap<>();

	public Set<WhyClass> getAllSuper(WhyClass from) {
		final Deque<Identifier.FQDN> toProcess = from.superNames().collect(Collectors.toCollection(ArrayDeque::new));
		final Set<WhyClass> result = new HashSet<>();

		while (!toProcess.isEmpty()) {
			final Identifier.FQDN i = toProcess.pop();
			if (classes.containsKey(i)) {
				final WhyClass c = classes.get(i);
				result.add(c);

				if (!i.equals(Identifier.Special.OBJECT)) {
					toProcess.addAll(c.superNames().toList());
				}
			}
		}

		return result;
	}

	public void addClass(final WhyClass classDeclaration) {
		classes.put(classDeclaration.type().fqdn(), classDeclaration);
	}

	public void addMethod(final WhyFunctionSignature m) {
		if (m.kind().isSpec()) {
			throw new IllegalArgumentException("spec function must be added as WhyFunction");
		}

		final Identifier.FQDN declaringClass = m.declaringClass();
		methods.computeIfAbsent(declaringClass, k -> new ArrayList<>()).add(m);
	}

	public void addFunction(final WhyFunction f) {
		final Identifier.FQDN declaringClass = f.getSignature().declaringClass();
		functions.computeIfAbsent(declaringClass, k -> new HashMap<>()).put(f.getSignature(), f);
	}

	public boolean isResolved(WhyType t) {
		return ReferenceVisitor.get(t)
				.filter(e -> !classes.containsKey(e.fqdn()))
				.isEmpty();
	}

	public List<WhyClass> classes() {
		// classes must be ordered in reverse post order according to the superclass - subclass relationship
		// this avoids forward references to class type constants, which are not allowed in WhyML

		final Map<WhyClass, Set<WhyClass>> superAdjMap = classes.values().stream()
				.collect(Collectors.toMap(Function.identity(), this::getAllSuper));

		return PostOrder.compute(superAdjMap);
	}

	public List<WhyFunctionSCC> functions() {
		final List<WhyFunction> functions = this.functions.values().stream().flatMap(e -> e.values().stream()).toList();

		final Map<WhyFunctionSignature, WhyFunction> bySignature = functions.stream()
				.collect(Collectors.toMap(WhyFunction::getSignature, Function.identity()));

		final Map<WhyFunction, Set<WhyFunction>> callees = functions.stream()
				.collect(Collectors.toMap(Function.identity(), e -> CallDependenceVisitor.getCallees(e.getBody())
						.stream()
						.map(bySignature::get)
						.collect(Collectors.toSet())));

		final Set<WhyFunctionSCC> sccMap = Tarjan.compute(callees).stream()
				.map(e -> new WhyFunctionSCC(e, callees))
				.collect(Collectors.toSet());

		final Map<WhyFunctionSCC, Set<WhyFunctionSCC>> sccAdjMap = sccMap.stream()
				.collect(Collectors.toMap(Function.identity(), e -> e.nearTo(sccMap)));

		return PostOrder.compute(sccAdjMap);
	}

	public Stream<Map.Entry<Identifier.FQDN, List<WhyFunctionSignature>>> methods() {
		return methods.entrySet().stream();
	}

	public WhyFunction getFunction(WhyFunctionSignature function) {
		return functions.get(function.declaringClass()).get(function);
	}
}
