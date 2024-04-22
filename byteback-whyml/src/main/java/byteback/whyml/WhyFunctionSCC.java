package byteback.whyml;

import byteback.whyml.syntax.function.WhyFunction;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class WhyFunctionSCC {
	private final List<WhyFunction> functionList;
	private final Set<WhyFunction> calls;
	private final boolean recursive;

	public WhyFunctionSCC(Set<WhyFunction> functions, Map<WhyFunction, Set<WhyFunction>> callees) {
		this.functionList = functions.stream().sorted(Comparator.comparing(e -> e.signature().vimp())).toList();

		// collect here the set of functions called by this SCC. We later remove functions that belong to the SCC itself
		this.calls = functions.stream()
				.map(callees::get)
				.flatMap(Collection::stream)
				.collect(Collectors.toSet());

		// if we need to remove a function of the SCC from the set of called function, then the SCC is self-recursive
		this.recursive = this.calls.removeAll(functions);
	}

	public boolean isRecursive() {
		return recursive;
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
