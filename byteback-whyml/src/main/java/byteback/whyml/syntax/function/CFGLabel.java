package byteback.whyml.syntax.function;

import byteback.whyml.identifiers.Identifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CFGLabel(int number) {
	public static <T> Map<T, CFGLabel> forBlocks(List<T> blocks) {
		final HashMap<T, CFGLabel> result = new HashMap<>();
		for (int i = 0; i < blocks.size(); i++) {
			result.put(blocks.get(i), new CFGLabel(i + 1));
		}
		return result;
	}

	public Identifier.U name() {
		return Identifier.Special.label(number);
	}
}