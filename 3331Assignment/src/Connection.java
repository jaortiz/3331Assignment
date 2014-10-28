import java.util.ArrayList;


public class Connection implements Comparable<Connection>{
	String start; 
	String end;
	double startTime;
	double duration;
	ArrayList<String> path;
	int numPackets;
	

	Connection(String start, String end, double startTime, double duration, int numPackets, ArrayList<String> path) {
		this.start = start;
		this.end = end;
		this.startTime = startTime;
		this.duration = duration;
		this.path = path;
		this.numPackets = numPackets;
	}
	
	
	//method to return the end time of the connection
	public double getEndTime() {
		return startTime + duration;
	}
	
	//getter method to get path
	public ArrayList<String> getPath() {
		return path;
	}


	@Override
	public int compareTo(Connection o) {
		return (int) (this.getEndTime() - o.getEndTime());
	}
	
	
}
