package byteback.whyml.vimp;

import byteback.whyml.WhyFunctionSCC;
import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.WhyClass;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.transformer.CallDependenceVisitor;
import byteback.whyml.syntax.function.VimpMethod;
import byteback.whyml.syntax.function.WhyFunction;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.type.ReferenceVisitor;
import byteback.whyml.syntax.type.WhyType;
import byteback.whyml.vimp.graph.PostOrder;
import byteback.whyml.vimp.graph.Tarjan;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WhyResolver {
	private final Map<Identifier.FQDN, WhyClass> classes = new HashMap<>();
	private final Map<Identifier.FQDN, Set<VimpMethod>> refsByClass = new HashMap<>();
	private final Map<VimpMethod, WhyFunctionSignature> specSignatures = new HashMap<>();
	private final Map<VimpMethod, Expression> specBodies = new HashMap<>();

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

	public Map.Entry<VimpMethod, Expression> resolveCondition(WhyFunctionSignature scope, String conditionValue, boolean hasResult) {
		final VimpMethod conditionRef = scope.vimp().condition(conditionValue, hasResult);

		final Expression body = Objects.requireNonNull(specBodies.get(conditionRef),
				() -> "body for condition reference " + conditionRef + " not found");

		return Map.entry(conditionRef, body);
	}

	public void addClass(final WhyClass classDeclaration) {
		classes.put(classDeclaration.type().fqdn(), classDeclaration);
	}

	public void addSpecSignature(final VimpMethod r, final WhyFunctionSignature sig) {
		refsByClass.computeIfAbsent(r.className(), (k) -> new HashSet<>()).add(r);
		specSignatures.put(r, sig);
	}

	public void addSpecBody(final VimpMethod r, final Expression body) {
		refsByClass.computeIfAbsent(r.className(), (k) -> new HashSet<>()).add(r);
		specBodies.put(r, body);
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
		final List<WhyFunction> functions = specSignatures.keySet()
				.stream()
				.filter(e -> e.decl().isSpec())
				.map(e -> new WhyFunction(specSignatures.get(e), specBodies.get(e)))
				.toList();

		final Map<WhyFunctionSignature, WhyFunction> bySignature = functions.stream()
				.collect(Collectors.toMap(WhyFunction::signature, Function.identity()));

		final Map<WhyFunction, Set<WhyFunction>> callees = functions.stream()
				.collect(Collectors.toMap(Function.identity(), e -> CallDependenceVisitor.getCallees(e.body())
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

	public Stream<Map.Entry<Identifier.FQDN, List<WhyFunctionSignature>>> methodDeclarations() {
		return refsByClass.entrySet()
				.stream()
				.map(e -> Map.entry(e.getKey(), e.getValue().stream()
						.filter(f -> !f.decl().isSpec())
						.map(specSignatures::get)
						.filter(Objects::nonNull)
						.toList()))
				.filter(e -> !e.getValue().isEmpty());
	}
}
