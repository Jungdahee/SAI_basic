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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SearchMiddlePlace implements Action {

	@Override
	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		System.out.println("--------------");
		
		String addr = "";

		if(request.getParameter("data") == null) {
			addr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=LzJ73NnpJ9i8FwimSqcbaJpLp6x9nN4TCDnDBSPhf8TEA05I5fi6G%2FIhjRdbQcD5FZ%2FH778Vpm4vE%2F9OTB6D6Q%3D%3D&mapX=" 
		               + SearchMiddlePoint.wgsX + "&mapY=" + SearchMiddlePoint.wgsY + "&radius=2000&listYN=Y&arrange=A&MobileOS=ETC&MobileApp=AppTest&arrange=B&numOfRows=100&_type=json";
		}
		
		else {
			String category = request.getParameter("data");
			System.out.println("서블릿으로 넘어온 카테고리 :: " + category);
			switch(category) {
				case "food": 
					addr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=LzJ73NnpJ9i8FwimSqcbaJpLp6x9nN4TCDnDBSPhf8TEA05I5fi6G%2FIhjRdbQcD5FZ%2FH778Vpm4vE%2F9OTB6D6Q%3D%3D&mapX=" 
				               + SearchMiddlePoint.wgsX + "&mapY=" + SearchMiddlePoint.wgsY + "&radius=2000&listYN=Y&arrange=A&contentTypeId=39&MobileOS=ETC&MobileApp=AppTest&arrange=B&numOfRows=100&_type=json";
					break;
				case "sights": 
					addr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=LzJ73NnpJ9i8FwimSqcbaJpLp6x9nN4TCDnDBSPhf8TEA05I5fi6G%2FIhjRdbQcD5FZ%2FH778Vpm4vE%2F9OTB6D6Q%3D%3D&mapX=" 
				               + SearchMiddlePoint.wgsX + "&mapY=" + SearchMiddlePoint.wgsY + "&radius=2000&listYN=Y&arrange=A&contentTypeId=12&MobileOS=ETC&MobileApp=AppTest&arrange=B&numOfRows=100&_type=json";
					break;
				case "culture":
					addr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=LzJ73NnpJ9i8FwimSqcbaJpLp6x9nN4TCDnDBSPhf8TEA05I5fi6G%2FIhjRdbQcD5FZ%2FH778Vpm4vE%2F9OTB6D6Q%3D%3D&mapX=" 
				               + SearchMiddlePoint.wgsX + "&mapY=" + SearchMiddlePoint.wgsY + "&radius=2000&listYN=Y&arrange=A&contentTypeId=12&MobileOS=ETC&MobileApp=AppTest&arrange=B&numOfRows=100&_type=json";
					break;
				case "festival":
					addr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=LzJ73NnpJ9i8FwimSqcbaJpLp6x9nN4TCDnDBSPhf8TEA05I5fi6G%2FIhjRdbQcD5FZ%2FH778Vpm4vE%2F9OTB6D6Q%3D%3D&mapX=" 
				               + SearchMiddlePoint.wgsX + "&mapY=" + SearchMiddlePoint.wgsY + "&radius=2000&listYN=Y&arrange=A&contentTypeId=15&MobileOS=ETC&MobileApp=AppTest&arrange=B&numOfRows=100&_type=json";
					break;
			}
			
		}

		try {
			URL url = new URL(addr);
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			String protocol = "GET";
			conn.setRequestMethod(protocol);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			String json = buffer.readLine();

			

			try {
				JSONParser jsonParser = new JSONParser();
				JSONObject result = new JSONObject();

				JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
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
					//String areacode = list.get("areacode").toString();
					String contentId = list.get("contentid").toString();
					String contentTypeId = list.get("contenttypeid").toString();
					String mapx = list.get("mapx").toString();
					String mapy = list.get("mapy").toString();
					
					res.put("firstImage", firstImage);
					res.put("title", title);
					res.put("readCount", readCount);
					res.put("addr1", addr1);
					res.put("addr2", addr2);
					res.put("tel", tel);
					//res.put("areaCode", areacode);
					res.put("contentId", contentId);
					res.put("contentTypeId", contentTypeId);
					res.put("mapX", mapx);
					res.put("mapY", mapy);
					
					result.put(i, res);

				}
				
				System.out.println("SearchMiddlePlace에서 출력한 결과 : " + result);
				
				PrintWriter out = response.getWriter();
				out.println(result);
				

			} catch (Exception e) {
				e.printStackTrace();
			} 

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		return null;

	}

}
