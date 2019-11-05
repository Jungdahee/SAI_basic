L.mapbox.accessToken = 'pk.eyJ1Ijoic3VhbWoiLCJhIjoiY2ptc203cWl0MGM1dDNwbnVwemdjZTFnbyJ9.v4QFpeaIi88xAl73QkEBUg';
var map = L.mapbox.map('map', 'mapbox.dark')
.setView([37.5624615, 126.98696440000003], 10);
//modif

var selectedStation = " ";

var myX = 126.98696440000003;
var myY = 37.5624615;

var virtualX = 0;
var virtualY = 0;

var sendSelectedObj = new Object();

$(document).ready(function() {
	$("#middlePoint").click(function(){
		alert("중간 지점 찾기 시작");

		var data = JSON.stringify(sendSelectedObj);
		console.log("mainPage.js ---- data: " + data);
		var redirectData = "";

		$.ajax({
			url: "./SearchMiddlePoint.do",
			type: "post",
			data: {
				"data" : data
			},
			datatype: "string", //원래는 "json"
			success: function(data){
				alert("뭐든 일단 되고 있음");
				alert(JSON.stringify(data));
				if(Object.keys(data).length == 0){
					alert("SearchTenSub 연결 실패, 다시 시도해주세요");
					location.href = "mainPage.html";
				}
				else
					location.href = "middlePointTotal.html";
					
			}
		});
	})


	$.ajax({
		url : "./LoadFriendsToMap.do",
		type : "post",
		dataType : "json",
		success: function(data){
			console.log("mainPage.js ------- \n LoadFriendsToMap.do에서 넘어온 DATA \n  " + JSON.stringify(data));
//DATA -친구들 버튼화 + MAPBOX객체생성
			//사용자 위치로 거리 계산하는 X, Y 변경
			myX = data[0].wgsX;
			myY = data[0].wgsY;
			
//			거리재는 함수
			distance();

//			사용자 정보 넣음 - 다른 색으로
			var memberId = [];
			var memberName = [];
			var properties = new Object();
			var xy = new Array();
			var geometry = new Object();
			var features = new Array();
			var features_child = new Object();

//			한 Station에 두명의 사용자 있을경우
			function plus(features, st , id ,name){
				for (feature in features){// 전체객체에서 하나씩 가져옴  features=배열
					if(features[feature].properties.station == st){
//						alert("features[feature].properties.station : " + features[feature].properties.station+ " \n st : " + st);
						features[feature].properties.memberId.push(id);
						features[feature].properties.memberName.push(name);
						return true;
					}
				};
				return false;
			};

			var i = 0;
			do{
				if(plus(features, data[i].memberStation , data[i].memberId ,data[i].memberName) == false){
					var memberId = [];
					var memberName = [];

					memberId.push(data[i].memberId);
					memberName.push(data[i].memberName);

					var properties = new Object();
					properties['station'] = data[i].memberStation;
					properties['memberId'] = memberId;
					properties['memberName'] = memberName;
					properties['marker-color'] = "#548cba";
					properties['marker-size'] = "large";
					properties['marker-symbol'] = "ferry";

					var xy = new Array();
					xy.push(data[i].wgsX);
					xy.push(data[i].wgsY);

					var geometry = new Object();
					geometry['type'] = "Point";
					geometry['coordinates'] = xy;

					var features_child = new Object();
					features_child['type'] = "Feature";
					features_child['properties'] = properties;
					features_child['geometry'] = geometry;

					features.push(features_child);

				}
				i++;
			}while(i < Object.keys(data).length);
			
			console.log( i +"번째 사용자 입력  : "+JSON.stringify(features));
//			사용자 색변경
			features[0]["properties"]["marker-color"] = '#ffaacc';
			console.log( "사용자 색 변경 :\n "+JSON.stringify(features[0]));
			
//			가상역
			$("#addStation").click(function(){
				var station = $("#virtualStation").val();
				alert("입력값 : " + station);
				//MemberDao.searchLocation불러오기
				findVirtualXY(station);
	
			});
			var geojson = new Object();
			geojson['type'] = 'FeatureCollection';
			geojson['features'] = features;

			var featureLayer = L.mapbox.featureLayer()
			.setGeoJSON(geojson)
			.addTo(map);

			
			featureLayer.eachLayer(function(layer) {

				var fObj = new Object();

				var memberArr = [layer.feature.properties.station];
				var content = "<h1 ><font color='#eeaeb4'> º " + layer.feature.properties.station +" 역 º</font></h1>";
				for(i in layer.feature.properties.memberId){
					memberArr.push(layer.feature.properties.memberId[i]);
					content +='<input type="checkbox" name="'+layer.feature.properties.station+
					'" id="'+layer.feature.properties.memberId[i]+
					'" value="'+layer.feature.properties.memberName[i]+
					'"><label for="'+ layer.feature.properties.memberId[i]+
					'">'+ layer.feature.properties.memberName[i]+'</label><br>';
				}
				content += '<br/><button id=" '+layer.feature.properties.station+'"  class="'+layer.feature.properties.station+'" > SELECT</button>';

				$('#map').on('click', '.'+layer.feature.properties.station, function() {
					$("#map input[name="+layer.feature.properties.station+"]:checked").each(function() {
						
						fObj['stationName'] = layer.feature.properties.station;
						fObj['memberName'] = $(this).val();
						fObj['mapX'] = layer.feature.geometry.coordinates[0];
						fObj['mapY'] = layer.feature.geometry.coordinates[1];

						sendSelectedObj[$(this).val()] = fObj;
						selectedArr(fObj);
						console.log("####sendSelectedObj 출력\n" + JSON.stringify(sendSelectedObj));
					});
				});
				//$("#hid").attr('value', Obj)
				layer.bindPopup(content);
			});
		}
	});
});

function selectedArr(fObj){
	var memberName = fObj["memberName"];
	var staionName =  fObj["stationName"];
	
	selectedStation += "<input type ='button' id = 'btn' name=" +memberName+ " style='color: skyblue; background: transparent; border: 1px solid skyblue ; border-radius: 25px;' value='" +memberName + "("+ staionName + ")  x' onclick='deleteSelected()'> &nbsp;";
	
	$("#station").html(selectedStation);
	
};

function deleteSelected(){
	alert(typeof(stationName));
	alert(stationName.toString());
	
	 document.getElementById("btn").value
	
//	$('#divP input[name="prod"]:checked').val();
//	alert("deleteSelected :: name :: " + typeof($(this).val()));
	delete sendSelectedObj[name];
	console.log("####sendSelectedObj 출력\n" + JSON.stringify(sendSelectedObj));
}


function distance(){
//	distance
	var fixedMarker = L.marker(new L.LatLng(myY + 0.012, myX + 0.0012), {
		icon: L.mapbox.marker.icon({
			'marker-color': 'ff8888'
		})
	}).bindPopup('우리집').addTo(map);

//	Store the fixedMarker coordinates in a variable.
	var fc = fixedMarker.getLatLng();

//	Create a featureLayer that will hold a marker and linestring.
	var featureLayer = L.mapbox.featureLayer().addTo(map);


	map.on('click', function(ev) {
		var c = ev.latlng;

		var geojsonDis = {
				type: 'FeatureCollection',
				features: [
					{
						"type": "Feature",
						"geometry": {
							"type": "Point",
							"coordinates": [c.lng, c.lat]
						},
						"properties": {
							"marker-color": "#ff8888"
						}
					}, {
						"type": "Feature",
						"geometry": {
							"type": "LineString",
							"coordinates": [
								[fc.lng, fc.lat],
								[c.lng, c.lat]
								]
						},
						"properties": {
							"stroke": "#ff8888",
							"stroke-opacity": 0.5,
							"stroke-width": 4
						}
					}
					]
		};

		featureLayer.setGeoJSON(geojsonDis);

		var container = document.getElementById('distance');
		container.innerHTML = (fc.distanceTo(c)).toFixed(0) + 'm';
	});
};

function findVirtualXY(station){
	
	$.ajax({
		url: "./FindVirtualXY.do",
		type: "post",
		data: {"station" : station},
		datatype: "string",
		success: function(data){
			alert("FindVirtualXY DAO갔다옴");
			var obj = JSON.parse(data);
			
			alert("FindVirtualXY DAO갔다옴 obj : \n" + JSON.stringify(obj));
			
			virtualX = obj["wgsX"];
			virtualY = obj["wgsY"];
			
			var fObj = new Object();
			fObj['stationName'] = station;
			fObj['memberName'] = "가상친구";
			fObj['mapX'] = virtualX;
			fObj['mapY'] = virtualY;

			sendSelectedObj["가상친구"] = fObj;
			selectedArr(fObj);
			
			alert("findVirtualXY의 마지막 \n "+ JSON.stringify(sendSelectedObj));
		}
	});
	
}



