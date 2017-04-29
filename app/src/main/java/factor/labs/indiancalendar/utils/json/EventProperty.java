package factor.labs.indiancalendar.utils.json;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class EventProperty {
	public static enum Religion{
		Islam(1),
		Sikh(2),
		Buddish(3),
		Hindu(4),
		Jewish(5),
		Christian(6),
		Orthodox(7),
		Jainism(8);
		
		private static Map<Object, Object> map = new HashMap<>();
		int value;
		
		private Religion(int n){ value = n; }
		public int getValue(){ return value; }
		
		static {
	        for (Religion religion : Religion.values()) {
				map.put(religion.getValue(), religion);
	        }
	    }

	    public static Religion valueOf(int pageType) {
	        return (Religion)map.get(pageType);
	    }
	};
	
	WebReference webref;
	String description;
	Recurrence recur;
	Religion religion;
	
	BigInteger created_at;
	BigInteger modified_at;
	
	public WebReference getWebref() {
		if(webref == null)
			webref = new WebReference();
		return webref;
	}
	public void setWebref(WebReference webref) {
		this.webref = webref;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Recurrence getRecur() {
		if(recur == null)
			recur = new Recurrence();
		return recur;
	}
	public void setRecur(Recurrence recur) {
		this.recur = recur;
	}
	public Religion getReligion() {
		return religion;
	}
	public void setReligion(Religion religion) {
		this.religion = religion;
	}
	public BigInteger getCreated_at() {
		return created_at;
	}
	public void setCreated_at(BigInteger created_at) {
		this.created_at = created_at;
	}
	public BigInteger getModified_at() {
		return modified_at;
	}
	public void setModified_at(BigInteger modified_at) {
		this.modified_at = modified_at;
	}
}
