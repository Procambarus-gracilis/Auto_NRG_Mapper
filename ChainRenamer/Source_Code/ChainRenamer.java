/* Last edited: May. 30, 2018
ANDROID EDITION */

/* This program re-names the 2 chains in ensembles resulting from MD simulations */

package edu.umn.EnsembleNRGMapper.llparker;

import java.io.FileWriter;
import java.io.*;
import java.util.*;

public class ChainRenamer
{
	public static void main(String[] args)
	{
		//Set filename
		String filename;
		String cwd = System.getProperty("user.dir");
		String line = null;

		if (args.length > 0){
			filename = args[0];
		}

		else {
			filename = cwd + File.separator + "input.pdb";
		}

		try{
			//read text files
			FileReader fileReader = new FileReader(filename);
			//always wrap filereader in buffered reader for some reason
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			PrintWriter writer = new PrintWriter(cwd + File.separator + "renamed_chains.pdb", "UTF-8");

			while ((line = bufferedReader.readLine()) != null){
				if(line.length() > 76){
					String substring1 = line.substring(0, 20);
					String substring2 = line.substring(20, 22); // Chain ID column
					String substring3 = line.substring(22, 75);
					String substring4 = line.substring(75, 76); // Chain ID recorded
					String substring5 = line.substring(76, 78);

					substring2 = substring4;

					//line = substring1 + substring2 + substring3 + substring4 + substring5;

					writer.println(substring1 + " " + substring2 + substring3 + substring4 + substring5);

					/* DIAGNOSTIC */
					//System.out.println(substring1 + substring2 + substring3 + substring4);
				}

				if((line.length() < 7) && (line.contains("END"))){
					writer.println(line);
				}
			} // End of 'while' loop

			//always close files & writer
			writer.close();
            fileReader.close();
            bufferedReader.close();
		} // End of 'try' block

		/* Catching exceptions */
		catch (FileNotFoundException ex){
			System.out.println("Unable to open file '" + filename + "'");
		} // End 'FileNotFoundException'

		catch (IOException ex) {
			System.out.println("Error reading file '" + filename + "'");
		} // End 'IOException'

	} // End 'main' method
} // End class 'ChainReNamer'
