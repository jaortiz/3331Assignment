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
	
	//Connection constructor
	Connection(String start, String end, double startTime, double duration, int numPackets, ArrayList<String> path) {
		this.start = start;
		this.end = end;
		this.startTime = startTime;
		this.duration = duration;
		this.path = path;
		this.numPackets = numPackets;
		endTime  = this.startTime + this.duration;
		
	}
	
	
	//Connection constructor for PACKET switching
	Connection(String start, String end, double startTime, double endTime) {
		this.start = start;
		this.end = end;
		this.startTime = startTime;
		this.endTime = endTime;
		packetNetwork = true;
	}
	
	//method to return the end time of the connection
	public double getEndTime() {
		return endTime;
		//return startTime + duration;
	}
	
	//getter method to get path
	public ArrayList<String> getPath() {
		return path;
	}

	//method to return start time
	public double getStartTime() {
		return startTime;
	}

	//method to return start node
	public String getStartNode() {
		return start;
	}
	
	//method to return start node
	public String getEndNode() {
		return end;
	}
	
	@Override
	public int compareTo(Connection o) {
		if(packetNetwork) {	
			return (int) (this.startTime - o.getStartTime());	//used in packetSwitching
		}
		return (int) (this.getEndTime() - o.getEndTime());
		
	}
	
	
}
