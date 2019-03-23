/**
 * ICS440 Spring 2019
 * @author Oussama El aallali
 *
 */
public class weatherData  implements Comparable <weatherData>{
	
	
	private String StationId;
	private int year;
	private int month;
	private int day;
	private float value;
	private String element;
	private String qflag;
	
	
	public String getStationId() {
		return StationId;
	}
	public void setStationId(String stationId) {
		StationId = stationId;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getQflag() {
		return qflag;
	}
	public void setQflag(String qflag) {
		this.qflag = qflag;
	}
	
	//compareTo method used to sort the final list 
	@Override
	public int compareTo(weatherData arg0) {
		
		Float x  = (float) this.value;
		
		Float y = (float) arg0.value;
		
		return (y.compareTo(x));
	}
	@Override
	public String toString() {
		return "weatherData [StationId=" + StationId + ", year=" + year
				+ ", month=" + month + ", day=" + day + ", value=" + value
				+ ", element=" + element + ", qflag=" + qflag + "]";
	}
	
	
	
	
	
	
	

}
