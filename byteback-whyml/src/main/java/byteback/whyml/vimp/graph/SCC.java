package byteback.whyml.vimp.graph;

import java.util.List;

public record SCC<Index, T extends Node<Index, T>>(List<T> elements) {
	public boolean hasLoop() {
		return elements.size() > 1 || elements.get(0).nearTo().contains(elements.get(0).index());
	}
}
