package byteback.core;

import com.beust.jcommander.JCommander;

public class Main {

	public static void main(final String[] args) {
		final Configuration configuration = new Configuration();
		JCommander.newBuilder().addObject(configuration).build().parse(args);
	}

}
