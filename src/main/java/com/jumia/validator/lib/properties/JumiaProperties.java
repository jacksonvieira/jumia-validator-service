package com.jumia.validator.lib.properties;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author jacksonvieira
 *
 */
@ConfigurationProperties("jumia")
public class JumiaProperties {

	private String url;

	@PostConstruct
	public void init() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
