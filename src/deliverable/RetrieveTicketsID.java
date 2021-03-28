package deliverable;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class RetrieveTicketsID {




   private static String readAll(Reader rd) throws IOException {
	      StringBuilder sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	         sb.append((char) cp);
	      }
	      return sb.toString();
	   }

   public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
      InputStream is = new URL(url).openStream();
      try {
    	 BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name()));
         String jsonText = readAll(rd);
         JSONArray json = new JSONArray(jsonText);
         return json;
       } finally {
         is.close();
       }
   }

   public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
      InputStream is = new URL(url).openStream();
      try {
    	 BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name()));
         String jsonText = readAll(rd);
         JSONObject json = new JSONObject(jsonText);
         return json;
       } finally {
         is.close();
       }
   }


  
  	   public static void main(String[] args) throws IOException, JSONException {
  		 List <LocalDate> date = new ArrayList();
  		 List <Integer> month = new ArrayList();
  		 List <String> finalList = new ArrayList();
  		 List <Integer> fixedTicket = new ArrayList();
		   String projName ="STDCXX";
	   Integer j = 0;  
	   Integer i = 0;
	   Integer n = 0;
	   Integer v=0;
	   Integer total = 1;
      //Get JSON API for closed bugs w/ AV in the project
      do {
         //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
         j = i + 1000;
         String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                + projName + "%22AND(%22status%22=%22closed%22OR"
                + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
                + i.toString() + "&maxResults=" + j.toString();
         JSONObject json = readJsonFromUrl(url);
         JSONArray issues = json.getJSONArray("issues");
         total = json.getInt("total");
         for (; i < total && i < j; i++) {
            //Iterate through each bug
            String data = issues.getJSONObject(i%1000).getJSONObject("fields").getString("resolutiondate");
            LocalDate dates = LocalDate.parse(data.substring(0,10));  //TRASFORMO LA DATA DA STRINGA A DATA
            date.add(dates); //AGGIUNGO LA DATA NELLA LISTA DATE
            date.sort(null); //ORDINO LA LISTA SECONDO L'ANNO
         } 
         for (; n < date.size();n++) {
        	 month.add(date.get(n).getMonthValue())  ;
        	 finalList.add(month.get(n).toString()+"-"+date.get(n).getYear());
         }
         finalList.add(null);
         Integer k =0;
         List <String> ultimateList = new ArrayList();
         for(;k<finalList.size();k++) {
        	 if (!ultimateList.contains(finalList.get(k))) {
        		  
                 ultimateList.add(finalList.get(k));
             }
         }
         for(int pasd = 0; pasd <ultimateList.size();pasd++) {
        	 fixedTicket.add(0);
         }
         Integer m = 0;
         Integer index = 0;
         Integer value = 0;
         for(; m < finalList.size();m++) {
        	 if(finalList.get(m) == null){
    			 value= 0;
    			 index = index;
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
         FileWriter csvWriter = new FileWriter("STDCXX-TicketFixed.csv");
         Integer mean = 0;
         csvWriter.append("Fixed Ticket");
         csvWriter.append(";");
         csvWriter.append("Data");
         csvWriter.append(";");
         csvWriter.append("Media");
         csvWriter.append("\n");
         for(; v < ultimateList.size();v++) {
        	 csvWriter.append(ultimateList.get(v));
        	 csvWriter.append(";");
        	 csvWriter.append(fixedTicket.get(v).toString());
        	 csvWriter.append("\n");
        	 
         }
         csvWriter.flush();
         csvWriter.close();
      } while (i < total);
   }

 
}
