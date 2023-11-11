package servers.jettyServer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import hotelapp.LogHelper;
import hotelapp.Review;
import hotelapp.ThreadSafeHashMapBuilder;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Set;

public class WordServlet extends HttpServlet {

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

            String word = StringEscapeUtils.escapeHtml4(request.getParameter("word"));
            String num = StringEscapeUtils.escapeHtml4(request.getParameter("num"));

            JsonObject wordResponse = new JsonObject();
            if (word != null && !word.isEmpty() && num != null && !num.isEmpty()) {
                int numInt = Integer.parseInt(num);

                ThreadSafeHashMapBuilder threadSafeHashMapBuilder = (ThreadSafeHashMapBuilder) getServletContext().getAttribute("data");
                wordResponse = threadSafeHashMapBuilder.getReviewsContainingSpecificWordInJsonFormat(word, numInt);
                out.println(wordResponse.toString());
                out.flush();
            } else {
                wordResponse.addProperty("success", false);
                wordResponse.addProperty("word", "invalid");
                out.println(wordResponse.toString());
                out.flush();
            }
        } catch (Exception ex) {
            LogHelper.getLogger().error(ex);
        }
    }
}
