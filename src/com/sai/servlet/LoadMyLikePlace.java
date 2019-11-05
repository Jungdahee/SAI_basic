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

public class LoadMyLikePlace implements Action {
	@Override
	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");

		String sessId = (String) session.getAttribute("sessId");

		JSONObject result = new JSONObject();
		result = new MemberDAO().loadMyLikePlace(sessId);
		
		System.out.println("LoadMyLikePlace에서 앞단으로 넘어갈 데이터 : " +  result);
		
		PrintWriter out = response.getWriter();
		out.println(result);
		
		return null;
	}

}
