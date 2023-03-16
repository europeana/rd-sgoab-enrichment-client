package europeana.sgoab.client;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.json.JsonObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.protocol.ResponseServer;

import europeana.sgoab.util.AccessException;

public class ScriptExportObjectDetectionAnnotations {

	public static void main(String[] args) throws Exception {
		try {
			SgoabClient client=new SgoabClient("http://growsmarter.bsc.es:5070/api/enrichment", "Bearer Ep8paH7GKHz9K75G5knles3nTZ0a5J");
			for(File urisFile : new File("data").listFiles()) {
				if(!urisFile.getName().startsWith("uris_") || !urisFile.getName().endsWith(".txt"))
					continue;
				
				File outFolder=new File(urisFile.getParentFile(), urisFile.getName().substring(5, urisFile.getName().lastIndexOf('.')));
				if(!outFolder.exists())
					outFolder.mkdir();
				for(String uri: FileUtils.readLines(urisFile, StandardCharsets.UTF_8)) {
					uri=uri.trim();
					if(uri.isEmpty()) continue;
					System.out.println("Requesting "+ uri);
					StopWatch stopWatch=new StopWatch();
					stopWatch.start();
					EnrichmentRequest req=new EnrichmentRequest(uri);
					JsonObject response = client.sendRequest(req);
					stopWatch.stop();
					System.out.println("Time taken: "+stopWatch.getTime(TimeUnit.MILLISECONDS)+"ms");
					
					File outAnnotFile=new File(outFolder, URLEncoder.encode(uri, "UTF8")+".json");
					FileUtils.write(outAnnotFile, response.toString(),StandardCharsets.UTF_8);
				}
			}
		} catch (AccessException e) {
			System.out.println(e.getCode());
			System.out.println(e.getResponse());
		}
	}
	
}
