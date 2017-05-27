package factor.labs.indiancalendar.utils.json;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Event {
	long id;
	String euid;
	String name;
	EventProperty property;
	long category;
	long sub_category;
	List<Location> locations;
	long flags;
	List<String> Tags;
	BigInteger start_date;
	BigInteger end_date;
	
	public Event() {
		super();
		id = 0;
		euid = "";
		name = "";
		property = null;
		category = 0;
		sub_category = 0;
		locations = null;
		flags = 0;
		Tags = null;
		start_date = BigInteger.ZERO;
		end_date = BigInteger.ZERO;
		
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public EventProperty getProperty() {
		if(property == null)
			property = new EventProperty();
		return property;
	}
	public void setProperty(EventProperty property) {
		this.property = property;
	}
	public long getCategory() {
		return category;
	}
	public void setCategory(long category) {
		this.category = category;
	}
	public long getSub_category() {
		return sub_category;
	}
	public void setSub_category(long sub_category) {
		this.sub_category = sub_category;
	}
	public List<Location> getLocations() {
		if(locations == null)
			locations = new ArrayList<Location>();
		return locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
	public long getFlags() {
		return flags;
	}
	public void setFlags(long flags) {
		this.flags = flags;
	}
	public List<String> getTags() {
		if(Tags == null)
			Tags = new ArrayList<String>();
		return Tags;
	}
	public void setTags(List<String> tags) {
		Tags = tags;
	}
	public BigInteger getStart_date() {
		return start_date;
	}
	public void setStart_date(BigInteger start_date) {
		this.start_date = start_date;
	}
	public BigInteger getEnd_date() {
		return end_date;
	}
	public void setEnd_date(BigInteger end_date) {
		this.end_date = end_date;
	}
}
