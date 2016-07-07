package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    final private RequestLine requestLine;
    private Map<String, String> headers;
    // parameters는 값이 없을 수도 있다. -> Optional ?
    private Map<String, String> parameters;


    public HttpRequest(final InputStream in) {

        String firstLine = null;
        
        try {
            final BufferedReader br = 
                    new BufferedReader(new InputStreamReader(in, "UTF-8"));

            // 1. Request Line - method, path, get-parameters
            firstLine = br.readLine();
            if (firstLine != null) {
                log.debug("request line : {}", firstLine);
                requestLine = new RequestLine(firstLine);
                if (requestLine.getMethod() == "GET" && requestLine.getParameters() != null)
                    this.parameters = requestLine.getParameters();

                // 2. parameter
                final List<String> allLines = br.lines()
                        .filter(s -> !(s.equals("")))
                        .collect(Collectors.toList());
                if (this.requestLine.isPost()) {
                    final String paramLine = allLines.get(allLines.size() - 1);
                    allLines.remove(allLines.size() -1);
                    parameters = Arrays.stream(paramLine.split("&"))
                            .map(s -> s.split("="))
                            .collect(Collectors.toMap(s -> s[0], s -> s[1]));
                    if (parameters == null) {
                        parameters = new HashMap<>();
                    }
                }
                headers = allLines.stream()
                        .map(s -> s.split(": "))
                        .collect(Collectors.toMap(s -> s[0], s -> s[1]));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        if (firstLine == null) {
            this();
        }
    } // end of HttpRequest(final InputStream in)
    
    public HttpRequest() {
        requestLine = null;
        headers = null;
        parameters = null;
    }

    public String getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getHeader(final String headerName) {
        return headers.get(headerName);
    }

    public String getParameter(final String parameterName) {
        String value = null;
        if (parameters != null) {
            if (parameters.containsKey(parameterName)) {
                value = parameters.get(parameterName);
            }
        }
        return value;
    }
}