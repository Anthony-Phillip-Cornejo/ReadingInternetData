import java.io.*;
import java.net.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * This class provides the scaffolding to send an http request to the
 * Google Maps API, get a packet of XML data in return, and parse
 * that data into a DOM document (a tree-like 
 */
public class InternetInterfacer {

	public static void main(String[] args) throws IOException,
	ParserConfigurationException, SAXException {

		/*
		 * Set up the parameters to send a http query
		 */
		String base = "https://maps.googleapis.com/maps/api/directions/xml?";
		String origin = "origin=4680+Kalanianaole+Highway+Honolulu+96821";
		String dest = "destination=Kahala+Mall+Honolulu";
		String extras = "mode=walking";
		String key = "key=AIzaSyAbPoFI7ul8T8h_khn3r1LPBBJYatDSPIQ";
		String urlString = base + "&" + origin + "&" + dest + "&" +
				extras + "&" + key;
		URL url = new URL(urlString);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("GET");

		/*
		 * Set up a buffered reader to receive the response
		 */
		BufferedReader in = new BufferedReader(
				new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		/*
		 * Convert the response into a string and trim off leading
		 * characters which would throw the ]parser off
		 */
		String responseMessage = response.toString();
		responseMessage = responseMessage.trim().replaceFirst("^([\\W]+)<","<");

		/*
		 * Pass the XML string through the XML parser and convert it into
		 * a DOM document (a tree-like structure)
		 */
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = db.parse(new InputSource(new StringReader(responseMessage)));
		/*
		 * This is where you begin.  Figure out how the Document data structure
		 * works and how to find and extract the information you want from it.
		 * Use that information to output something useful and interesting to
		 * the end-user.  The document 'doc' contains all of the data from
		 * the XML parser, and this is the object that you'll want to work
		 * with.
		 */
		Node temp;
		
		System.out.println(responseMessage);
		System.out.println("");
		
		int loc = 0;
		int text = 0;
		for(int i = 0; i < 7; i++) {
			
			System.out.println("Step Number " + (i+1));
			System.out.println("");
			
			System.out.println("Travel Type");
			temp = doc.getElementsByTagName("travel_mode").item(i);
			System.out.println(temp.getTextContent());
			System.out.println("");
			
			if(i != 0) {
				loc++;
			}
			System.out.println("Starting Latitude");
			temp = doc.getElementsByTagName("lat").item(loc);
			System.out.println(temp.getTextContent());
			System.out.println("");
			
			System.out.println("Starting Longitude");
			temp = doc.getElementsByTagName("lng").item(loc);
			System.out.println(temp.getTextContent());
			System.out.println("");
			
			loc++;
			System.out.println("Ending Latitude");
			temp = doc.getElementsByTagName("lat").item(loc);
			System.out.println(temp.getTextContent());
			System.out.println("");
			
			System.out.println("Ending Longitude");
			temp = doc.getElementsByTagName("lng").item(loc);
			System.out.println(temp.getTextContent());
			System.out.println("");
			
			if(i != 0) {
				text++;
			}
			System.out.println("Duration");
			temp = doc.getElementsByTagName("text").item(text);
			System.out.println(temp.getTextContent());
			System.out.println("");
			
			System.out.println("Directions");
			temp = doc.getElementsByTagName("html_instructions").item(i);
			String instructions = temp.getTextContent();
			instructions = instructions.replace("<b>", "");
			instructions = instructions.replaceAll("</b>" , "");
			if(i == 6) {
				instructions = instructions.replaceAll("<div style=\"font-size:0.9em\">" , "");
				instructions = instructions.replace("</div>", "");
				instructions = instructions.replace("Ave", "Ave. Your ");
			}
			System.out.println(instructions);
			System.out.println("");
			
			text++;
			System.out.println("Distance");
			temp = doc.getElementsByTagName("text").item(text);
			System.out.println(temp.getTextContent());
			System.out.println("");
			
			if(i != 0 && i < 6) {
				System.out.println("Manuever");
				temp = doc.getElementsByTagName("maneuver").item(i);
				System.out.println(temp.getTextContent());
				System.out.println("");
			}
			
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}
		
		
	}
}
