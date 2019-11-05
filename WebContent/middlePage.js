//수정
var myX = 126.98696440000003;
var myY = 37.5624615;

var resultObj = new Object(); //mapBox뿌릴 JSON

var geojson = new Object();
var geojson2 = new Object();

function pathObjToMapbox(result, sName) {

	var featureArr = new Array();
	var feature = new Object();
	var geom = new Object();
	var coordinate = new Array();

	console.log("ShowPath,html ----- \n line만들기 위한 Object.keys(result[sName]).length : " + Object.keys(result[sName]).length);
	for (var i = 0; i < Object.keys(result[sName]).length - 2; i++) {

		var xy = new Array();
		xy.push(result[sName][i]["wgsX"]);
		xy.push(result[sName][i]["wgsY"]); //data[i].wgsX
		coordinate.push(xy);
	}


	geom['coordinates'] = coordinate;
	geom['type'] = 'LineString';

	feature['type'] = 'Feature';
	feature['geometry'] = geom;

	featureArr.push(feature);

	geojson['type'] = 'FeatureCollection';
	geojson['features'] = featureArr;

	//alert("json check : " + JSON.stringify(geojson) );


	//마커객체 만들기


	var featureArr2 = new Array();
	var feature2 = new Object();
	var proper2 = new Object();
	var geom2 = new Object();
	var xy2 = new Array();


	xy2.push(result[sName][Object.keys(result[sName]).length - 3]["wgsX"] );//- 0.02001
	xy2.push(result[sName][Object.keys(result[sName]).length - 3]["wgsY"] );//- 0.02

	geom2['type'] = 'Point';
	geom2['coordinates'] = xy2;


	proper2['title'] = result[sName][Object.keys(result[sName]).length - 3]["stationName"];
	proper2['icon'] = "subwayStation";


	feature2['type'] = 'Feature';
	feature2['geometry'] = geom2;
	feature2['properties'] = proper2;

	featureArr2.push(feature2);

	//초기화
	xy2 = [];
	geom2 = {};
	proper2 = {};
	feature2 = {};

	xy2.push(result[sName][0]["wgsX"] );//+ 0.02001
	xy2.push(result[sName][0]["wgsY"] );//+ 0.02

	geom2['type'] = 'Point';
	geom2['coordinates'] = xy2;

	proper2['title'] = result[sName][0]["stationName"];
	proper2['icon'] = "subwayStation";


	feature2['type'] = 'Feature';
	feature2['geometry'] = geom2;
	feature2['properties'] = proper2;
	featureArr2.push(feature2);


	geojson2['type'] = 'FeatureCollection';
	geojson2['features'] = featureArr2;

	console.log("geojson :: " + JSON.stringify(geojson));
	console.log("geojson2 :: " + JSON.stringify(geojson2));

}

mapboxgl.accessToken = 'pk.eyJ1Ijoic3VhbWoiLCJhIjoiY2ptc203cWl0MGM1dDNwbnVwemdjZTFnbyJ9.v4QFpeaIi88xAl73QkEBUg';
var geocoder = new MapboxGeocoder({
	accessToken: mapboxgl.accessToken
});
var map;
function loadMap(){

	//mapbox준비
	map = new mapboxgl.Map({
		container: 'map',
		style: 'mapbox://styles/mapbox/dark-v9',
		center: [126.98696440000003, 37.5624615],
		zoom: 11
	});

	map.on('load', function () {
		map.addSource('line', {
			type: 'geojson',
			lineMetrics: true,
			data: geojson
		});	
		map.addLayer({
			"id": "markers",
			"type": "symbol",
			"source": {
				"type": "geojson",
				"data": geojson2
			},
			"layout": {
				"text-field": "{title}",
			},
			"paint": {
				"text-color": "hotpink",
			}
		});
		map.addLayer({
			type: 'line',
			source: 'line',
			id: 'line',
			paint: {
				'line-color': 'purple',
				'line-width': 5,
				// 'line-gradient' must be specified using an expression
				// with the special 'line-progress' property
				'line-gradient': [
					'interpolate',
					['linear'],
					['line-progress'],
					0, "pink",
					0.1, "hotpink",
					0.3, "purple",
					0.5, "royalblue",
					0.7, "skyblue",
					1, "pink"
					]
			},
			layout: {
				'line-cap': 'round',
				'line-join': 'round'
			}
		})

		$(document).on('click', '#likeBtn', function () {
			var likeBtn = $(this);

			var parent = $(likeBtn).parent();
			var contentId = $(parent).children('#add_contentId').val();
			var contentTypeId = $(parent).children('#add_contentTypeId').val();
			var areaCode = $(parent).children('#add_areaCode').val();
			var firstImage = $(parent).children('#add_firstImage2').val();
			var title = $(parent).children('#add_title').text();
			var readCount = $(parent).children('#add_readCount').text();
			var addr1 = $(parent).children('#add_addr1').text();
			var addr2 = $(parent).children('#add_addr2').text();
			var tel = $(parent).children('#add_tel').text();
			var mapX = $(parent).children('#add_mapX').val();
			var mapY = $(parent).children('#add_mapY').val();

			var obj = new Object();

			obj.contentId = contentId;
			obj.contentTypeId = contentTypeId;
			obj.areaCode = areaCode;
			obj.firstImage = firstImage;
			obj.title = title;
			obj.readCount = readCount;
			obj.addr1 = addr1;
			obj.addr2 = addr2;
			obj.tel = tel;
			obj.mapX = mapX;
			obj.mapY = mapY;

			//데이터 직렬화
			var data = JSON.stringify(obj); 

			$.ajax({
				url: "./AddLikePlace.do",
				type: "post",
				dataType: "json",
				data: { "data": contentId },
				success: function (data) {
					alert("좋아요를 추가했습니다.");
					//location.href = "myPrefer.html";
				}

			})
		});
	});
};


$(document).ready(function () { 
	var defaultStation;
	$("#subway").click(function () {
		location.href = "shtTimePath.html?station=" + defaultStation;
	});
	
	$("#selectStation").on('click', '#stationBtn', function () {

		var selectStation = $(this).val();


		alert(selectStation + "을 출발지로 변경");
		pathObjToMapbox(resultObj, selectStation);

		loadMap();
	});
	
	$.ajax({
		cache: false,
		url: "./LoadLastSubPath.do",
		type: "post",
		dataType: "json",
		success: function (result) {

			var result = JSON.stringify(result);
			console.log("middlePointCafe ----- : LoadLastSubPath.do에서 넘어온 값 :: " + result);

			resultObj = JSON.parse(result);
			var top = 100;
			var stationsBtn = "";
			for (key in resultObj) {
				top += 35;
				console.log(" key in resultObj : " + key);

				stationsBtn += " <button class='fix00' id='stationBtn' name='"+key+"Btn' style='position: absolute; top:" + top + "px'  value=" + key + "> " +key+" </button>";
			}
			
			$("#selectStation").html(stationsBtn);

			console.log("첫역 : " + Object.keys(resultObj)[0]);
			defaultStation = Object.keys(resultObj)[0];

			var midStation = resultObj[Object.keys(resultObj)[0]][Object.keys(resultObj[defaultStation]).length - 3]["stationName"];

			alert("우리 SAI 중간지점은 : " + midStation);


			//json객체 만들기
			pathObjToMapbox(resultObj, Object.keys(resultObj)[0]);

			//mapBox LOAD
			loadMap();

			//mapBox search
			document.getElementById('geocoder').appendChild(geocoder.onAdd(map));
		}
	});

	//    < !--플레이스 ajax-- >
	$.ajax({
		cache: false,
		url: "./SearchMiddlePlace.do",
		type: "post",
		dataType: "json",
		success: function (data) {

			var sResult = JSON.stringify(data);

			var result = JSON.parse(sResult)

			for (var i = 0; i < Object.keys(data).length; i++) {

				var data1 = "<br><br><div class='info' id='" + i + "'>" +
				"<img id='add_firstImage1' width='300px' height='300px' src='" + result[i].firstImage + "'/>" +
				"<input type='hidden' id='add_contentId' value='" + result[i].contentId + "'</input>" +
				"<input type='hidden' id='add_contentTypeId' value='" + result[i].contentTypeId + "'>" + "</input>" +
				"<input type='hidden' id='add_areaCode' value='" + result[i].areaCode + "'>" + "</input>" +
				"<input type='hidden' id='add_firstImage2' value='" + result[i].firstImage + "'>" + "</input>" +
				"<p id='add_title'>" + result[i].title + "</p>" +
				"<p type='text' id='add_readCount'>조회수 : " + result[i].readCount + "</p>" +
				"<p type='text' id='add_addr1'>" + result[i].addr1 + "</p>" +
				"<input type='hidden' id='add_addr2' value='" + result[i].addr2 + "'>" + "</input>" +
				"<p type='text' id='add_tel'>" + result[i].tel + "</p>" +
				"<input type='hidden' id='add_mapX' value='" + result[i].mapX + "'>" + "</input>" +
				"<input type='hidden' id='add_mapY' value='" + result[i].mapY + "'>" + "</input>" +
				"<button id='likeBtn' value='좋아요'><img id='myImage' src='images/heart1.png' width:'50px'/></button></div>";


				var html = $.parseHTML(data1);

				$("#div").append(html);

			}
		}

	})
});
/*
        //distance
        var fixedMarker = L.marker(new L.LatLng(myY, myX), {
            icon: L.mapbox.marker.icon({
                'marker-color': 'ff8888'
            })
        }).bindPopup('우리집').addTo(map);

        //Store the fixedMarker coordinates in a variable.
        var fc = fixedMarker.getLatLng();

        //Create a featureLayer that will hold a marker and linestring.
        var featureLayer = L.mapbox.featureLayer().addTo(map);


        map.on('click', function (ev) {
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

            // Finally, print the distance between these two points
            // on the screen using distanceTo().
            var container = document.getElementById('distance');
            container.innerHTML = (fc.distanceTo(c)).toFixed(0) + 'm';


        });*/