import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
	
public class Graph {

	//private ArrayList<Node> nodes;
	private Map<String, Node> adjList; 	//Used to store the adjacency list, where each map pair has key<name,node> and each node contains edges connected to it
	
	//For SHP and/or SDP
	private PriorityQueue<State> toVisit;
	
	public Graph() {
		adjList =  new HashMap<String, Node>();
		toVisit = new PriorityQueue<State>();
	}
	
	
	//Method to add node to graph
	public void addNode(String nodeName, Node n) {
		
		if(!adjList.containsKey(nodeName)) {	//check if the node is not already in the map
			adjList.put(nodeName,n);
		}
	}
	
	
	//Method to add an arc to graph
	public void addArc(String from, String to, int propDelay, int capacity) {
		adjList.get(from).addArc(to, propDelay, capacity);
		adjList.get(to).addArc(from, propDelay, capacity);
	}
	
	
	//Find Shortest Hop Path (Kinda Dijkstra's or BFS with priority Queue of states)
	public ArrayList<String> SHP(String from, String to) {
		//toVisit = new PriorityQueue<State>();
		toVisit.clear();
		Boolean found = false;
		
		State currentState = new State(from);		//creating initial state
		toVisit.add(currentState);
		
		while(!toVisit.isEmpty() && !found) {
			currentState = toVisit.poll();		//de-queue current state
			Node currNode = adjList.get(currentState.getLastNode());	//get current location from current state
			ArrayList<Arc> successors = currNode.getArcs();			//get current location successors/edges/links
			
			for(int i = 0; i < successors.size(); i ++) {
				String nextNode = successors.get(i).getName();
				if(!currentState.getPath().contains(nextNode)) {	//Optimisation to ensure nodes already in the path are not added again
					State newState = new State(currentState.getPath(), nextNode, currentState.getCost(),1);		//create new updated state
					if(nextNode.equalsIgnoreCase(to)) {		//if the goal node has been generated stop
						found = true;
						currentState = newState;
						break;
					} else {		//otherwise add to priority queue
						toVisit.add(newState);
					}
				}
			}	
		}
		
		return currentState.getPath();		//ATM just returning the shortest path
	}
	
	
	//Find Shortest Delay Path (Kinda Dijkstra's or BFS with priority Queue of states)
	public ArrayList<String> SDP(String from, String to) {
		//toVisit = new PriorityQueue<State>();
		toVisit.clear();
		State currentState = new State(from);	//creating initial state
		ArrayList<Arc> successors = new ArrayList<Arc>();
		toVisit.add(currentState);
		
		while(!toVisit.isEmpty()) {
			currentState = toVisit.poll();
			if(currentState.getLastNode().equalsIgnoreCase(to)) {		//stop if the popped state contains the end goal
				break;
			}
			
			Node currNode = adjList.get(currentState.getLastNode());	//get current location in graph state
			successors = currNode.getArcs();
			
			for(int i = 0; i < successors.size(); i ++) {
				String nextNode = successors.get(i).getName();
				if(!currentState.getPath().contains(nextNode)) {		//Optimisation to ensure nodes already in the path or not added
					State newState = new State(currentState.getPath(), nextNode, currentState.getCost(),successors.get(i).getPropDelay());
					toVisit.add(newState);
				}
				
			}	
		}
		//System.out.println("\nSDP Cost: " + currentState.getCost());
		return currentState.getPath();
	}
	

	public void print() {
		
		for(String key : adjList.keySet()) {
			System.out.print("Node(" + key + "): ");
			adjList.get(key).printArcs();
			System.out.println();
		}
		
		
		/* Testing
		ArrayList<Arc> test = adjList.get("A").getArcs();
		for(int i = 0; i < test.size(); i++) {
			System.out.println(test.get(i).getPropDelay());
		}
		*/
		
	}
	
}
