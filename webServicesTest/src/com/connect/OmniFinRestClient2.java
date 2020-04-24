package com.connect;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.springframework.web.client.RestTemplate;

public class OmniFinRestClient2 {
	public static class DummyTrustManager implements X509TrustManager {
		public DummyTrustManager() {
		}

		public boolean isClientTrusted(X509Certificate cert[]) {
			return true;
		}

		public boolean isServerTrusted(X509Certificate cert[]) {
			return true;
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

		}

		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

		}
	}
	public static class DummyHostnameVerifier implements X509HostnameVerifier {

		public void verify( String host, X509Certificate cert ) {
			
		}

		public void verify(String host, SSLSocket ssl) {
			
		}
		public void verify(String host, String[] cns,String[] subjectAlts) {
			
		}
		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}
	}
	
		
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
		String url="http://omnifinuat.arthfc.com:610/OmniFinServicesV2/restServices/panService/fetchPanDetails";
		try{
			if(url.toLowerCase().contains("https")){
				jsonOutputString=callThroughHttps(jsonInputString,url);
			}else{
				jsonOutputString = restTemplate.postForObject(url, jsonInputString,String.class);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("jsonOutputString : "+jsonOutputString);
	}
	
	
	public static String callThroughHttps(String jsonInputString,String apiUrl){
		String apiResponse="";
		try{
			CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
			try {
					System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
					SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
					sslContext.init(new KeyManager[0],new TrustManager[] { new DummyTrustManager() },new SecureRandom());
					SchemeRegistry schemeRegistry = new SchemeRegistry();
					schemeRegistry.register(new Scheme("https", 80, new SSLSocketFactory(sslContext,new DummyHostnameVerifier())));
					ClientConnectionManager cm =  new SingleClientConnManager(schemeRegistry);
					httpClient = new DefaultHttpClient(cm);
			}catch (Exception e) {
				e.printStackTrace();
			}
			java.lang.System.setProperty("https.protocols", "TLSv1.2");
			StringEntity entity2 = new StringEntity(jsonInputString,ContentType.APPLICATION_FORM_URLENCODED);
			HttpPost httpPost= new HttpPost(apiUrl);
			httpPost.addHeader("Content-Type", "application/json; utf-8");
			httpPost.setEntity(entity2);
				
			HttpResponse response = httpClient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
			System.out.println("statusCode : "+status);
			if(status==200){
				org.apache.http.HttpEntity entity = response.getEntity();
				InputStream responseContent = entity.getContent(); // Read content into String tempResponse
				BufferedReader in = new BufferedReader(new InputStreamReader(responseContent));
				String current="";
				while((current = in.readLine()) != null){
					apiResponse += current;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			apiResponse="";
		}
		return apiResponse;
	}
}