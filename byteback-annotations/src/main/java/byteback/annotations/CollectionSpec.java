package byteback.annotations;

import java.util.Collection;

import byteback.annotations.Contract.Attach;
import byteback.annotations.Contract.Return;

@Attach(Collection.class)
public abstract class CollectionSpec {

	@Return
	public CollectionSpec() {}

	@Return
	abstract Object[] toArray();

}
