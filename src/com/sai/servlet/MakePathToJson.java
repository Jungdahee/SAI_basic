package com.sai.servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.simple.JSONObject;

public class MakePathToJson {
	
	public static JSONObject makeJson(String str) throws Exception {
		String shtState = str;
		String sub[];
		sub = shtState.split(",");
			
		JSONObject result = new JSONObject();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("���쇱�대� 寃��� OK");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hanium_prac?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC", "root", "tndkmj89!!");				

			for(int i = 0; i < sub.length; i++) {
				JSONObject res = new JSONObject();

				String sql = "select stationName, subwayName, wgsX, wgsY from subway where stationId = ?";

				pstmt = conn.prepareStatement(sql); 

				pstmt.setInt(1, Integer.parseInt(sub[i]));

				rs = pstmt.executeQuery();
				while(rs.next()) {
					String stationName = rs.getString(1);
					String subwayName = rs.getString(2);
					float wgsX = rs.getFloat(3);
					float wgsY = rs.getFloat(4);
					
					res.put("stationName", stationName);
					res.put("subwayName", subwayName);
					res.put("wgsX", wgsX);
					res.put("wgsY", wgsY);
					
					result.put(i, res);
		
				}
//				System.out.println("MakePathToJson ------- result異��� : " + result.toJSONString());
				
			}
			
			pstmt.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
