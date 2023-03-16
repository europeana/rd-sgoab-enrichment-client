package europeana.sgoab.annotations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

public class TagToSemanticMapping {
	private static Map<String, String> mappings=new HashMap<String, String>();
	
	static {
		try {
			InputStream mapIs = TagToSemanticMapping.class.getClassLoader().getResourceAsStream("sgoab-classes-mapping.csv");
			CSVParser csv=new CSVParser(new InputStreamReader(mapIs, StandardCharsets.UTF_8), CSVFormat.DEFAULT);
			boolean first=true;
			for(CSVRecord rec : csv) {
				if(first) {
					first=false;
				} else {
					String wikidata=rec.get(2);
					String iconClass=rec.get(3);
					String newUri=null;
					if(!wikidata.equals("?") && !wikidata.equals("N/A") ) {
						newUri="http://www.wikidata.org/entity/"+wikidata;
					} else if(!iconClass.equals("?") && !iconClass.equals("N/A") ) 
						newUri="http://iconclass.org/"+URLEncoder.encode(iconClass, "UTF-8");
					if(newUri==null)
						System.out.println("WARNING: Not SGoaB mapping for "+rec.get(0));
					else
						mappings.put(StringUtils.isEmpty(rec.get(1)) ? rec.get(0).toLowerCase() : rec.get(1).toLowerCase(), newUri);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static String getSemanticUriForTag(String tag) {
		return mappings.get(tag.toLowerCase());
	}
}
