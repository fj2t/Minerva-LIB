package Protocol;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

/**
 * Clase con las respuestas del servidor. La estructura es la misma, ya que lo
 * que puede variar (la "response") es un json
 *
 * @author fj
 */
public class ServerResponse {
    private final String response;
    private final ServerError responseError;
    private final boolean shutDown;
    private final String softwareID;

    private ServerResponse ( String response, ServerError responseError, boolean shutDown, String softwareID ) {
        this.response = response;
        this.shutDown = shutDown;
        this.softwareID = softwareID;
        this.responseError = responseError;
    }

    public ServerResponse ( ServerError responseError, boolean shutDown, String softwareID ) {
        this.response = "";
        this.shutDown = shutDown;
        this.softwareID = softwareID;
        this.responseError = responseError;
    }

    public ServerResponse ( String response, boolean shutDown, String softwareID ) {
        this.response = response;
        this.shutDown = shutDown;
        this.softwareID = softwareID;
        this.responseError = ServerError.NO_ERROR;
    }

    public ServerError getResponseError () {
        return responseError;
    }

    public String getResponse () {
        return response;
    }

    public String getSoftwareID () {
        return softwareID;
    }

    public boolean isShutDown () {
        return shutDown;
    }

    public String toJSONString () {
        JsonObject model = Json.createObjectBuilder()
                .add( "response", response )
                .add( "Error", responseError.name() )
                .add( "shutdown", shutDown )
                .add( "softwareID", softwareID ).build();

        return model.toString();
    }

    public static ServerResponse fromJSONString ( String jsonString ) {
        JsonReader reader = Json.createReader( new StringReader( jsonString ) );
        JsonObject json = reader.readObject();

        String response = json.getString( "response" );
        ServerError responseError = ServerError.valueOf( json.getString( "Error" ) );
        boolean shutDown = json.getBoolean( "shutdown" );
        String softwareID = json.getString( "softwareID" );

        return new ServerResponse( response, responseError, shutDown, softwareID );
    }

}
