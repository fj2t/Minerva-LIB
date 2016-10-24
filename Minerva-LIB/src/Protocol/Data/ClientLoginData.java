package Protocol.Data;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Encapsula los datos del usuario que se está logeando.
 * 
 * @author fj
 */
public class ClientLoginData {

    private final String user;
    private final String password;

    public ClientLoginData(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String toJSONString() {
        JsonObject model = Json.createObjectBuilder()
                .add("user", user)
                .add("password", password).build();

        return model.toString();
    }

    public static ClientLoginData fromJSONString(String jsonString) {
        
        JsonReader reader = Json.createReader( new StringReader( jsonString ) );
        JsonObject json = reader.readObject();

        String user = json.getString("user");
        String password = json.getString("password");       
        

        return new ClientLoginData(user, password);       
      
    }
}
