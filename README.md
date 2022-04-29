# moviess


MOVIE THEATER SEATING ALLOCATION CHALLENGE

Overview: 

Given an empty Theater seating arrangement, allocate seats based on reservation requests that maximizes both public safety and customer satisfaction

I choose to leave buffer seats in the row.

Here I dynamically assign Rows and Columns and also number of seats to leave for public safety.

Programming Language Used: JAVA

Input: Through Command line with full path to the input file.
Output: Path to the Output file, movie theater layout and summary of seats booked


#assumptions
1) Customer Satisfaction:

@ The allocation of seats to the customers will start from middle row as it provides better screen visibility (optimal distance from the screen).
@ Customers who come as a group are made to sit in a single row or in the row above or below.

2) Public Safety

@ In order to ensure public safety, a buffer of 3 seats is given beside each group of size <=3
@ If the group size is greater than, divide it into subgroups of sizes <=3
@ In the group division, ensured that subgroup has at least size of 2

Assumptions:

1) The order of reservation requesting in the input file is sequential.
2) Since there is a buffer gap between customers, the total seating capacity in the theater cannot be attained maximum capacity.
3) If the group size is greater than the theater capacity, reservation is not processed.
4) Number of seats requested would be greater than 0. As zero or less than zero number of seats reservation is not valid scenario.


Algorithm:

I have 2 classes:

1) Booking.java :
This takes in the argument(input.txt) file from the command line
It calls the SeatingArrangement.java class with arguments rows, Columns and Covid norms distance and writes the output to the file.
In any case if the reservation is not successful, it will output the reason to the command line.

2) SeatingArrangement.java
This class contains the logic for seat allocation
@ The first row to be reserved is the middle row and it happens on a first come first serve basis.
@ If the group size is larger than the number of seats in the row, the seats are allocated in the nearest row adjacent to it.
@ Ensure that the public safety is maintained by having a 3 seat gap between customers
@ If seats are not available in the adjacent row, allocate seat wherever possible


Execution Steps:

@ Compile the Booking.java, SeatingArrangement.java, using command: javac Booking.java, SeatingArrangement.java TestCaseChecking.java
@ Run the input file using command: java Booking








 
