package servers.httpServer;

import hotelapp.LogHelper;
import org.apache.commons.text.StringEscapeUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that represents an http request
 */
public class HttpRequest {
    private String requestMethodType;
    private String requestPath;
    private String requestProtocolVersion;
    private Map<String, String> requestParameter;

    /**
     * Initializes request component
     *
     * @param request Request
     */
    public HttpRequest(String request) {
        requestParameter = new HashMap<>();
        parseRequest(request);
    }

    /**
     * Parse request and parameters
     *
     * @param request Request
     */
    private void parseRequest(String request) {
        int methodTypeIndex = 0;
        int pathIndex = 1;
        int protocolIndex = 2;
        try {
            String[] requestComponents = request.split(" ");
            this.requestMethodType = requestComponents[methodTypeIndex];

            if (requestComponents[pathIndex].startsWith("/")) {
                String[] handlerDetails = requestComponents[pathIndex].split("\\?");
                this.requestPath = StringEscapeUtils.escapeHtml4(handlerDetails[0].substring(1));
                if (handlerDetails.length > 1) {
                    String[] parameters = handlerDetails[1].split("&");
                    for (String parameter : parameters) {
                        String[] parameterKeyValue = parameter.split("=");
                        if (parameterKeyValue.length == 1) {
                            String key = StringEscapeUtils.escapeHtml4(parameterKeyValue[0]);
                            if (key != null && !key.isEmpty()) {
                                this.requestParameter.put(key, null);
                            }
                        } else if (parameterKeyValue.length == 2) {
                            String key = StringEscapeUtils.escapeHtml4(parameterKeyValue[0]);
                            String value = StringEscapeUtils.escapeHtml4(parameterKeyValue[1]);
                            if (key != null && !key.isEmpty() && value != null && !value.isEmpty()) {
                                this.requestParameter.put(key, value);
                            }
                        }
                    }
                }
            }

            if (requestComponents[protocolIndex].startsWith("HTTP")) {
                this.requestProtocolVersion = requestComponents[protocolIndex];
            }
        } catch (Exception ex) {
            LogHelper.getLogger().error(ex);
        }
    }

    /**
     * Returns request type
     *
     * @return Request method type
     */
    public String getRequestMethodType() {
        return requestMethodType;
    }

    /**
     * Returns request path
     *
     * @return Request path
     */
    public String getRequestPath() {
        return requestPath;
    }

    /**
     * Returns request protocol version
     *
     * @return Request protocol version
     */
    public String getRequestProtocolVersion() {
        return requestProtocolVersion;
    }

    /**
     * Returns request parameter map
     *
     * @return Request parameter map
     */
    public Map<String, String> getRequestParameter() {
        return Collections.unmodifiableMap(requestParameter);
    }

}
