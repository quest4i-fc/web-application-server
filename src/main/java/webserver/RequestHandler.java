package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import util.HttpRequestUtils;



public class RequestHandler extends Thread {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	
	@Override
	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
		
		try (InputStream in = connection.getInputStream(); 
		     OutputStream out = connection.getOutputStream()) {
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
		    // GET /index.html HTTP/1.1
		    
		    // 요구사항 0 - HTTP헤더를 출력한다.
		    final List<String> httpHeader = HttpRequestUtils.getHttpHeaderList(in);
		    for (final String head : httpHeader) {
		        log.debug("HTTP Header : {}", head);
		    }
		    
			final DataOutputStream dos = new DataOutputStream(out);

		    // 요구사항 1 - index.html 파일을 읽어 클라이언트에 응답한다.
		    String url = HttpRequestUtils.getUrl(httpHeader);
		    
		    // 요구사항 2 - form.html에서 /user/create 로 보내는 회원가입 폼을 처리해서 model.User에 저장한다.
		    // form 데이터가 전달되는 /user/create URL 호출후에 다시 초기 화면으로 가도록 설정한다.
		    if (url.startsWith("/user/create")) {
		        int index = url.indexOf("?");
		        String requestPath = url.substring(0, index);
		        String queryString = url.substring(index+1);
		        Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);
		        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
		        log.debug("User : {}", user);
		        url = "/index.html";
		    }

		    
			final byte[] body = HttpRequestUtils.getBody(url);

			response200Header(dos, body.length);
			responseBody(dos, body);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	} // end of run()

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
