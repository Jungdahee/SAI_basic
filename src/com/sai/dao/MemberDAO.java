package com.sai.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONObject;

//import com.mysql.jdbc.Statement;
import com.sai.servlet.SearchMiddlePoint;
import com.sai.vo.MemberVO;
//DAO분리 필요
public class MemberDAO {
   private Connection conn;

   public MemberDAO() {
      try {
         Class.forName("com.mysql.jdbc.Driver");
         System.out.println("\n ***MemberDAO ---Class.forName(\"com.mysql.jdbc.Driver\"); 에러 없음 ");
         conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hanium_prac?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC", "root", "1102");   

      }catch(Exception e) {
         e.printStackTrace();
      }   

   }

   public MemberVO login(String memberId, String memberPw) {

      System.out.println("* MemberDAO.login() 넘어온값:" + memberId + " , " + memberPw);

      //DTO(vo) 생성
      MemberVO vo = new MemberVO();
      String sql = "select memberId, memberPw, memberName, memberPhone, memberStation from members where memberId = ? and memberPw = ?";

      PreparedStatement pstmt = null;
      ResultSet rs= null;

      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, memberId);
         pstmt.setString(2, memberPw);

         rs = pstmt.executeQuery();

         System.out.println("* MemberDAO.login() --  rs = pstmt.executeQuery(); 체크" + rs.getRow());

         while(rs.next()){
            /*if(memberPw.equals(rs.getString(2))) {
               //vo.getMEMBER_PHOTO(rs.getInt(1));
             */               
            vo.setMEMBER_ID(rs.getString(1));
            vo.setMEMBER_PW(rs.getString(2));
            vo.setMEMBER_NAME(rs.getString(3));
            vo.setMEMBER_PHONE(rs.getString(4));
            vo.setMEMBER_STATION(rs.getString(5));

         }
  

      } catch (SQLException e) {
    	  
         e.printStackTrace();
      }
      return vo;
   }

   public void join(String memberPhoto, String memberId, String memberPw, String memberName, String memberPhone, String memberStation) {
      String sql = "insert into members values(?,?,?,?,?,?)";

      PreparedStatement pstmt = null;

      try {
         pstmt = conn.prepareStatement(sql);     
         pstmt.setString(1, memberPhoto);
         pstmt.setString(2, memberId);
         pstmt.setString(3, memberPw);
         pstmt.setString(4, memberName);
         pstmt.setString(5, memberPhone);
         pstmt.setString(6, memberStation);

         pstmt.executeUpdate();

      } catch (Exception e) {
         e.printStackTrace();
      } 

   }

   public JSONObject showMyInfo(String memberId) {
      String sql = "select memberPhoto, memberId, memberPw, memberName, memberStation, memberPhone from members where memberId = ?";

      JSONObject result = new JSONObject();
      PreparedStatement pstmt = null;
      ResultSet rs= null;

      try {
         pstmt = conn.prepareStatement(sql);     
         pstmt.setString(1, memberId);

         rs = pstmt.executeQuery();

         if(rs.next()) {
            result.put("memberPhoto", rs.getString(1));
            result.put("memberId", rs.getString(2));
            result.put("memberPw", rs.getString(3));
            result.put("memberName", rs.getString(4));
            result.put("memberStation", rs.getString(5));
            result.put("memberPhone", rs.getString(6));
         }

      } catch (Exception e) {
         e.printStackTrace();
      } 

      return result;
   }

   public void modifyInfo(String memberId, String memberPw, String memberName, String memberPhone, String memberStation) {
      String sql = "update members set memberPw = ?, memberName = ?, memberPhone = ?, memberStation = ? where memberId = ?";

      PreparedStatement pstmt = null;

      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, memberPw);
         pstmt.setString(2, memberName);
         pstmt.setString(3, memberPhone);
         pstmt.setString(4, memberStation);
         pstmt.setString(5, memberId);

         pstmt.executeUpdate();

      } catch (SQLException e) {
         e.printStackTrace();
      }

   }

   public void addLikePlace(String memberId, String contentId, String contentTypeId, String areaCode, String firstImage, String title, String readCount, String addr1, String addr2, String tel, Double mapX, Double mapY) {
      String sql = "insert likeplace values(?, ? ,? ,? ,? ,?, ?, ?, ?, ?, ?, ?)";

      PreparedStatement pstmt = null;

      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, memberId);
         pstmt.setString(2, contentId);
         pstmt.setString(3, contentTypeId);
         pstmt.setString(4, areaCode);
         pstmt.setString(5, firstImage);
         pstmt.setString(6, title);
         pstmt.setString(7, readCount);
         pstmt.setString(8, addr1);
         pstmt.setString(9, addr2);
         pstmt.setString(10, tel);
         pstmt.setDouble(11, mapX);
         pstmt.setDouble(12, mapY);

         pstmt.executeUpdate();

      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      System.out.println("醫뗭븘�슂 �뵒鍮꾩뿉 異붽� �셿猷�");

   }

   public void loadFriendsMap(String sessId, ArrayList<String> friendsId) {

      String sql = "select f2 from friends where f1 = ?";

      PreparedStatement pstmt = null;
      ResultSet rs = null;

      try {
         pstmt = conn.prepareStatement(sql);     
         pstmt.setString(1, sessId);
         rs = pstmt.executeQuery();
         
         friendsId.clear();
         friendsId.add(sessId);
         while(rs.next()) {
             friendsId.add(rs.getString(1));
         }
       
  /*       rs.last();
         int nFriends = rs.getRow();
         rs.beforeFirst();

         friendsId = new String[nFriends + 1];
         friendsId[0] = sessId;

         int i = 1;

         System.out.println(friendsId.length);
         while(rs.next()) {
            friendsId[i] = rs.getString(1);
            i++;
         }
*/
         rs.close();

      } catch (Exception e) {
         e.printStackTrace();
      } 

   }

   public JSONObject searchFriendInfo(String memberId) {
      String sql = "select memberId, memberName, memberStation from members where memberId = ?";

      JSONObject json = new JSONObject();
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, memberId);

         rs = pstmt.executeQuery();

         if(rs.next()) {
            json.put("memberId", rs.getString(1));
            json.put("memberName", rs.getString(2));
            json.put("memberStation", rs.getString(3));
         }

      } catch (SQLException e) {
         e.printStackTrace();
      }

      return json;

   }

   public JSONObject searchLocation(String fStation) {
      System.out.println("\n#### -----searchLocation : " + fStation);

      String sql = "select wgsX, wgsY from subway where stationName=?";

      JSONObject json = new JSONObject();
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, fStation);

         rs = pstmt.executeQuery();
         //Double.toString(rs.getDouble(1))

         while(rs.next()) {
            json.put("wgsX", rs.getDouble(1)); //�뵒鍮꾩뿉�뒗 Double ���엯�씠誘�濡� rs.getDouble()濡� 媛��졇�샂.
            json.put("wgsY", rs.getDouble(2));

         }

         /*if(rs.next()){
            json.put("wgsX", rs.getDouble(1));
            json.put("wgsY", rs.getDouble(2));
         }else{
            json.put("key", "�떆諛�");
         }*/

      } catch (SQLException e) {
         e.printStackTrace();
      }

      return json;      
   }

   public boolean checkId(String checkId) {
      String sql = "select count(memberId) from members where memberId = ?";

      System.out.println("checkId 吏꾩엯 �꽦怨�");

      PreparedStatement pstmt = null;
      ResultSet rs = null;

      int result = 1;

      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, checkId);
         rs = pstmt.executeQuery();

         if(rs.next()) {
            System.out.println("if臾� 吏꾩엯");
            result = rs.getInt(1);
            if(result == 0) {
               System.out.println("result 媛믪씠 0�엫");
               return true;
            }
         }
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return false;
   }

   public JSONObject loadMyLikePlace(String memberId) {
      String sql = "select contentId, contentTypeId, areaCode, firstImage, title, readCount, addr1, addr2, tel, mapX, mapY from likeplace where memberId = ?";

      JSONObject result = new JSONObject();

      PreparedStatement pstmt = null;
      ResultSet rs = null;

      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, memberId);

         rs = pstmt.executeQuery();

         int count = 0;
         while(rs.next()) {
            JSONObject json = new JSONObject();
            json.put("contentId", rs.getString(1));
            json.put("contenTypeId", rs.getString(2));
            json.put("areaCode", rs.getString(3));
            json.put("firstImage", rs.getString(4));
            json.put("title", rs.getString(5));
            json.put("readCount", rs.getString(6));
            json.put("addr1", rs.getString(7));
            json.put("addr2", rs.getString(8));
            json.put("tel", rs.getString(9));
            json.put("mapX", rs.getString(10));
            json.put("mapY", rs.getString(11));

            result.put(count, json);
            count++;
         }

      } catch (SQLException e) {
         e.printStackTrace();
      }

      return result;

   }

   public void deleteLikePlace(String memberId, String contentId) {
      // TODO Auto-generated method stub
      String sql = "delete from likeplace where memberId = ? and contentId = ?";

      PreparedStatement pstmt = null;

      try {
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, memberId);
         pstmt.setString(2, contentId);

         pstmt.executeUpdate();


      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

}
