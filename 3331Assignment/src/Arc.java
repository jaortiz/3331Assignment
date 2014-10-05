
public class Arc {
	
	private String to;
	private int propDelay = 0;
	private int linkCapacity = 0;
	
	
	//Constructor
	public Arc(String dest, int pDelay, int lCapacity) {
		to = dest;
		propDelay = pDelay;
		linkCapacity = lCapacity;
	}
	
	
	//Getter Method to get Arc node name
	public String getName() {
		return to;
	}
	
	
	//Getter method to get propagation delay of edge/arc/link
	public int getPropDelay() {
		return propDelay;
	}
}
