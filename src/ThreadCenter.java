import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * ICS440 Spring 2019
 * @author Oussama El aallali
 *
 */
public class ThreadCenter implements Callable<ConcurrentLinkedQueue<weatherData>> {
	

	private static FileProcessor run = new FileProcessor();// instance of FileProcessor
	
	private static int sYear,eYear,sMonth,eMonth; static String elem; // variables for user input
	
	private  static ConcurrentLinkedQueue<File> Filesqueue =
	new ConcurrentLinkedQueue<File>(Arrays.asList(new File("Data files").listFiles()));//Concurrent list for storing the path to each file

	
	private static ConcurrentLinkedQueue<ConcurrentLinkedQueue<weatherData>> bigList = //big list for storing  weatherData objects from all the files
	new ConcurrentLinkedQueue<ConcurrentLinkedQueue<weatherData>>();
	
	private static int TaskNum=1;// this variable will help split the tasks for the call() method (more detail on this in the call method) 

	
	private static List<weatherData> finalList = new ArrayList<weatherData>();//final list that contains the top 5 results from each file
	
	//call method that each feature will call once 
	
	@Override
	public ConcurrentLinkedQueue<weatherData> call() throws Exception {
		
	// task 1 indicates the first task which is creating as many features as files 
	if(TaskNum==1){	
		
		
		
		
		System.out.println(Filesqueue.size()+" files processed");
		
	return run.processOneFile(sYear,eYear,sMonth,eMonth,elem,Filesqueue.remove());}//call processOneFile() to process one file and get  the returned list
	
	
	// task 2 indicates the second task which is generating 4 new features each will make a call
	//to getTopFive to get top 5 results from each small list in the big list
	else if(TaskNum==2){
		
		return run.getTopFive(bigList);}
	
	
	else return null;
	}
	
	// a method that prints the top 5 temperatures 
	public static void printTopFive(ArrayList<weatherData> FnList) throws FileNotFoundException{
		
		try{
		
			WeatherStation test = new WeatherStation();// new object for weather stations object
			test.WeatherStationGen();// generate the list of stations from the file(check WeatherStation class for more info on how it's methods operate)
			
			int k =0;
			while(k < 5){//get only the object with the highest or lowest temperatures
				
				if(k!=0 && FnList.get(k).getValue() ==FnList.get(k-1).getValue()){//condition to handle objects that have the same temperature values
				
					FnList.remove(k);continue;}//remove the object with the same value(there exist weatherData objects with different station id, year, and month 
												// but have the same temp values)
				
				else{
				System.out.println(FnList.get(k).toString());}// print K-th weatherData object
				
				int index = test.findStation(FnList.get(k).getStationId());//Get the index of station corresponding to the k-th 
																		// weatherData object(check findStation() for more  Details on how it operates)
				
				System.out.println(WeatherStation.getListOfStations().get(index).toString()+"\n");//print the station object
				
				
				k++;
			
			}
			
		}
		// Condition to handle the case where there are less than 5 results avoiding null pointer exception
		catch(IndexOutOfBoundsException e){
			
			System.out.println("There are only " + FnList.size()+" Results");
			System.exit(0);
		}
	}

	
	public static void main(String args[]) throws InterruptedException, ExecutionException, FileNotFoundException{

		
		Scanner reader = new Scanner(System.in);// scanner for user input
		
		//Initializing user input
		System.out.print("Enter starting year: "); sYear=reader.nextInt();
		System.out.print("Enter ending year: "); eYear=reader.nextInt();
		System.out.print("Enter starting month: "); sMonth=reader.nextInt();
		System.out.print("Enter ending month: "); eMonth=reader.nextInt();
		System.out.print("Enter tmin for minimum temp or tmax for maximum temp: "); elem=reader.next();	
		
		reader.close();
		
	ExecutorService executor = Executors.newFixedThreadPool(10);//creating a thread pool with 10 threads in it
	
	
	
	Callable<ConcurrentLinkedQueue<weatherData>> callable = new ThreadCenter();
	
	TaskNum=1;//starting point of the first task(file processing)
	

	
	while(!Filesqueue.isEmpty()){
		Future<ConcurrentLinkedQueue<weatherData>> futureSetOne = executor.submit(callable);//create one future for each file and submit a callable to a thread 
		
		
		
		bigList.add(futureSetOne.get());// add the returned list by each future to the bigList
		
		
		}
	// starting point of second task (getting the top 5 result in each list in the big list)
	TaskNum=2;
	
		for(int k = 0; k < 4 ; k++){//for loop to generate the four new futures
			
			
			
			Future<ConcurrentLinkedQueue<weatherData>> futureSetTwo = executor.submit(callable);//create a feature and submit a callable to a thread to get the top 5 results
		
			
		
			while(!futureSetTwo.get().isEmpty()){// go through the list that the future has returned
				
				finalList.add(futureSetTwo.get().remove());// add the head of the list to the final result list
				
				//sort the final list based on the desired query
				if(finalList.get(0).getElement().equalsIgnoreCase("tmin")){
					Collections.sort(finalList, Collections.reverseOrder());}// lowest to highest
				
				else{Collections.sort(finalList);}// highest to lowest
				
			}
			
			
		}
		
		executor.shutdown();// finish the thread work 
	
		printTopFive((ArrayList<weatherData>) finalList);// use one thread to print the top 5
		
	
	}

	

}