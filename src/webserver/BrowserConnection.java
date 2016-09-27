package webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;


/*
 * Simple class to abstract away from sockets, input-, and output-streams.
 * Usage:
 * 	BrowserConnection.listen( <PORT:int>) allows for connections at port <PORT>.
 *
 * 	BrowserConnection.accept() waits for a browser to communicate at this port.
 * 	BrowserConnection.accept() returns a BrowserConnection instance.
 *
 * 	These instances support the following methods:
 * 		readLine()	to read from the request by a browser (line by line)
 * 		write()	to write a part of the response to a browser (end line by \r\n)
 * 		close()	to terminate a conversation with a browser cleanly 
 */
public class BrowserConnection {
	
	public static ServerSocket ss;
	
	private Socket         s;
	private BufferedReader in;
	private PrintWriter    out;
	
	private BrowserConnection(Socket s){
		try {
			this.s = s;
			in  = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream());
		} catch (IOException ex) {
			System.err.println("Failed accept connection ...");
		}
	}

	public static BrowserConnection accept(){
		try {
			return new BrowserConnection(ss.accept());
		} catch (IOException ex) {
			return accept();
		}
	}

	public static void listen(int port){
		try {
			ss = new ServerSocket(port);
		} catch (IOException ex) {
			System.err.println("Failed to start listening on port "+port);
			System.err.println("Shutting down ...");
			System.exit(-1);
		}
	}

	public String readLine(){
		try {
			return in.readLine();
		} catch (IOException ex) {
			System.err.println("Failed to read");
			System.err.println("Shutting down ...");
			System.exit(-1);
		}
		return null;
	}

	public void write(String str){
		out.print(str);
		out.flush();
	}

	public void writeFile(String filepath){
		try {
			s.getOutputStream().write(Files.readAllBytes(Paths.get(filepath)));
		} catch (IOException ex) {
			System.err.println("Failed to read file "+filepath);
		}
		
	}

	protected void close(){
		try {
			in.close();
			out.close();
			s.close();		
		} catch (IOException ex) {
			System.err.println("Failed to close connection");
		}
		
	}

	void read(char[] buffer) {
		try {
			in.read(buffer);
		} catch (IOException ex) {
			System.err.println("Failed to read into char[]");
		}
	}

}
