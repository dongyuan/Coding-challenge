package worldremit.worldremit.android.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import worldremit.worldremit.android.model.Transaction;
import worldremit.worldremit.android.presenter.MainPresenter;

/**
 * Created by ericyuan on 14/03/2015.
 */
public interface MainView {


    public Context getContext();

    public Activity getActivity();

    public void setPresenter(MainPresenter presenter);
    /**
     * Helper method to set these two currencies spinners once we get the available currencies list back from the server
     * We may need to consider caching the list somewhere to avoid unnecessary network requests.
     */
    public void setCurrencySpinner(List<String> currencies);

    public void setRecipient(String recipient);

    public void setSendButtonEnabled(boolean buttonEnabled);

    public void showDialog(Dialog dialog);

    public void showToast(String message);

    public void setReceiveAmount(String amount);

    public String getRecipient();

    public String getSendAmount();

    public String getSendCurrency();

    public String getReceiveAmount();

    public String getReceiveCurrency();
}
