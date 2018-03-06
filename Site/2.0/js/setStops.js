//endereco do icone dos marcadores
var imageStop = 'img/stopPin.png';

//array dos marcadores da parada
var markers = [];

//Localiza��o das paradas para os marcadores
var pointsMarkers = [
   {lat: -1.47309 , lng:-48.45862},  //0- Transporte
        {lat: -1.47552, lng: -48.45836}, //1- Seguran�a
        {lat: -1.47719, lng:   -48.4581}, //2- RU
        {lat: -1.47779,  lng:  -48.45676}, //3- MIRANTE
        {lat: -1.47654,  lng: -48.45487}, //4- VADIAO
        {lat: -1.4749,  lng: -48.45426}, //5- INCUBADORA
        {lat:-1.47386, lng:  -48.45463}, //6- ARTE
        {lat: -1.47289, lng:  -48.45158}, //7- TERMINAL
        {lat: -1.47157, lng:-48.44981}, //8- JURIDICO (NAEA)
        {lat: -1.47293,  lng:  -48.44877}, //9- ICSA/ICJ
        {lat: -1.47148,  lng: -48.448}, //10- ODONTOLOGIA
        {lat:  -1.47008, lng:-48.44669}, //11 - NUTRI��O
        {lat: -1.46972, lng:  -48.44655}, //12- BETTINA
        {lat: -1.46846, lng:-48.44822}, //13- GENOMA
        {lat:-1.46436, lng:-48.44475}, //14- INOVACAO
        {lat:-1.46159, lng:-48.44209}, //15- INPE

            //VOLTA

        {lat: -1.46436, lng:-48.44475}, //16- INOVACAO
        {lat: -1.46846, lng:-48.44822}, //17 - GENOMA
     //   { -1.471116,  -48.448037,0}, //18- BETTINA
    //    { -1.470950,  -48.447908,0}, //19- NUTRICAO
        {lat:  -1.47148, lng:-48.448}, //20- ODONTOLOGIA
     //   { -1.467505,  -48.447569,0}, //21- ICSA/ICJ
     //   { -1.467192,  -48.446933,0}, //22- JURIDICO/NAIE
    //    { -1.468420,  -48.448159,0}, //23- TERMINAL
    //    { -1.468937,  -48.448218,0}, //24- ARTE
    //    { -1.463631,  -48.444562,0}, //25- INCUBADORA
     //   { -1.463215,  -48.444622,0}, //26- VADIAO
            {lat: -1.47622, lng:-48.45562}, //27- REITORIA
            {lat: -1.4744, lng:-48.45583}, //28 - ICEN
           {lat: -1.47297, lng:-48.45598}, //29 - GINASIO
            {lat:-1.47243, lng:-48.45728} //30 - CAPACIT

];

//Informac�es das paradas
var informations = new Array(
         //IDA
    ["1-Transporte"          , "Departamento de transporte da UFPA"                 ],        //0
    ["2-Seguran�a"    , "Seguran�a Universidade Federal do Par�"                    ],        //1
    ["3-R.U."      , "Restaurante Universit�rio"                                    ],        //2
    ["4-Mirante", "Bloco de Aulas Mirante do Rio"                                   ],        //3
    ["5-Vadi�o", "Espa�o de Recrea��o, Bancos, Lojas e Servi�os"                    ],        //4
    ["6-Incubadora", "Universitec, PPGITEC, Newton"                                 ],        //5
    ["7-Arte", "Instituto de Ci�ncias da arte / Espa�o verde do ITEC"               ],        //6
    ["8-Terminal", "Terminal Rodovi�rio, Port�o 3 da UFPA"                          ],        //7
    ["9-Juridico (NAEA)", "ICJ, NAEA"                                               ],        //8
    ["10-ICSA, ICJ", "Instituto de ci�ncias sociais aplicadas"                      ],        //9
    ["11-Odontologia", "Faculdade de Odontologia"                                   ],        //10
    ["12-Nutri��o", "Enfermagem"                                                    ],        //11
    ["13-Bettina", "Hospital bettina ferro de souza"                                ],        //12
    ["14-Genoma", "Bloco de P�s Gradua��o do ITEC"                                  ],        //13
    ["15-Espa�o Inova��o", "Parque de Ci�ncia e Tecnologia da UFPA"                 ],        //14
     ["16-INPE", "Instituto de Pesquisas Espaciais"                                 ],        //15


            //VOLTA

    ["17-Espa�o Inova��o", "Parque de Ci�ncia e Tecnologia da UFPA"                                                             ],        //16
    ["18-Genoma", "Bloco de P�s Gradua��o do ITEC"                      ],        //17
 //   {"#19-Bettina", "Hospital bettina ferro de souza"                          },        //18
 //   {"#20-Nutri��o", "Enfermagem"                       },        //19
    ["21-Odontologia", "Faculdade de Odontologia"                                  ],        //20
 //   {"22-Port�o 4", "Port�o 4 da UFPA"                                             },        //21
 //   {"23-CEAMAZON", "Centro de Exc. em Efici�ncia energ�tica da Amaz�nia "         },        //22
 //   {"24-Biomedicina", ""                                                          },        //23
 //   {"25-Parada Circular", ""                                                      },        //24
 //   {"26-PCT guam�", "Parque de ci�ncia e Tecnologia"                              },        //25
 //   {"27-PCT guam�", "Parque de Ci�ncia e Tecnologia"                              },        //26
      ["28-Reitoria", "Reitoria da Universidade Federal do Par�"                     ],         //27
            ["29-ICEN", "Instituto de ci�ncias Exatas e Naturais"                    ],         //28
            ["30-Gin�sio", "Gin�sio de Esportes da Ufpa"                             ],         //29
            ["31-CAPACIT", "Centro de capacita��o e desenvolvimento"                 ]         //30
);

//funcao que insere no mapa as paradas de uma a uma  			marker
function addMarker(location, index) {
	
	var marker = new google.maps.Marker({
    	position: location,
        icon: imageStop, // define a nova imagem do marcador
        title: informations[index][0],
		map: map
    });
		
    marker.addListener('mouseout', function() {
        infowindow.close(map, marker);
    });
	
	marker.addListener('mouseover', function() {
        infowindow.open(map, marker);
    });
	
	
    var contentString = '<b>' + informations[index][0] + '</b>' +
        '<div id="bodyContent">' +
        '<p>' + informations[index][1];

    var infowindow = new google.maps.InfoWindow({
        content: contentString,
        maxWidth: 200
    });
    markers.push(marker);
    
}

// Seta no mapa as paradas do array
function setMap(map) {
	
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}

//funcao que � chamada para apagar as paradas
function clearMarkers() {
    setMap(null);
}

//funcao que e chamada para mostrar as paradas
function showMarkers() {
    setMap(map);
}