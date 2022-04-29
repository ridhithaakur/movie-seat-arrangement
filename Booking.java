import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Booking {

	//Method to write output to a text file using BufferedWriter
	public static void writeToFile(LinkedHashMap<String, ArrayList<String>> map) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("output.txt"));
			Iterator<Entry<String, ArrayList<String>>> itr = map.entrySet()
					.iterator();
			while (itr.hasNext()) {
				Entry<String, ArrayList<String>> pairs = itr.next();
				String list = Arrays.toString(pairs.getValue().toArray()).replace("[", "").replace("]", "");

				//Output form - Reservation ID and list of seat numbers
				String str = pairs.getKey() + " " + list;
				bw.write(str + "\n");
			}
			bw.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		if (args.length > 0) {
			SeatingArrangement SeatingArrangement = new SeatingArrangement(10,20,3);

			try {
				//args[0] - Input file containing Reservation ID and no. of seats needed
				//consider each line as a reservation request
				File file = new File(args[0]);
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);

				//Read each line of input file
				String reservation = bufferedReader.readLine();

				while (reservation != null) {
					//for each reservation request, call reserveBooking() method
					try{
						SeatingArrangement.reserveBooking(reservation);
					}catch(Exception e){
						System.out.println(" ***********  For  "+reservation+"   OutPut is:  "+e.getMessage()+" ********** ");
					}
					
					reservation = bufferedReader.readLine();
				}

				// Write the output_file to the file
				writeToFile(SeatingArrangement.getSeatList());
				//print theatre seating arrangement layout
       	        SeatingArrangement.seating_layout();

				//print theatre summary information
				SeatingArrangement.info();

				
			
			} catch (FileNotFoundException f) {
				System.err.println("Input file not Found.");
				f.printStackTrace();
				System.exit(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
