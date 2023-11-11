package servers.jettyServer;

import com.google.gson.JsonObject;
import hotelapp.LogHelper;
import hotelapp.ThreadSafeHashMapBuilder;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ReviewServlet extends HttpServlet {

    /**
     *Process http get request and writes Json response containing reviews
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

            String hotelId = StringEscapeUtils.escapeHtml4(request.getParameter("hotelId"));
            String num = StringEscapeUtils.escapeHtml4(request.getParameter("num"));

            JsonObject reviewInfoResponse = new JsonObject();
            if (hotelId != null && !hotelId.isEmpty() && num != null && !num.isEmpty()) {
                int numInt = Integer.parseInt(num);

                ThreadSafeHashMapBuilder threadSafeHashMapBuilder = (ThreadSafeHashMapBuilder) getServletContext().getAttribute("data");
                reviewInfoResponse = threadSafeHashMapBuilder.getAllReviewsForHotelInJsonFormat(hotelId, numInt);
                out.println(reviewInfoResponse.toString());
                out.flush();
            }
            else{
                reviewInfoResponse.addProperty("success", false);
                reviewInfoResponse.addProperty("hotelId", "invalid");
                out.println(reviewInfoResponse.toString());
                out.flush();
            }
        } catch (Exception ex) {
            LogHelper.getLogger().error(ex);
        }
    }

}
