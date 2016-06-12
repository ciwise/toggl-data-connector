package org.ciwise.toggl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;


public class TogglDataAPIHandler extends BaseObject {

	private String apiToken;
	
	private static final String AUTH_URL = "https://www.toggl.com/api/v8/me";
	private static final String GET_DETAIL_REPORT_URL = "https://toggl.com/reports/api/v2/details"; // https://toggl.com/reports/api/v2/details?workspace_id=1498014&project_ids=17654629&user_agent=david@ciwise.com
	private static final String GET_WEEKLY_REPORT_URL = "https://toggl.com/reports/api/v2/weekly";
	private static final String GET_SUMMARY_REPORT_URL = "https://toggl.com/reports/api/v2/summary";
	
	/**
	 * No public default constructor.
	 */
	private TogglDataAPIHandler() {}
	
	/**
	 * singleton instance.
	 */
	private static TogglDataAPIHandler instance;
	
	/**
	 * static get instance method
	 * @return
	 */
	public static TogglDataAPIHandler getInstance() {
		if (instance == null) {
			instance = new TogglDataAPIHandler();
		}
		return instance;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (getInstance().authenticate("david@ciwise.com:RoS0706DLW#")) {
			System.out.println(getInstance().getDetailReportDataForProjectNoTags("17654629","david@ciwise.com"));
		}
	}
	

	public boolean authenticate(final String secret) {
		boolean retVal = false;
		StringBuilder result = new StringBuilder();
	      URL url;
		try {
			url = new URL(AUTH_URL);
			HttpURLConnection conn;
				conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			String userpass = secret;
		    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

		    conn.setRequestProperty("Authorization", basicAuth);
		      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		      String line;
		      while ((line = rd.readLine()) != null) {
		         result.append(line);
		      }
		      rd.close();
		      getInstance().setApiToken(getInstance().getTokenFromJSON(result.toString()));
		      retVal = true;
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return retVal;
	}

	public String getDetailReportDataForProjectNoTags(final String projectId, final String user) { // 17654629
		String json = null;
		StringBuilder result = new StringBuilder();
	      URL url;
	      String loaded = GET_DETAIL_REPORT_URL + "?workspace_id=1498014&tag_ids=0&project_ids=" + projectId  + "&user_agent=" + user;
	      
		try {
			url = new URL(loaded);
			HttpURLConnection conn;
				conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
		    // b470ef7cfd23ef21c2e30222aad6b937:api_token
			String authtoken = TogglDataAPIHandler.getInstance().getApiToken() + ":api_token";
			String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(authtoken.getBytes());
			conn.setRequestProperty("Authorization", basicAuth);
		      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		      String line;
		      while ((line = rd.readLine()) != null) {
		         result.append(line);
		      }
		      rd.close();
		      json = result.toString();
		      System.out.println("Project Data Report -> " + json);
		      System.out.println("Project Data -> " + getDataArrayFromJSON(json));
		      
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getDataArrayFromJSON(json);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @return the apiToken
	 */
	public String getApiToken() {
		return apiToken;
	}

	/**
	 * @param apiToken the apiToken to set
	 */
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	private String getTokenFromJSON(final String json) {

		JSONObject jsonObject = new JSONObject(json);
		System.out.println(jsonObject.toString());
        JSONObject newJSON = jsonObject.getJSONObject("data");
        System.out.println(newJSON.toString());

	    String token = newJSON.getString("api_token");
	    System.out.println(token);
	    return token;
	}
	
	private String getDataArrayFromJSON(final String json) {

		JSONObject jsonObject = new JSONObject(json);
		System.out.println(jsonObject.toString());
        JSONArray newJSONArray = jsonObject.getJSONArray("data");
        
		return newJSONArray.toString();
	}
	
}