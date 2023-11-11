package servers.httpServer;

public class ResponseHelper {
    /**
     * Returns HTTP 404 response
     *
     * @return Response
     */
    public static String getPageNotFoundResponse(){
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 404 Not Found" + System.lineSeparator());
        builder.append("ContentType: application/json" + System.lineSeparator() + System.lineSeparator());
        return builder.toString();
    }

    /**
     * Returns HTTP 405 response
     *
     * @return Response
     */
    public static String getMethodNotAllowedResponse(){
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 405 Method Not Allowed" + System.lineSeparator());
        builder.append("ContentType: application/json" + System.lineSeparator() + System.lineSeparator());
        return builder.toString();
    }
}
