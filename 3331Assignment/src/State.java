import java.util.ArrayList;


public class State implements Comparable<State>{
	
	ArrayList<String> path;
	int cost;
	double load = 0.0;
	
	//Initial Constructor
	public State(String initial) {
		path = new ArrayList<String>();
		path.add(initial);
		cost = 0;
	}
	
	
	//Path Constructor
	public State(ArrayList<String> oldPath, String newNode, int oldCost, int extraCost) {
		path = new ArrayList<String>(oldPath);
		path.add(newNode);
		cost = oldCost + extraCost;
		
		/*Testing
		System.out.println();
		for(int i = 0; i < path.size(); i++) {
			System.out.print(path.get(i) + " -> ");
		}
		System.out.println("Cost: " + cost);
		*/
		
	}
	
	
	//overloading constructor for LLP, takes in a double for the load
	public State(ArrayList<String> oldPath, String newNode, double leastLoad) {
		path = new ArrayList<String>(oldPath);
		path.add(newNode);
		load = leastLoad;
	}
	
	//Method to get state path
	public ArrayList<String> getPath() {
		return path;
	}
	
	
	//Method to get current location in path/graph
	public String getLastNode() {
		return path.get(path.size() -1);
	}
	
	
	//Method to get path cost
	public int getCost() {
		return cost;
	}

	//Method to get the current load of a link in path
	public double getLoad() {
		return load;
	}
	
	//Comparator used for priority queue
	@Override
	public int compareTo(State o) {
		
		if(load != 0.0) {	//used to compare doubles for LLP 
			if(load < o.getLoad()) return -1;
			if(load > o.getLoad()) return 1;
			return 0;
		}
		
		return cost - o.getCost();	//used for SDP and SHP
		
	}
}
