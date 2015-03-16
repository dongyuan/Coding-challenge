package worldremit.worldremit.android.network;

import com.squareup.otto.Subscribe;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import worldremit.worldremit.android.error.WRError;
import worldremit.worldremit.android.model.Transaction;
import worldremit.worldremit.android.util.BusProvider;

/**
 *
 * Wrap all rest interfaces and we can manipulate all these rest requests easily if needed.
 * I am using <a href="http://square.github.io/otto/">Otto</a> library to communicate between Activity and this Class.
 *
 * Created by ericyuan on 13/03/2015.
 */
public class WorldRemitService {

    /** network request identifier **/
    public static final String NETWORK_REQUEST_RETRIEVE_CURRENCIES = "network_request_retrieve_currencies" ;
    public static final String NETWORK_REQUEST_CALCULATE_CURRENCY = "network_request_calculate_currency" ;
    public static final String NETWORK_REQUEST_SEND_MONEY = "network_request_send_money" ;

    private static final String TAG = WorldRemitService.class.getSimpleName();

    private WorldRemitApi mWorldRemitApi;
    private String mApiUrl;

    /** All retrieve currencies events declaration **/

    public static class RetrieveCurrenciesEvent {}

    public static class RetrieveCurrenciesOnSuccessEvent {
        List<String> currencies;
        public RetrieveCurrenciesOnSuccessEvent(List<String> currencies) {
            this.currencies = currencies;
        }

        public List<String> getCurrencies(){
            return this.currencies;
        }
    }


    /** All calculate currency events declaration **/
    public static class CalculateCurrencyEvent {
        // It's better to encapsulate these properties and use setter/getter.
        float amount;
        String sendCurrency;
        String receiveCurrency;
        public CalculateCurrencyEvent(float amount, String sendCurrency, String receiveCurrency){
            this.amount = amount;
            this.sendCurrency = sendCurrency;
            this.receiveCurrency = receiveCurrency;
        }
    }

    public static class CalculateCurrencyOnSuccessEvent {
        private Transaction transaction;
        public CalculateCurrencyOnSuccessEvent(Transaction transaction){
            this.transaction = transaction;
        }

        public Transaction getTransaction() {
            return transaction;
        }
    }

    /** All send money events declaration **/
    public static class SendMoneyEvent {
        Transaction transaction;
        public SendMoneyEvent(Transaction transaction){
            this.transaction = transaction;
        }
    }

    public static class SendMoneyOnSuccess {
        Transaction transaction;
        public SendMoneyOnSuccess(Transaction transaction){
            this.transaction = transaction;
        }

        public Transaction getTransaction() {
            return this.transaction;
        }
    }


    /**  Request error event declaration **/
    public static class RequestOnFailureEvent{
        WRError error;
        public RequestOnFailureEvent(WRError error){
            this.error = error;
        }
    }


    /**
     * WorldRemitService  Constructor
     * @param apiUrl the base URL for the REST Sever.
     */
    public WorldRemitService(String apiUrl) {
        mApiUrl = apiUrl;
    }

    @Subscribe
    public void getCurrencies(RetrieveCurrenciesEvent event){

        getCurrencies(new Callback<List<String>>() {
            @Override
            public void success(List<String> currencies, Response response) {
                BusProvider.getInstance().post(new RetrieveCurrenciesOnSuccessEvent(currencies));
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO: we should do some error handling here.
                BusProvider.getInstance().post(new RequestOnFailureEvent(new WRError(NETWORK_REQUEST_RETRIEVE_CURRENCIES,WRError.NETWORK_ERROR)));
            }
        });
    }

    @Subscribe
    public void calculateCurrency(CalculateCurrencyEvent event){

        calculate(event.amount, event.sendCurrency, event.receiveCurrency, new Callback<Transaction>() {
            @Override
            public void success(Transaction transaction, Response response) {
                BusProvider.getInstance().post(new CalculateCurrencyOnSuccessEvent(transaction));
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO: we should do some error handling here.
                BusProvider.getInstance().post(new RequestOnFailureEvent(new WRError(NETWORK_REQUEST_CALCULATE_CURRENCY,WRError.NETWORK_ERROR)));
            }
        });
    }

    @Subscribe
    public void sendMoney(SendMoneyEvent event){
        final Transaction originalTransaction = event.transaction;
        sendMoney(event.transaction, new Callback<Transaction>() {
            @Override
            public void success(Transaction transaction, Response response) {
                if (response.getStatus()== HttpURLConnection.HTTP_CREATED){
                    // the server doesn't return the transaction sent for the request
                    BusProvider.getInstance().post(new SendMoneyOnSuccess(originalTransaction));
                }

            }

            @Override
            public void failure(RetrofitError error) {
                // TODO: we should do some error handling here.
                BusProvider.getInstance().post(new RequestOnFailureEvent(new WRError(NETWORK_REQUEST_SEND_MONEY,WRError.NETWORK_ERROR)));
            }
        });
    }


    /**
     * Get all available currencies.
     * Suggest using {@link com.squareup.otto.Bus} to invoke this request,which makes it easier to handle the activity lifecycle.
     *
     * @param callback {@link retrofit.Callback} get the result back.
     */
    public void getCurrencies(Callback<List<String>> callback){
        getmWorldRemitApi().getCurrencies(callback);
    }


    /**
     *
     * Perform a currency calculation operation.
     * Recommend to use the shared {@link com.squareup.otto.Bus} to send this request.
     *
     * @param amount
     * @param sendCurrency
     * @param receiveCurrency
     * @param callback
     */
    public void calculate(float amount, String sendCurrency, String receiveCurrency, Callback<Transaction> callback){

        // check all parameters
        if (amount < 0) {
            // Throw exception.
            throw new IllegalArgumentException("amount < 0");
        }

        if (callback == null){
            throw new IllegalArgumentException("callback == null");
        }

        // TODO: if from currency and to currency are same, return the amount immediately, no need to make a network request.

        getmWorldRemitApi().calculate(amount, sendCurrency, receiveCurrency, callback);
    }

    public void sendMoney(Transaction transaction, Callback<Transaction> callback){
        // check all parameters
        if (transaction == null) {
            // Throw exception.
            throw new IllegalArgumentException("transaction == null");
        }

        if (callback == null){
            throw new IllegalArgumentException("callback == null");
        }
        getmWorldRemitApi().sendMoney(transaction, callback);
    }


    private synchronized WorldRemitApi getmWorldRemitApi() {
        if (mWorldRemitApi == null) {
            // Create a REST adapter which points the WorldRemit API endpoint.
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(mApiUrl)
                    .build();

            mWorldRemitApi = restAdapter.create(WorldRemitApi.class);
        }

    return mWorldRemitApi;
    }



}
