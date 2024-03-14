package byteback.mlcfg.vimp;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.WhyClass;
import byteback.mlcfg.syntax.types.ReferenceVisitor;
import byteback.mlcfg.syntax.types.WhyType;
import byteback.mlcfg.vimp.order.ReversePostOrder;
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

	public Set<WhyClass> getAllSuper(WhyClass from) {
		final Deque<Identifier.FQDN> toProcess = from.superNames().collect(Collectors.toCollection(ArrayDeque::new));
		final Set<WhyClass> result = new HashSet<>();

		while (!toProcess.isEmpty()) {
			final Identifier.FQDN i = toProcess.pop();
			if (classes.containsKey(i)) {
				final WhyClass c = classes.get(i);
				result.add(c);

				if (!i.equals(Identifier.getRoot())) {
					toProcess.addAll(c.superNames().toList());
				}
			}
		}

		return result;
	}

	public void add(final WhyClass classDeclaration) {
		classes.put(classDeclaration.type().fqdn(), classDeclaration);
	}

	public boolean isResolved(WhyType t) {
		return ReferenceVisitor.get(t)
				.filter(e -> !classes.containsKey(e.fqdn()))
				.isEmpty();
	}

	public Stream<WhyClass> stream() {
		final Map<WhyClass, Set<WhyClass>> superAdjMap = classes.values().stream()
				.collect(Collectors.toMap(Function.identity(), this::getAllSuper));

		final WhyClass object = Objects.requireNonNull(classes.get(Identifier.getRoot()));
		final List<WhyClass> rpo = ReversePostOrder.sort(ReversePostOrder.reverseAdjacencyMap(superAdjMap), object);

		return rpo.stream().filter(e -> e != object);
	}
}
