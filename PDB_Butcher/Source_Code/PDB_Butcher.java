/* Authors: Oscar Bastidas and John Blankenhorn
Last edited: Feb. 8, 2018
ANDROID EDITION*/

//package edu.umn.EnsembleNRGMapper.llparker;

//import java.io.IOException; ---> required libraries for standard Java
//import java.nio.file.Files; ---> required libraries for standard Java
//import java.io.FileWriter;
import java.io.*;
import java.io.File;

public class PDB_Butcher {
    public static void main(String[] args) {
        //Set filename
        String filename;
        String cwd = System.getProperty("user.dir");
        String line = null;

        if (args.length > 0){
            filename = args[0];
        } else {
            filename = cwd + "input.csv";
        }


        try {
            //read text file
            FileReader fileReader = new FileReader(filename);
            //always wrap filereader in buffered reader for some reason
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            /* File output creation/generation */
            // "SSNum" variable here allows the first single snapshot file to be created prior to being populated
            int SSNum = 1;
            PrintWriter writer = new PrintWriter(cwd + File.separator + "SS" + SSNum + ".pdb", "UTF-8");

            /* CHECK HERE TO SEE  IF THERE'S ONLY ONE SS */

            while (((line = bufferedReader.readLine()) != null)) {
                String search1 = "HSE";
                String search2 = "HSD";
                // String search3 = "HETATM";  WAIT FOR THIS TILL YOU KNOW HOW SIMULATION HANDLES IT-CAN IT HANDLE IT STRAIGHT UP AS 'HETATM'?
                String search4 = "ENDMDL";
                String search5 = "END";

                // THIS SECTION WILL MOST LIKELY BE ANNIHILATED BECAUSE PROPKA WILL DO THIS WORK-BEGIN SECTION
                /* RENAME 'HSE' OR 'HSD' TO 'HIS' */
                if (line.contains(search1) || line.contains(search2)) {
                    String str1 = line.substring(0, 17);
                    String str2 = line.substring(17, 20); /*This is the amino acid column */
                    String str3 = line.substring(20, 69);

                    if (str2.matches(search1) || str2.matches(search2)) {
                        str2 = "HIS";
                        line = str1 + str2 + str3;
                    }
                }
                // THIS SECTION WILL MOST LIKELY BE ANNIHILATED BECAUSE PROPKA WILL DO THIS WORK-END SECTION

                writer.println(line);

                /* Butcher the *.pdb file if the line itself is either the "END" or "ENDMDL" delimiter */
                if (line.contains(search4) || line.contains(search5)) {
                    ++SSNum;
                    writer.close();
                    PrintWriter writer2 = new PrintWriter(cwd + File.separator + "SS" + SSNum + ".pdb", "UTF-8");
                    writer = writer2;
		    
		    // Necessary for ecj compiler
		    writer2.close();
                }

            }

            //always close files & writer
            writer.close();
            fileReader.close();
            bufferedReader.close();


            // This section deletes the final artifact snapshot that is always printed out, but always empty
            if (SSNum > 2) {

                //String toDelete = cwd + File.separator + "SS" + SSNum + ".pdb";
                File fileToDelete = new File(cwd + File.separator + "SS" + SSNum + ".pdb");
                fileToDelete.delete();

                int valid_PDB_SS_checker = 0;

                // Lastly, IF ensemble comes from NMR: delete the very last snapshot which MIGHT simply contain "housekeeping" notes,
                // which would otherwise confuse down-stream Open-Contact operations if not deleted
                try {
                    //read text file
                    FileReader fileReader2 = new FileReader(cwd + File.separator + "SS" + (SSNum - 1) + ".pdb");
                    //always wrap filereader in buffered reader for some reason
                    BufferedReader bufferedReader2 = new BufferedReader(fileReader2);

                    // Going to step through potential last artifactual NMR snapshot
                    line = null;

                    while((line = bufferedReader2.readLine()) != null ){
                        if (line.contains("ATOM") == true){
                            valid_PDB_SS_checker = 1;
                        }
                    }


                    // Close readers (if readers-and writers-are opened in a 'try' block, then they must be closed in a 'try' block)
                    fileReader2.close();
                    bufferedReader2.close();

                } // End of 'try' block

                /* CATCHING EXCEPTIONS: Always go IMMEDIATELY AFTER the 'try' block */
                catch (FileNotFoundException ex) {
                    System.out.println("Unable to open file '" + filename + "'");
                }
                catch (IOException ex) {
                    System.out.println("Error reading file '" + filename + "'");
                }

                if (valid_PDB_SS_checker != 1){
                        File fileToDelete2 = new File(cwd +File.separator + "SS" + (SSNum - 1) + ".pdb");
                        fileToDelete2.delete();
                }

            } // End of 'if' block for deleting artifactual last snapshot (whether from NMR or MD)

        } // End of 'try' block

            /* CATCHING EXCEPTIONS: Always go IMMEDIATELY AFTER the 'try' block */
        catch (FileNotFoundException ex){
            System.out.println(
            "Unable to open file '" +
            filename + "'");
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '"
                + filename + "'");
        }


    }// End of main

} // End of class
