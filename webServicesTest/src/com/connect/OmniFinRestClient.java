package com.connect;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class OmniFinRestClient {
	

	public static void main(String[] args) {
		String jsonInputString=""+
		"{"+
			"\"userCredentials\" : {"+
				"\"userId\" : \"APPUSER\","+
				"\"userPassword\" : \"21232f297a57a5a743894a0e4a801fc3\","+
				"\"source\" : \"WEB\""+
			"},"+
			"\"panRequest\" : {"+
				"\"panNumber\" : \"ASDFG2345\","+
				"\"customerName\" : \"SATTU SINGH\","+
				"\"source\" : \"WEB\","+
				"\"txnType\" : \"DC\","+
				"\"applicationFormNo\" : \"1103583\","+
				"\"txnId\" : 18089,"+
				"\"makerId\" : \"SATINDER\","+
				"\"sourceId\" : \"WEB\""+
			"}"+
		"}";
		String jsonOutputString="";
		RestTemplate restTemplate = new RestTemplate();
		//String url="http://omnifinuat.arthfc.com:610/OmniFinServicesV2/restServices/panService/fetchPanDetails";
		String url="http://10.10.0.69:9090/OmniFinServicesV2/restServices/panService/fetchPanDetails";
		try{
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> newRequest = new HttpEntity<String>(jsonInputString,headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, newRequest, String.class);
			
			if(response!=null){
				jsonOutputString = response.getBody();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("jsonOutputString : "+jsonOutputString);
	}
}