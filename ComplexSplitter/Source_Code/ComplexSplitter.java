/* Author: Oscar Bastidas
Last edited: Mar. 25, 2018
ANDROID EDITION */

/* This program splits two chains of a multi-chain ensemble system apart and writes each chain to its own *.pdb file for further RMSD analysis */

package edu.umn.EnsembleNRGMapper.llparker;

//import java.io.IOException; ---> required libraries for standard Java
//import java.nio.file.Files; ---> required libraries for standard Java
import java.io.FileWriter;
import java.io.*;
import java.util.*;

public class ComplexSplitter
{
	public static void main(String[] args)
	{
		//Set filename
		String filename;
		String cwd = System.getProperty("user.dir");
		String ChainA, ChainB;
		String line = null;

		if (args.length > 0){
            filename = args[0];
            ChainA = args[1];
            ChainB = args[2];
        } else {
            filename = cwd + "input.csv";
            ChainA = "A";
            ChainB = "B";
        }

			try{
				/* First see if ensemble comes from NMR and if it contains the 'END' keyword; the 'END' keyword at the very end of NMR-derived ensemble structure files MUST be deleted as the keyword interferes with the RMSD calculations carried out later */
				FileReader EndKiller = new FileReader(filename);
				BufferedReader EndKillerBuffer = new BufferedReader(EndKiller);

				int NMRChecker = 0;

				while ((line = EndKillerBuffer.readLine()) != null){
					if(line.contains("EXPDTA") && line.contains("NMR")){
						NMRChecker = 1;
					}
				}

				line = null;

				EndKiller.close();
				EndKillerBuffer.close();

				// This scenario establishes that ensemble comes from NMR experiments
				if(NMRChecker == 1){

				/* Next create a new *.pdb file lacking the 'END' keyword if the ensemble comes from NMR */
				FileReader EndKiller2 = new FileReader(filename);
				BufferedReader EndKillerBuffer2 = new BufferedReader(EndKiller2);
				PrintWriter ToNewFile = new PrintWriter(cwd + File.separator + "NewFile.pdb", "UTF-8");

				// Sets 'filename' equal to the new file created above, which is created if and only if the
				// originally input ensemble came from NMR
				filename = cwd + File.separator + "NewFile.pdb";

					while((line = EndKillerBuffer2.readLine()) != null){
						if(line.equals("END") || line.contains("END ")){
							line = "";
						}

						ToNewFile.println(line);
					}

				/* Close file writers and readers */
				ToNewFile.close();
				EndKiller2.close();
				EndKillerBuffer2.close();

				}

				line = null;

				/* Count number of snapshots */
				//read text files
				// At this point, the 'filename' variable either contains the address to the original pdb input file (MD)
				// or the address to the newly created file which lacks the problematic 'END' (NMR)-either way, 'filename'
				// is now the address to what is to be considered the master PDB file
				FileReader fileReader = new FileReader(filename);
				//always wrap filereader in buffered reader for some reason
				BufferedReader bufferedReader = new BufferedReader(fileReader);

				/* First count how many SS are in master *.pdb file */
				int SSCounter = 0;

				while ((line = bufferedReader.readLine()) != null){

					if(line.matches("END.*") || line.matches("ENDMDL.*")){
						SSCounter = SSCounter +1;
					}

				}

				line = null;

				/* Always close file readers */
				fileReader.close();
				bufferedReader.close();

/* *********************** STRIPPING CHAINS & WRITING EACH TO ITS OWN FILE ************************** */
/* ************************************** STRIPPING CHAIN A ***************************************** */
			/* Reset 'line'  back to 'null' */
			line = null;

			/* CHAIN A: Preparing reader for reading master *.pdb file */
			//read text file
			FileReader fileReader6 = new FileReader(filename);
			//always wrap filereader in buffered reader for some reason
			BufferedReader bufferedReader6 = new BufferedReader(fileReader6);

			/* CHAIN A: Preparing writer for writing to CHAIN A's *.pdb file */
			PrintWriter writer = new PrintWriter(cwd + File.separator + "ChainA.pdb", "UTF-8");

			String SpaceToCheck;

			while((line = bufferedReader6.readLine()) != null){
				/* DIAGNOSTIC */
				//System.out.println(line.substring(20, 21));
				//System.out.println((line.substring(20, 21)).length());

				if(line.contains("END") || line.contains("ENDMDL")){
					writer.println(line);
				}

				if(line.length() > 50){
					SpaceToCheck = line.substring(21, 22);

					/* DIAGNOSTIC */
					//System.out.println(SpaceToCheck);

					if(ChainA.contains(SpaceToCheck)){
						writer.println(line);

					/* DIAGNOSTIC */
					//System.out.println(SpaceToCheck);
					}
				}

			}

			/* Always close file readers and writers */
			writer.close();
			fileReader6.close();
			bufferedReader6.close();

/* ************************************** STRIPPING CHAIN B ***************************************** */
			/* Reset 'line'  back to 'null' */
			line = null;

			/* CHAIN B: Preparing reader for reading master *.pdb file */
			//read text file
			FileReader fileReader7 = new FileReader(filename);
			//always wrap filereader in buffered reader for some reason
			BufferedReader bufferedReader7 = new BufferedReader(fileReader7);

			/* CHAIN B: Preparing writer for writing to CHAIN A's *.pdb file */
			PrintWriter writer2 = new PrintWriter(cwd + File.separator + "ChainB.pdb", "UTF-8");

			while((line = bufferedReader7.readLine()) != null){
				/* DIAGNOSTIC */
				//System.out.println(line.substring(20, 21));
				//System.out.println((line.substring(20, 21)).length());

				if(line.contains("END") || line.contains("ENDMDL")){
					writer2.println(line);
				}

				if(line.length() > 50){
					SpaceToCheck = line.substring(21, 22);

					/* DIAGNOSTIC */
					//System.out.println(SpaceToCheck);

					if(ChainB.contains(SpaceToCheck)){
						writer2.println(line);

					/* DIAGNOSTIC */
					//System.out.println(SpaceToCheck);
					}
				}

			}

			/* Always close file readers and writers */
			writer2.close();
			fileReader7.close();
			bufferedReader7.close();

			} /* End 'try' block */

			/* Catching exceptions */
			catch (FileNotFoundException ex){
				System.out.println("Unable to open file '" + filename + "'");
			}

			catch (IOException ex) {
				System.out.println("Error reading file '" + filename + "'");
			}

			// Deletes 'NewFile' temporary file that was created if the original input ensemble came from NMR
			File fileToDelete = new File(cwd + File.separator + "NewFile.pdb");
			fileToDelete.delete();

	} /* End 'main' method */
} /* End 'ComplexSplitter' class */
