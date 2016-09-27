/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import java.util.HashMap;

/**
 *
 * @author Digaly
 */
public class Request {
    private HashMap headerFields;
    private HashMap getParameters;
    private HashMap postParameters;
    private final String method;
    private final String URI;
    private final String version;
    
    
    public Request(String requestString) {
        headerFields = new HashMap();
        getParameters = new HashMap();
        postParameters = new HashMap();
        
        String[] requestData = requestString.split("\r\n");
        String[] startLine = requestData[0].split(" ");
        method = startLine[0];
        URI = startLine[1];
        version = startLine[2];
        
        if (URI.contains("?")) {
            String parameterString = URI.split("\\?")[1];
            String[] getParams = parameterString.split("&");
            
            for (String param : getParams) {
                String[] parameter = param.split("=");

                getParameters.put(parameter[0], parameter[1]);
            }
            
        }
        
        for (int i = 1; i < requestData.length; i++) {
            String[] headerField = requestData[i].split(":");
            
            if (headerField.length > 1) {
                headerField[1] = headerField[1].trim();
                headerFields.put(headerField[0], headerField[1]);
            } else {
                headerFields.put(headerField[0], "");
            }
                        
        }
    }
    
    public String getMethod() {
        return method;
    }
    
    public String getURI() {
        return URI;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void loadPOSTParameters(String in) {
        String[] postParameterList = in.split("&");
        
        for (int i = 0; i < postParameterList.length; i++) {
            String[] parameter = postParameterList[i].split("=");

            postParameters.put(parameter[0], parameter[1]);
        }
    }
    
    public String getHeader(String name) {
        return headerFields.get(name).toString();
    }
    
    public String getGETParameter(String name) {
        return (String) getParameters.get(name);
    }
    
    public String getPOSTParameter(String name) {
        return (String) postParameters.get(name);
    }
    
}
