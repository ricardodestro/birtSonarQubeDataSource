package org.destro.birt.datasource.json;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

/**
 * DataSet respons�vel em buscar dados do SonarQube e tranforma-los em VO para
 * consumo como um POJO no Eclipse BIRT
 * 
 * @author destro
 */
public class SonarQubeMessagesDataSet {

	private static final String JSON_URL = "http://ci.buscapecompany.com/sonarqube/api/issues/search?";

	private static final String PROJECT_KEYS = "projectKeys";

	private static final String SEVERITIES = "severities";
	

	private Iterator<?> iterator;

	public List<SonarQubeMessageVO> getMessages(String projectList, String severities) throws Exception {

		List<SonarQubeMessageVO> list = new ArrayList<>();

		String[] projects = projectList.split(",");

		for (String p : projects) {

			list.add(getData(p, severities));

		}

		return list;
	}

	private SonarQubeMessageVO getData(String projectName, String severities) throws Exception {
		StringBuilder sUrl = new StringBuilder();
		sUrl.append(JSON_URL);
		sUrl.append(PROJECT_KEYS);
		sUrl.append("=");
		sUrl.append(projectName);
		sUrl.append("&");
		sUrl.append(SEVERITIES);
		sUrl.append("=");
		sUrl.append(severities);
		sUrl.append("&ps=1");

		URL url = new URL(sUrl.toString());

		BufferedReader b = new BufferedReader(new InputStreamReader(url.openStream()));

		String line = null;

		StringBuilder json = new StringBuilder();

		while ((line = b.readLine()) != null) {
			json.append(line);
		}

		JSONObject jres = new JSONObject(json.toString());

		SonarQubeMessageVO vo = new SonarQubeMessageVO(projectName, jres.getInt("total"));

		return vo;
	}

	// The following method will be called by BIRT engine once when
	// the report is invoked. It is also a mandatory method.
	public void open(Object appContext, Map<String, Object> map) throws Exception {
		iterator = this.getMessages(String.valueOf(map.get("projectName")), String.valueOf(map.get("severities")))
				.iterator();
	}

	// this method is a mandatory method. It must be implemented. This method
	// is used by the BIRT Reporting engine.
	public Object next() {
		if (iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}

	// The following method is also a mandatory method. This will be
	// called by the BIRT engine once at the end of the report.
	public void close() {

	}

	public static void main(String[] args) throws Exception {
		SonarQubeMessagesDataSet sq = new SonarQubeMessagesDataSet();

		List<SonarQubeMessageVO> list = sq.getMessages(
				"com.buscapecompany.api.offers:offer-commons,com.buscapecompany.api.offers:api-debugging,com.buscapecompany.api.offers:api-commons",
				"CRITICAL,BLOCKER");

		for (SonarQubeMessageVO vo : list) {
			System.out.println(vo.toString());
		}
	}
}
