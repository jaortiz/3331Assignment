import java.util.ArrayList;

//Packet Connection class is used for Packet Switching
public class PacketConnection implements Comparable<PacketConnection> {
	String start; 	//start node
	String end;		//end node
	double startTime;	
	double endTime;
	double duration;
	
	/**
	 * Overloading constructor for Packet Switching
	 * 
	 * @param start			Start Node
	 * @param end			End Node
	 * @param startTime		Start time of the connection
	 * @param endTime		End time of the connection
	 */
	PacketConnection(String start, String end, double startTime, double endTime) {
		this.start = start;
		this.end = end;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	
	/**
	 * @return the start
	 */
	public String getStartNode() {
		return start;
	}

	
	/**
	 * @return the end
	 */
	public String getEndNode() {
		return end;
	}

	
	/**
	 * @return the startTime
	 */
	public double getStartTime() {
		return startTime;
	}

	
	/**
	 * @return the endTime
	 */
	public double getEndTime() {
		return endTime;
	}

	
	/**
	 * @return the duration
	 */
	public double getDuration() {
		return duration;
	}
	
	
	/**
	 * Comparator for packetConnection class, sorts based on start time
	 * 
	 */
	@Override
	public int compareTo(PacketConnection o) {
		if(this.startTime < o.getStartTime()) return -1;
		if(this.startTime > o.getStartTime()) return 1;
		return 0;
		
	}
}
