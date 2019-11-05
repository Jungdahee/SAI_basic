package com.sai.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sai.servlet.Action;
import com.sai.servlet.AddLikePlace;
import com.sai.servlet.CheckMemberId;
import com.sai.servlet.DeleteLikePlace;
import com.sai.servlet.FilteringHotPlace;
import com.sai.servlet.FindVirtualXY;
import com.sai.servlet.GetSession;
import com.sai.servlet.JoinMember;
import com.sai.servlet.LoadFriendsToMap;
import com.sai.servlet.LoadLastSubPath;
import com.sai.servlet.LoadMyLikePlace;
import com.sai.servlet.LoginMember;
import com.sai.servlet.ModifyMyInfo;
import com.sai.servlet.SearchMiddlePlace;
import com.sai.servlet.SearchMiddlePoint;
import com.sai.servlet.ShowHotPlace;
import com.sai.servlet.ShowMyInfo;

@WebServlet("*.do")
public class Controller extends HttpServlet{
   private static final long serialVersionUID = 1L;
   static String viewPage = null;
//11.03 그냥 page forwarding시 처리하는 함수 필료
   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      HttpSession session = req.getSession();
     // System.out.println("req:" + req.toStrineg() + "resp:" + resp.toString());
      controll(session, req, resp);
   }

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      HttpSession session = req.getSession();
      controll(session,req, resp);
   }
   
   private void controll(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
      System.out.println("\n@@ controller");
      request.setCharacterEncoding("UTF-8");
      Action action = null;
      
      String uri = request.getRequestURI();
      String conPath = request.getContextPath();
      String com = uri.substring(conPath.length());
      System.out.println("@@ 넘어갈 sevlet com : " + com);
      
      if(com.equals("/LoginMember.do")){
         action = new LoginMember();
         System.out.println("로그인 페이지 연결 완료");
         viewPage = action.execute(session, request, response);
         
         //response.setContentType("text/html;charset=UTF-8");
         RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
         dispatcher.forward(request, response);
      }
      
      else if(com.equals("/JoinMember.do")) {
         action = new JoinMember();
         System.out.println("회원가입 페이지 연결 완료");
         viewPage = action.execute(session, request, response);
      }
      
      else if(com.equals("/ShowMyInfo.do")) {
         action = new ShowMyInfo();
         System.out.println("마이페이지 연결 완료");
         viewPage = action.execute(session, request, response);
      }
      
      else if(com.equals("/ShowHotPlace.do")) {
         action = new ShowHotPlace();
         System.out.println("핫플레이스 페이지 연결 완료");
         viewPage = action.execute(session, request, response);
      }
      
      else if(com.equals("/ModifyMyInfo.do")) {
         action = new ModifyMyInfo();
         System.out.println("회원 정보 수정 페이지 연결 완료");
         viewPage = action.execute(session, request, response);   
         
         /*RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
         dispatcher.forward(request, response);*/
      }
      
      else if(com.equals("/AddLikePlace.do")) {
         action = new AddLikePlace();
         System.out.println("좋아요 추가 서블릿 연결 완료");
         action.execute(session, request, response);
      }
      
      else if(com.equals("/LoadFriendsToMap.do")) {
         action = new LoadFriendsToMap();
         System.out.println("친구 로드 서블릿 연결 완료");
         action.execute(session, request, response);
      }
      
      else if(com.equals("/GetSession.do")) {
         action = new GetSession();
         System.out.println("세션 얻는 서블릿 연결 완료");
         action.execute(session, request, response);
      }
      
      else if(com.equals("/CheckMemberId.do")){
         action = new CheckMemberId();
         System.out.println("아이디 체크 서블릿 연결 완료");
         action.execute(session, request, response);
      }
      
      else if(com.equals("/SearchMiddlePoint.do")) {
         action = new SearchMiddlePoint();
         System.out.println("가운데 지점 찾기 페이지 연결 완료");
         viewPage = action.execute(session, request, response);
      }
      
      else if(com.equals("/LoadMyLikePlace.do")) {
         action = new LoadMyLikePlace();
         System.out.println("마이 좋아요 페이지 연결 완료");
         action.execute(session, request, response);
      }
      
      else if(com.equals("/DeleteLikePlace.do")) {
         action = new DeleteLikePlace();
         System.out.println("좋아요 취소 페이지 연결 완료");
         action.execute(session, request, response);
      
      }
      
      else if(com.equals("/SearchMiddlePlace.do")) {
         action = new SearchMiddlePlace();
         System.out.println("가운데 지점 핫플레이스 페이지 연결 완료");
         action.execute(session, request, response);
      
      }
      
      else if(com.equals("/LoadLastSubPath.do")) {
         action = new LoadLastSubPath();
         System.out.println("지하철 경로 페이지 연결 완료");
         action.execute(session, request, response);
      
      }
      
      else if(com.equals("/FilteringHotPlace.do")) {
         action = new FilteringHotPlace();
         System.out.println("핫플레이스 필터링 페이지  연결 완료");
         action.execute(session, request, response);
      
      }
      
      else if(com.equals("/FindVirtualXY.do")) {
         action = new FindVirtualXY();
         System.out.println("FindVirtualXY 연결 완료");
         action.execute(session, request, response);
      
      }
      
   }
   
}