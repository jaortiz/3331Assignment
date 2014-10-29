import java.util.ArrayList;


public class Node {

	private String name;		//node name
	private ArrayList<Arc> arcs;
	
	/**
	 * Default constructor
	 * 
	 * @param node name
	 */
	public Node(String n) {
		name = n;
		arcs = new ArrayList<Arc>();
	}
	
	/**
	 * Method to add an arc/edge/link to this node
	 * 
	 * @param to			connection to
	 * @param propDelay		propagation delay of this connection
	 * @param capacity		capacity of this link
	 */
	public void addArc(String to, int propDelay, int capacity) {
		arcs.add(new Arc(to, propDelay, capacity));
	}
	
	
	/**
	 * 
	 * @param to node to
	 * @return	the arc of the to node if it connects to this node
	 */
	public Arc getArc(String to) {
		Arc arc = null;
		for(int i =0;i < arcs.size(); i++) {
			if(arcs.get(i).getName().equalsIgnoreCase(to)) {
				arc = arcs.get(i);
				break;
			}
		}
		return arc;
		
	}
	
	
	/**
	 * 
	 * @return arcs/edges/links to this node
	 */
	public ArrayList<Arc> getArcs() {
		return arcs;
	}
	
	
	/**
	 * Method to print the arcs/edges/links connected to this node
	 */
	public void printArcs() {
		for(int i = 0; i < arcs.size(); i ++) {
			if(i == arcs.size() - 1) {
				System.out.print(arcs.get(i).getName());
			} else {
				System.out.print(arcs.get(i).getName() + " -> ");
			}
		}
	}
	
}
