package europeana.sgoab.annotations;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

public class Annotation {
	JsonObject json;

	public Annotation(JsonObject json) {
		super();
		this.json = json;
	}
	
	public String getTag() {
		JsonObject body = json.getJsonObject("body");
		return body.getString("value");
	}

	public JsonObject getJson() {
		return json;
	}
	
	public void changeToSemanticTag(String bodyUri) {
		JsonObjectBuilder newJson=Json.createObjectBuilder(json);
		newJson.add("body", bodyUri);
		json=newJson.build();
	}
	
}
