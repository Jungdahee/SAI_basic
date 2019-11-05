package com.sai.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SearchTenSub {
	
	static String statName[] = new String[10]; 
	static String lastSub = "";
	
	public static JSONObject search(double mapX, double mapY) {
		
		System.out.println("\n ##### -----SearchTenSub START");
		
		JSONObject result = new JSONObject();
		//JSONObject place = new JSONObject();
		
		try{

			String locURL = "http://swopenapi.seoul.go.kr/api/subway/5947735178616c77393747474d664c/xml/nearBy/0/10/" + mapX + "/" + mapY;

			DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
			Document doc = dBuilder.parse(locURL);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("row");

			for(int i = 0; i < nList.getLength(); i++){
				Node nNode = nList.item(i);
				if(nNode.getNodeType() == Node.ELEMENT_NODE){

					Element eElement = (Element) nNode;

					NodeList statnNm = eElement.getElementsByTagName("statnNm").item(0).getChildNodes();
					Node statnNmInfo = (Node) statnNm.item(0);
					String state = statnNmInfo.getNodeValue();

					NodeList subwayNm = eElement.getElementsByTagName("subwayNm").item(0).getChildNodes();
					Node subwayNmInfo = (Node) subwayNm.item(0);
					String subway = subwayNmInfo.getNodeValue();

					NodeList subwayXcnts = eElement.getElementsByTagName("subwayXcnts").item(0).getChildNodes();
					Node subwayXcntsInfo = (Node) subwayXcnts.item(0);
					String subwayX = subwayXcntsInfo.getNodeValue();

					NodeList subwayYcnts = eElement.getElementsByTagName("subwayYcnts").item(0).getChildNodes();
					Node subwayYcntsInfo = (Node) subwayYcnts.item(0);
					String subwayY = subwayYcntsInfo.getNodeValue();

					statName[i] = state;
				}
				
				System.out.println("statName" + i + " : " + statName[i]);
			}

			lastSub = ChooseLastSub.getTotalTime();
			
			System.out.println("##-----SearchTenSub -  lastSub = ChooseLastSub.getTotalTime();:  " + lastSub);
			
			result = SearchLastSubPath.findPath(lastSub);
			
			System.out.println("##-----SearchTenSub result(=path)  " + result.toJSONString());
			
			
		} catch (Exception e){   
			e.printStackTrace();
			
		}
		//�깃났
		return result; //!寃곌낵媛��대�� �����댁�� 紐⑸��� 媛��� 由ы�대���댁�� ��.
	} 
}
