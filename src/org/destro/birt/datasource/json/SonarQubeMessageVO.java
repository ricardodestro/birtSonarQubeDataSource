package org.destro.birt.datasource.json;

public class SonarQubeMessageVO {

	private String project;

	private int issues;
	

	public SonarQubeMessageVO(String project, int issues) {
		super();
		this.project = project;
		this.issues = issues;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public int getIssues() {
		return issues;
	}

	public void setIssues(int issues) {
		this.issues = issues;
	}

	@Override
	public String toString() {
		return "SonarQubeMessageVO [project=" + project + ", issues=" + issues + "]";
	}
}
