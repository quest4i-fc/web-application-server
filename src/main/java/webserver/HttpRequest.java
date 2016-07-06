package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private RequestLine requestLine;
    private Map<String, String> headers;
    private Map<String, String> parameters;


    public HttpRequest(final InputStream in) {

        try {
            final BufferedReader br = 
                    new BufferedReader(new InputStreamReader(in, "UTF-8"));

            // 1. Request Line - method, path, get-parameters
            String line = br.readLine();
            if (line == null) {
                return;
            }
            log.debug("request line : {}", line);
            requestLine = new RequestLine(line);
            this.parameters = requestLine.getParameters();

            // 2. parameter
            List<String> allLines = br.lines()
                    .filter(s -> !(s.equals("")))
                    .collect(Collectors.toList());
            if (this.requestLine.isPost()) {
                String paramLine = allLines.get(allLines.size() - 1);
                allLines.remove(allLines.size() -1);
                parameters = Arrays.stream(paramLine.split("&"))
                        .map(s -> s.split("="))
                        .collect(Collectors.toMap(s -> s[0], s -> s[1]));
            }
            headers = allLines.stream()
                    .map(s -> s.split(": "))
                    .collect(Collectors.toMap(s -> s[0], s -> s[1]));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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