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

public class CheckMemberId implements Action {

	@Override
	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String checkId = request.getParameter("checkId");
		System.out.println("CheckMemberId에서의 ID값: " + checkId);
		boolean isOk = new MemberDAO().checkId(checkId);
		
		String status = null;
		if(isOk == true) {
			status = "true";
		}
		else
			status = "false";
		
		System.out.println("isOk의 값은: " + status);
		
		JSONObject result = new JSONObject();
		result.put("status", status);
		
		System.out.println(result);
		
		PrintWriter out = response.getWriter();
		out.println(result);
		
		return null;
	}

}
