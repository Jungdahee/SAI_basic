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

public class GetSession implements Action {

	@Override
	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		String sessId = (String) session.getAttribute("sessId");
		String sessPw = (String) session.getAttribute("sessPw");
		String sessName = (String) session.getAttribute("sessName");
		String sessPhone = (String) session.getAttribute("sessPhone");
		String sessStation = (String) session.getAttribute("sessStation");

		JSONObject json = new JSONObject();
		
		json.put("sessId", sessId);
		json.put("sessPw", sessPw);
		json.put("sessName", sessName);
		json.put("sessPhone", sessPhone);
		json.put("sessStation", sessStation);
		
		PrintWriter out = response.getWriter();
		out.println(json);
		
		return null;
	}

}
