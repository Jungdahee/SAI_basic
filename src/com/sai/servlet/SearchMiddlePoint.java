package com.sai.servlet;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sai.dao.MemberDAO;

public class SearchMiddlePoint implements Action {

	
	public static List<String> friends = new ArrayList<String>();
	static double wgsX;
	static double wgsY;

	static JSONObject result = new JSONObject();
	
	@Override
	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("\n### SearchMiddlePoint START");
//---1 넘어온 json 정리
		try {
		Point2D.Double mCenterPoint = null;
		ArrayList<Point2D> mVertexs = new ArrayList<Point2D>();
		
		String nextLine = null;
		Double tmX = null;
		Double tmY = null;

		String sData = request.getParameter("data");
		System.out.println("#-- DATA by Ajax(post,json) " + sData);

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = new JSONObject();

		try {	
			jsonObject = (JSONObject) jsonParser.parse(sData);
			Iterator<JSONObject> iterator = jsonObject.values().iterator();

			System.out.println("#--info of selected Friends ");
			while (iterator.hasNext()) {
				JSONObject jsonChildObject = iterator.next();
				
				System.out.println(" jsonChildObject : " + jsonChildObject);
				String stationName = (String) jsonChildObject.get("stationName");
				Double mapX = Double.parseDouble(jsonChildObject.get("mapX").toString());
				Double mapY = Double.parseDouble(jsonChildObject.get("mapY").toString());
				
				friends.add(stationName);
				mVertexs.add(new Point2D.Double(mapX, mapY));
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//---2 무게중심구하기
		if (mCenterPoint != null) 
			System.out.println("mCenterPoint != null");

		double centerX = 0.0, centerY = 0.0;
		double area = 0.0;

		mCenterPoint = new Point2D.Double(0.0, 0.0);
		int firstIndex, secondIndex, sizeOfVertexs = mVertexs.size();

		Point2D.Double  firstPoint;
		Point2D.Double  secondPoint;

		double factor = 0.0;

		for (firstIndex = 0; firstIndex < sizeOfVertexs; firstIndex++) {
			secondIndex = (firstIndex + 1) % sizeOfVertexs;

			firstPoint  = (java.awt.geom.Point2D.Double) mVertexs.get(firstIndex);
			secondPoint = (java.awt.geom.Point2D.Double) mVertexs.get(secondIndex);

			factor = ((firstPoint.getX() * secondPoint.getY()) - (secondPoint.getX() * firstPoint.getY()));

			area += factor;

			centerX += (firstPoint.getX() + secondPoint.getX()) * factor;
			centerY += (firstPoint.getY() + secondPoint.getY()) * factor;
		}

		area /= 2.0;
		area *= 6.0f;

		factor = 1 / area;

		centerX *= factor;
		centerY *= factor;

		mCenterPoint.setLocation(centerX, centerY);
		System.out.println("#---centerXY : " + mCenterPoint.toString());
		System.out.print("#----area : " + area);
		
//---4 TM좌표 변황 --무게중심점 
		wgsX = mCenterPoint.getX();
		wgsY = mCenterPoint.getY();

		URL locURL = new URL("https://dapi.kakao.com/v2/local/geo/transcoord.json?x=" +  mCenterPoint.getX() + "&y=" + mCenterPoint.getY() + "&input_coord=WGS84&output_coord=TM");
		HttpsURLConnection urlConn;

		urlConn = (HttpsURLConnection) locURL.openConnection();
		urlConn.setRequestMethod("GET");
		urlConn.setAllowUserInteraction(false); // no user interaction
		urlConn.setDoOutput(true); // want to send
		urlConn.setRequestProperty( "Content-type", "text/xml" );
		urlConn.setRequestProperty( "accept", "text/xml" );
		urlConn.setRequestProperty( "Authorization", "KakaoAK 637a12488992c575ed97c918c0eee5e3");
		Map headerFields = urlConn.getHeaderFields();
		
		System.out.println("#---KaKao TM변환 -- header fields are: " + headerFields);

		int rspCode = urlConn.getResponseCode();
		System.out.println("#---KaKao TM responseCODE : " + rspCode);
		if (rspCode == 200) {
			InputStream ist = urlConn.getInputStream();
			InputStreamReader isr = new InputStreamReader(ist);
			BufferedReader br = new BufferedReader(isr);

			nextLine = br.readLine();

		}
		System.out.println("nextLine :" + nextLine);
//---- 5 TM좌표(JSON) 풀어서 변수에 저장 
		try {
			jsonObject = (JSONObject) jsonParser.parse(nextLine);
			JSONArray documents = (JSONArray)jsonObject.get("documents");
			
			for(int j = 0; j < documents.size(); j++) {
				JSONObject list = (JSONObject) documents.get(j);
				tmX = (Double) list.get("x");
				tmY = (Double) list.get("y");

				System.out.println("tmX : " + tmX);
				System.out.println("tmY : " + tmY);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//----6 인근역 10개 탐색		
		System.out.println("\n#----SearchMiddlePoint SearchTenSub - 인근역 10개 탐색 START");
		result	= SearchTenSub.search(tmX, tmY);
		
		System.out.println("#--- 인근 station 10 :  " +result);

		PrintWriter out = response.getWriter();
		out.println(result);
		}
		catch(Exception e) {
			PrintWriter out = response.getWriter();
			JSONObject result = new JSONObject();
			result.put("erro", e.toString());
			out.println(result);
		}

		return null;

	}
}


