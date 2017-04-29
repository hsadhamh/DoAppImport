package factor.labs.indiancalendar.utils.json;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CurrentYearEvents {
	long id;
	String euid;
	Timestamp start_date;
	Timestamp end_date;
	long status;
	List<OverrideProperty> properties;
	
	public CurrentYearEvents(){
		id = 0;
		euid = "";
		start_date = new Timestamp(System.currentTimeMillis());
		end_date = new Timestamp(System.currentTimeMillis());;
		status = 0;
		properties = null;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEuid() {
		return euid;
	}
	public void setEuid(String euid) {
		this.euid = euid;
	}
	public Timestamp getStart_date() {
		return start_date;
	}
	public void setStart_date(Timestamp start_date) {
		this.start_date = start_date;
	}
	public Timestamp getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Timestamp end_date) {
		this.end_date = end_date;
	}
	public long getStatus() {
		return status;
	}
	public void setStatus(long status) {
		this.status = status;
	}
	public List<OverrideProperty> getProperties() {
		if(properties == null)
			properties = new ArrayList<OverrideProperty>();
		return properties;
	}
	public void setProperties(List<OverrideProperty> properties) {
		this.properties = properties;
	}
}
