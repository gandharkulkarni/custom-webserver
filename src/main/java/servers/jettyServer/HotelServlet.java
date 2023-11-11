package servers.jettyServer;

import com.google.gson.JsonObject;
import hotelapp.LogHelper;
import hotelapp.ThreadSafeHashMapBuilder;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class HotelServlet extends HttpServlet {

    /**
     *Process http get request and writes Json response containing hotel info
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);

            PrintWriter out = response.getWriter();
            JsonObject hotelInfoResponse = new JsonObject();
            String hotelId = StringEscapeUtils.escapeHtml4(request.getParameter("hotelId"));
            if (hotelId != null && !hotelId.isEmpty()) {
                ThreadSafeHashMapBuilder threadSafeHashMapBuilder = (ThreadSafeHashMapBuilder) getServletContext().getAttribute("data");
                hotelInfoResponse = threadSafeHashMapBuilder.getHotelByHotelIdInJsonFormat(hotelId);

                out.println(hotelInfoResponse.toString());
                out.flush();

            } else {
                hotelInfoResponse.addProperty("success", false);
                hotelInfoResponse.addProperty("hotelId", "invalid");

                out.println(hotelInfoResponse.toString());
                out.flush();
            }
        } catch (Exception ex) {
            LogHelper.getLogger().error(ex);
        }
    }
}
