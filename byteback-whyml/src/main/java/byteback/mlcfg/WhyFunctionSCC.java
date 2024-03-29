package byteback.mlcfg;

import byteback.mlcfg.syntax.WhyFunction;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class WhyFunctionSCC {
	private final List<WhyFunction> functionList;
	private final Set<WhyFunction> calls;

	public WhyFunctionSCC(Set<WhyFunction> functions, Map<WhyFunction, Set<WhyFunction>> callees) {
		this.functionList = functions.stream().toList();
		this.calls = functions.stream()
				.map(callees::get)
				.flatMap(Collection::stream)
				.filter(f -> !functions.contains(f))
				.collect(Collectors.toSet());
	}

	public Set<WhyFunctionSCC> nearTo(Set<WhyFunctionSCC> sccSet) {
		return sccSet.stream()
				.filter(e -> e.functionList().stream().anyMatch(calls::contains))
				.collect(Collectors.toSet());
	}

	public List<WhyFunction> functionList() {
		return functionList;
	}

	public Set<WhyFunction> calls() {
		return calls;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (WhyFunctionSCC) obj;
		return Objects.equals(this.functionList, that.functionList) &&
				Objects.equals(this.calls, that.calls);
	}

	@Override
	public int hashCode() {
		return Objects.hash(functionList, calls);
	}

	@Override
	public String toString() {
		return "WhyFunctionSCC[" +
				"functionList=" + functionList + ", " +
				"calls=" + calls + ']';
	}

}
