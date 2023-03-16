package europeana.sgoab.annotations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;

import org.apache.commons.io.FileUtils;

public class ObjectDetectionAnnotations {
	JsonObject rootJson;
	File jsonFile;
	
//	public ObjectDetectionAnnotations(JsonObject rootJson) {
//		this.rootJson = rootJson;
//	}
	public ObjectDetectionAnnotations(File jsonFile) throws IOException {
		this.jsonFile=jsonFile;
		FileInputStream in = new FileInputStream(jsonFile);
		JsonReader parser = Json.createReader(in);
		rootJson=parser.readObject();
		parser.close();
		in.close();
	}
	
	public void removeAnnotationsNotIn(Set<String> tagsToKeep) {
		JsonArrayBuilder newArrayOfAnnotations=Json.createArrayBuilder();
		
		JsonObject first = rootJson.getJsonObject("first");
		JsonArray annotations = first.getJsonArray("items");
		HashSet<String> dupTagsDetector=new HashSet<>();
		for(int i=0; i<annotations.size() ; i++) {
			Annotation anno = new Annotation(annotations.getJsonObject(i));
			if(dupTagsDetector.contains(anno.getTag()))
				continue;
			if(tagsToKeep.contains(anno.getTag())) {
				newArrayOfAnnotations.add(anno.getJson());
				dupTagsDetector.add(anno.getTag());
			}
		}
//		first.put("items", (JsonValue)newArrayOfAnnotations.build());
		
		JsonArray annotsArray = newArrayOfAnnotations.build();
		JsonObject newRootJson=Json.createObjectBuilder(rootJson).add("first", 
				Json.createObjectBuilder(first).add("items", annotsArray)
				).add("total",annotsArray.size()).build();
		
		rootJson=newRootJson;
	}
	
	public void save() throws IOException {
		FileUtils.write(jsonFile, rootJson.toString(), StandardCharsets.UTF_8);
	}
	
	public void saveTo(File jsonFile) throws IOException {
		FileUtils.write(jsonFile, rootJson.toString(), StandardCharsets.UTF_8);
	}

	public void changeToSemanticTags() {
//		  "body": {
//			    "type": "TextualBody",
//			    "value": "book",
//			    "language": "en"
//			  },

//		{
//			  "motivation": "tagging",
//			  "body": "http://sws.geonames.org/2988507",
//			  "target": "http://data.europeana.eu/item/09102/_UEDIN_214"
//			}
		JsonArrayBuilder newArrayOfAnnotations=Json.createArrayBuilder();
		
		JsonObject first = rootJson.getJsonObject("first");
		JsonArray annotations = first.getJsonArray("items");
		HashSet<String> dupTagsDetector=new HashSet<>();
		for(int i=0; i<annotations.size() ; i++) {
			Annotation anno = new Annotation(annotations.getJsonObject(i));
			String semanticUri=TagToSemanticMapping.getSemanticUriForTag(anno.getTag());
			if(semanticUri!=null)
				anno.changeToSemanticTag(semanticUri);
			else
				System.out.println("WARNING: No mapping for "+anno.getTag());
			newArrayOfAnnotations.add(anno.getJson());
		}
//		first.put("items", (JsonValue)newArrayOfAnnotations.build());
		
		JsonArray annotsArray = newArrayOfAnnotations.build();
		JsonObject newRootJson=Json.createObjectBuilder(rootJson).add("first", 
				Json.createObjectBuilder(first).add("items", annotsArray)
				).add("total",annotsArray.size()).build();
		
		rootJson=newRootJson;
		
	}

	
}
