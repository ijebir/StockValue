import org.w3c.dom.*;
import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import java.util.Scanner;

public class StockValue{
        
        public static void main(String[] args) throws Exception{
                
                Scanner scan = new Scanner(System.in);
                System.out.println("Type Index");
                String index = scan.next();
                index = index.toUpperCase();

                String base = "http://query.yahooapis.com/v1/public/yql?q=";
                String YQL_Command_Base = "select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
                String YQL_Command_Append = "%22)&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
                String source = base + YQL_Command_Base + index + YQL_Command_Append;

                Double ret = getValue(source);
                System.out.println("The current value for " + index + " is " + ret);
        }

        public static Double getValue(String url) throws Exception{
                URL source = new URL(url);
                String USER_AGENT = "Mozilla/5.0";
                HttpURLConnection con = (HttpURLConnection) source.openConnection();

                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", USER_AGENT);

                //get response code from query 
                int responseCode = con.getResponseCode();
                System.out.println("Response Code : " + responseCode);

                //get Document from the inputStream
                InputStream xml = con.getInputStream();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(xml);

                //acess the quote node in the XML
                doc.getDocumentElement().normalize();
                NodeList stuff = doc.getElementsByTagName("quote");
                Node quote = stuff.item(0);

                //get the current stock value and return it
                Element e = (Element) quote;
                String ret = e.getElementsByTagName("LastTradePriceOnly").item(0).getTextContent();
                Double value = Double.parseDouble(ret);
                return value;
        }
}
