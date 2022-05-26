package byteback.dummy.complete;

import static byteback.annotations.Contract.*;

public class Dice {

	public interface Die {

		public int roll(int max);

	}

	public static class FaultyDie implements Die {
		
		public int roll(int max) {
			return 1;
		}

	}

	public static void throwDie() {
		Die die = new FaultyDie();
		int max = 6;
		int result = die.roll(max);

		assertion(result < max);
	}
}
