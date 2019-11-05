package com.sai.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Servlet implementation class ShowHotPlace
 */
public class ShowHotPlace implements Action {

	@Override
	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		System.out.println("--------------");
		
		JSONObject result = new JSONObject();

		String addr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=";

		String serviceKey = "LzJ73NnpJ9i8FwimSqcbaJpLp6x9nN4TCDnDBSPhf8TEA05I5fi6G%2FIhjRdbQcD5FZ%2FH778Vpm4vE%2F9OTB6D6Q%3D%3D";
		String param = "";
		param = param + "&" + "areaCode=1";
		param = param + "&" + "MobileOS=ETC&MobileApp=SAI";
		param = param + "&" + "arrange=B";
		param = param + "&" + "numOfRows=100";
		param = param + "&" + "_type=json";
		addr = addr + serviceKey + param;
		
		URL url;
		
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
					String mapX = list.get("mapx").toString();
					String mapY = list.get("mapy").toString();
					
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
				
				PrintWriter out = response.getWriter();
				out.println(result);
				//request.setAttribute("hotPlaceList", result);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		return null;

	}

}



