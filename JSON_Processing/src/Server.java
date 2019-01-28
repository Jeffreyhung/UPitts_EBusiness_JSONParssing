import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 

@WebServlet("/Server")
public class Server extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Server() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String input_text = request.getParameter("input_text").trim();
		JSONObject jsonObj = new JSONObject(input_text);
		String hashed = hash(jsonObj.getString("message"), jsonObj.getString("salt"), jsonObj.getString("hash"));
		JSONObject result = new JSONObject(); 
		result.put("message",jsonObj.getString("message"));
		result.put("salt", jsonObj.getString("salt"));
		result.put("hash", jsonObj.getString("hash"));
		result.put("result", hashed);
		
	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(result.toString());
	}

	public String hash(String message, String salt, String hash_function) {
		String result="";
		try {
	         MessageDigest md = MessageDigest.getInstance(hash_function);
	         md.update(salt.getBytes(StandardCharsets.UTF_8));
	         byte[] bytes = md.digest(message.getBytes(StandardCharsets.UTF_8));
	         StringBuilder sb = new StringBuilder();
	         for(int i=0; i< bytes.length ;i++){
	            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	         }
	         result = sb.toString();
	        } 
	       catch (NoSuchAlgorithmException e){
	        e.printStackTrace();
	       }
		return result;
	}

}
