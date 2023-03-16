package europeana.sgoab.client;

public enum EnrichmentType {
	OBJECT_DETECTION;
	
	public String toString() {
		switch(this) {
		case OBJECT_DETECTION:
			return "object-detection";
		}
		throw new RuntimeException("Implementation missing for toString of "+this);
	}
}
