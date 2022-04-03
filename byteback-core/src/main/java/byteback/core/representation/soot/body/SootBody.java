package byteback.core.representation.soot.body;

import byteback.core.representation.Visitable;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.annotation.logic.LoopFinder;

public class SootBody implements Visitable<SootStatementVisitor<?>> {

	private final Body sootBody;

	public SootBody(final Body sootBody) {
		this.sootBody = sootBody;
	}

	public Stream<SootStatement> statements() {
		return sootBody.getUnits().stream().map(SootStatement::new);
	}

	public int getStatementCount() {
		return sootBody.getUnits().size();
	}

	public Collection<Local> getLocals() {
		final Collection<Local> parameterLocals = getParameterLocals();

		return sootBody.getLocals().stream().filter((local) -> !parameterLocals.contains(local))
				.collect(Collectors.toList());
	}

	public Collection<Local> getParameterLocals() {
		return Stream.concat(getThisLocal().stream(), sootBody.getParameterLocals().stream())
				.collect(Collectors.toList());
	}

	public Optional<Local> getThisLocal() {
		try {
			return Optional.of(sootBody.getThisLocal());
		} catch (final RuntimeException exception) {
			return Optional.empty();
		}
	}

  public Collection<Loop> getLoops() {
    return new LoopFinder().getLoops(sootBody);
  }

	public void apply(final SootStatementVisitor<?> visitor) {
		for (Unit unit : sootBody.getUnits()) {
			unit.apply(visitor);
		}
	}

	@Override
	public String toString() {
		return sootBody.toString();
	}

}
