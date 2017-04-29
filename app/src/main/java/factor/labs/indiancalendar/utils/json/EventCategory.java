package factor.labs.indiancalendar.utils.json;

import java.util.HashMap;
import java.util.Map;

public class EventCategory {
	public static enum Category{
		NONE(0),
		HOLIDAY(1),
		RELIGIOUS(2), 
		ALL_DAY(4);
		
		long value;
		private Category(long n){ value = n; }
		public long getValue(){ return value; }
		
		private static Map<Object, Object> map = new HashMap<>();
		
		static {
	        for (Category category : Category.values()) {
				map.put(category.getValue(), category);
	        }
	    }

	    public static Category valueOf(int pageType) {
	        return (Category)map.get(pageType);
	    }
	};
}
