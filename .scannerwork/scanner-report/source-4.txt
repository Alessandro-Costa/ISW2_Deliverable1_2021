package deliverable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.json.JSONArray;

public class RetrieveTicketsID {
	private RetrieveTicketsID() {
	    throw new IllegalStateException("Utility class");
	  }




   private static String readAll(Reader rd) throws IOException {
	      var sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	         sb.append((char) cp);
	      }
	      return sb.toString();
	   }

   public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
      
      try(InputStream is = new URL(url).openStream()) {
    	 var rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name()));
         String jsonText = readAll(rd);
         return new JSONArray(jsonText);
       } 
       
   }

   public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
      try(InputStream is = new URL(url).openStream()) {
    	 var rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name()));
         String jsonText = readAll(rd);
         return new JSONObject(jsonText);
         
       } 
   }


  
  	   public static void reportTicket() throws IOException, JSONException, GitAPIException {
  		 List <LocalDateTime> date = new ArrayList<>();
  		 List <Integer> month = new ArrayList<>();
  		 List <String> finalList = new ArrayList<>();
  		 List <Integer> fixedTicket = new ArrayList<>();
  		 List <RevCommit> commits = GetInfoCommit.commitList();
		 var projName ="STDCXX";
	   Integer j = 0;  
	   Integer i = 0;
	   Integer n = 0;
	   Integer k;
	   Integer pasd;
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
         var issues = json.getJSONArray("issues");
         total = json.getInt("total");
         for (; i < total && i < j; i++) {
            //Iterate through each bug
            var data = issues.getJSONObject(i%1000).getJSONObject("fields").getString("resolutiondate");
            var dates = LocalDateTime.parse(data.substring(0,16));  //TRASFORMO LA DATA DA STRINGA A DATA
            var ticketID = issues.getJSONObject(i%1000).get("key").toString();
            for(k = 0;k < commits.size();k++) {
         		String message = commits.get(k).getFullMessage();
         		if (message.contains(ticketID +",") || message.contains(ticketID +"\r") || message.contains(ticketID +"\n")|| message.contains(ticketID + " ") || message.contains(ticketID +":")
     					 || message.contains(ticketID +".")|| message.contains(ticketID + "/") || message.endsWith(ticketID) ||
     					 message.contains(ticketID + "]")|| message.contains(ticketID+"_") || message.contains(ticketID + "-") || message.contains(ticketID + ")") ) 
         		{
         			System.out.println("Sto entrando nell'if lunghissimo");
     				 if(dates.isAfter(commits.get(k).getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())) {
     					date.add(dates); //AGGIUNGO LA DATA NELLA LISTA DATE
     					System.out.println("Sto stampando la data del commit" + dates);
     				 }
     				 else {
     					 date.add(commits.get(k).getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
     					 System.out.println("Sto stampando la data di JIRA"+dates);
     				 }
         		}
     		 }
         } 
         date.sort(null);
         System.out.println(date);
         for (; n < date.size();n++) {
        	 month.add(date.get(n).getMonthValue());
        	 finalList.add(month.get(n).toString()+"-"+date.get(n).getYear());
         }
         finalList.add(null);
         Integer x=0;
         List <String> ultimateList = new ArrayList<>();
         for(;x<finalList.size();x++) {
        	 if (!ultimateList.contains(finalList.get(x))) {
        		  
                 ultimateList.add(finalList.get(x));
             }
         }
         for(pasd = 0; pasd <ultimateList.size();pasd++) {
        	 fixedTicket.add(0);
         }
         TicketsArray.write(finalList,fixedTicket);
         CsvWriter.write(ultimateList,fixedTicket);
         
      } while (i < total);
   }

 
}
