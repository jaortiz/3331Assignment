import java.util.ArrayList;


public class State implements Comparable<State>{
	
	ArrayList<String> path;
	int cost;
	
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

	
	//Comparator used for priority queue
	@Override
	public int compareTo(State o) {
		return cost - o.getCost();
	}
}
