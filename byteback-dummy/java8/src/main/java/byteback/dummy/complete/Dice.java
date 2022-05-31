package byteback.dummy.complete;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;

public class Dice {

	public interface Die {

		default boolean outcome_is_positive(int max, int outcome) {
			return lte(1, outcome);
		}

		default boolean outcome_is_leq_than_max(int max, int outcome) {
			return lte(outcome, max);
		}

		@Ensure("outcome_is_positive")
		@Ensure("outcome_is_leq_than_max")
		public int roll(int max);

	}

	public static class FaultyDie implements Die {

		public boolean result_is_max(int max, int returns) {
			return eq(max, returns);
		}
		
		public int roll(int max) {
			return max;
		}

	}

	public static void throwDie() {
		Die die = new FaultyDie();
		int max = 6;
		int result = die.roll(max);

		assertion(lte(result, max));
	}
}
