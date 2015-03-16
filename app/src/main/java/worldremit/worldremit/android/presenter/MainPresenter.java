package worldremit.worldremit.android.presenter;

import android.content.Intent;
import android.os.Bundle;

import worldremit.worldremit.android.ui.view.MainView;

/**
 *
 * Interface representing a Presenter in a model view presenter (MVP) pattern.
 *
 * Created by ericyuan on 14/03/2015.
 */
public interface MainPresenter {

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onCreate() method.
     */
    public void onCreate(Bundle savedInstanceState);

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onResume() method.
     */
    public void onResume();

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onPause() method.
     */
    public void onPause();

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity) onActivityResult() method.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * Send amount done button is clicked.
     */
    public void onSendAmountEditorActionDone(String recipient, String sendAmount, String sendCurrency, String receiveAmount, String receiveCurrency);

    /**
     * An item in the currency spinner has been selected.
     */
    public void onItemSelected(String recipient, String sendAmount, String sendCurrency, String receiveAmount, String receiveCurrency);

    /**
     * The contact picker button is clicked.
     */
    public void onContactPickerClicker();

    /**
     * Send money button is clicked.
     */
    public void onSendButtonClicked(String recipient, String sendAmount, String sendCurrency, String receiveAmount, String receiveCurrency);

    /**
     * Detach the view with the presenter.
     */
    public void removeView(MainView mainView);

    /**
     * Validate the required fields for the given operation and
     * display error message to end user if any error occurs.
     *
     */
    public boolean validateFields(int operation, String recipient, String sendAmount, String sendCurrency, String receiveAmount, String receiveCurrency);
}
