package com.sai.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.sai.dao.MemberDAO;
import com.sai.vo.MemberVO;


public class ShowMyInfo implements Action {

	@Override
	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		session = request.getSession();
		String sessId = (String) session.getAttribute("sessId");
		
		System.out.println("ShowMyInfo의 세션값" + sessId);
		JSONObject json = new JSONObject();
		
		json = new MemberDAO().showMyInfo(sessId);
		
		JSONObject result = new JSONObject();
		
		System.out.println(json.get("memberName"));
		
		result.put("result", json);
		
		PrintWriter out = response.getWriter();
		out.println(result);
		
		return null;
	}

}
