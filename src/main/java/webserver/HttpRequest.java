package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    final private String method;
    final private String path;
    final private Map<String, String> headers = new HashMap();
    final private Map<String, String> paras;


    public HttpRequest(final InputStream in) {

        try {
            final private BufferedReader br = 
                    new BufferedReader(new InputStreamReader(in, "UTF-8"));

            // 1. Request Line - method, path
            String line = br.readLine();
            if (line == null) {
                return;
            }
            log.debug("request line : {}", line);
            String[] tokens = line.split(" ");
            this.method = tokens[0];
            this.path = tokens[1];

            // 2. header
            while (!(line = br.readLine()).equals("")) {
                tokens = line.split(": ");
                headers.put(tokens[0], tokens[1]);
            }

            // 3. parameter
            
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public String getParameter(String parameterName) {
        return paras.get(parameterName);
    }

}
