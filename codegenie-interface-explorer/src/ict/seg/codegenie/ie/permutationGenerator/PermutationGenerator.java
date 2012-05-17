//This code was caught from: http://www.merriampark.com/perm.htm

//The source code is free for you to use in whatever way you wish.
//--------------------------------------
//Systematically generate permutations. 
//--------------------------------------

package ict.seg.codegenie.ie.permutationGenerator;


import java.math.BigInteger;
import java.util.ArrayList;

public class PermutationGenerator {

	private int[] a;
	private BigInteger numLeft;
	private BigInteger total;

	//-----------------------------------------------------------
	// Constructor. WARNING: Don't make n too large.
	// Recall that the number of permutations is n!
	// which can be very large, even when n is as small as 20 --
	// 20! = 2,432,902,008,176,640,000 and
	// 21! is too big to fit into a Java long, which is
	// why we use BigInteger instead.
	//----------------------------------------------------------

	public PermutationGenerator (int n) {
	  if (n < 1) {
	    throw new IllegalArgumentException ("Min 1");
	  }
	  a = new int[n];
	  total = getFactorial (n);
	  reset ();
	}

	//------
	// Reset
	//------
	
	public void reset () {
		
		  for (int i = 0; i < a.length; i++) {
			  a[i] = i;
		  }
		  
		  numLeft = new BigInteger (total.toString ());
	}
	
	//------------------------------------------------
	// Return number of permutations not yet generated
	//------------------------------------------------
	
	public BigInteger getNumLeft () {
		return numLeft;
	}
	
	//------------------------------------
	// Return total number of permutations
	//------------------------------------
	
	public BigInteger getTotal () {
		return total;
	}
	
	//-----------------------------
	// Are there more permutations?
	//-----------------------------
	
	public boolean hasMore () {
		return numLeft.compareTo (BigInteger.ZERO) == 1;
	}
	
	//------------------
	// Compute factorial
	//------------------
	
	private static BigInteger getFactorial (int n) {
		BigInteger fact = BigInteger.ONE;
		for (int i = n; i > 1; i--) {
			fact = fact.multiply (new BigInteger (Integer.toString (i)));
		}
		return fact;
	}
	
	//--------------------------------------------------------
	// Generate next permutation (algorithm from Rosen p. 284)
	//--------------------------------------------------------
	
	public int[] getNext () {
	
	  if (numLeft.equals (total)) {
	    numLeft = numLeft.subtract (BigInteger.ONE);
	    return a;
	  }
	
	  int temp;
	
	  // Find largest index j with a[j] < a[j+1]
	
	  int j = a.length - 2;
	  while (a[j] > a[j+1]) {
	    j--;
	  }
	
	  // Find index k such that a[k] is smallest integer
	  // greater than a[j] to the right of a[j]
	
	  int k = a.length - 1;
	  while (a[j] > a[k]) {
	    k--;
	  }
	
	  // Interchange a[j] and a[k]
	
	  temp = a[k];
	  a[k] = a[j];
	  a[j] = temp;
	
	  // Put tail end of permutation after jth position in increasing order
	
	  int r = a.length - 1;
	  int s = j + 1;
	
	  while (r > s) {
	    temp = a[s];
	    a[s] = a[r];
	    a[r] = temp;
	    r--;
	    s++;
	  }
	
	  numLeft = numLeft.subtract (BigInteger.ONE);
	  return a;
	
	}
	
	
	//Created by Rafael B. Januzi - 09/11/2011
	
	public static String[] getParametersPermutations(String[] parameters){
		
		//Parameters: "{parameter01 , parameter02 ,..., parameterN}"
		
		ArrayList<String> permutationsAux = new ArrayList<String>();
		PermutationGenerator perGen = new PermutationGenerator(parameters.length);
		String[] permutationsReturn;
		
		while(perGen.hasMore()){
			
			String strAux = new String();
			int[] indices = perGen.getNext();
			
			strAux = parameters[indices[0]];
			
			for(int i = 1; i < indices.length; i++)
				strAux += "," + parameters[indices[i]];
			
			permutationsAux.add(strAux);
			
		}
		
		permutationsReturn = new String[permutationsAux.size()];
		
		for(int i = 0; i < permutationsAux.size(); i++)
			permutationsReturn[i] = permutationsAux.get(i);
		
			
		
		return permutationsReturn; 
		
	}
}