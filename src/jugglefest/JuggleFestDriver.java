package jugglefest;

/**
 * Class JuggleFestDriver
 * @author Shalini Israni
 * This class instantiates JuggleFest which actually loads the file, solves the problem,
 * and outputs a file to output.txt.
 * The argument to this class's main method is the name of the input file.
 * Sample call: java JuggleFestDriver jugglefest.txt
 *
 */

public class JuggleFestDriver {

	
	public static void main(String[] args) {
		JuggleFest juggleFest = new JuggleFest();
		if(args.length != 1){
			System.out.println("Please specify file name, and nothing else.");
			return;
		}
		if(juggleFest.loadFile(args[0])){
			juggleFest.makeAssignments();
			juggleFest.outputAssignments();
		}
	}
}
