package factor.labs.indiancalendar.utils.json;

public class OverrideProperty {
	long flag; // what property to over-ride.
	EventProperty property;
	Location location;
	
	public long getFlag() {
		return flag;
	}
	public void setFlag(long flag) {
		this.flag = flag;
	}
	public EventProperty getProperty() {
		return property;
	}
	public void setProperty(EventProperty property) {
		this.property = property;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
}
