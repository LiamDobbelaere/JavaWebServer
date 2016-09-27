/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Digaly
 */
public class WebServer {

    public final static int PORT = 8080;
    public final static String DOCUMENT_ROOT = "C:\\Users\\Digaly\\Documents\\NetBeansProjects\\WebServer\\serveme";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BrowserConnection.listen(PORT);

        while (true) {
            BrowserConnection conn = BrowserConnection.accept();
            
            System.out.println("New incoming connection");
            
            String line = conn.readLine();
            String firstLine = line;
            int contentLength = 0;
            
            
            while (line.length() != 0) {
                System.out.println(line);
                
                if (line.startsWith("Content-Length")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
                
                line = conn.readLine();
            }
            
            String document = firstLine.split(" ")[1];
            
            if (document.equals("/")) document = "\\index.html";
            else document = "\\" + document.substring(1);
            /*String body = "";
            
            if (contentLength > 0) {
                body = conn.readLine();
                System.out.println("lol");
            }
            
            System.out.println(body);
            */
            
            System.out.println(firstLine);
            
            if (document.equals("\\info.html")) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();

                conn.write(dateFormat.format(date));
            } else conn.writeFile(DOCUMENT_ROOT + document);
           
            conn.close();
        }
    }

}
