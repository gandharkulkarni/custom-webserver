package servers.httpServer;

import com.google.gson.JsonObject;
import hotelapp.ThreadSafeHashMapBuilder;

import java.io.PrintWriter;

public class HotelHandler implements HttpHandler {
    private ThreadSafeHashMapBuilder threadSafeHashMapBuilder;

    /**
     * Process http request and writes Json response containing hotel details
     *
     * @param request client's http request
     * @param writer PrintWriter of the response
     */
    @Override
    public void processRequest(HttpRequest request, PrintWriter writer) {
        if (request.getRequestParameter().containsKey("hotelId")) {
            writer.write("HTTP/1.1 200 OK" + System.lineSeparator());
            writer.write("ContentType: application/json" + System.lineSeparator() + System.lineSeparator());
            String hotelId = request.getRequestParameter().get("hotelId");
            if (hotelId == null) {
                hotelId = "-1";
            }
            JsonObject hotelInfoResponse = threadSafeHashMapBuilder.getHotelByHotelIdInJsonFormat(hotelId);
            writer.write(hotelInfoResponse.toString());
            writer.write(System.lineSeparator());
            writer.flush();
        } else {
            writer.write("HTTP/1.1 200 OK" + System.lineSeparator());
            writer.write("ContentType: application/json" + System.lineSeparator() + System.lineSeparator());
            JsonObject hotelInfoResponse = threadSafeHashMapBuilder.getHotelByHotelIdInJsonFormat("-1");
            writer.write(hotelInfoResponse.toString());
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
