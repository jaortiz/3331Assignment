import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RoutingPerformance {
	public static void main(String[] args) {
		//Arguments: Circuit/Packet [0], SHP/SDP/LLP [1], topology.txt [2], workload.txt [3]
		
		Scanner sc = null;
		String str;
		String[] topology;
		
		Graph g = new Graph();
		
		String node1, node2;
		int propDelay;	//Propagation Delay
		int linkCapacity;
		
		String networkScheme = args[0];
		String routingScheme = args[1];
		
		System.out.println("Network Scheme: " + networkScheme);
		System.out.println("Routing Scheme: " + routingScheme);
		
		//try and catch block for topology.txt
		try {
			sc = new Scanner(new FileReader(args[2]));
			
		} catch (IOException e) {							
			System.out.println("File not Found");
			System.exit(1);
		} 
		
		//reading topology.txt
		while(sc.hasNext()) {
			str = sc.nextLine();
			topology = str.split("\\s+");
			node1 = topology[0];
			node2 = topology[1];
			propDelay = Integer.parseInt(topology[2]);
			linkCapacity = Integer.parseInt(topology[3]);
			
			//System.out.println(node1 + " " + node2 + " " + propDelay + " " + linkCapacity);
			
			g.addNode(node1,new Node(node1));		//adding nodes to graph
			g.addNode(node2,new Node(node2));
			
			g.addArc(node1, node2, propDelay, linkCapacity);	//adding arcs to graph
			
		}
		
		//g.print();
		
		//Testing Shortest Hop Path
		ArrayList<String> shp = g.SHP("A", "F");
		System.out.println();
		System.out.println("Shortest Hop Path");
		for(int i = 0; i < shp.size(); i++) {
			if(i == shp.size() - 1) {
				System.out.println(shp.get(i));
			} else {
				System.out.print(shp.get(i) + " -> ");
			}
		}
		
		
		//Testing Shortest Delay Path
		ArrayList<String> sdp = g.SDP("A", "F");
		System.out.println();
		System.out.println("Shortest Delay Path");
		for(int i = 0; i < sdp.size(); i++) {
			if(i == sdp.size() - 1) {
				System.out.println(sdp.get(i));
			} else {
				System.out.print(sdp.get(i) + " -> ");
			}
		}
		
	}
}
