import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SeatingArrangement {
	int total_rows;
	int total_columns ;
	int total_remainingSeats ;
	int total_no_of_customers;
	String[][] seats ;
	LinkedHashMap<String, ArrayList<String>> seatmap;
	int covidSpace;
	public SeatingArrangement(Integer r,Integer c,Integer k){
		total_rows = r;
		total_columns = c;
		seats = new String[r][c];
		total_no_of_customers =0;
		total_remainingSeats = r*c;
		seatmap = new LinkedHashMap<>();
		covidSpace = k;
	}
	//returns total number of remaining seats in the theatre
	public int getRemainingSeats(){
		return total_remainingSeats;
	}

	//Printing the layout of the Movie Theatre
	public void seating_layout() {
		System.out.println("[-------------------------------------- Screen ------------------------------------]");
		String padString = "    ";

		for(int i =0;i<total_rows;i++)
		{
			System.out.print((char) (i + 65) + "     ");
			for(int j=0;j<total_columns;j++)
			{
			    if(seats[i][j]==null)
			    {
			        System.out.print("XXXX");
			    }else
			    {
				String padded = (padString.substring(0, padString.length() - seats[i][j].length())) + seats[i][j];
				System.out.print(padded);
				
			    }
			    System.out.print(" ");
			}
			System.out.println();
		}
	}

	Boolean reservationIdValidation(String id){
		if(id.charAt(0)!='R'){
			return false;
		}
		for(int i = 1;i<id.length();i++){
			if(id.charAt(i)<'0' || id.charAt(i)>'9'){
				return false;
			}
		}
		return true;
	}	
	
	//Check the RequestID and number of seats to reserved are valid or not
	//Calls getGroups() method and creates subgroups for the number of seats to be allocated
	//For each subgroup reserve tickets
	//If for any subgroup, we are not able to assign seats, revert the allocated seats to the previous subgroups of the same reservation number
	public int reserveBooking(String reservation) throws Exception{
		String[] booking = reservation.split(" ");
		if(booking.length!=2){
			throw new Exception("Request in wrong format"); 
		}
		//store reservation request ID
		String reservation_number = booking[0];
		if(reservationIdValidation(reservation_number) == false){
			throw new Exception("Request id in wrong format"); 
		}
		//store no. of seats needed

		int seats_to_reserve;
		try{
			seats_to_reserve= Integer.parseInt(booking[1]);
		}catch(Exception e){
			throw new Exception("Requested number of seats are in wrong format"); 
		}
		
		if(seats_to_reserve <=0){
			throw new Exception("Requested number of seats is not valid number"); 
		}

		if (seats_to_reserve>getRemainingSeats()) {
			throw new Exception("Requested seats are greater than avaliable seats"); 
		}


		//
		int result=0;
		
		
		total_no_of_customers += seats_to_reserve;

		//subdivide a group into smaller subgroups
		List<Integer> groups = new ArrayList<>();
		getGroups(seats_to_reserve,groups);
		
		//System.out.println("---------------");
		//for each subgroup, assign seats
		for(int group:groups){
			//System.out.println("-- "+i);
			if(seats_assign(reservation_number, group) != group){
				//System.out.println("///"+i);
				total_no_of_customers -= seats_to_reserve;
				revertChanges(reservation_number);
				seatmap.remove(reservation_number);
				throw new Exception("Requested seats are greater than avaliable seats to accomudate"); 
			}
		}
		//returns the total number of seats allocated for a single booking
		return result;
	}
	private void revertChanges(String id){
		for(int i =0;i<total_rows;i++){
			for(int j=0;j<total_columns;j++){
				if(seats[i][j] == id){
					seats[i][j] = null;
					total_remainingSeats--;
				}
			}
		}
		return ;
	}
	//method to divide a group into subgroups
	private void getGroups(int seats_to_reserve, List<Integer> groups ) {
		//switch case
		switch(seats_to_reserve) {
			case 1:
				groups.add(1);
				break;
			case 2:
				groups.add(2);
				break;
			case 3:
				groups.add(3);
				break;
			case 4:
				groups.add(2);
				groups.add(2);
				break;
			case 5:
				groups.add(3);
				groups.add(2);
				break;
			case 6:
				groups.add(3);
				groups.add(3);
				break;
			default:
				groups.add(3);
				getGroups(seats_to_reserve-3,groups);
		}
		return ;
	}
	private int findKSeatsEmpty(int row,int kSeats){
		

		for(int i=0;i<total_columns;i++){
			
			if(seats[row][i]==null){
				
				int preIndex = i-1;
				int count = 0;
				while(preIndex>=0 && seats[row][preIndex] == null && count<covidSpace){
					preIndex--;
					count++;
					
					// 1 1 0 0 0 0
				}
			

				if(count== covidSpace || preIndex<0){
					if(count!=covidSpace)
					count = 0;
					
					preIndex = i;
					while(preIndex<total_columns && seats[row][preIndex]==null && count<covidSpace+kSeats){
						preIndex++;
						count++;
					}
							

					if(count == covidSpace+kSeats){
									
						return i;
					}
				}
			}
		}
	
		return -1;
	}

	// sending the row
	private int[] getSeatsIndex(int number_of_seatsToBook){
		int countOfRows = 1;
		boolean flagForNextRows = true;
		int current_row = (total_rows / 2) - 1;
		int index = -1;
		while(current_row>=0 && current_row<total_rows){
			 index = findKSeatsEmpty(current_row, number_of_seatsToBook);
			 
			 // 4 5 3 6 2 7 1 8 0 9
			if(index == -1){
				if (flagForNextRows) {
					//5 - 6 - 7 - 8 - 9
					current_row = current_row + countOfRows;
					countOfRows++;
					flagForNextRows = false;
				}
				else {
					//- 3 - 2 - 1 - 0   
					current_row = current_row - countOfRows;
					countOfRows++;
					flagForNextRows = true;
				}
			}else{
				break;
			}
		}
		int[] indexArray = {-1,-1};
		if(current_row<0 || current_row>=total_rows){
			return indexArray;
		}
	

		indexArray[0] = current_row;
		indexArray[1] = index;
		return indexArray;
	}
    //Get the index of start column of a row and fills the seats matrix, also adds elements to the seatmap HashMap 
	private int seats_assign(String reservation_number, int number_of_seatsToBook) {
		

			int[] indexs = getSeatsIndex(number_of_seatsToBook);
			if(indexs[0]== -1){
				return -1;
			}else{
				
				for(int c = 0;c<number_of_seatsToBook;c++){
					seats[indexs[0]][indexs[1]+c] = reservation_number;
					total_remainingSeats++;
					if (!seatmap.containsKey(reservation_number)) {
						ArrayList<String> list = new ArrayList<>();
						seatmap.put(reservation_number, list);
					} 
					seatmap.get(reservation_number).add(
							(char) (indexs[0] + 65) + Integer.toString(c + indexs[1]+1));
					
					
				}
				return number_of_seatsToBook;
			}
			
			
		}
				
		
	

	public LinkedHashMap<String, ArrayList<String>> getSeatList() {
		//return a map of reservation ids and the list of tickets
		return seatmap;
	}

	//printing theatre summary of bookings
	public void info() {
		System.out.println("---------------------Summary-----------------------");
		System.out.println("Total tickets booked in the theatre: " + total_no_of_customers);
		System.out.println("Total number of group_bookings: " + seatmap.size());
		
	}
}