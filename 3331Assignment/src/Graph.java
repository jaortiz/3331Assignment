import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
	
public class Graph {

	//private ArrayList<Node> nodes;
	private Map<String, Node> adjList; 	//Used to store the adjacency list, where each map pair has key<name,node> and each node contains edges connected to it
	private PriorityQueue<State> toVisit;
	private PriorityQueue<Connection> connections;
	private int numPackets = 0;			//total number of packets sent
	private int numSuccessPackets = 0;	//total number of successful packets
	private int totalHops = 0;			//total number of hops
	private int totalConnections = 0;	//total number of successful connections
	private int totalPropDelay = 0;		//total propagation delay
	int totalLoad = 0;
	int cumulativeLoad = 0;
	
	/**
	 * Default Constructor
	 */
	public Graph() {
		adjList =  new HashMap<String, Node>();	
		toVisit = new PriorityQueue<State>();
		connections = new PriorityQueue<Connection>();
	}

	
	/**
	 * Method to add a node to the graph
	 * 
	 * @param nodeName	node to adds name
	 * @param n			Node object to add
	 */
	public void addNode(String nodeName, Node n) {
		
		if(!adjList.containsKey(nodeName)) {	//check if the node is not already in the map
			adjList.put(nodeName,n);
		}
	}
	
	
	/**
	 * Method to add an arc/edge/link to the graph
	 * 	
	 * @param from			From node
	 * @param to			To node
	 * @param propDelay		Propagation delay of the link
	 * @param capacity		Capacity of the link
	 */
	public void addArc(String from, String to, int propDelay, int capacity) {
		adjList.get(from).addArc(to, propDelay, capacity);		//adding connection to both nodes
		adjList.get(to).addArc(from, propDelay, capacity);
	}
	
	
	/**
	 * Find Shortest Hop Path (SHP)
	 * Uses Disjkstra's/BFS with a priority queue of states, where each connection between nodes is 1.
	 * 
	 * @param from		Start node
	 * @param to		Goal node
	 * @return			ArrayList<String> containing the SHP path
	 */
	public ArrayList<String> SHP(String from, String to) {
		//toVisit = new PriorityQueue<State>();
		toVisit.clear();
		Boolean found = false;
		
		State currentState = new State(from);		//creating initial state
		toVisit.add(currentState);
		ArrayList<Arc> successors = new ArrayList<Arc>();
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
					State newState = new State(currentState.getPath(), nextNode, currentState.getCost(),1);
					toVisit.add(newState);
				}
			}	
		}
		return currentState.getPath();		//return the shortest hop path
	}
	
	
	/**
	 * Find the Shortest Delay Path
	 * Uses Disjkstra's/BFS with a priority queue of states, where each connection between nodes is the propagation delay of the link.
	 * 
	 * @param from	Start Node
	 * @param to	Goal Node
	 * @return		ArrayList<String> containing the shortest delay path
	 */
	public ArrayList<String> SDP(String from, String to) {
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
	
	
	/**
	 * Find the Least Loaded Path
	 * Uses Disjkstra's/BFS with a priority queue of states. Overall cost isn't additive but the least loaded path is always selected when
	 * determining which node we should go next to.
	 * 
	 * @param from	Start Node
	 * @param to	Goal Node
	 * @return		ArrayList<String> containing the least loaded path
	 */
	public ArrayList<String> LLP(String from, String to) {
		//toVisit = new PriorityQueue<State>();
		toVisit.clear();
		Boolean found = false;
		ArrayList<Arc> successors = new ArrayList<Arc>();
		State currentState = new State(from);		//creating initial state
		toVisit.add(currentState);
		boolean maxSet = false;
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
					State newState = new State(currentState.getPath(), nextNode);		//create new updated state
					double maxLoad = getMax(newState.getPath());	//find the maximum load on a path
					newState.setLoad(maxLoad);	//add the maximum load to the state
					toVisit.add(newState);		//add to priority queue
				}
			}	
		}
		//System.out.println("\nSDP Cost: " + currentState.getCost());
		return currentState.getPath();	
	}
	
	
	/**
	 * Finds the maximum load/traffic on a given path i.e. the maximum load on any given link
	 * 
	 * @param path
	 * @return
	 */
	private double getMax(ArrayList<String> path) {
		double max = 0.0;
		
		for(int i = 0; i < path.size() - 1;i++) {
			if(adjList.get(path.get(i)).getArc(path.get(i + 1)).getLoad() > max) {
				max  = adjList.get(path.get(i)).getArc(path.get(i + 1)).getLoad();
			}
		}
		
		return max;
	}


	/**
	 * Method used to create the connections in Circuit Switching
	 * 
	 * @param startNode
	 * @param endNode
	 * @param start
	 * @param duration
	 * @param networkScheme
	 * @param routingScheme
	 * @param packetRate
	 */
	public void createConnection(String startNode, String endNode, double start, double duration, String networkScheme,String routingScheme, int packetRate) {
		if(routingScheme.equalsIgnoreCase("SHP")) {
			ArrayList<String> path = SHP(startNode,endNode);		//find SHP path
			
			numPackets += (int) Math.ceil((duration * packetRate));	//getting number of packets to be sent in this connection
			
			for(int i = 0;i < path.size() - 1;i++) {
				if(adjList.get(path.get(i)).getArc(path.get(i + 1)).isFull()) {	//checking if the path we want to go on is full
					return;		//if full return i.e. block this connection
				}
			}
	
			//otherwise path is successful, now update the traffic on the paths
			for(int i = 0;i < path.size() - 1;i++) {
				adjList.get(path.get(i)).getArc(path.get(i + 1)).incrementTraffic(); 
				adjList.get(path.get(i + 1)).getArc(path.get(i)).incrementTraffic();
			}
			
			numSuccessPackets  += (int) Math.ceil((duration * packetRate));		//adding to the number of successful packets sent
			connections.add(new Connection(startNode, endNode, start, duration,numPackets, path));	//add the made connection to the priority queue
			totalConnections ++;	//incrementing total successful connections
			totalHops += path.size() - 1;	//adding to the total hops
			totalPropDelay  += getDelay(path);	//adding to the total propagation delay
			
		} else if(routingScheme.equalsIgnoreCase("SDP")) {
			ArrayList<String> path = SDP(startNode,endNode);	//find SDP path
			
			numPackets += (int) Math.ceil((duration * packetRate));	//getting number of packets to be sent in this connection
			
			for(int i = 0;i < path.size() - 1;i++) {
				if(adjList.get(path.get(i)).getArc(path.get(i + 1)).isFull()) {	//checking if the path we want to go on is full
					return;		//if full return i.e. block this connection
				}
			}
	
			//otherwise path is successful, now update the traffic on the paths
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
		}
	}
	
	
	/**
	 * Method to create connections for Packet Switching
	 * 
	 * @param startNode
	 * @param endNode
	 * @param start
	 * @param duration
	 * @param networkScheme
	 * @param routingScheme
	 */
	public void createConnection(String startNode, String endNode, double start, double duration, String networkScheme,String routingScheme) {
		if(routingScheme.equalsIgnoreCase("SHP")) {
			ArrayList<String> path = SHP(startNode,endNode);
			
			numPackets++;	//only have to increment since each connection in packet switching is one packet
			
			for(int i = 0;i < path.size() - 1;i++) {
				if(adjList.get(path.get(i)).getArc(path.get(i + 1)).isFull()) {
					//System.out.println("start = " + startNode + " end = " + endNode + " starting at " + start);
					//System.out.println("wtf");
					return;
				}
			}
	
			for(int i = 0;i < path.size() - 1;i++) {
				adjList.get(path.get(i)).getArc(path.get(i + 1)).incrementTraffic(); 
				adjList.get(path.get(i + 1)).getArc(path.get(i)).incrementTraffic();
				totalLoad++;
			}
			
			numSuccessPackets++; //only have to increment since each connection in packet switching is one packet
			connections.add(new Connection(startNode, endNode, start, duration,1, path));	
			totalConnections ++;	//incrementing total successful connections
			totalHops += path.size() - 1;
			totalPropDelay  += getDelay(path);
			
		} else if(routingScheme.equalsIgnoreCase("SDP")) {
			ArrayList<String> path = SDP(startNode,endNode);
			
			numPackets++;	//only have to increment since each connection in packet switching is one packet
			
			for(int i = 0;i < path.size() - 1;i++) {
				if(adjList.get(path.get(i)).getArc(path.get(i + 1)).isFull()) {
					return;
				}
			}
	
			for(int i = 0;i < path.size() - 1;i++) {
				adjList.get(path.get(i)).getArc(path.get(i + 1)).incrementTraffic(); 
				adjList.get(path.get(i + 1)).getArc(path.get(i)).incrementTraffic();
				totalLoad++;
			}
			
			numSuccessPackets++; //only have to increment since each connection in packet switching is one packet
			connections.add(new Connection(startNode, endNode, start, duration,1, path));	
			totalConnections ++;	//incrementing total successful connections
			totalHops += path.size() - 1;
			totalPropDelay  += getDelay(path);
			
		} else if(routingScheme.equalsIgnoreCase("LLP")) {
			ArrayList<String> path = LLP(startNode,endNode);
			numPackets++;	//only have to increment since each connection in packet switching is one packet
				
			for(int i = 0;i < path.size() - 1;i++) {
				if(adjList.get(path.get(i)).getArc(path.get(i + 1)).isFull()) {
					//System.out.println("start = " + startNode + " end = " + endNode + " starting at " + start);
					//System.out.println("wtf");
					return;
				}
			}
			
			for(int i = 0;i < path.size() - 1;i++) {
				adjList.get(path.get(i)).getArc(path.get(i + 1)).incrementTraffic(); 
				adjList.get(path.get(i + 1)).getArc(path.get(i)).incrementTraffic();
				totalLoad++;
			}
			
			numSuccessPackets++; //only have to increment since each connection in packet switching is one packet
			connections.add(new Connection(startNode, endNode, start, duration,1, path));	
			totalConnections ++;	//incrementing total successful connections
			totalHops += path.size() - 1;
			totalPropDelay  += getDelay(path);
		}
		cumulativeLoad += totalLoad;
	}
	

	/**
	 * Method to print the graph
	 */
	public void print() {
		for(String key : adjList.keySet()) {
			System.out.print("Node(" + key + "): ");
			adjList.get(key).printArcs();
			System.out.println();
		}
	}

	
	/**
	 * Method to update a finished connection.
	 */
	public void updateConnections() {
		Connection c = connections.poll();		//remove connection that has ended i.e. connection at front of queue
		removeTraffic(c.getPath());				//remove the traffic that path used
	}

	
	/**
	 * Method to remove the traffic on a link used by a connection
	 * 
	 * @param tempPath	the path we want to remove traffic/load from
	 */
	private void removeTraffic(ArrayList<String> tempPath) {
		for(int i = 0;i < tempPath.size() - 1;i++) {
			adjList.get(tempPath.get(i)).getArc(tempPath.get(i + 1)).decrementTraffic(); 	//remove the traffic 
			adjList.get(tempPath.get(i + 1)).getArc(tempPath.get(i)).decrementTraffic();
			totalLoad--;
		}
		
	}
	
	
	/**
	 * Method to get the total delay for a path
	 * 
	 * @param tempPath		path to get delay of
	 * @return				total delay of the path
	 */
	private int getDelay(ArrayList<String> tempPath) {
		int delay = 0;
		for(int i = 0;i < tempPath.size() - 1;i++) {
			delay += adjList.get(tempPath.get(i)).getArc(tempPath.get(i + 1)).getPropDelay(); 
		}
		return delay;
	}

	
	/**
	 * 
	 * @return	connections queue
	 */
	public PriorityQueue<Connection> getConnections() {
		return connections;
	}


	/**
	 * 
	 * @return the number of packets
	 */
	public int getNumPackets() {
		return numPackets;
	}


	/**
	 * 
	 * @return the number of successfully sent packets
	 */
	public int getNumSuccessPackets() {
		return numSuccessPackets;
	}


	/**
	 * 
	 * @return the total number of connections made
	 */
	public int getTotalConnections() {
		return totalConnections;
	}

	/**
	 * 
	 * @return the total number of hops 
	 */
	public int getTotalHops() {
		return totalHops;
	}


	/**
	 * 
	 * @return the total propagation delay
	 */
	public int getTotalPropDelay() {
		return totalPropDelay;
	}


	/**
	 * @return the totalLoad
	 */
	public double getAvgTotalLoad() {
		return 1.0*cumulativeLoad/totalConnections;
	}

	
	
}
