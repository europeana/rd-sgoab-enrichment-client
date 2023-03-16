package europeana.sgoab.client;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Properties;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;

import europeana.sgoab.util.AccessException;
import europeana.sgoab.util.http.HttpRequest;
import europeana.sgoab.util.http.HttpRequestService;
import europeana.sgoab.util.http.UrlRequest;
import europeana.sgoab.util.http.UrlRequest.HttpMethod;

public class SgoabClient {
	HttpRequestService httpService;
	String url;
	String authorizationBearer;
	
	public SgoabClient(String url, String authorizationBearer) {
		this.url=url;
		this.authorizationBearer = authorizationBearer;

		httpService=new HttpRequestService();
		httpService.init();
	}
	
	public JsonObject sendRequest(EnrichmentRequest req) throws InterruptedException, IOException, AccessException {
		try {
			UrlRequest urlRequestSettings = new UrlRequest(url, HttpMethod.POST);
			urlRequestSettings.addHttpHeader("Authorization", authorizationBearer);
//			System.out.println(req.toJson().toString());
			
			HttpEntity entity = new ByteArrayEntity(req.toJson().toString().getBytes("UTF-8"), ContentType.APPLICATION_JSON);
			urlRequestSettings.setRequestContent(entity);
			HttpRequest httpReq= new HttpRequest(urlRequestSettings);
			httpService.fetch(httpReq);
			String responseStr = httpReq.getContent().asString();
			if(httpReq.getResponseStatusCode()!=200)
				throw new AccessException(url, httpReq.getResponseStatusCode(), responseStr);
			JsonParser parser = Json.createParser(new StringReader(responseStr));
			parser.next();
			JsonObject responseJson=parser.getObject();
			return responseJson;
		} catch (UnsupportedEncodingException e) { throw new RuntimeException(e.getMessage(), e); }
	}


	public static Properties readProperties() {
		Properties prop=new Properties();
		try {
			prop.load(SgoabClient.class.getClassLoader().getResourceAsStream("sgoab-api.properties"));
			return prop;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}