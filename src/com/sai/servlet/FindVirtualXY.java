package com.sai.servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.sai.dao.MemberDAO;
import com.sai.servlet.Action;

public class FindVirtualXY implements Action {

	@Override
	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("FindVirtualXY 진입 : ");
		String station = request.getParameter("station");
		
		System.out.println("넘어온 station값 : " + station);

		JSONObject xyObj = new MemberDAO().searchLocation(station);
		
		System.out.println(xyObj);
		
		PrintWriter out = response.getWriter();
		out.println(xyObj);
		
		return null;
	}

}
