package factor.labs.indiancalendar.utils.json;

import java.math.BigInteger;

public class Location {
	String HouseNo;
	String StreetName;
	String AreaName;
	String city;
	String state;
	String Country;
	BigInteger ZipCode;
	public String getHouseNo() {
		return HouseNo;
	}
	public void setHouseNo(String houseNo) {
		HouseNo = houseNo;
	}
	public String getStreetName() {
		return StreetName;
	}
	public void setStreetName(String streetName) {
		StreetName = streetName;
	}
	public String getAreaName() {
		return AreaName;
	}
	public void setAreaName(String areaName) {
		AreaName = areaName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	public BigInteger getZipCode() {
		return ZipCode;
	}
	public void setZipCode(BigInteger zipCode) {
		ZipCode = zipCode;
	}
	
}
