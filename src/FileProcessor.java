import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * ICS440 Spring 2019
 * @author Oussama El aallali
 *
 */

public class FileProcessor {

	
	
	// A Concurrent linked list for each file to store the weatherData objects
	private ConcurrentLinkedQueue<weatherData> ListPerFile;
	
	// a Concurrent linked list to store the top 5 results from each file
	private ConcurrentLinkedQueue<weatherData> Result;

	
	// a method that takes one file and processes each line in it according to the input given to the method
	public ConcurrentLinkedQueue<weatherData> processOneFile(int startYear, int endYear, int startMonth, int endMonth, String 	tempValue, File file) 
			throws FileNotFoundException{

		//create a list for each file
		this.ListPerFile = new ConcurrentLinkedQueue<weatherData>();
		

		//scanner for each line in the file
		Scanner scan = new Scanner(file);


		// a while loop that goes through every line in the the file and makes a weatherData object according to the user input (year, month,element)
		
		while (scan.hasNextLine()){


			String curLine = scan.nextLine();

			int days =  (curLine.length() - 21) / 8;
			int month = Integer.valueOf(curLine.substring(15,17).trim());
			int year = Integer.valueOf(curLine.substring(11,15).trim());
			String id = curLine.substring(0,11);
			String element = curLine.substring(17,21);

			if((startYear<=year && endYear>=year)
			  &&(startMonth<=month && endMonth>=month)
			  && (element.equalsIgnoreCase(tempValue))){//checking if the next line meets the query requirements

				//for loop that goes over each line and extract data for 30 days and makes an object of weatherData
				for(int j =0;j<days;j++){

					String qflag = curLine.substring(27+8*j,28+8*j);

					if(qflag.equalsIgnoreCase(" ")){ continue;}

					weatherData wData = new weatherData();//new weatherData object

					wData.setDay(j+1);
					wData.setMonth(month);
					wData.setYear(year);
					wData.setStationId(id);
					wData.setQflag(qflag);
					wData.setElement(element);
					float value =(Integer.valueOf(curLine.substring(21+8*j,26+8*j).trim()));
					wData.setValue(value/100*10);

					this.ListPerFile.add(wData);// add the object to the list of the current file
					
		

				}
				
				


			}

		}	
			
			
		
		return this.ListPerFile;//return the list of the processed file
	}

	// A method that takes the big list that contains smaller lists from each file and find the top 5 results for each list in the big list
	public  ConcurrentLinkedQueue<weatherData> getTopFive(ConcurrentLinkedQueue<ConcurrentLinkedQueue<weatherData>> bigList)
			throws InterruptedException, ExecutionException{
		
		weatherData current;// pointer to the current object in the list
		
		weatherData dummy = new weatherData();//a dummy weatherData object to initialize the top 5 object's value  to either .MIN_VALUE or .max_VALUE
		
		weatherData  first,second,third,fourth,fifth;// 5 variables the present the top five results
		
		
		this.Result = new ConcurrentLinkedQueue<weatherData>();// Initialize a list for each caller(feature) to this method 
		
		
		
	while(!bigList.isEmpty()){//go through each list in the big list
		
		boolean wasEmpty = true;//keeping a boolean variable in case if the a small list was empty thus preventing from adding the dummy object to the result list
		
		first=dummy;second = dummy;third=dummy;fourth=dummy;fifth=dummy;//Initializing  
		
		while(!bigList.peek().isEmpty()){//go through each small list 
			
			wasEmpty=false;// indicate that the list was not empty
			
			current=bigList.peek().peek();// take a peek at the head of big list and then a peek the head of the small list and initialize current to the
										  // object in head of the current small list 	
			
			//This block will be executed only if the query specify  maximum temperature
			if(current.getElement().equalsIgnoreCase("tmax")){
						
				dummy.setValue(Integer.MIN_VALUE);// set the dummy's value to  MIN_VALUE
				
				if(current.getValue()>first.getValue()){fifth=fourth;fourth=third;third=second;second=first;first=current;}
				
				else if(current.getValue()>second.getValue()){fifth=fourth;fourth=third;third=second;second=current;}
				
				else if(current.getValue()>third.getValue()){fifth=fourth;fourth=third=current;}
				
				else if(current.getValue()>fourth.getValue()){fifth=fourth;fourth=current;}
				
				else if(current.getValue()>fifth.getValue()){fifth=current;}
				
				bigList.peek().remove(); //REMOVE THE HEAD OF THE BIG LIST (work for one list is done at this point)
				
				}
			
			//This block will be executed only if the query specifies the minimum temperature
			else if(current.getElement().equalsIgnoreCase("tmin")){
				
				dummy.setValue(Integer.MAX_VALUE); //set the dummy's value to  MAX_VALUE
				
				// check and set first if current is smaller than first
				if(current.getValue()<first.getValue()){fifth=fourth;fourth=third;third=second;second=first;first=current;}
				
				//check and set second if current is smaller than second
				else if(current.getValue()<second.getValue()){fifth=fourth;fourth=third;third=second;second=current;}
				
				//check and set third if current is smaller than third
				else if(current.getValue()<third.getValue()){fifth=fourth;fourth=third=current;}
				
				//check and set fourth if current is smaller than fourth
				else if(current.getValue()<fourth.getValue()){fifth=fourth;fourth=current;}
				
				//check and set fifth if current is smaller than fifth
				else if(current.getValue()<fifth.getValue()){fifth=current;}
				bigList.peek().remove(); //REMOVE THE HEAD OF THE BIG LIST (work for one list is done at this point)
				
			}
			
			
		}
		
		// if the list was empty skip this block else add first, second,third,fourth,fifth to the result list
		
		if(!wasEmpty){
			
			
		Result.add(first);
		if(first.getValue()!=second.getValue()&& !second.equals(dummy)){Result.add(second);}
		if(second.getValue()!=third.getValue()&& !third.equals(dummy)){Result.add(third);}
		if(third.getValue()!=fourth.getValue()&& !fourth.equals(dummy)){Result.add(fourth);}
		if(fourth.getValue()!=fifth.getValue()&& !fifth.equals(dummy)){Result.add(fifth);}
		}
		
		bigList.remove();// //REMOVE THE HEAD OF THE BIG LIST (work for one list is done at this point)
		
		
		
		}
	
	
	
	return Result; 
	}

	
}
