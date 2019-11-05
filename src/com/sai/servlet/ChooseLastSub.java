package com.sai.servlet;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ChooseLastSub {
	static String lastSub = "";

	public static String getTotalTime() {
		System.out.println("SearchMiddlePoint->SearchTenSub -> ChooseLastSub ");
		
		double sum = 0;
		double avg = 0;
		double var = 0;
		double std = 0;
		double totalTime[] = new double[10];
		double time = 0;
		double sevTime[] = new double[SearchMiddlePoint.friends.size()];
		double totalSd[] = new double[10];
		double total[] = new double[10];
		HashMap result = new HashMap();

		for(int i = 0; i < SearchTenSub.statName.length; i++) {
			System.out.println("SearchTenSub.statName  ****" + SearchTenSub.statName[i]);
			for(int j = 0; j < SearchMiddlePoint.friends.size(); j++) {
				System.out.println("SearchTenSub.friends " + SearchMiddlePoint.friends.get(j));

				try {			
					String url = "http://swopenapi.seoul.go.kr/api/subway/sample/xml/shortestRoute/0/1/";
					String fURL = URLEncoder.encode(SearchMiddlePoint.friends.get(j), "UTF-8");
					String sURL = URLEncoder.encode(SearchTenSub.statName[i], "UTF-8");
					String shortURL = url + fURL + "/" + sURL;

					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document document = builder.parse(shortURL);

					document.getDocumentElement().normalize();

					NodeList shortList = document.getElementsByTagName("row");
				
					for(int k = 0; k < shortList.getLength(); k++){
						Node nNode = shortList.item(k);
						System.out.println("nNode.getNodeType() : " + nNode.getNodeType());
						if(nNode.getNodeType() == Node.ELEMENT_NODE){

							Element eElement = (Element) nNode;

							NodeList shtTravelTm = eElement.getElementsByTagName("shtTravelTm").item(0).getChildNodes();
							Node shtTrabelInfo = (Node) shtTravelTm.item(0);
							String shtTime = shtTrabelInfo.getNodeValue();

							time = Double.parseDouble(shtTime);
							totalTime[i] += time;
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}	
				sevTime[j] = time;
			}

			//역마다의 평균 시간 계산
			avg = totalTime[i] / SearchMiddlePoint.friends.size();
			System.out.println("��洹� ���� ��媛��� : " + avg);

			sum = 0; 
			for(int l = 0; l < SearchMiddlePoint.friends.size(); l++) {
				sum += Math.pow(sevTime[l] - avg, 2.0);
			}
			
			//표준 편차를 구하기 위한 계산
			var = sum / SearchMiddlePoint.friends.size();

			//표준 편차 계산
			std = Math.sqrt(var);
			totalSd[i] = std;
			
			System.out.println("��以� �몄�� : " + std);
			
			//역마다의 표준 편차와 총 소요시간 계산 -> 공평성을 위한 변수들
			total[i] = totalSd[i] + totalTime[i];
			
			result.put(total[i], SearchTenSub.statName[i]);
		}	
		
		//표준 편차와 소요시간을 더한 값 오름차순으로 정렬
		Arrays.sort(total);
		
		//정렬 후 가장 짧은 시간 선택
		lastSub = (String)result.get(total[0]);
		System.out.println(lastSub);
		System.out.println("ㄱㄴㄷㄹ: " + result.get(total[0]));
		
		//중간지점 반환(String)
		return lastSub;
	}
}




