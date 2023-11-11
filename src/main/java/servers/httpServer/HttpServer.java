package servers.httpServer;

import hotelapp.LogHelper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implements an http server using raw sockets
 */
public class HttpServer {

    public static final int PORT = 8080;
    private Map<String, String> handlers; // maps each url path to the appropriate handler
    private final ExecutorService threads;
    private Object data;

    /**
     * Http server constructor
     *
     * @param threadLimit Thread limit
     * @param data        Data object
     */
    public HttpServer(int threadLimit, Object data) {
        this.handlers = new HashMap<>();
        threads = Executors.newFixedThreadPool(threadLimit);
        this.data = data;
    }

    /**
     * Adds path and respective handler class to hashmap
     *
     * @param path         Request path
     * @param handlerClass Handler class name
     */
    public void addMapping(String path, Class handlerClass) {
        this.handlers.put(path, handlerClass.getName());
    }

    /**
     * Initializes server
     */
    public void startServer() {
        System.out.println("HTTP server started");

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {

                try {
                    ServerSocket welcomingSocket = new ServerSocket(PORT);
                    while (true) {
                        Socket clientSocket = welcomingSocket.accept();
                        RequestWorker requestWorker = new RequestWorker(clientSocket);
                        threads.submit(requestWorker);
                    }
                } catch (Exception ex) {
                    LogHelper.getLogger().error(ex);
                }
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();
    }

    /**
     * Internal class to handle client request
     */
    public class RequestWorker implements Runnable {
        final Socket clientConnectionSocket;
        String requestHeader;

        /**
         * Initialize client socket
         *
         * @param clientSocket Socket
         */
        public RequestWorker(Socket clientSocket) {
            clientConnectionSocket = clientSocket;
            System.out.println("Client connected");
        }

        /**
         * Reads request from connection socket input stream and calls appropriate handler to process request
         *
         */
        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientConnectionSocket.getInputStream()))) {
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientConnectionSocket.getOutputStream()), true);
                String input;
                input = reader.readLine();
                if (input == null) {
                    writer.write(ResponseHelper.getMethodNotAllowedResponse());
                    writer.flush();
                } else if (input.startsWith("GET")) {

                    requestHeader = input;
                    System.out.println("Server received: " + input);
                    HttpRequest httpRequest = new HttpRequest(requestHeader);

                    if (httpRequest.getRequestMethodType().equals("GET")) {
                        if (handlers.containsKey(httpRequest.getRequestPath())) {
                            String className = handlers.get(httpRequest.getRequestPath());
                            try {
                                HttpHandler httpHandler = (HttpHandler) Class.forName(className).
                                        getConstructor().newInstance();
                                httpHandler.setAttribute(data);
                                httpHandler.processRequest(httpRequest, writer);
                            } catch (ClassNotFoundException classNotFoundException) {
                                classNotFoundException.printStackTrace();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            writer.write(ResponseHelper.getPageNotFoundResponse());
                            writer.flush();
                        }
                    } else {
                        writer.write(ResponseHelper.getMethodNotAllowedResponse());
                        writer.flush();
                    }
                } else {
                    writer.write(ResponseHelper.getMethodNotAllowedResponse());
                    writer.flush();
                }
                clientConnectionSocket.close();
                System.out.println("Closing connection socket.");
            } catch (IOException ioException) {
                LogHelper.getLogger().error(ioException);
            } catch (Exception ex) {
                LogHelper.getLogger().error(ex);
            } finally {
                try {
                    if (clientConnectionSocket != null)
                        clientConnectionSocket.close();
                } catch (IOException e) {
                    LogHelper.getLogger().error("Can't close the socket : " + e);
                }
            }

        }
    }
}
