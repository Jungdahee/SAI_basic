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

//Q이거 질문					
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

			//�� ���� ���� �щ�� 紐��� 珥� ������媛��� �몄�� ��濡� ���� 寃곌낵(=��洹�)
			avg = totalTime[i] / SearchMiddlePoint.friends.size();
			System.out.println("��洹� ���� ��媛��� : " + avg);

			sum = 0; //珥�湲고��
			for(int l = 0; l < SearchMiddlePoint.friends.size(); l++) {
				sum += Math.pow(sevTime[l] - avg, 2.0);
			}
			
			//遺��� 援ы���� ��
			var = sum / SearchMiddlePoint.friends.size();

			//��以� �몄감 援ы���� ��
			std = Math.sqrt(var);
			totalSd[i] = std;
			
			System.out.println("��以� �몄�� : " + std);
			
			//���� 留ㅺ린湲� ���� ��以� �몄감 + 珥� ���� ��媛�
			total[i] = totalSd[i] + totalTime[i];
			
			//留ㅽ���� ���� hashMap �ъ��
			result.put(total[i], SearchTenSub.statName[i]);
		}	
		
		//�ㅻ�李⑥���쇰� ����
		Arrays.sort(total);
		
		//媛��� 泥ル�吏� �몃�깆�ㅼ�� 寃곌낵媛��� 理����대��濡� 理�醫���
		lastSub = (String)result.get(total[0]);
		System.out.println(lastSub);
		System.out.println("理�醫� 異�泥���: " + result.get(total[0]));
		
		//理�醫��� 諛���
		return lastSub;
	}
}




