
public class Arc {
	
	private String to;
	private int propDelay = 0;
	private int linkCapacity = 0;
	private int traffic = 0;
	
	
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
	
	public boolean isFull() {
		boolean full = false;
		
		if(traffic >= linkCapacity) {
			full = true;
		}
		
		return full;
	}


	//increments the traffic on the link
	public void incrementTraffic() {
		traffic++;
	}
	
	//increments the traffic on the link
	public void decrementTraffic() {
		traffic--;
	}
	
	//method to get the current load on the link
	public double getLoad() {
		return ((double)traffic /(double)linkCapacity); 
	}
}
