package servers.httpServer;

import com.google.gson.JsonObject;
import hotelapp.ThreadSafeHashMapBuilder;

import java.io.PrintWriter;

public class ReviewsHandler implements HttpHandler {
    private ThreadSafeHashMapBuilder threadSafeHashMapBuilder;

    /**
     * Process http request and writes Json response containing reviews
     *
     * @param request client's http request
     * @param writer PrintWriter of the response
     */
    @Override
    public void processRequest(HttpRequest request, PrintWriter writer) {
        if (request.getRequestParameter().containsKey("hotelId") && request.getRequestParameter().containsKey("num")) {
            String hotelId = request.getRequestParameter().get("hotelId");
            String requestedCount = request.getRequestParameter().get("num");
            if (hotelId == null) {
                hotelId = "-1";
            }
            if (requestedCount == null) {
                requestedCount = "0";
            }
            JsonObject reviewInfoResponse = threadSafeHashMapBuilder.getAllReviewsForHotelInJsonFormat(
                    hotelId,
                    Integer.parseInt(requestedCount)
            );
            writer.write("HTTP/1.1 200 OK" + System.lineSeparator());
            writer.write("ContentType: application/json" + System.lineSeparator() + System.lineSeparator());
            writer.write(reviewInfoResponse.toString());
            writer.write(System.lineSeparator());
            writer.flush();
        } else {
            writer.write("HTTP/1.1 200 OK" + System.lineSeparator());
            writer.write("ContentType: application/json" + System.lineSeparator() + System.lineSeparator());
            JsonObject reviewInfoResponse = threadSafeHashMapBuilder.getAllReviewsForHotelInJsonFormat("-1", 0);
            writer.write(reviewInfoResponse.toString());
            writer.write(System.lineSeparator());
            writer.flush();
        }
    }

    /**
     * Sets Sets value to instance variable
     *
     * @param data Data object
     */
    @Override
    public void setAttribute(Object data) {
        this.threadSafeHashMapBuilder = (ThreadSafeHashMapBuilder) data;
    }
}
