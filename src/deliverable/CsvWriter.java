package deliverable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
public class CsvWriter {
	private static Logger logger = Logger.getLogger(CsvWriter.class.getName());
	private CsvWriter() {
	    throw new IllegalStateException("Utility class");
	  }

	public static void write(List <String> ultimateList,List <Integer> fixedTicket)  {
		try (FileWriter csvWriter = new FileWriter("STDCXX-TicketFixed.csv")){
		csvWriter.append("Fixed Ticket");
        csvWriter.append(";");
        csvWriter.append("Data");
        csvWriter.append("\n");
        for(int v = 0; v < ultimateList.size();v++) {
       	 csvWriter.append(ultimateList.get(v));
       	 csvWriter.append(";");
       	 csvWriter.append(fixedTicket.get(v).toString());
       	 csvWriter.append("\n");
       	 
        }
        csvWriter.flush();
        } catch (IOException e) {
        	logger.log(Level.INFO,"Errore");
        }
	}
}
