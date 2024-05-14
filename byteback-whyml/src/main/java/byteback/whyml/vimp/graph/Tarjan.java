package byteback.whyml.vimp.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @param <T> the vertex type
 * @see <a href="https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm">Wikipedia implementation</a>
 */
public final class Tarjan<Index, T extends Node<Index, T>> {
	private final Map<Index, T> map;
	private final ArrayDeque<T> stack = new ArrayDeque<>();
	private final Map<T, VertexState> vertexState = new HashMap<>();
	private final List<SCC<Index, T>> sccList = new ArrayList<>();
	private int globalIndex = 0;

	private Tarjan(Map<Index, T> map) {
		this.map = map;
	}

	public static <Index, T extends Node<Index, T>> List<SCC<Index, T>> compute(Map<Index, T> map) {
		return new Tarjan<Index, T>(map).apply();
	}

	public List<SCC<Index, T>> apply() {
		if (globalIndex > 0) {
			throw new IllegalStateException("tarjan instance already executed");
		}

		for (final T v : map.values()) {
			if (!vertexState.containsKey(v)) {
				strongConnect(v);
			}
		}

		return sccList;
	}

	private void strongConnect(T v) {
		final VertexState vState = new VertexState();
		vertexState.put(v, vState);

		stack.push(v);
		vState.onStack = true;

		v.neighbours(map).forEachOrdered(w -> {
			if (!vertexState.containsKey(w)) {
				// Successor w has not yet been visited; recurse on it
				strongConnect(w);
				vertexState.get(v).lowLink = Math.min(vertexState.get(v).lowLink, vertexState.get(w).lowLink);
			} else if (vertexState.get(w).onStack) {
				// Successor w is in stack S and hence in the current SCC
				// If w is not on stack, then (v, w) is an edge pointing to an SCC already found and must be ignored
				// The next line may look odd - but is correct.
				// It says w.index not w.lowLink; that is deliberate and from the original paper
				vertexState.get(v).lowLink = Math.min(vertexState.get(v).lowLink, vertexState.get(w).index);
			}
		});

		// If v is a root node, pop the stack and generate an SCC
		if (vertexState.get(v).lowLink == vertexState.get(v).index) {
			final List<T> scc = new ArrayList<>();
			T w;
			do {
				w = stack.pop();
				vertexState.get(w).onStack = false;
				scc.add(w);
			} while (w != v);
			sccList.add(new SCC<>(scc));
		}
	}

	private class VertexState {
		private final int index;
		private int lowLink;
		private boolean onStack;

		private VertexState() {
			// Set the depth index for v to the smallest unused index
			this.index = globalIndex;
			this.lowLink = globalIndex;
			globalIndex++;
		}
	}
}
