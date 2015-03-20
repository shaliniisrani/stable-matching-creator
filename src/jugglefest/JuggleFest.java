package jugglefest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * Class JuggleFest
 * @author Shalini Israni
  * Instantiated in JuggleFestDriver, this class is the meat of the solution.
 * It has methods to load the file, create the assignments of jugglers to circuits,
 * and write the output file. It has helper methods to perform tasks such as finding 
 * a match value, finding the least compatible assigned juggler, etc that have been 
 * separated out as functions for readability, and so as not to distract from the
 * main algorithm.
 *
 */

 class JuggleFest {
	/***
	 * An extensible array is needed to store jugglers when reading from the file since
	 * we aren't sure of the exact number in the beginning.
	 * LinkedList, not ArrayList,because this also functions as the unassigned list of jugglers,
	 * and needs frequent insertion and deletion, and not necessarily fast iteration that
	 * ArrayList provides.
	 * Not Vector, since for this program, I have assumed that multiple threads
	 * will not be accessing the same Juggler objects at once, and we can avoid the 
	 * performance hit that the synchronized methods in Vector add.
	 */
	
	private LinkedList<Juggler>jugglers; 
	/**
	 * The size of list of circuits is also unknown until the file is read, so needs to
	 * be growable, but unlike the list of jugglers, doesn't need frequent insertion and
	 * deletion.
	 * Since all circuits come before jugglers in the input file, quick retrieval of circuits
	 * to store a juggler's preferences would be well achieved with a HashMap. 
	 * Even though a HashMap is unsorted and unordered, since we do not really need it to be
	 * ordered or sorted for this program, it is a good choice. I confirmed with the sample
	 * output file that the circuits do not need to be ordered in the same order that they 
	 * came in, before I made this choice.
	 * */
	private HashMap<String,Circuit>circuits;
	/**
	 * This is a list of all jugglers remaining at the end of the pairing attempt.
	 * Jugglers do not rank all circuits. It is possible a juggler does not get assigned
	 * to any circuit in his ranking.
	 * But we still want to fill all circuits with the best jugglers (for the specific circuit)
	 * until all circuits are filled.
	 * I use this list after the pairing by ranking is done, to fill all circuits while
	 * maximizing the "value" of a circuit by picking remaining jugglers with the highest match
	 * for the circuit.	 
	 */
	private LinkedList<Juggler> remainingJugglers ;
	
	
	/***
	 * Constructor
	 */
	public JuggleFest() {
		this.circuits= new HashMap<String, Circuit>();
		this.jugglers=new LinkedList<Juggler>();
		this.remainingJugglers= new LinkedList<Juggler>();
	}

	/**
	 * 
	 * @param line
	 * @return boolean
	 * This method loads a circuit from a single line in the file.
	 */
	
	private boolean loadCircuit(String line){
		//C C0 H:10 E:8 P:10
		String H=null, E=null, P=null, name=null ;
		String [] circuitData = line.split(" ");
		for(int i=0; i<circuitData.length; i++){
			if(circuitData[i].equals("C"))
				continue ;
			else{
				switch(circuitData[i].charAt(0)){
				case 'C':
					name = circuitData[i];
					break;
				case 'H':
					H = circuitData[i].substring(2);
					break;
				case 'E':
					E= circuitData[i].substring(2);
					break;
				case 'P':
					P = circuitData[i].substring(2);
					break;
				}
			}
			
		}
		if(name == null || H == null || E == null || P == null){
			return false;
		}
		circuits.put(name, new Circuit(name, (new Float(H)).floatValue(), (new Float(E)).floatValue(), (new Float (P).floatValue())));
		return true;
	}
	/**
	 * 
	 * @param line
	 * @return
	 * This method loads a Juggler from a single line in the file
	 */
	
	private boolean loadJuggler(String line){
		//J J0 H:7 E:6 P:0 C453,C1706,C318,C271,C1958,C1051,C241,C1736,C304,C518
		String H=null, E=null, P=null, name=null, preferences=null ;
		String [] jugglerData = line.split(" ");
		for(int i=0; i<jugglerData.length; i++){
			if(jugglerData[i].equals("J"))
				continue ;
			else{
				switch(jugglerData[i].charAt(0)){
				case 'J':
					name = jugglerData[i];
					break;
				case 'H':
					H = jugglerData[i].substring(2);
					break;
				case 'E':
					E= jugglerData[i].substring(2);
					break;
				case 'P':
					P = jugglerData[i].substring(2);
					break;
				case 'C':
					preferences=jugglerData[i];
					break;
				}
			}
		}
		if(name == null || H == null || E == null || P == null || preferences == null){
			return false;
		}
		Juggler j = new Juggler(name, (new Float(H)).floatValue(), (new Float(E)).floatValue(), (new Float (P).floatValue()));
		String[] preferenceData = preferences.split(",");
		Circuit c;
		for(int i=0;i<preferenceData.length;i++){
			c=circuits.get(preferenceData[i]);
			if(c==null){
				System.out.println("Invalid circuit preference " + preferenceData[i]);
				return false;
			}
			j.addPreference(c, false);
		}
		jugglers.add(j);		
		return true ;
	}
	
	/**
	 * 
	 * @param filename
	 * @return
	 * This method reads reach line from the file, and invokes either loadJuggler or
	 * loadCircuit depending on the first character.
	 * If it finds an unexpected character, or there is an error loading the circuit or juggler
	 * it will output the problematic line number.
	 */
	
	public boolean loadFile (String filename){
		BufferedReader input=null;
		String line = new String();
		int lineNum = 0 ;
		try {
			 input = new BufferedReader(new FileReader(filename));
			 while( (line = input.readLine()) != null){
				lineNum ++ ; 
				line = line.toUpperCase().trim();
				if (line.length() > 0) { //skip empty lines
					if (line.charAt(0) == 'C') {
						if (!loadCircuit(line)) {
							System.out.println("Error loading circuit on line "
									+ lineNum);
							input.close();
							return false;
						}
					} else if (line.charAt(0) == 'J') {
						if (!loadJuggler(line)) {
							System.out.println("Error loading juggler on line "
									+ lineNum);
							input.close();
							return false;
						}
					} else {
						System.out.println("Unexpected first character "
								+ line.charAt(0) + " found on line " + lineNum);
						input.close();
						return false;
					}
				}
			 }
			 return true ;
		} catch (FileNotFoundException fileNotFoundException) {
			fileNotFoundException.printStackTrace();
			return false;
		} catch (IOException ioException){
			
		}
		finally{			
			try {
				input.close(); //cleanup
			} catch (IOException ioException) {				
				ioException.printStackTrace();
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param j
	 * @param c
	 * @return dot product
	 * This method calculates a match value based on H,E,P values of the juggler and circuit
	 * passed to it. 
	 */
	
	private float calculateMatch(Juggler j, Circuit c){
		//dot product
		return ( (j.getHandEyeCoord()*c.getHandEyeCoord()) + (j.getEndurance()*c.getEndurance()) + j.getPizzazz()*c.getPizzazz());	 
	}
	
	/**
	 * 
	 * @param j1
	 * @param j2
	 * @param c
	 * @return Juggler 
	 * find the better match of the two jugglers, for the circuit passed.
	 */
	
	private Juggler returnBetterMatch(Juggler j1, Juggler j2, Circuit c){
		if(calculateMatch(j1,c) > calculateMatch(j2,c)){
			return j1;
		}
		return j2;
	}
	
	/**
	 * 
	 * @param c
	 * @return Juggler
	 * find the juggler with the lowest match that is currently assigned to this circuit. 
	 */
	
	private Juggler findLeastMatchingJuggler(Circuit c){
		Juggler minMatch = c.getJugglers().get(0);
		for(int i=0;i<c.getJugglers().size();i++){
			if( calculateMatch(c.getJugglers().get(i),c) < calculateMatch(minMatch,c) )
				minMatch = c.getJugglers().get(i);
		}
		return minMatch ;
	}
	
	/**
	 * 
	 * @param j
	 * @param c
	 * remove a juggler from a circuit
	 */
	private void removeJugglerFromCircuit(Juggler j, Circuit c){
		for(int i=0; i<c.getJugglers().size();i++){
			if(c.getJugglers().get(i).equals(j)){
				c.getJugglers().remove(i);
				return;
			}
		}
	}
	/**
	 * This method writes the output file. 
	 * to accept the filename from the user, I would let that be passed as a command line
	 * argument, and then pass it to this method. 
	 */
	public void outputAssignments() {
		File file = new File("juggler-circuit-assignments.txt");
		StringBuilder outputLine = new StringBuilder();

		try {
			
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			Iterator it = circuits.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Circuit> pairs = (Entry<String, Circuit>) it.next();
				Circuit c = (Circuit)pairs.getValue();
				outputLine.append(c.getName()).append(" ");
				for(int i=0; i<c.getJugglers().size();i++){
					Juggler j = c.getJugglers().get(i);
					outputLine.append(j.getName()+" ");
					Circuit cPref ;
					for(int k=0; k<j.getPreferences().size();k++){
						cPref= j.getPreferences().get(k).c;
						outputLine.append(cPref.getName()+":");
						outputLine.append(calculateMatch(j, cPref) + " ");
					}
					outputLine.append(",");
				}
				outputLine.replace(outputLine.length()-1, outputLine.length(), "\n");
				bw.write(outputLine.toString());
				outputLine.replace(0, outputLine.length(), "") ;
			}
			bw.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}finally{
			
		}
	}
	
	/**
	 * This method is the one that assignes jugglers to circuits based on juggler preferences,
	 * match values. If jugglers remain after the initial assignment attempt,
	 * that means that there also are unfilled circuits. As explained above, a "remainingJugglers"
	 * list is maintained during the initial assignment attempt in order to match these with unfilled
	 * circuits in a way that attempts to assign the best possible matching jugglers from the remaining
	 * list, to each unfilled circuit, until all are filled (and there are no remaining jugglers).
	 */
	
	public void makeAssignments(){
		Juggler j, j1, jBetter ;
		Circuit c ;
		int numJugglersPerCircuit = jugglers.size()/circuits.size();
		//System.out.println(numJugglersPerCircuit + "\n");		
		
		
		while (!jugglers.isEmpty()) {// while there is still some unassigned
										// juggler
			j = jugglers.remove();

			c = j.getNextUnvisitedPreference();

			if (c != null) {
				if (c.getJugglers().size() < numJugglersPerCircuit) { // if
																		// circuit
																		// not
																		// empty,
																		// assign
																		// j to
																		// c
					c.getJugglers().add(j);
				} else {
					// find least match
					j1 = findLeastMatchingJuggler(c);
					// find better match
					jBetter = returnBetterMatch(j1, j, c);
					if (jBetter.equals(j)) {
						removeJugglerFromCircuit(j1, c); // remove j1 from this
															// circuit
					//	j1.markAllUnvisited(); // when j1 is bumped off, allow
												// them to start at the top of
												// their list
						c.getJugglers().add(j); // add j to this circuit
						jugglers.add(j1); // add j1 to unassigned list

					} else {
						// even the least compatible juggler on the circuit is a
						// better match than j
						// add j back to unassigned list
						jugglers.add(j);
					}
				}
				j.markVisited(c); // mark this circuit visited for j
			}
			else{
				//if all circuits are visited, add this juggler to a "remaining list"
				//these are jugglers that could not be paired with any of the circuits
				//that they ordered. But there still might be other circuits with space for them.
				//We will attempt to pair these such that, for
				//every J,C pair (from all remaining Js and unfilled Cs, dot product J.C
				//is maximized.
				remainingJugglers.add(j);
				
			}
		}
			
		//find unfilled circuit, find best juggler (highest dot product) for that until filled.
		//since num of jugglers / num circuits has no remainder, all circuits should be filled
		//and all jugglers assigned, after this.
		Circuit cUnfilled;
		while( (cUnfilled = findUnfilledCircuit(numJugglersPerCircuit)) != null){			
			Juggler jBest; 
			while(cUnfilled.getJugglers().size()<numJugglersPerCircuit){
				jBest = findBestRemainingJuggler(cUnfilled); 
				if(jBest!=null){
				cUnfilled.getJugglers().add(jBest);
				if(remainingJugglers.size() ==0 )
					break;
				}
			}
			if(remainingJugglers.size() ==0 )
				break;
		}
	}
	
	/**
	 * 
	 * @param c
	 * @return Juggler
	 * Given a circuit, find the remaining juggler that is the highest match for this circuit.
	 */
	
	private Juggler findBestRemainingJuggler(Circuit c) {
		float match = -1, maxMatch = -1;
		int maxMatchIndex = -1;
		
		for(int i=0;i<remainingJugglers.size();i++){
			match=  calculateMatch(remainingJugglers.get(i), c);
			if(match > maxMatch){
				maxMatch = match;
				maxMatchIndex = i;
			}
		}
		if(maxMatchIndex == -1)
			return null;
		return(remainingJugglers.remove(maxMatchIndex));
	}

	/**
	 * 
	 * @param num
	 * @return Circuit
	 * From the HashMap of circuits, find an unfilled one.
	 */
	
	private Circuit findUnfilledCircuit(int num) {
		Iterator<Circuit> it = circuits.values().iterator();
		Circuit c; 
		while(it.hasNext()){
			c= it.next();
			if(c.getJugglers().size()<num)
				return c;
		}
		return null;
	}
		
}
 
 
