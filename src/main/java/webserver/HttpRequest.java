package webserver;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.*;

public class HttpRequest {
    
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class;
    
    final private BufferedReader br;
    final private Map<String, String> header;
    final private Map<String, String> parameter;
    final private String method;


    public HttpRequest(final InputStream in) throws IOException {
        // 1. br
        this.br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        // 2. method
        String line = br.readLine();
        if (line == null) {
            return;
        }
        log.debug("request line : {}", line);
        String[] tokens = line.split(" ");
        this.method = tokens[0];
        // 3. header
        
        // 4. parameter
    }


    public Object[] getMethod() {
        return null;
    }
    
    public String getPath() {
        return null;
    }

    public String getHeader(String headerName) {
        return header.get(headerName);
    }

    public String getParameter(String parameterName) {
        return parameter.get(parameterName); 
    }


}
