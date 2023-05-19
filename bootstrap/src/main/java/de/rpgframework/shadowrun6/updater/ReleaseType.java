package de.rpgframework.shadowrun6.updater;

/**
 * @author prelle
 *
 */
public enum ReleaseType {

	STABLE("eden.rpgframework.de"),
	TESTING("eden.rpgframework.de"),
	DEVELOP("eden.rpgframework.de")
	;

	String server;

	ReleaseType(String value) {
		server = value;
	}

	public String getServer() {
		return server;
	}

}
