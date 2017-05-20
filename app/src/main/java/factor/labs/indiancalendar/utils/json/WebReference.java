package factor.labs.indiancalendar.utils.json;

import java.util.HashMap;
import java.util.Map;


public class WebReference {
	public static enum WebRefType{
		WIKI (1),
		THIRD(2);
		
		private static Map<Object, Object> map = new HashMap<>();
		int value;
		
		private WebRefType(int value){ this.value = value; }
		public int getValue(){ return value; }
		
		static {
	        for (WebRefType web : WebRefType.values()) {
				map.put(web.getValue(), web);
	        }
	    }

	    public static WebRefType valueOf(int pageType) {
	        return (WebRefType)map.get(pageType);
	    }
	};
	
	WebRefType type;
	String value;
	
	public WebRefType getType() {
		return type;
	}
	public void setType(WebRefType type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
