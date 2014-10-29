
public class Arc {
	
	private String to;
	private int propDelay = 0;
	private int linkCapacity = 0;
	private int traffic = 0;
	
	
	/**
	 * Default Constructor
	 * 
	 * @param dest			Node to
	 * @param pDelay		Propagation delay of this link
	 * @param lCapacity		Link capacity
	 */
	public Arc(String dest, int pDelay, int lCapacity) {
		to = dest;
		propDelay = pDelay;
		linkCapacity = lCapacity;
	}
	
	
	/**
	 * 
	 * @return arc node name
	 */
	public String getName() {
		return to;
	}
	
	
	/**
	 * 
	 * @return propagation delay of this link
	 */
	public int getPropDelay() {
		return propDelay;
	}
	
	
	/**
	 * Method to check whether or not the link is at full capacity
	 * 
	 * @return	
	 */
	public boolean isFull() {
		boolean full = false;
		
		if(traffic >= linkCapacity) {
			full = true;
		}
		
		return full;
	}


	/**
	 * Increment the traffic on this link
	 */
	public void incrementTraffic() {
		traffic++;
	}
	

	/**
	 * Decrement the traffic on this link
	 */
	public void decrementTraffic() {
		traffic--;
	}
	
	
	/**
	 * 
	 * @return	current load of the link
	 */
	public double getLoad() {
		return ((double)traffic /(double)linkCapacity); 
	}
}
