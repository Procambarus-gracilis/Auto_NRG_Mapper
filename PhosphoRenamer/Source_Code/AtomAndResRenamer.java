/* Author: Oscar Bastidas
Last edited: June. 14, 2018
ANDROID EDITION */

/* This program re-names phosphate moiety atoms from MD simulations so Open-Contact can map them */

//package edu.umn.EnsembleNRGMapper.llparker; --> wrong package address for tablet; won't compile if included

//import java.io.FileWriter; --> not needed for ecj compiler; won't compile if included
import java.io.*;
//import java.util.*; --> not needed for ecj compiler; won't compile if included

public class AtomAndResRenamer
{
	public static void main(String[] args)
	{
		// Set filename
		String filename;
		String cwd = System.getProperty("user.dir");
		String line = null;


        if (args.length > 0){
            filename = args[0];
        } else {
            filename = cwd + "input.csv";
        }

        String pResNum = null;
        String pResChainID = null;

		try{

			// Read text files
			FileReader fileReader = new FileReader(filename);

			// Wrap filereader in buffered reader
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			// Prepare file writer
			PrintWriter writer = new PrintWriter(cwd + File.separator + "interim_renamed_phosphates-1.pdb", "UTF-8");

			while ((line = bufferedReader.readLine()) != null){

				// Process lines that may contain phosphate atoms; can handle non-phosphate situations
				if(line.length() >= 74){
					String string1 = line.substring(0,13);
					String string2 = line.substring(13,16); // phosphate atom field
					String string3 = line.substring(16,76); // residue name field

					if(string2.equals("P1 ")){
						string2 = "P  ";

						// Variables for changing phospho-negative naming applied by NAMD to phospho positive residues
						// for appropriate NRG mapping by Open-Contact down-stream
						pResNum = line.substring(22,26);
						pResChainID = line.substring(21,22);

					}

					if(string2.equals("O2 ")){
						string2 = "O1P";

						// Variables for changing phospho-negative naming applied by NAMD to phospho positive residues
						// for appropriate NRG mapping by Open-Contact down-stream
						pResNum = line.substring(22,26);
						pResChainID = line.substring(21,22);
					}

					if(string2.equals("O3 ")){
						string2 = "O2P";

						// Variables for changing phospho-negative naming applied by NAMD to phospho positive residues
						// for appropriate NRG mapping by Open-Contact down-stream
						pResNum = line.substring(22,26);
						pResChainID = line.substring(21,22);
					}

					if(string2.equals("O4 ")){
						string2 = "O3P";

						// Variables for changing phospho-negative naming applied by NAMD to phospho positive residues
						// for appropriate NRG mapping by Open-Contact down-stream
						pResNum = line.substring(22,26);
						pResChainID = line.substring(21,22);
					}

					writer.println(string1 + string2 + string3);
				} // End 'if' block for phosphate atom processing criteria
				else {
					if (line.length() < 74){
						writer.println(line);
					}
				}

				

			} // End 'while' loop; this 'while' loop changes phosphate atom IDs and identifies residue numbers and residue
			  // chain IDs

			// Close file readers and writers
			writer.close();
        	fileReader.close();
        	bufferedReader.close();

		} // End 1st 'try' block

		// Catching exceptions-always catch exceptions AFTER 'try' block
		catch (FileNotFoundException ex){
			System.out.println("Unable to open file '" + filename + "'");
		} // End 'FileNotFoundException'

		catch (IOException ex) {
			System.out.println("Error reading file '" + filename + "'");
		} // End 'IOException'

//**********************************************************************************************************************************************************************

			// Now the actual re-naming of the NAMD improperly named phospho-residues
			try {
				// Read text files
				FileReader fileReader2 = new FileReader(cwd + File.separator + "interim_renamed_phosphates-1.pdb");

				// Wrap filereader in buffered reader
				BufferedReader bufferedReader2 = new BufferedReader(fileReader2);

				// Prepare file writer
				PrintWriter writer2 = new PrintWriter(cwd + File.separator + "interim_renamed_phosphates-2.pdb", "UTF-8");

				line = null;

				while ((line = bufferedReader2.readLine()) != null) {

					// 'if' block for changing phospho residue 3-letter IDs to the appropriate ones
					if ((line.length() > 30) && line.substring(22, 26).equals(pResNum) && line.substring(21, 22).equals(pResChainID)) {
						String string1A = line.substring(0, 17);
						String string2A = line.substring(17, 20); // phospho residue's 3-letter name-OF INTEREST
						String string3A = line.substring(20, 76);

						if (string2A.equals("TYR")) {
							string2A = "PTR";
						}

						if (string2A.equals("SER")) {
							string2A = "SEP";
						}

						if (string2A.equals("THR")) {
							string2A = "TPO";
						}

						writer2.println(string1A + string2A + string3A);
					} // End 'if'

					else {
						writer2.println(line);
					}
				} // End 'while'

				// Close file readers and writers-always close BEFORE end of 'try' block since opened inside a 'try' block
				writer2.close();
				fileReader2.close();
				bufferedReader2.close();

			} // End 2nd 'try' block

			// Catching exceptions-always catch exceptions AFTER 'try' block
			catch (FileNotFoundException ex){
				System.out.println("Unable to open file '" + filename + "'");
			} // End 'FileNotFoundException'

			catch (IOException ex) {
				System.out.println("Error reading file '" + filename + "'");
			} // End 'IOException'

//**********************************************************************************************************************************************************************

			// This section re-names 'HETATM' entries if the ensemble comes from NMR-it's possible there may be no
			// 'HETATM' entries
			try {
				// Read text files
				FileReader fileReader3 = new FileReader(cwd + File.separator + "interim_renamed_phosphates-2.pdb");

				// Wrap filereader in buffered reader
				BufferedReader bufferedReader3 = new BufferedReader(fileReader3);

				// Prepare file writer
				PrintWriter writer3 = new PrintWriter(cwd + File.separator + "renamed_phosphates.pdb", "UTF-8");

				line = null;

				while ((line = bufferedReader3.readLine()) != null) {
					if ((line.length()>30) && line.substring(0,6).equals("HETATM")) {
						String substring1B = line.substring(0,6);
						String substring2B = line.substring(6,76);

						if (substring1B.equals("HETATM")) {
							substring1B = "ATOM  ";
						}

						writer3.println(substring1B + substring2B);
					}

					else {
						writer3.println(line);
					}
				} // End 'while'

				// Close file readers and writers-always close BEFORE end of 'try' block since opened inside a 'try' block
				writer3.close();
				fileReader3.close();
				bufferedReader3.close();

			} // End 'try' block

			// Catching exceptions-always catch exceptions AFTER 'try' block
			catch (FileNotFoundException ex){
				System.out.println("Unable to open file '" + filename + "'");
			} // End 'FileNotFoundException'

			catch (IOException ex) {
				System.out.println("Error reading file '" + filename + "'");
			} // End 'IOException'

			// Deleting interim renamed files so there's only one output file-the one the user wants
			File fileToDelete1 = new File(cwd + File.separator + "interim_renamed_phosphates-1.pdb");
			fileToDelete1.delete();

			File fileToDelete2 = new File(cwd + File.separator + "interim_renamed_phosphates-2.pdb");
			fileToDelete2.delete();

	} // End 'main' method
} // End 'PhosphoRenamer' class
