package worldremit.worldremit.android.network;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import worldremit.worldremit.android.model.Transaction;

/**
 *
 *  Rest client implementation
 *
 *  It matches the WorldMerit REST server's specification.
 *
 *  Retrofit library is used to simplify the implementation, please see the doc from <a href="http://square.github.io/retrofit/">here</a>
 *
 * Created by ericyuan on 13/03/2015.
 */
public interface WorldRemitApi {

    @GET("/api/currencies")
    void getCurrencies(Callback<List<String>> getCurrenciesCallback);

    @GET("/api/calculate")
    void calculate(@Query("amount") float amount,@Query("sendcurrency") String sendCurrency, @Query("receivecurrency") String receiveCurrency, Callback<Transaction> calculateCallback);

    @POST("/api/send")
    void sendMoney(@Body Transaction transaction, Callback<Transaction> sendMoneyCallback);

}
