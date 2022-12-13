package byteback.analysis.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import byteback.analysis.SwappableUnitBox;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.annotation.logic.LoopFinder;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.ExceptionalBlockGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;
import soot.util.HashChain;

public class SootBodies {

	public static Collection<Local> getLocals(final Body body) {
		final Collection<Local> parameterLocals = getParameterLocals(body);

		return body.getLocals().stream().filter((local) -> !parameterLocals.contains(local))
				.collect(Collectors.toList());
	}

	public static Collection<Local> getParameterLocals(final Body body) {
		final List<Local> locals = new ArrayList<>(body.getParameterLocals());
		getThisLocal(body).ifPresent((thisLocal) -> locals.add(0, thisLocal));

		return locals;
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

	public static Chain<UnitBox> getUnitBoxes(final Body body) {
		final var unitBoxes = new HashChain<UnitBox>();

		for (final Unit unit : body.getUnits()) {
			unitBoxes.add(new SwappableUnitBox(unit, body));
		}

		return unitBoxes;
	}

}