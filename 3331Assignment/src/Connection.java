import java.util.ArrayList;


public class Connection implements Comparable<Connection>{
	String start; 	//start node
	String end;		//end node
	double startTime;	
	double endTime;
	double duration;
	ArrayList<String> path;
	int numPackets;
	boolean packetNetwork = false;
	

	/**
	 * Default Constructor
	 * 
	 * @param start			Start node
	 * @param end			End node
	 * @param startTime		Start time of the connection
	 * @param duration		Duration of the connection
	 * @param numPackets	Number of packets being sent in this connection
	 * @param path			Path that the connection is going to take
	 */
	Connection(String start, String end, double startTime, double duration, int numPackets, ArrayList<String> path) {
		this.start = start;
		this.end = end;
		this.startTime = startTime;
		this.duration = duration;
		this.path = path;
		this.numPackets = numPackets;
		endTime  = this.startTime + this.duration;
		
	}
	
	
	/**
	 * Overloading constructor for Packet Switching
	 * 
	 * @param start			Start Node
	 * @param end			End Node
	 * @param startTime		Start time of the connection
	 * @param endTime		End time of the connection
	 */
	Connection(String start, String end, double startTime, double endTime) {
		this.start = start;
		this.end = end;
		this.startTime = startTime;
		this.endTime = endTime;
		packetNetwork = true;
	}
	
	
	/**
	 * 
	 * @return the end time of the connection
	 */
	public double getEndTime() {
		return endTime;
		//return startTime + duration;
	}
	
	
	/**
	 * 
	 * @return the path used by this connection
	 */
	public ArrayList<String> getPath() {
		return path;
	}

	
	/**
	 * 
	 * @return Start time of this connection
	 */
	public double getStartTime() {
		return startTime;
	}

	
	/**
	 * 
	 * @return Starting node of this connection
	 */
	public String getStartNode() {
		return start;
	}
	
	
	/**
	 * 
	 * @return End node of this connection
	 */
	public String getEndNode() {
		return end;
	}
	
	
	/**
	 * Comparator used for priority queue
	 */
	@Override
	public int compareTo(Connection o) {
		if(packetNetwork) {	
			return (int) (this.startTime - o.getStartTime());	//used in Packet Switching
		}
		return (int) (this.getEndTime() - o.getEndTime());		//used in Circuit Switching
		
	}
	
	
}
