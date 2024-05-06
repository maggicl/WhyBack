package byteback.whyml.vimp;

import byteback.analysis.VimpCondition;
import byteback.whyml.WhyFunctionSCC;
import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.WhyClass;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.function.WhyFunction;
import byteback.whyml.syntax.function.WhyFunctionBody;
import byteback.whyml.syntax.function.WhyFunctionContract;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.type.ReferenceVisitor;
import byteback.whyml.syntax.type.WhyType;
import byteback.whyml.vimp.graph.PostOrder;
import byteback.whyml.vimp.graph.Tarjan;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import soot.SootClass;
import soot.SootMethod;

public class WhyResolver {
	private final VimpClassParser classParser;
	private final VimpMethodParser methodParser;
	private final VimpMethodBodyParser methodBodyParser;

	private final Map<Identifier.FQDN, WhyClass> classes = new HashMap<>();
	private final Set<SootMethod> parsed = new HashSet<>();
	private final Map<SootMethod, WhyFunctionContract> signatures = new HashMap<>();
	private final Map<SootMethod, WhyFunctionBody> bodies = new HashMap<>();
	private final Map<SootMethod, List<VimpCondition>> conditions = new HashMap<>();

	public WhyResolver(VimpClassParser classParser,
					   VimpMethodParser methodParser,
					   VimpMethodBodyParser methodBodyParser) {
		this.classParser = classParser;
		this.methodParser = methodParser;
		this.methodBodyParser = methodBodyParser;
	}

	private Set<WhyClass> getAllSuper(WhyClass from) {
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

	public Expression getSpecBody(final SootMethod method) {
		resolveMethod(method);
		final WhyFunctionBody body = bodies.get(method);

		if (body == null) {
			throw new IllegalArgumentException("method has no body thus cannot be called: " + method);
		}

		if (body instanceof WhyFunctionBody.SpecBody specBody) {
			return specBody.getExpression();
		} else {
			throw new IllegalArgumentException("method is not a spec method: " + method);
		}
	}

	public void resolveAllConditionData(final Map<SootMethod, List<VimpCondition>> data) {
		conditions.putAll(data);
	}

	public void resolveClass(final SootClass clazz) {
		final WhyClass c = classParser.parse(clazz);
		classes.put(c.name(), c);
	}

	public void resolveMethod(final SootMethod method) {
		if (parsed.contains(method)) return;
		parsed.add(method);

		VimpMethodParser.declaration(method).ifPresent(decl -> {
			final List<VimpCondition> methodConditions = conditions.getOrDefault(method, List.of());

			methodParser.contract(method, methodConditions, decl, this)
					.ifPresent(signature -> signatures.put(method, signature));

			methodBodyParser.parse(decl, method).ifPresent(e -> bodies.put(method, e));
		});
	}

	public boolean isClassResolved(WhyType t) {
		return ReferenceVisitor.get(t)
				.filter(e -> !classes.containsKey(e.fqdn()))
				.isEmpty();
	}

	public List<WhyClass> classes() {
		// classes must be ordered in reverse post order according to the superclass - subclass relationship
		// this avoids forward references to class type constants, which are not allowed in WhyML

		final Map<WhyClass, Set<WhyClass>> superAdjMap = classes.values().stream()
				.collect(Collectors.toMap(Function.identity(), this::getAllSuper));

		return PostOrder.compute(superAdjMap, Comparator.comparing(WhyClass::name));
	}

	public List<WhyFunctionSCC> functions() {
		final List<WhyFunction> functions = signatures.entrySet()
				.stream()
				.map(e -> new WhyFunction(e.getValue(), Optional.ofNullable(bodies.get(e.getKey()))))
				.toList();

		final Map<WhyFunctionSignature, WhyFunction> bySignature = functions.stream()
				.collect(Collectors.toMap(e -> e.contract().signature(), Function.identity()));

		final Map<WhyFunction, Set<WhyFunction>> callees = functions.stream()
				.collect(Collectors.toMap(Function.identity(), e -> e.body().map(WhyFunctionBody::getCallees).orElseGet(Set::of)
						.stream()
						.map(bySignature::get)
						.collect(Collectors.toSet())));

		final Set<WhyFunctionSCC> sccMap = Tarjan.compute(callees).stream()
				.map(e -> new WhyFunctionSCC(e, callees))
				.collect(Collectors.toSet());

		final Map<WhyFunctionSCC, Set<WhyFunctionSCC>> sccAdjMap = sccMap.stream()
				.collect(Collectors.toMap(Function.identity(), e -> e.nearTo(sccMap)));

		// order by post-order, sorting siblings based on the first function on the SCC. Functions within the SCC
		// are already sorted by the WhyFunctionSCC constructor
		return PostOrder.compute(sccAdjMap, Comparator.comparing(e -> e.functionList().get(0).contract()));
	}
}
