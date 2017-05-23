package org.destro.birt.datasource.json;

import java.sql.Timestamp;

public class SonarQubeMessageVO {

	private String type;

	private String user;

	private String text;

	private Timestamp ts;

	public SonarQubeMessageVO(String type, String user, String text, Timestamp ts) {
		super();
		this.type = type;
		this.user = user;
		this.text = text;
		this.ts = ts;
	}

	public String getType() {
		return type;
	}

	public String getUser() {
		return user;
	}

	public String getText() {
		return text;
	}

	public Timestamp getTs() {
		return ts;
	}

	@Override
	public String toString() {
		return "SlackMessageVO [type=" + type + ", user=" + user + ", text=" + text + ", ts=" + ts + "]";
	}
}
