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
	private PriorityQueue<Connection> connections;
	private int numPackets = 0;			//total number of packets sent
	private int numSuccessPackets = 0;	//total number of successful packets
	private int totalHops = 0;			//total number of hops
	private int totalConnections = 0;	//total number of successful connections
	private int totalPropDelay = 0;		//total propagation delay
	
	//Default Constructor
	public Graph() {
		adjList =  new HashMap<String, Node>();
		toVisit = new PriorityQueue<State>();
		connections = new PriorityQueue<Connection>();
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
						/*
						if(!successors.get(i).isFull()) {	//check if can make connection i.e. if link is not full
						*/
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
	
	//Find Least Loaded Path 
		public ArrayList<String> LLP(String from, String to) {
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
						State newState = new State(currentState.getPath(), nextNode, successors.get(i).getLoad());		//create new updated state
						if(nextNode.equalsIgnoreCase(to)) {		//if the goal node has been generated stop
							found = true;
							currentState = newState;
							break;
						} else {		//otherwise add to priority queue
							/*
							if(!successors.get(i).isFull()) {	//check if can make connection i.e. if link is not full
							*/
							toVisit.add(newState);
							
							
						}
					}
				}	
			}
			
			return currentState.getPath();	
		}
	
	
	//Method to create a connections
	public void createConnection(String startNode, String endNode, double start, double duration, String networkScheme,String routingScheme, int packetRate) {
		if(routingScheme.equalsIgnoreCase("SHP")) {
			ArrayList<String> path = SHP(startNode,endNode);
			
			numPackets += (int) Math.ceil((duration * packetRate));
			
			for(int i = 0;i < path.size() - 1;i++) {
				if(adjList.get(path.get(i)).getArc(path.get(i + 1)).isFull()) {
					return;
				}
			}
	
			for(int i = 0;i < path.size() - 1;i++) {
				adjList.get(path.get(i)).getArc(path.get(i + 1)).incrementTraffic(); 
				adjList.get(path.get(i + 1)).getArc(path.get(i)).incrementTraffic();
			}
			
			numSuccessPackets  += (int) Math.ceil((duration * packetRate));
			connections.add(new Connection(startNode, endNode, start, duration,numPackets, path));	
			totalConnections ++;	//incrementing total successful connections
			totalHops += path.size() - 1;
			totalPropDelay  += getDelay(path);
			
		} else if(routingScheme.equalsIgnoreCase("SDP")) {
			ArrayList<String> path = SDP(startNode,endNode);
			
			numPackets += (int) Math.ceil((duration * packetRate));
			
			for(int i = 0;i < path.size() - 1;i++) {
				if(adjList.get(path.get(i)).getArc(path.get(i + 1)).isFull()) {
					return;
				}
			}
	
			for(int i = 0;i < path.size() - 1;i++) {
				adjList.get(path.get(i)).getArc(path.get(i + 1)).incrementTraffic(); 
				adjList.get(path.get(i + 1)).getArc(path.get(i)).incrementTraffic();
			}
			
			numSuccessPackets  += (int) Math.ceil((duration * packetRate));
			connections.add(new Connection(startNode, endNode, start, duration,numPackets, path));	
			totalConnections ++;	//incrementing total successful connections
			totalHops += path.size() - 1;
			totalPropDelay  += getDelay(path);
			
		} else if(routingScheme.equalsIgnoreCase("LLP")) {
			ArrayList<String> path = LLP(startNode,endNode);
			numPackets += (int) Math.ceil((duration * packetRate));
			//if(!path.isEmpty()) {	//check if the end node was added/ CHECK THE LAST NODE ????
				
				for(int i = 0;i < path.size() - 1;i++) {
					if(adjList.get(path.get(i)).getArc(path.get(i + 1)).isFull()) {
						return;
					}
				}
				
				for(int i = 0;i < path.size() - 1;i++) {
					adjList.get(path.get(i)).getArc(path.get(i + 1)).incrementTraffic(); 
					adjList.get(path.get(i + 1)).getArc(path.get(i)).incrementTraffic();
				}
				
				numSuccessPackets  += (int) Math.ceil((duration * packetRate));
				connections.add(new Connection(startNode, endNode, start, duration,numPackets, path));	
				totalConnections ++;	//incrementing total successful connections
				totalHops += path.size() - 1;
				totalPropDelay  += getDelay(path);
			//}
		}
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

	
	//method to check if a connection is done and if so update the traffic 
	public void updateConnections() {
		/*ArrayList<String> tempPath;
		for(int i = 0; i < connections.size();i++) {
			if(connections.get(i).getEndTime() == time) {
				tempPath = connections.get(i).getPath();
				removeTraffic(tempPath);
				connections.remove(i);
			}
		}
		*/
		Connection c = connections.poll();
		removeTraffic(c.getPath());
	}

	//method to remove the traffic in a link when a connection is finished
	private void removeTraffic(ArrayList<String> tempPath) {
		for(int i = 0;i < tempPath.size() - 1;i++) {
			adjList.get(tempPath.get(i)).getArc(tempPath.get(i + 1)).decrementTraffic(); 
			adjList.get(tempPath.get(i + 1)).getArc(tempPath.get(i)).decrementTraffic();
		}
		
	}
	
	
	//method to get the total delay for a path
	private int getDelay(ArrayList<String> tempPath) {
		int delay = 0;
		for(int i = 0;i < tempPath.size() - 1;i++) {
			delay += adjList.get(tempPath.get(i)).getArc(tempPath.get(i + 1)).getPropDelay(); 
		}
		return delay;
	}

	
	//getter method to get connections
	public PriorityQueue<Connection> getConnections() {
		return connections;
	}


	/**
	 * @return the numPackets
	 */
	public int getNumPackets() {
		return numPackets;
	}


	/**
	 * @return the numSuccessPackets
	 */
	public int getNumSuccessPackets() {
		return numSuccessPackets;
	}


	public int getTotalConnections() {
		return totalConnections;
	}


	public int getTotalHops() {
		return totalHops;
	}


	public int getTotalPropDelay() {
		return totalPropDelay;
	}



	
}
