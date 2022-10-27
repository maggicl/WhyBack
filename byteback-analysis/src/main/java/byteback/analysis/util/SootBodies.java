package byteback.analysis.util;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import soot.Body;
import soot.Local;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.annotation.logic.LoopFinder;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.ExceptionalBlockGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

public class SootBodies {

	public static Collection<Local> getLocals(final Body body) {
		final Collection<Local> parameterLocals = getParameterLocals(body);

		return body.getLocals().stream().filter((local) -> !parameterLocals.contains(local))
				.collect(Collectors.toList());
	}

	public static Collection<Local> getParameterLocals(final Body body) {
		return Stream.concat(getThisLocal(body).stream(), body.getParameterLocals().stream())
				.collect(Collectors.toList());
	}

	public static Optional<Local> getThisLocal(final Body body) {
		try {
			return Optional.of(body.getThisLocal());
		} catch (final RuntimeException exception) {
			return Optional.empty();
		}
	}

	public static Collection<Loop> getLoops(final Body body) {
		final LoopFinder loopFinder = new LoopFinder();
		loopFinder.transform(body);

		return loopFinder.getLoops(body);
	}

	public static BlockGraph getBlockGraph(final Body body) {
		return new ExceptionalBlockGraph(body);
	}

	public static UnitGraph getUnitGraph(final Body body) {
		return new ExceptionalUnitGraph(body);
	}

}
