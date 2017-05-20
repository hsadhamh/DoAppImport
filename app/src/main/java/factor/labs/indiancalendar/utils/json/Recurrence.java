package factor.labs.indiancalendar.utils.json;

import java.util.HashMap;
import java.util.Map;


public class Recurrence {
	public static enum RecurType{
		DAY(1),
		MONTH(2),
		YEAR(3),
		HOUR(4),
		MINUTES(5),
		SECONDS(6);
		
		int value;
		private RecurType(int value){ this.value = value; }
		public int getValue(){ return value; }
		
		private static Map<Object, Object> map = new HashMap<>();
		
		static {
	        for (RecurType category : RecurType.values()) {
				map.put(category.getValue(), category);
	        }
	    }

	    public static RecurType valueOf(int pageType) {
	        return (RecurType)map.get(pageType);
	    }
	};
	
	RecurType type;
	int value;
	
	public RecurType getType() {
		return type;
	}
	public void setType(RecurType type) {
		this.type = type;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
