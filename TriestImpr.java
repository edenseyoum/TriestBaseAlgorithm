import java.util.*;
import java.io.*;
import java.math.BigInteger; 
import java.math.BigDecimal;



public class TriestImpr implements DataStreamAlgo {
    /*
     * Constructor.
     * The parameter samsize denotes the size of the sample, i.e., the number of
     * edges that the algorithm can store.
     */


    private int msize;
    public TriestImpr(int samsize) {
	msize = samsize;
    }
    
    HashMap<Integer, HashSet<Integer>> map = new HashMap<Integer, HashSet<Integer>>();
    int t =0;
    int noOfEdgesSample = 0;
    
    int finalCount = 0;//number of triangles in sample
    int d = 0;//TriestImpr's estimate number of triangles for graph
    
    public void handleEdge(Edge edge) {
	t= t+1;
	//System.out.println("t= "+t);
	int firstNode = edge.u;
	int secondNode = edge.v;
	
	
	//case1: before sample is full
	if(t <= msize){
	    if (!map.containsKey(firstNode)){
		HashSet<Integer> temp = new HashSet<Integer>();
	        temp.add(secondNode);
		map.put(firstNode, temp);}
	    else /*(map.containsKey(firstNode))*/{
		HashSet<Integer> temp2 = map.get(firstNode);
		temp2.add(secondNode);
		map.put(firstNode, temp2);}
	    if (!map.containsKey(secondNode)){
		HashSet<Integer> temp3 = new HashSet<Integer>();
	        temp3.add(firstNode);
		map.put(secondNode, temp3);}
	    else /*(map.containsKey(secondNode))*/{
		HashSet<Integer> temp4 = map.get(secondNode);
		temp4.add(firstNode);
		/*for(int i: temp4){
		  System.out.println(" "+i);}*/
		map.put(secondNode, temp4);}

	     
	     updateCounts(true,  firstNode, secondNode);
	     noOfEdgesSample++;

	     d =  finalCount;
	}


	// after sample is full
	else if(t > msize){

	    double tD = (double) t;
	    double mD = (double) msize;

	    BigDecimal M = BigDecimal.valueOf(mD);
	    BigDecimal mminus1 = BigDecimal.valueOf(mD-1.0);
	    BigDecimal tminus1 = BigDecimal.valueOf(tD-1.0);
	    BigDecimal tminus2 = BigDecimal.valueOf(tD-2.0);

	    BigDecimal denom = M.multiply(mminus1);
	    BigDecimal numerator = tminus1.multiply(tminus2);


	    BigDecimal ba = numerator.divide(denom,  3, BigDecimal.ROUND_HALF_UP);
	    int incr = ba.intValue();
	 
	
	    

	    //to find g (triangles addes because of this edge)
	    int g = additionalTriangle(firstNode, secondNode);
	    
	    //incrementing D
	    int inter = g * incr;
	    d = d + inter;

	    
	    //flip coin
	    int rand = (int)(t * Math.random());
	    //replacement case if rand <= msize. probability of rand being <= msize is (msize/t);
	    if(rand <= msize){
		//System.out.println("has entered");
		//picking edges to remove
		int doubleNoEdges = noOfEdgesSample*2;
		int random = (int)(doubleNoEdges * Math.random()) ;
		int req = 0;
		int remove1 = -1;
		HashSet<Integer> curSet = new HashSet<Integer>();
		
		
		for(Map.Entry<Integer, HashSet<Integer>> entry : map.entrySet()){
		    remove1 = entry.getKey();
		    curSet = entry.getValue();
		    req = req + curSet.size();
		    if(req == 0){
			System.out.println("key with no hashset " + remove1);
			System.out.println("wtf hashset " +  map.get(remove1));}
		    if(req >= random && curSet.size()!=0){
			break;}
		}
		//System.out.println("remove1 " + remove1);
		//System.out.println("req " +req);
		
		int s = curSet.size();
		int k = 0;
		int remove2 = -1;
		int order = random -(req -s);
		//System.out.println("order " + order);
		for(int point: curSet){
		    remove2 = point;
		    k = k +1;
		    if (k == order){
			break;}
		}
		//System.out.println("k "+k);
		
		//System.out.println("remove2 " + remove2);

		
		//removing edge(toBeRemoved1, toBeRemoved2)
	
		HashSet<Integer> temp6 = map.get(remove1);
		HashSet<Integer> temp7 = map.get(remove2);
		temp6.remove(remove2);
		temp7.remove(remove1);
		map.put(remove1, temp6);
		map.put(remove2, temp7);
		    
		
		updateCounts(false, remove1, remove2);
		noOfEdgesSample--;
		
		if(temp6.size() == 0){
		    map.remove(remove1);}
		
		if(temp7.size() == 0){
		    map.remove(remove2);}



		
		//adding new edge
		if (!map.containsKey(firstNode)){
		    HashSet<Integer> tem = new HashSet<Integer>();
		    tem.add(secondNode);
		    map.put(firstNode, tem);}
		else {
		    HashSet<Integer> tem2 = map.get(firstNode);
		    tem2.add(secondNode);
		    map.put(firstNode, tem2);}
		if (!map.containsKey(secondNode)){
		    HashSet<Integer> tem3 = new HashSet<Integer>();
		    tem3.add(firstNode);
		    map.put(secondNode, tem3);}
		else {
		    HashSet<Integer> tem4 = map.get(secondNode);
		    tem4.add(firstNode);
		    map.put(secondNode, tem4);}
		
		updateCounts(true, firstNode, secondNode);
		noOfEdgesSample++;
		
		
	    }
	    
	}
    }



    //to find g
    public int additionalTriangle(int node1, int node2){
	if((!map.containsKey(node1)) || (!map.containsKey(node2))){
	    return 0;
	}
	else{
	HashSet<Integer> te1 = map.get(node1);
	HashSet<Integer> te2 = map.get(node2);


	int simil = 0;


	for(int i: te1){
	    for(int j: te2){
		if (i == j){
		    simil = simil + 1;}
	    }
	}
	
	return simil;
	}
    }
    
    

    public void updateCounts(boolean isInsertion, int node1, int node2){


	//adding new edge
	if(isInsertion == true){
	      HashSet<Integer> tempr1 = map.get(node1);
	      HashSet<Integer> tempr2 = map.get(node2);

	      
	      int similar = 0;
	      for(int i: tempr1){
		  for(int j: tempr2){
		      if (i == j){
			  similar = similar + 1;}
		  }
	      }
	      finalCount = finalCount + similar;
	}

	//removing edge
	else if(isInsertion == false){
	      HashSet<Integer> tempr1 = map.get(node1);
	      HashSet<Integer> tempr2 = map.get(node2);
	      

	      int similar = 0;
	      for(int i: tempr1){
		  for(int j: tempr2){
		      if (i == j){
			  similar = similar + 1;}
		  }
	      }
	      finalCount = finalCount - similar;
	}


    }
    

	public int getEstimate() { 
	    return  d; } // You shouldn't return 0 ;-)
}
