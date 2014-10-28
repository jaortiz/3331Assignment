import java.util.ArrayList;


public class Node {

	private String name;
	private ArrayList<Arc> arcs;
	
	//constructor
	public Node(String n) {
		name = n;
		arcs = new ArrayList<Arc>();
	}
	
	//add arc to node
	public void addArc(String to, int propDelay, int capacity) {
		arcs.add(new Arc(to, propDelay, capacity));
	}
	
	
	//Getter method to get the arcs/edges connected to this node
	public ArrayList<Arc> getArcs() {
		return arcs;
	}
	
	
	//Print arcs/edges connected to this node
	public void printArcs() {
		for(int i = 0; i < arcs.size(); i ++) {
			if(i == arcs.size() - 1) {
				System.out.print(arcs.get(i).getName());
			} else {
				System.out.print(arcs.get(i).getName() + " -> ");
			}
		}
	}
	
	//get node arc
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
}
