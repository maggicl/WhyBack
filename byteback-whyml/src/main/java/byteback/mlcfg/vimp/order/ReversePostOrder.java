package byteback.mlcfg.vimp.order;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ReversePostOrder {
	private ReversePostOrder() {
	}

	public static <T> Map<T, Set<T>> reverseAdjacencyMap(final Map<T, Set<T>> reverseAdjacencyMap) {
		final Map<T, Set<T>> result = new HashMap<>();
		for (final T key : reverseAdjacencyMap.keySet()) {
			for (final T value : reverseAdjacencyMap.get(key)) {
				result.computeIfAbsent(value, k -> new HashSet<>()).add(key);
			}
		}
		return result;
	}

	/**
	 * Given an adjacency map, sorts the elements in the map in reverse post order
	 * From: <a href="https://eli.thegreenplace.net/2015/directed-graph-traversal-orderings-and-applications-to-data-flow-analysis/">...</a>
	 */
	public static <T> List<T> sort(final Map<T, Set<T>> adjacencyMap, final T start) {
		final Set<T> visited = new HashSet<>();
		final List<T> order = new ArrayList<>();
		final Deque<T> visitStack = new ArrayDeque<>();
		visitStack.push(start);

		mainLoop:
		while (!visitStack.isEmpty()) {
			final T node = visitStack.pop();
			if (adjacencyMap.containsKey(node)) {
				for (final T successor : adjacencyMap.get(node)) {
					if (!visited.contains(successor)) {
						visited.add(successor);
						visitStack.push(node);
						visitStack.push(successor);
						continue mainLoop;
					}
				}
			}
			order.add(node);
		}

		Collections.reverse(order);
		return order;
	}
}
