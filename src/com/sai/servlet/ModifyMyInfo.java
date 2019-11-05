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
import com.sai.vo.MemberVO;

/**
 * Servlet implementation class ModifyIMyInfo
 */
public class ModifyMyInfo implements Action {

	@Override
	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		String memberId = (String) session.getAttribute("sessId");
		String memberPw = request.getParameter("memberPw");
		String memberName = request.getParameter("memberName");
		String memberPhone = request.getParameter("memberPhone");
		String memberStation = request.getParameter("memberStation");
		
		System.out.println("----->" + memberId + "------>" + memberPw);
		
		new MemberDAO().modifyInfo(memberId, memberPw, memberName, memberPhone, memberStation);
		
		JSONObject json = new MemberDAO().showMyInfo(memberId);
		
		memberId = (String) json.get("memberId");
		memberPw = (String) json.get("memberPw");
		memberName = (String) json.get("memberName");
		memberPhone = (String) json.get("sessPhone");
		memberStation = (String) json.get("sessStation");
		
		session.setAttribute("sessId", memberId);
		session.setAttribute("sessPw", memberPw);
		session.setAttribute("sessName", memberName);
		session.setAttribute("sessPhone", memberPhone);
		session.setAttribute("sessStation", memberStation);
		
		System.out.println("새로 설정된 세션에 저장된 이름: " + session.getAttribute("sessName"));
		
		JSONObject result = new JSONObject();
		
		result.put("sessId", memberId);
		result.put("sessPw", memberPw);
		result.put("sessName", memberName);
		result.put("sessPhone", memberPhone);
		result.put("sessStation", memberStation);
		
		PrintWriter out = response.getWriter();
		out.println(result);
		//request.setAttribute("myInfo", vo);
		
		response.setContentType("text/html;charset=UTF-8");
		
		return "myPage.html";
	}

}
