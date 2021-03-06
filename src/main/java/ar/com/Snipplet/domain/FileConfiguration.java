package ar.com.Snipplet.domain;

import java.io.Serializable;

public class FileConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String prefix;
	private String uri;
	private String username;
	private String password;
	private String configurationPrefix;
	private String ipPingServer;

	public String getPrefix() {
		return prefix;
	}

	public String getConfigurationPrefix() {

		return configurationPrefix;

	}

	public void setConfigurationPrefix(String configurationPrefix) {

		this.configurationPrefix=configurationPrefix;

	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIpPingServer() {
		return ipPingServer;
	}

	public void setIpPingServer(String ipPingServer) {
		this.ipPingServer = ipPingServer;
	}
}
