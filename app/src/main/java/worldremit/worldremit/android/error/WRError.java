package worldremit.worldremit.android.error;

/**
 * A POJO represents the unrecoverable errors in which the app is interested.
 * When errors occurs, the app should handle them accordingly.
 * <p/>
 * Created by ericyuan on 14/03/2015.
 */
public class WRError {

    // They are just few examples, we can add more error code based on the requirements.
    public static final int NETWORK_ERROR = 0;
    public static final int CURRENCY_NOT_AVAILABLE = 2;

    private String request;
    private int errorCode;

    /**
     * Constructs a new {@code WRError} with request identifier and the
     * specified error code.
     *
     * @param request   which operation causes this error.
     * @param errorCode specified error code.
     */
    public WRError(String request, int errorCode) {
        this.request = request;
        this.errorCode = errorCode;
    }

    public String getRequest() {
        return request;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
