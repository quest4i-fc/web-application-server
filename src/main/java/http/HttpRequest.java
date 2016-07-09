package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    final private RequestLine requestLine;
    final private Map<String, String> headers;
    final private Map<String, String> parameters;


    public HttpRequest(final InputStream in) {

        RequestLine _requestLine = null;
        Map<String, String> _headers = new HashMap<>();
        Map<String, String> _parameters = new HashMap<>();

        try {
            final BufferedReader br = 
                    new BufferedReader(new InputStreamReader(in, "UTF-8"));

            // 1. Request Line - method, path, get-parameters
            String str = br.readLine();
            if (str == null) {
                return;
            }
            _requestLine = new RequestLine(str);
            log.debug("request line : {}", _requestLine.toString());
            
            final List<String> LINES = br.lines()
                    .filter(s -> !(s.equals("")))
                    .collect(Collectors.toList());

            switch (_requestLine.getMethod()) {
                case GET:
                    _parameters = _requestLine.getParameters();
                    break;
                case POST:
                    _parameters = Stream.of(LINES.get(LINES.size() - 1).split("&"))
                        .map(s -> s.split("="))
                        .collect(Collectors.toMap(s -> s[0], s -> s[1]));
                    LINES.remove(LINES.size() -1);
                    log.debug("parameters : {}", _parameters);
                    break;
            }
            _headers = LINES.stream()
                    .map(s -> s.split(": "))
                    .collect(Collectors.toMap(s -> s[0], s -> s[1]));
            log.debug("_headers : {}", _headers);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            this.requestLine = _requestLine;
            this.headers = _headers;
            this.parameters = _parameters;
        }
    } // end of HttpRequest(final InputStream in)
    

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getHeader(final String headerName) {
        return headers.get(headerName);
    }

    public String getParameter(final String parameterName) {
        return parameters.get(parameterName);
    }
    
    public boolean isPost() {
        return requestLine.isPost();
    }
    
    public boolean isGet() {
        return requestLine.isGet();
    }
}