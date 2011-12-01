package repast.simphony.space.gis;

public class House {
	String address;

	String city;

	String state;

	String owner;

	public House(String owner, String address, String city, String state) {
		this.address = address;
		this.city = city;
		this.state = state;
		this.owner = owner;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
