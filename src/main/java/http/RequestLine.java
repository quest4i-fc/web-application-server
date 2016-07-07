package http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// HttpRequest를 통해서 다른 헤더값이랑 똑가이 사용할 수 있도록 구현한다.
// GET일 경우 path에서 parameter를 분리한다.
// POST일 경우 parameter를 가지고 있지 않다.
public class RequestLine {

    private static final Logger log = LoggerFactory.getLogger(RequestLine.class);
    
    final private String method;
    final private String path;
    final private String protocol;
    // GET일 경우에만 parameters를 가진다.
    final private Map<String, String> parameters;

    
    RequestLine(String line) {

        String[] tokens = line.split(" ");
        log.debug("RequestLine : method {}, path {}, protocol {}",
                tokens[0], tokens[1], tokens[2]);

        this.method = tokens[0];

        String[] pathLine = tokens[1].split("\\?");
        log.debug("pathLine : {}", pathLine[0]);

        this.path = pathLine[0];

        if (this.method.equals("GET") && pathLine.length > 1) {
            this.parameters = Arrays.stream(pathLine[1].split("&"))
                    .map(e -> e.split("="))
                    .collect(Collectors.toMap(e -> e[0],  e -> e[1]));
        } else {
            this.parameters = new HashMap<>();
        }

        this.protocol = tokens[2];
    } // end of RequestLine(String line)
    
    
    String getMethod() {
        return method;
    }
    
    String getPath() {
        log.debug("in getPath : {}", path);
        return path;
    }
    
    String getProtocol() {
        return protocol;
    }
    
    Map<String, String> getParameters() {
        return parameters;
    }
    
    boolean isPost() {
        return this.method.equals("POST");
    }
}