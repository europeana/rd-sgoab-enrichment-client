package europeana.sgoab.client;

import java.io.IOException;

import javax.json.JsonObject;

import org.apache.http.protocol.ResponseServer;

import europeana.sgoab.util.AccessException;

public class ScriptTestInvokeService {

	public static void main(String[] args) throws Exception {
		try {
			SgoabClient client=new SgoabClient("http://growsmarter.bsc.es:5070/api/enrichment", "Bearer Ep8paH7GKHz9K75G5knles3nTZ0a5J");
			EnrichmentRequest req=new EnrichmentRequest("http://data.europeana.eu/item/90402/SK_A_3911");
			JsonObject response = client.sendRequest(req);
			System.out.println(response.toString());
		} catch (AccessException e) {
			System.out.println(e.getCode());
			System.out.println(e.getResponse());
		}
	}
	
}
