package byteback.dummy.function;

import byteback.annotations.Contract.Pure;
import static byteback.annotations.Quantifier.forall;

import byteback.annotations.Quantifier;

import static byteback.annotations.Quantifier.exists;

public class Simple {

  @Pure
  public static boolean universalQuantifier() {
    int i = Quantifier.INTEGER;

    return forall(i, true);
  }

  @Pure
  public static boolean existentialQuantifier() {
    int i = Quantifier.INTEGER;

    return exists(i, true);
  }

}
