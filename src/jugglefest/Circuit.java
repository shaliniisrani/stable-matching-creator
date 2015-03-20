package jugglefest;

import java.util.LinkedList;

/**
 * Class Circuit
 * @author Shalini Israni
 * shalini.israni@gmail.com
 *
 */
public class Circuit {
	private String name;
	private float handEyeCoord;
	private float endurance;
	private float pizzazz;
	//List of jugglers assigned to this circuit. This list could keep changing based on preferences
	//and match values.
	private LinkedList<Juggler> jugglers;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	
	
	public LinkedList<Juggler> getJugglers() {
		return jugglers;
	}
	public void setJugglers(LinkedList<Juggler> jugglers) {
		this.jugglers = jugglers;
	}
	/**
	 * Constructor
	 * @param name
	 * @param handEyeCoord
	 * @param endurance
	 * @param pizzazz
	 */
	public Circuit(String name, float handEyeCoord, float endurance,
			float pizzazz) {		
		this.name = name;
		this.handEyeCoord = handEyeCoord;
		this.endurance = endurance;
		this.pizzazz = pizzazz;
		this.jugglers = new LinkedList<Juggler>();
	}
	@Override
	// print friendly. 
	public String toString() {
		StringBuilder circuitString = new StringBuilder();
		circuitString.append("Name: ").append(this.name).append(" H:").append(this.handEyeCoord);
		circuitString.append(" E:").append(this.endurance);
		circuitString.append(" P:").append(this.pizzazz);
		return circuitString.toString();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		if(! (obj instanceof Circuit) )
			return false;
		Circuit c = (Circuit) obj;
		return(c.getName().equals(name));
	}
	
}
