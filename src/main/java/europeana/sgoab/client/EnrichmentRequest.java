package europeana.sgoab.client;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class EnrichmentRequest {
	String item;
	OutputProfile profile=OutputProfile.EF_ANNOTATION_SIMPLE;
	ArrayList<EnrichmentType> enrichments=new ArrayList<EnrichmentType>() 
		{{add(EnrichmentType.OBJECT_DETECTION);}};// currently only object detection is implemented by SGoaB
	
	public EnrichmentRequest(String imageUri) {
		super();
		this.item = imageUri;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String imageUri) {
		this.item = imageUri;
	}

	public OutputProfile getProfile() {
		return profile;
	}

	public void setProfile(OutputProfile profile) {
		this.profile = profile;
	}
	
	
	public JsonObject toJson() {
		JsonObjectBuilder ret=Json.createObjectBuilder();
		ret.add("item", item);
		ret.add("output_profile", profile.toString());
		JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
		for(EnrichmentType type: enrichments)
			arrayBuilder.add(type.toString());
		ret.add("enrichments", arrayBuilder.build());
		return ret.build();
	}
}
