package servers.jettyServer;

import hotelapp.ThreadSafeHashMapBuilder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

/** This class uses Jetty & servlets to implement server serving hotel and review info */
public class JettyServer {
    // FILL IN CODE
    private static final int PORT = 8090;
    private Object data;

    /**
     * Constructor to initialize data attribute
     *
     * @param data Data object
     */
    public JettyServer(Object data) {
        this.data = data;
    }

    /**
     * Function that starts the server
     * @throws Exception throws exception if access failed
     */
    public  void start() throws Exception {
        Server server = new Server(PORT);

        ServletContextHandler handler = new ServletContextHandler();

        server.setHandler(handler);
        handler.setAttribute("data",this.data);
        handler.addServlet(HotelServlet.class, "/hotelInfo");
        handler.addServlet(ReviewServlet.class, "/reviews");
        handler.addServlet(WordServlet.class, "/index");
        handler.addServlet(WeatherServlet.class, "/weather");
        server.start();
        server.join();
    }

}
