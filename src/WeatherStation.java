import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * ICS440 Spring 2019
 * @author Oussama El aallali
 *
 */

public class WeatherStation {

	private String Id;
	private float latitude;
	private float longitude;
	private float elevation;
	private String state;
	private String name;
	private static  List<WeatherStation> ListOfStations; //list to store the weather stations objects
	
	
	
	
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public float getElevation() {
		return elevation;
	}
	public void setElevation(float elevation) {
		this.elevation = elevation;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public static List<WeatherStation> getListOfStations() {
		return ListOfStations;
	}
	
	
	// a method to create all the weather stations in the file
	public void WeatherStationGen() throws FileNotFoundException{
		
		ListOfStations = new ArrayList<WeatherStation>();//Initialize the static list for the weather station objects
		
		File stationFile = new File("ghcnd-stations.txt");//path to the file
		
		Scanner scanFile = new Scanner(stationFile);//scanner for the file
		
		
		while(scanFile.hasNextLine()){//go through the entire file
			
			String line = scanFile.nextLine(); //get the next line in the file 
			
			WeatherStation WsObject = new WeatherStation();// create a new WeatherStation object
			
			//Assigning the values
			WsObject.Id = line.substring(0,11);
			WsObject.latitude = Float.valueOf(line.substring(12,20).trim());
			WsObject.longitude = Float.valueOf(line.substring(21,30).trim());
			WsObject.elevation = Float.valueOf(line.substring(31,37).trim());
			WsObject.state = line.substring(38,40);
			WsObject.name = line.substring(41,71);
			
			ListOfStations.add(WsObject);// adding the object to the weather station list
			
		}
		
		
		scanFile.close();
	}
	
	// a method that searches for a match of station id from a WeatherData object 
	//and return the index of corresponding index of the weather station  
	public int findStation(String id){
		
		int Index = 0;
		
		for(WeatherStation object : ListOfStations){
			
			if(ListOfStations.get(Index).Id.equalsIgnoreCase(id)){
				
				return Index;
				
			}
			
			else{
				
				Index++;
				continue;
			}
			
			
		}
		
		return Index;
	}
	
	@Override
	public String toString() {
		return "WeatherStation [Id=" + Id + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", elevation=" + elevation
				+ ", state=" + state + ", name=" + name + "]";
	}
		
}
