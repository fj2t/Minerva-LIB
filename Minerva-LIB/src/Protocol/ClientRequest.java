package Protocol;

import java.io.StringReader;
import javax.json.*;

/**
 *
 * @author fj
 */
public class ClientRequest {
    private final String request;
    private final String softwareID;

    public ClientRequest ( String request, String softwareID ) {
        this.request = request;
        this.softwareID = softwareID;
    }

    public String getRequest () {
        return request;
    }

    public String getSoftwareID () {
        return softwareID;
    }

    public String toJSONString () {        
        JsonObject model = Json.createObjectBuilder()
                .add( "request", request )
                .add( "softwareID", softwareID ).build();       
        return model.toString();
    }

    public static ClientRequest fromJSONString ( String jsonString ) {
        JsonReader reader = Json.createReader( new StringReader( jsonString ) );
        JsonObject json = reader.readObject();

        String request = json.getString( "request" );
        String softwareID = json.getString( "softwareID" );

        return new ClientRequest( request, softwareID );
    }

}
