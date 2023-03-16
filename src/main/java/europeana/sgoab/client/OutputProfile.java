package europeana.sgoab.client;

public enum OutputProfile {
	JSON, EF_ANNOTATION_SIMPLE, EF_ANNOTATION_COMPLETE;


	public String toString() {
		return name().replace('_', '-');
	}
}
