import java.util.*;
import java.io.*;
import java.math.BigInteger; 
import java.math.BigDecimal;


public class TriestBase implements DataStreamAlgo {
    /*
     * Constructor.
     * The parameter samsize denotes the size of the sample, i.e., the number of
     * edges that the algorithm can store.
     */

    private int msize;
	public TriestBase(int samsize) {
	    msize = samsize;
	

	}

    HashMap<Integer, HashSet<Integer>> map = new HashMap<Integer, HashSet<Integer>>();
    int t =0;
    int noOfEdgesSample = 0;
    int finalCount = 0;
    BigDecimal pi = BigDecimal.valueOf(1.0);

    
    public void handleEdge(Edge edge) {
	t= t+1;

	pi = BigDecimal.valueOf(1.0);//so the value of pi doesn't carry over from previous edge
	//System.out.println("t="+t);
	
	int firstNode = edge.u;
	int secondNode = edge.v;
	
	if(t <= msize){
	    if (!map.containsKey(firstNode)){
		HashSet<Integer> temp = new HashSet<Integer>();
	        temp.add(secondNode);
		map.put(firstNode, temp);}
	    else {
		HashSet<Integer> temp2 = map.get(firstNode);
		temp2.add(secondNode);
		map.put(firstNode, temp2);}
	    if (!map.containsKey(secondNode)){
		HashSet<Integer> temp3 = new HashSet<Integer>();
	        temp3.add(firstNode);
		map.put(secondNode, temp3);}
	    else {
		HashSet<Integer> temp4 = map.get(secondNode);
		temp4.add(firstNode);
		map.put(secondNode, temp4);}

	     
	     updateCounts(true, firstNode, secondNode);
	     noOfEdgesSample++;
	}
	
	else if(t > msize){

	    double tD = (double) t;
	    double mD = (double) msize;
	    
	    BigDecimal T = BigDecimal.valueOf(tD);
	    BigDecimal tminus1 = BigDecimal.valueOf((double)(t -1));
	    BigDecimal tminus2 = BigDecimal.valueOf((double)(t- 2));
        
  
	    BigDecimal nu = tminus1.multiply(T);
	    BigDecimal numerator = nu.multiply(tminus2);
 
	    BigDecimal M = BigDecimal.valueOf(mD);
	    BigDecimal mminus1 = BigDecimal.valueOf((double)(msize - 1));
	    BigDecimal mminus2 = BigDecimal.valueOf((double)(msize - 2));
	    
	    BigDecimal de = M.multiply(mminus1);
	    BigDecimal denom = de.multiply(mminus2);

	    /*int numerator = binomialCoeff(t-3, msize-3);
	      int denom = binomialCoeff(t, msize);*/

	    /*System.out.println("msize is" + msize);
	    System.out.println("numerator "+numerator);
	    System.out.println("denom " + denom);*/
		
	    BigDecimal eps = numerator.divide(denom,  3, BigDecimal.ROUND_HALF_UP);
	    BigDecimal one = BigDecimal.valueOf(1.0);


	    //choosing the maximum between 1 and epselon(numerator/denom)
	    if(eps.compareTo(one) == -1 || eps.compareTo(one) == 0){
		pi = one;}
	    else{
		pi = eps;}


	    //flip coin
	    int rand = (int)(t * Math.random());
	    //replacement case if rand <= msize. probability of rand being <= msize is (msize/t);
	    if(rand <= msize){
        	//System.out.println("has entered");
		
		//picking edge at random
		int doubleNoEdges = noOfEdgesSample*2;
		int random = (int)(doubleNoEdges * Math.random()) ;
		int req = 0;
		int remove1 = -1;
		HashSet<Integer> curSet = new HashSet<Integer>();

		
		for(Map.Entry<Integer, HashSet<Integer>> entry : map.entrySet()){
		    remove1 = entry.getKey();
		    curSet = entry.getValue();
		    req = req + curSet.size();
	
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
		
		//System.out.println("removed " + remove1 + " " + remove2);


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
		else /*(map.containsKey(firstNode))*/{
		    HashSet<Integer> tem2 = map.get(firstNode);
		    tem2.add(secondNode);
		    map.put(firstNode, tem2);}
		if (!map.containsKey(secondNode)){
		    HashSet<Integer> tem3 = new HashSet<Integer>();
		    tem3.add(firstNode);
		    map.put(secondNode, tem3);}
		else /*(map.containsKey(secondNode))*/{
		    HashSet<Integer> tem4 = map.get(secondNode);
		    tem4.add(firstNode);
		    map.put(secondNode, tem4);}
	     
		updateCounts(true, firstNode, secondNode);
		noOfEdgesSample++;
	   	    
	    }

	   	
	}


    }



    /*public int binomialCoeff(int n, int k) 
    { 
        int C[] = new int[k + 1]; 
        
        C[0] = 1;   
       
        for (int i = 1; i <= n; i++) 
        { 

            for (int j = Math.min(i, k); j > 0; j--) 
                C[j] = C[j] + C[j-1]; 
        } 
        return C[k]; 
	} */
    
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

	BigDecimal count = BigDecimal.valueOf((double)finalCount);

        BigDecimal estimate = count.multiply(pi);
	//System.out.println("finalCount " + finalCount);
	//System.out.println("pi "+ pi);

	int toReturn = estimate.intValue();
	return toReturn;

	} // You shouldn't return 0 ;-)
}
