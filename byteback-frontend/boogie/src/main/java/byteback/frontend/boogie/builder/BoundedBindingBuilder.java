package byteback.frontend.boogie.builder;

import byteback.frontend.boogie.ast.*;
import java.util.Optional;

public class BoundedBindingBuilder extends BindingBuilder {

	private List<Declarator> declarators;

	private Optional<WhereClause> whereClauseParameter;

	public BoundedBindingBuilder() {
		this.declarators = new List<>();
		this.whereClauseParameter = Optional.empty();
	}

	public BoundedBindingBuilder addName(final String name) {
		declarators.add(new Declarator(name));

		return this;
	}

	public BoundedBindingBuilder typeAccess(final TypeAccess typeAccess) {
		super.typeAccess(typeAccess);

		return this;
	}

	public BoundedBindingBuilder whereClause(final WhereClause whereClause) {
		whereClauseParameter = Optional.of(whereClause);

		return this;
	}

	public BoundedBinding build() {
		final TypeAccess typeAccess = typeAccessParameter
				.orElseThrow(() -> new IllegalArgumentException("Bounded binding must include a type access"));
		final WhereClause whereClause = whereClauseParameter.orElse(null);

		if (declarators.getNumChild() == 0) {
			throw new IllegalArgumentException("Bounded binding must declare at least one name");
		}

		return new BoundedBinding(typeAccess, declarators, new Opt<>(whereClause));
	}

}
