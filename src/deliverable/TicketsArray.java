package deliverable;

import java.util.List;

public class TicketsArray {
	private TicketsArray() {
	    throw new IllegalStateException("Utility class");
	  }
	public static void write(List <String> finalList,List <Integer> fixedTicket) {
		Integer m = 0;
        Integer index = 0;
        Integer value = 0;
        for(; m < finalList.size();m++) {
       	 if(finalList.get(m) == null){
   			 value= 0;
       		 fixedTicket.set(index, value);
       		 break;
   			 
   		 }	 
       	 else if(finalList.get(m).equals(finalList.get(m+1))) {
       			 value = value +1;
       			 fixedTicket.set(index, value);
       		 }
       		  
       	 else {
       			 value = value +1;
       			 fixedTicket.set(index, value);
       			 index = index +1;
       			 value = 0;
       		 }
       	 
        }
	}
}
