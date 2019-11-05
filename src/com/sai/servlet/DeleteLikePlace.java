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

public class DeleteLikePlace implements Action {

	@Override
	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		String contentId = request.getParameter("data");
		System.out.println("DeleteLikePlace에서 데이터 : " + contentId);
		
		String sessId = (String) session.getAttribute("sessId");

		new MemberDAO().deleteLikePlace(sessId, contentId);
		
		JSONObject result = new JSONObject();
		result.put("result", "성공");
		PrintWriter out = response.getWriter();
		out.println(result);
		
		return null;
	}

}
