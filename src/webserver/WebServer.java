/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import java.io.File;
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
            
            System.out.println("[INFO] New incoming connection");
            
            String line = conn.readLine();
            String firstLine = line;
            String method = firstLine.split(" ")[0];
            
            int lineWasEmpty = 0;
                        
            //String request = "";
            
            StringBuilder requestBuilder = new StringBuilder();
            requestBuilder.append(line);
            requestBuilder.append("\r\n");
            
            System.out.println("[INFO] Reading request...");
            
            while (conn.canRead() && lineWasEmpty < 1) {          
                line = conn.readLine();
                requestBuilder.append(line);
                requestBuilder.append("\r\n");
                
                if (line.equals("")) lineWasEmpty += 1;
                
            }
            System.out.println("[INFO] Read request.");

            Request requestInfo = new Request(requestBuilder.toString());
            
            if (lineWasEmpty == 1 && method.equals("POST")) {                
                char[] buffer = new char[Integer.parseInt(requestInfo.getHeader("Content-Length"))];
                while (conn.canRead()) {
                    conn.read(buffer);
                }
                
                String postParameters = new String(buffer);
                requestInfo.loadPOSTParameters(postParameters);
            }
            
            System.out.println(requestBuilder.toString());            
            
            String document = firstLine.split(" ")[1];
            
            if (document.equals("/")) document = "\\index.html";
            else document = "\\" + document.substring(1);
            

            if (document.equals("\\info.html")) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();

                conn.write(dateFormat.format(date));
            } else {
                File file = new File(DOCUMENT_ROOT + document);
                if (file.exists()) {
                    conn.writeFile(DOCUMENT_ROOT + document);
                }
                else {
                    conn.writeFile(DOCUMENT_ROOT + "\\error-404.html");
                }
            }
                        
            conn.close();
        }
    }

}
