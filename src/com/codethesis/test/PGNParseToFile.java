
package com.codethesis.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.codethesis.pgnparse.MalformedMoveException;
import com.codethesis.pgnparse.PGNGame;
import com.codethesis.pgnparse.PGNMove;
import com.codethesis.pgnparse.PGNParseException;
import com.codethesis.pgnparse.PGNSource;

public class PGNParseToFile {

	public static void main(String[] args) throws IOException, PGNParseException, NullPointerException, MalformedMoveException {
		if (args.length == 0) {
			System.out.println("Usage:");
			System.out.println("\tpgn_path");
		}
		
		File f = new File("PGNStage.txt");
		File file = new File(args[0]);
		String VisitedGamesTag = "";
		

		if (!f.exists()) {
			f.createNewFile();
		}
		

		
		if (!file.exists()) {
			System.out.println("File does not exist!");
			return;
		}
		
		PGNSource source = new PGNSource(file);
		Iterator<PGNGame> i = source.listGames().iterator();
		
		
		
		while (i.hasNext()) {
			PGNGame game = i.next();
			
			System.out.println("############################");
			Iterator<String> tagsIterator = game.getTagKeysIterator();
			
			VisitedGamesTag = "[" + game.getTag("Event") + "/" +game.getTag("White") +"/" + game.getTag("Black") + "]" ;
			System.out.println("gamtag ===> " + VisitedGamesTag);
			
			while (tagsIterator.hasNext()) {
				String key = tagsIterator.next();
				System.out.println(key + " {" + game.getTag(key) + "}");
				
			}
			
			System.out.println();
			Iterator<PGNMove> movesIterator = game.getMovesIterator();
			int num = 1;
			
			
			while (movesIterator.hasNext()) {
				PGNMove move = movesIterator.next();
				
				try {
			           
					// If the file exists we edit the file
		            BufferedReader br = new BufferedReader(new FileReader("PGNStage.txt"));
		            String strLine;
		            StringBuilder fileContent = new StringBuilder();
		            
		            List<String> list = new ArrayList <String>(); // temprary list for storing the stages
		            
		            //Read File Line By Line
		            boolean exist = false; // if the next stage lareaady exist in the data
		            while ((strLine = br.readLine()) != null) {
		                // Print the content on the console
		                //System.out.println(strLine);
		                String tokens[] = strLine.split("\t");
		                
		                
		                if (tokens[0].equals("<" + move.getStage() +">")) {
		                	
		                	String newLine = strLine + "\t" + "<" + move.getFullMove() + ">";
		                	
		                	list.add(newLine);
	                        exist= true;
	                        
		                	
		                } else {		                	
		                	list.add(strLine);
		                }
		            }
		             
		            if (exist == false){
		            	list.add("<" + move.getStage() + ">" +"\t" +"<" + move.getFullMove() +">");
		            }
		            
		            //Collections.sort(list , Collections.reverseOrder());
		            
		            for(String val : list ){
		            	fileContent.append(val);	
		            	fileContent.append('\n');
		            }
		            
		            // Now fileContent will have updated content , which you can override into file
		            BufferedWriter out = new BufferedWriter(new FileWriter("PGNStage.txt"));
		            out.write(fileContent.toString());
		            out.close();
		            //Close the input stream
		            br.close();
		            list.clear();
					
				} catch (Exception e) {//Catch exception if any
		            System.err.println("Error: " + e.getMessage());
		        }
				
				
				if (num % 2 == 1 && !move.isEndGameMarked()) {
					System.out.print((num + 1) / 2 + ". ");
				}
				
				num++;
				
				if (move.isEndGameMarked()) {
					System.out.print("(" + move.getMove() + ")");
				} else if (move.isKingSideCastle()) {
					System.out.print("[O-O] ");
				} else if (move.isQueenSideCastle()) {
					System.out.print("[O-O-O] ");
				} else {
					System.out.print("[" + move.getFromSquare() + "]->[" + move.getToSquare() + "] ");
				}
			}
			
			
			System.out.println();
		}
	}

}
