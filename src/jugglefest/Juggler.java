package jugglefest;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class Preference
 * @author Shalini Israni
 * Stores a reference to a circuit, and a boolean to know whether visited.
 * The Juggler class uses this. Each juggler instance has an instance variable - an ArrayList
 * of preferences. The reason for using ArrayList is explained in the Juggler class.
 */
//Added a test comment from remote git from new branch
class Preference{
	Circuit c ;	
	boolean visited ;
	
	public Preference(Circuit c/*, int number*/, boolean visited) {	
		this.c = c;
	//	this.number = number;
		this.visited = visited;
	}
	
}

/**
 * Class Juggler
 * @author Shalini Israni
 * shalini.israni@gmail.com
 */

public class Juggler {

	private float handEyeCoord;
	private float endurance;
	private float pizzazz;
	private String name;
	
	private ArrayList<Preference> preferences;
	public float getHandEyeCoord() {
		return handEyeCoord;
	}
	public void setHandEyeCoord(float handEyeCoord) {
		this.handEyeCoord = handEyeCoord;
	}
	public float getEndurance() {
		return endurance;
	}
	public void setEndurance(float endurance) {
		this.endurance = endurance;
	}
	public float getPizzazz() {
		return pizzazz;
	}
	public void setPizzazz(float pizzazz) {
		this.pizzazz = pizzazz;
	}
	public ArrayList<Preference> getPreferences() {
		return preferences;
	}
	public void setPreferences(ArrayList<Preference> preferences) {
		this.preferences = preferences;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Constructor
	 * @param name
	 * @param handEyeCoord
	 * @param endurance
	 * @param pizzazz
	 */
	public Juggler(String name ,float handEyeCoord, float endurance, float pizzazz
			) {
		this.name = name ;
		this.handEyeCoord = handEyeCoord;
		this.endurance = endurance;
		this.pizzazz = pizzazz;
		this.preferences = new ArrayList<Preference>();
	}
	
	/**
	 * Adds a circuit to the preference list for this juggler.
	 * @param circuit
	 * @param visited
	 */
	public void addPreference(Circuit circuit, boolean visited){
		
		this.preferences.add(new Preference(circuit,visited)) ;
		
	}
	
	/**
	 * Mark a circuit visited for this juggler.
	 * @param c
	 */
	
	public void markVisited(Circuit c){
		Iterator<Preference> preferenceIterator = preferences.iterator();
		Preference preference;
		while(preferenceIterator.hasNext()){
			preference = (Preference)preferenceIterator.next();
			if (preference.c.equals(c)){
				preference.visited = true;
				break;
			}
		}
	}
	
	/**
	 * Returns the next unvisited circuit for a juggler in descending order of preference.
	 * Since the input contains circuits in order of preference, I build them into a 
	 * preference linked list whose natural order is the order in which they came in.
	 * So iterating from the front to the end of the list amounts to going in descending
	 * order of preference.
	 * @return Circuit
	 */
	
	public Circuit getNextUnvisitedPreference(){
		Iterator<Preference> preferenceIterator = preferences.iterator();
		Preference preference;
		while(preferenceIterator.hasNext()){
			preference = (Preference)preferenceIterator.next();	
			if(!preference.visited)
				return preference.c;
		}
		return null;
	}
	
	//override for meaningful comparison of two Juggler objects when testing for equality.
	@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		if (!(obj instanceof Juggler))
			return false;
		Juggler j = (Juggler)obj;
		return j.getName().equals(name); 
	}

	@Override
	//I overrode this to make the Juggler object print friendly. I used it in the methods I wrote
	//to test my file load (by printing to the console, and to a file), which I removed in the end.
	public String toString() {
		StringBuilder jugglerString = new StringBuilder();
		jugglerString.append("Name: ").append(this.name).append(" H:").append(this.handEyeCoord);
		jugglerString.append(" E:").append(this.endurance);
		jugglerString.append(" P:").append(this.pizzazz).append(" ");
		Iterator<Preference> preferenceIterator = preferences.iterator();
		Preference preference;
		while(preferenceIterator.hasNext()){
			preference = (Preference)preferenceIterator.next();
			jugglerString.append(preference.c.getName()).append(",");
		}	
		return jugglerString.toString();
	}
	
}
