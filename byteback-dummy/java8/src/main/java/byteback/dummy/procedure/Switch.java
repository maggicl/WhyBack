package byteback.dummy.procedure;

public class Switch {

	public enum Fruit {
		banana, apple, orange
	}

	public static int intSwitch(final int a) {
		int b;

		switch(a) {
		case 1:
			b = 1;
		case 2:
			b = 2;
			break;
		default:
			b = 0;
		}

		return b;
	}

	public static int enumSwitch(final Fruit a) {
		int b;

		switch(a) {
		case banana:
			b = 1;
		case apple:
			b = 2;
			break;
		case orange:
			b = 3;
			break;
		default:
			b = 0;
		}

		return b;
	}
	
}
