package byteback.converter.soottoboogie;

import byteback.util.Lazy;

public class Configuration {

	private static final Lazy<Configuration> instance = Lazy.from(Configuration::new);

	private boolean message;

	public static Configuration v() {
		return instance.get();
	}

	private Configuration() {}

	public void setMessage(final boolean message) {
		this.message = message;
	}

	public boolean getMessage() {
		return message;
	}

}
