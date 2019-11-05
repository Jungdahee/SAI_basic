package com.sai.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.jasper.tagplugins.jstl.core.Url;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.javafx.scene.paint.GradientUtils.Parser;

public class FilteringHotPlace implements Action {

	@Override
	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");

		System.out.println("--------------");

		String data = request.getParameter("data");
		
		JSONParser parser = new JSONParser();
		JSONObject json = new JSONObject();
		
		JSONArray filter = new JSONArray();
		JSONArray local = new JSONArray();
		
		try {
			json = (JSONObject) parser.parse(data);
			
			filter = (JSONArray) json.get("filter");
			local = (JSONArray) json.get("local");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject result = new JSONObject();

		if(filter.size() == 1 && local.size() == 0) {
			String addr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=LzJ73NnpJ9i8FwimSqcbaJpLp6x9nN4TCDnDBSPhf8TEA05I5fi6G%2FIhjRdbQcD5FZ%2FH778Vpm4vE%2F9OTB6D6Q%3D%3D";
			String param = "";
			String contentTypeId = "&contentTypeId=" + filter.get(0).toString();
			System.out.println(contentTypeId);
			param = param + "&MobileOS=ETC&MobileApp=SAI&arrange=B&numOfRows=100&_type=json";
			addr = addr + contentTypeId + param;

			result = filterPlace(addr);
		}
		
		else if(filter.size() == 0 && local.size() == 1) {
			String addr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=LzJ73NnpJ9i8FwimSqcbaJpLp6x9nN4TCDnDBSPhf8TEA05I5fi6G%2FIhjRdbQcD5FZ%2FH778Vpm4vE%2F9OTB6D6Q%3D%3D";
			String param = "";
			String areaCode = "&areaCode=" + local.get(0).toString();
			param = param + "&MobileOS=ETC&MobileApp=SAI&arrange=B&numOfRows=100&_type=json";
			addr = addr + areaCode + param;

			result = filterPlace(addr);
		}

		else if(filter.size() >= 1 && local.size() >= 1) {
			
			String addr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=LzJ73NnpJ9i8FwimSqcbaJpLp6x9nN4TCDnDBSPhf8TEA05I5fi6G%2FIhjRdbQcD5FZ%2FH778Vpm4vE%2F9OTB6D6Q%3D%3D";
			String param = "";
			String areaCode = "&areaCode=" + local.get(0).toString();
			String contentTypeId = "&contentTypeId=" + filter.get(0).toString();
			param = param + "&MobileOS=ETC&MobileApp=SAI&arrange=B&numOfRows=100&_type=json";
			addr = addr + areaCode + contentTypeId + param;
			
			System.out.println("주소가 맞는가요?" + addr);
			
			result = filterPlace(addr);
			
		}

		PrintWriter out = response.getWriter();
		out.println(result);

		return null;

	}

	JSONObject filterPlace(String addr) throws IOException {

		URL url = null;
		JSONObject result = new JSONObject();

		try {

			url = new URL(addr);

			HttpURLConnection conn;
			String protocol = "GET";
			BufferedReader buffer;
			String json;

			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod(protocol);
			buffer = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			json = buffer.readLine();

			try {
				JSONParser jsonParser = new JSONParser();

				JSONObject jsonObject = (JSONObject) jsonParser.parse(json);

				System.out.println(jsonObject.toString());
				JSONObject responseObject = (JSONObject) jsonObject.get("response");
				JSONObject bodyObject = (JSONObject) responseObject.get("body");
				JSONObject itemsObject = (JSONObject) bodyObject.get("items");
				JSONArray itemList = (JSONArray) itemsObject.get("item");

				System.out.println("itemList은 " + itemList.size() + "개입니다.");

				for(int i = 0; i < itemList.size(); i++) {
					JSONObject res = new JSONObject();

					JSONObject list = (JSONObject) itemList.get(i);
					String firstImage = (String) list.get("firstimage");
					String title = (String) list.get("title");
					String readCount = list.get("readcount").toString();
					String addr1 = (String) list.get("addr1");
					String addr2 = (String) list.get("addr2");
					String tel = (String) list.get("tel");
					String areacode = list.get("areacode").toString();
					String contentId = list.get("contentid").toString();
					String contentTypeId = list.get("contenttypeid").toString();
					Double mapX = Double.parseDouble(list.get("mapx").toString());
					Double mapY = Double.parseDouble(list.get("mapy").toString());

					res.put("firstImage", firstImage);
					res.put("title", title);
					res.put("readCount", readCount);
					res.put("addr1", addr1);
					res.put("addr2", addr2);
					res.put("tel", tel);
					res.put("areaCode", areacode);
					res.put("contentId", contentId);
					res.put("contentTypeId", contentTypeId);
					res.put("mapX", mapX);
					res.put("mapY", mapY);

					result.put(i, res);

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

		} catch(MalformedURLException e) {
			e.printStackTrace();
		}

		return result;

	}

}
