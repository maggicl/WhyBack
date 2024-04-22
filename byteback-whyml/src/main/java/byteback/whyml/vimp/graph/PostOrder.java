package byteback.whyml.vimp.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PostOrder {
	private PostOrder() {
	}

	public static <T> List<T> compute(final Map<T, Set<T>> adjacencyMap, Comparator<T> adjacentSortOrder) {
		final Set<T> visited = new HashSet<>();
		final Set<T> added = new HashSet<>();
		final List<T> order = new ArrayList<>();
		final Deque<T> visitStack = new ArrayDeque<>();

		for (final T e : adjacencyMap.keySet()) {
			visitStack.push(e);

			mainLoop:
			while (!visitStack.isEmpty()) {
				final T node = visitStack.pop();
				if (added.contains(node)) continue;

				final List<T> adjacentNodes = new ArrayList<>(adjacencyMap.getOrDefault(node, Set.of()));
				adjacentNodes.sort(adjacentSortOrder);

				for (final T successor : adjacentNodes) {
					if (!visited.contains(successor)) {
						visited.add(successor);
						visitStack.push(node);
						visitStack.push(successor);
						continue mainLoop;
					}
				}

				order.add(node);
				added.add(node);
			}
		}

		return order;
	}
}
