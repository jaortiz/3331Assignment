import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RoutingPerformance {
	public static void main(String[] args) {
		//Arguments: Circuit/Packet [0], SHP/SDP/LLP [1], topology.txt [2], workload.txt [3] packetRate
		
		//
		Scanner topologySC = null;
		Scanner workSC = null;
		String str;
		String[] topology;
		int packetRate = 0;
		String line;
		String[] work;
		double start = 0.0, duration = 0.0;
		int blocked = 0;
		
		
		Graph g = new Graph();
		
		String node1 = null, node2 = null;
		int propDelay;	//Propagation Delay
		int linkCapacity;
		
		String networkScheme = args[0];
		String routingScheme = args[1];
		packetRate = Integer.parseInt(args[4]);
		
		System.out.println("Network Scheme: " + networkScheme);
		System.out.println("Routing Scheme: " + routingScheme);
		
		//try and catch block for topology.txt
		try {
			topologySC = new Scanner(new FileReader(args[2]));
			
		} catch (IOException e) {							
			System.out.println("File not Found");
			System.exit(1);
		} 
		
		//reading topology.txt
		while(topologySC.hasNext()) {
			str = topologySC.nextLine();
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
		
		//Reading in workload.txt
		try {
			workSC = new Scanner(new FileReader(args[3]));
		} catch (IOException e) {							
			System.out.println("File not Found");
			System.exit(1);
		} 
	
	
		//int time = 0;
		int virtualCircuitRequests = 0;
		
		//reading in workload line by line
		if(networkScheme.equalsIgnoreCase("CIRCUIT")) {
			while(workSC.hasNext()) {
				//is the lowest time to end less than our current time to start
				//if yes finish lowest time and repeat
				
				line = workSC.nextLine();
				work = line.split("\\s+");
				node1 = work[1];
				node2 = work[2];
				start = Double.parseDouble(work[0]);
				duration = Double.parseDouble(work[3]);
				
				virtualCircuitRequests++;
				
				while(!g.getConnections().isEmpty() && g.getConnections().peek().getEndTime() <  start){
					g.updateConnections();
				}
				
				g.createConnection(node1,node2,start,duration,networkScheme,routingScheme, packetRate);
				//System.out.println("connection from " + node1  +  node2);
			}
		} else if(networkScheme.equalsIgnoreCase("PACKET")) {
			
		}
		//Printing out statistics
		System.out.println("total number of virtual circuit requests: " + virtualCircuitRequests);
		System.out.println("total number of packets: " + g.getNumPackets());
		System.out.println("total number of successsfully routed packets: " + g.getNumSuccessPackets());
		System.out.printf("percentage of successfully routed packets: %.2f\n" , (double)((g.getNumSuccessPackets() * 1.0/g.getNumPackets()))*100);
		System.out.println("number of blocked packets: " + (g.getNumPackets() - g.getNumSuccessPackets()));
		System.out.printf("percentage of successfully routed packets: %.2f\n" , (double)((1 - g.getNumSuccessPackets() * 1.0/g.getNumPackets()))*100);
		System.out.printf("average number of hops per circuit: %.2f\n" , (double)(g.getTotalHops()*1.0/g.getTotalConnections()));
		System.out.printf("average cumulative propagation delay per circuit: %.2f\n" , (double)(g.getTotalPropDelay()*1.0/g.getTotalConnections()));

		//Testing
		//g.print();
		/*
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
		 */
	}
	
}
