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
  		 List <Month> month = new ArrayList();
  		 List <String> finalList = new ArrayList();
  		 List <Integer> fixedTicket = new ArrayList();
  		 Dictionary<String, LocalDate> dictionary = new Hashtable<String, LocalDate>();
  		 Integer year = 2007;
		   String projName ="STDCXX";
	   Integer j = 0, i = 0,n = 0, l = 0,v=0, total = 1; 
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
            String key = issues.getJSONObject(i%1000).get("key").toString();
            String data = issues.getJSONObject(i%1000).getJSONObject("fields").getString("resolutiondate");
            LocalDate resolutionDate = LocalDate.parse(data.substring(0,10));
            LocalDate dates = LocalDate.parse(data.substring(0,10));  //TRASFORMO LA DATA DA STRINGA A DATA

            //ticket.add(key);
            date.add(dates); //AGGIUNGO LA DATA NELLA LISTA DATE
            date.sort(null); //ORDINO LA LISTA SECONDO L'ANNO
            dictionary.put(key, resolutionDate);
           
            
      
         } 
         for (; n < date.size();n++) {
        	 month.add(date.get(n).getMonth())  ;
        	 finalList.add(month.get(n).toString()+date.get(n).getYear());
         }
         Integer m = 0;
         while(finalList.get(m+1) != null) {    //da migliorare come ciclare i fixed ticket
        	 Integer count = 0;
        	 Integer value = 0;
        	 if (finalList.get(m) == finalList.get(m+1)) {
        		 fixedTicket.add(count, value);
        		 if(m == finalList.size()) {
        			 break;
        		 }
        		 m++;
        	 }
        	 else {
        		 fixedTicket.add(count,1);
        		 count ++;
        		 if(m<finalList.size()) {
        			 break;
        		 }
        		 m++;
        	 }
         }
         /*for(;m < finalList.size();m++) {
        	 Integer count = 0;
        	 if (finalList.get(m) == finalList.get(m+1)) {
        		 fixedTicket.add(1, count);
        	 }
        	 else {
        		 fixedTicket.add(1,count);
        		 count ++;
        	 }
         }*/
         int occurrences = Collections.frequency(date,year);
         System.out.println(finalList);
         System.out.println(fixedTicket);
         Enumeration enu = dictionary.keys();
         Enumeration enu1 = dictionary.elements();
         FileWriter csvWriter = new FileWriter("STDCXX-TicketFixed.csv");
         csvWriter.append("Fixed Ticket");
         csvWriter.append(";");
         csvWriter.append("Data");
         csvWriter.append("\n");
         for(; v < fixedTicket.size();v++) {
        	 csvWriter.append(fixedTicket.get(v).toString());
        	 csvWriter.append(";");
        	 csvWriter.append("\n");
         }
         for(;l < finalList.size();l++) {
        	 csvWriter.append(finalList.get(l));
        	 csvWriter.append(";");
        	 csvWriter.append("\n");
         }
         /*for(i = 0 ; i<date.size();i++) {
        	 csvWriter.append(enu.toString());
        	 csvWriter.append(";");
        	 csvWriter.append(dictionary.values().toString());
        	 csvWriter.append("\n");
         }*/
        	 
         
         csvWriter.flush();
         csvWriter.close();
      } while (i < total);
      return;
   }

 
}
