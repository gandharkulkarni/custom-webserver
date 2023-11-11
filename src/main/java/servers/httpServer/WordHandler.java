package servers.httpServer;

import com.google.gson.JsonObject;
import hotelapp.ThreadSafeHashMapBuilder;

import java.io.PrintWriter;

public class WordHandler implements HttpHandler {

    private ThreadSafeHashMapBuilder threadSafeHashMapBuilder;

    /**
     * Process http request and writes Json response containing reviews
     *
     * @param request client's http request
     * @param writer PrintWriter of the response
     */
    @Override
    public void processRequest(HttpRequest request, PrintWriter writer) {
        if (request.getRequestParameter().containsKey("word") && request.getRequestParameter().containsKey("num")) {
            String word = request.getRequestParameter().get("word");
            String requestedCount = request.getRequestParameter().get("num");
            if (word == null) {
                word = "";
            }
            if (requestedCount == null) {
                requestedCount = "0";
            }
            JsonObject wordResponse = threadSafeHashMapBuilder.getReviewsContainingSpecificWordInJsonFormat(word, Integer.parseInt(requestedCount));
            writer.write("HTTP/1.1 200 OK" + System.lineSeparator());
            writer.write("ContentType: application/json" + System.lineSeparator() + System.lineSeparator());
            writer.write(wordResponse.toString());
            writer.write(System.lineSeparator());
            writer.flush();
        } else {
            JsonObject wordResponse = threadSafeHashMapBuilder.getReviewsContainingSpecificWordInJsonFormat("", 0);
            writer.write("HTTP/1.1 200 OK" + System.lineSeparator());
            writer.write("ContentType: application/json" + System.lineSeparator() + System.lineSeparator());
            writer.write(wordResponse.toString());
            writer.write(System.lineSeparator());
            writer.flush();
        }
    }

    /**
     * Sets value to instance variable
     *
     * @param data Data object
     */
    @Override
    public void setAttribute(Object data) {
        this.threadSafeHashMapBuilder = (ThreadSafeHashMapBuilder) data;
    }
}
