package byteback.whyml.vimp.graph;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface Node<Index, T extends Node<Index, T>> {
	Index index();

	List<Index> nearTo();

	default Stream<T> neighbours(Map<Index, T> nodeMap) {
		return nearTo().stream().map(nodeMap::get);
	}
}
