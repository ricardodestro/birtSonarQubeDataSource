package org.destro.birt.datasource.json;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * DataSet responsável em buscar mensagens de um canal no slack e tranforma-los em VO para consumo como um POJO no Eclipse BIRT
 * 
 * @author destro
 */
public class SonarQubeMessagesDataSet {

	private static final String jsonUrl = "https://slack.com/api/channels.history";

	private Iterator<?> iterator;

	public List<SonarQubeMessageVO> getMessages(String token, String channel, int count, long days) throws Exception {

		List<SonarQubeMessageVO> list = new ArrayList<>();

		long beginDate = (System.currentTimeMillis() - days * 24L * 60L * 60L * 1000L) / 1000L;

		URL url = new URL(
				jsonUrl + "&token=" + token + "&channel=" + channel + "&count=" + count + "&oldest=" + beginDate);

		BufferedReader b = new BufferedReader(new InputStreamReader(url.openStream()));

		String line = null;

		StringBuilder json = new StringBuilder();

		while ((line = b.readLine()) != null) {
			json.append(line);
		}

		JSONObject jres = new JSONObject(json.toString());

		JSONArray jo = jres.getJSONArray("messages");

		jo.forEach(action -> {
			String user = ((JSONObject) action).optString("user", null);
			String type = ((JSONObject) action).getString("type");
			String text = ((JSONObject) action).getString("text");

			long ts = (((JSONObject) action).getBigDecimal("ts")).longValue() * 1000L;

			Timestamp date = new Timestamp(ts);

			SonarQubeMessageVO vo = new SonarQubeMessageVO(type, user, text, date);

			list.add(vo);
		});

		return list;
	}

	// The following method will be called by BIRT engine once when
	// the report is invoked. It is also a mandatory method.
	public void open(Object appContext, Map<String, Object> map) throws Exception {
		iterator = this.getMessages(String.valueOf(map.get("token")), String.valueOf(map.get("channel")),
				Integer.valueOf(String.valueOf(map.get("count"))), Long.valueOf(String.valueOf(map.get("days"))))
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
}
