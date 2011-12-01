package repast.simphony.space.gis;

public class Car {
	private double speed;
	private double direction;
	private String color;
	private String make;
	private String model;
	private int year;
	
	public Car(String make, String model, int year, String color){
		this.make = make;
		this.model = model;
		this.year = year;
		this.color = color;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public double getDirection() {
		return direction;
	}
	public void setDirection(double direction) {
		this.direction = direction;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	

}
