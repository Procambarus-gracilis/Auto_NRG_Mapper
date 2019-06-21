/* Author: Oscar Bastidas
Last edited: Mar. 14, 2018
ANDROID EDITION */

/* This program calculates RMSDs for SINGLE CHAIN SYSTEM CAs per-residue basis ONLY */

//package edu.umn.EnsembleNRGMapper.llparker; --> wrong package/directory path for termux

//import java.util.*; --> Not necessary AND won't compile in termux-based java
//import java.io.FileWriter; --> Not necessary AND won't compile in termux-based java
import java.io.*;

public class RMSDCalculatorA
{
	public static void main(String[] args)
	{
		//Set filename
        String filename;
        String cwd = System.getProperty("user.dir");
        String line = null;

        if (args.length > 0){
            filename = args[0];
        } else {
            filename = cwd + "input.csv";
        }

		String space = " ";
		char ignore_space = space.charAt(0);

		/* Character counters for individual character analysis in a line being analyzed */
		int x_counter = 0; /* when this variable is taken into a 'try' block, it must be re-initialized */
		int y_counter = 0;
		int z_counter = 0;

			try{
				//read text files
				FileReader fileReader = new FileReader(filename);
				//always wrap filereader in buffered reader for some reason
				BufferedReader bufferedReader = new BufferedReader(fileReader);

				/* First determine index values for the arrays */
				int SSCounter = 0;
				int CACounter_x = 0;
				int CACounter_y = 0;
				int CACounter_z = 0;
				String search1 = "CA";

				while ((line = bufferedReader.readLine()) != null){

					/* Outer index */
					if(line.contains("END") || line.contains("ENDMDL")){
						SSCounter = SSCounter +1;
					}

					/* Inner index */
// PROPOSED CHANGE: if((line.contains("ATOM") && line.contains(search1)) || (line.contains("HETATM") && line.contains(search1))
// OLD: if (line.contains("ATOM") && line.contains(search1))
					if((line.contains("ATOM") && line.contains(search1)) || (line.contains("HETATM") && line.contains(search1))){
						CACounter_x = CACounter_x +1;
					}

				}

				/* Always close file readers */
				fileReader.close();
				bufferedReader.close();

				int inner_index = CACounter_x;

				/* DIAGNOSTICS */
				//System.out.println("inner index value: " + inner_index);

				double [] x_coord = new double[inner_index];
				double [] y_coord = new double[inner_index];
				double [] z_coord = new double[inner_index];

				/* Now actually populate above arrays */
				CACounter_x = 0;	/* Reset CACounter_x back to '0' */

				FileReader fileReader2 = new FileReader(filename);
				BufferedReader bufferedReader2 = new BufferedReader(fileReader2);
				while ((line = bufferedReader2.readLine()) != null){

					/* Identifying CA and its x, y and z coordinate values */
// PROPOSED CHANGE: if((line.contains("ATOM") && line.contains(search1)) || (line.contains("HETATM") && line.contains(search1))
					if((line.contains("ATOM") && line.contains(search1)) || (line.contains("HETATM") && line.contains(search1))){
						/* Chop up line to isolate x, y and z coordinate values */
						String str_x = line.substring(27,38); /* This is the CA x coordinate */
						String str_y = line.substring(38,46); /* This is the CA y coordinate */
						String str_z = line.substring(46,54); /* This is the CA z coordinate */

/*************************************** Storing X Coordinates **************************************/
						/* READING X-COORDINATE: Right-justified numerical data; eliminate
						leading blank columns */
						int x_length = str_x.length(); /* New substring's UPPER BOUND */
						int x_space_checker = 0;
						x_counter = 0;

						/* Establishing new substring's LOWER BOUND ---> x_counter */
						while(x_space_checker == 0){
							if(str_x.charAt(x_counter) == ignore_space){
								x_counter = x_counter + 1;
							}
							else{
								x_space_checker = 1;
							}
						}
						String sub_str_x = str_x.substring(x_counter,x_length);

						/* CONVERTING X COORDINATE value from string to double */
						double x_coordinate_numerical_value = Double.parseDouble(sub_str_x);
						//double x_coordinate_numerical_value = new Double(sub_str_x).doubleValue();
						//x_coordinate_numerical_value = new bigDecimal(sub_str_x).doubleValue();

						/* Store x_coordinate_numerical_value in its appropriate array */
						x_coord[CACounter_x] = x_coordinate_numerical_value;


						/* DIAGNOSTICS */
						//System.out.println("Each x: " + x_coord[CACounter_x]);

						CACounter_x = CACounter_x + 1;
/****************************************** END X Coordinates *****************************************/

/*************************************** Storing Y Coordinates ****************************************/
						/* READING Y-COORDINATE: Right-justified numerical data; eliminate
						leading blank columns */
						int y_length = str_y.length(); /* New substring's UPPER BOUND */
						int y_space_checker = 0;
						y_counter = 0;

						/* Establishing new substring's LOWER BOUND ---> y_counter */
						while(y_space_checker == 0){
							if(str_y.charAt(y_counter) == ignore_space){
								y_counter = y_counter + 1;
							}
							else{
								y_space_checker = 1;
							}
						}
						String sub_str_y = str_y.substring(y_counter,y_length);

						/* CONVERTING Y COORDINATE value from string to double */
						double y_coordinate_numerical_value = Double.parseDouble(sub_str_y);
						//double y_coordinate_numerical_value = new Double(sub_str_y).doubleValue();

						/* Store y_coordinate_numerical_value in its appropriate array */
						y_coord[CACounter_y] = y_coordinate_numerical_value;


						/* DIAGNOSTICS */
						//System.out.println("Each y: " + y_coord[CACounter_y]);

						CACounter_y = CACounter_y + 1;
/****************************************** END Y Coordinates *****************************************/

/*************************************** Storing Z Coordinates ****************************************/
						/* READING Z-COORDINATE: Right-justified numerical data; eliminate
						leading blank columns */
						int z_length = str_z.length(); /* New substring's UPPER BOUND */
						int z_space_checker = 0;
						z_counter = 0;

						/* Establishing new substring's LOWER BOUND ---> z_counter */
						while(z_space_checker == 0){
							if(str_z.charAt(z_counter) == ignore_space){
								z_counter = z_counter + 1;
							}
							else{
								z_space_checker = 1;
							}
						}
						String sub_str_z = str_z.substring(z_counter,z_length);

						/* CONVERTING Z COORDINATE value from string to double */
						double z_coordinate_numerical_value = Double.parseDouble(sub_str_z);
						//double z_coordinate_numerical_value = new Double(sub_str_z).doubleValue();

						/* Store z_coordinate_numerical_value in its appropriate array */
						z_coord[CACounter_z] = z_coordinate_numerical_value;


						/* DIAGNOSTICS */
						//System.out.println("Each z: " + z_coord[CACounter_z]);

						CACounter_z = CACounter_z + 1;
/****************************************** END Z Coordinates *****************************************/
					} /* End 'if' block that checks if characters of interest are found in the line */

				} /* End 'while' loop that reads and checks a line of the *.pdb file */

				/* DIAGNOSTICS */
				//System.out.println("SS counter: " + (SSCounter));
				//System.out.println("CACounter_x: " + (CACounter_x));
				//System.out.println("CACounter_y: " + (CACounter_y));
				//System.out.println("CACounter_z: " + (CACounter_z));
				//System.out.println("Print select array element: " + x_coord[CACounter_x - 1]);

/*************************************** DOING RMSD ARITHMETIC ****************************************/
				/* INSIDE OF 'try' BLOCK */
				int Num_to_Add = CACounter_x/SSCounter; /* Only one CACounter is needed since all CACounters are the same value and Num_to_Add's value applies to all coordinate species */

				/* X_COORD DIFFERENCE SUMS */
				double [][] CA = new double [Num_to_Add + 1][SSCounter];
				int i = 0, j = 0;

				for(i = 0; i < Num_to_Add ; i++){
					for(j = 0; j <= (SSCounter - 1); j++){
						CA[i][j] = ((x_coord[i] - x_coord[((j) * Num_to_Add) + (i)]) * (x_coord[i] - x_coord[((j) * Num_to_Add) + (i)])) + ((y_coord[i] - y_coord[((j) * Num_to_Add) + (i)]) * (y_coord[i] - y_coord[((j) * Num_to_Add) + (i)])) + ((z_coord[i] - z_coord[((j) * Num_to_Add) + (i)]) * (z_coord[i] - z_coord[((j) * Num_to_Add) + (i)]));

					/* DIAGNOSTIC */
					//System.out.println("DIAGNOSTIC-CA: " + CA[i][j] + " mom");
					}
				}


				/* Summing the appropriate CA values from the wholesale CA sums above */
				double CA_Sum = 0;
				double RMSD = 0;

				/* Preparing file writer to write results */
				PrintWriter writer = new PrintWriter(cwd + File.separator + "RMSD-A.txt", "UTF-8");

				for(i = 0; i < Num_to_Add; i++){
					for(j = 0; j <= (SSCounter - 1); j++){
						CA_Sum = CA_Sum + CA[i][j];
					}
					/* Print out the results of the arithmetic */
					RMSD = Math.sqrt(CA_Sum/SSCounter);
					writer.println(Double.toString(RMSD));
					/* DIAGNOSTIC */
					//System.out.println("RMSD value of CA# " + i + " is:" + RMSD);
					CA_Sum = 0;
				}

				//always close files & writer
         		writer.close();
				fileReader2.close();
            	bufferedReader2.close();

				/* DIAGNOSTIC */
				//System.out.println("Number of CAs in any given SS:" + Num_to_Add);

			} /* END OF TRY BLOCK */

			/* Catching exceptions */
			catch (FileNotFoundException ex){
				System.out.println("Unable to open file '" + filename + "'");
			}

			catch (IOException ex) {
				System.out.println("Error reading file '" + filename + "'");
			}

	}
}
