package com.sai.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sai.dao.MemberDAO;

public class JoinMember implements Action {

	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		String memberPhoto = request.getParameter("memberPhoto");
		String memberId = request.getParameter("memberId");
		String memberPw = request.getParameter("memberPw");
		String memberName = request.getParameter("memberName");
		String memberPhone = request.getParameter("memberPhone");
		String memberStation = request.getParameter("memberStation");
		
		new MemberDAO().join(memberPhoto, memberId, memberPw, memberName, memberPhone, memberStation);
		
		System.out.println("가입 완료");

		return "index.html";
	}

}


