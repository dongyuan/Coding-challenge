package worldremit.worldremit.android.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.squareup.otto.Subscribe;

import java.util.List;

import worldremit.worldremit.android.R;
import worldremit.worldremit.android.model.Transaction;
import worldremit.worldremit.android.network.WorldRemitService;
import worldremit.worldremit.android.ui.view.MainView;
import worldremit.worldremit.android.util.BusProvider;
import worldremit.worldremit.android.util.ContactHelper;

/**
 * {@link MainPresenter} that controls communication between views and models of the presentation
 * layer.
 * <p/>
 * Created by ericyuan on 14/03/2015.
 */
public class MainPresenterImpl implements MainPresenter {


    final static int OPERATION_CALCULATE_CURRENCY = 0;
    final static int OPERATION_SEND_MONEY = 1;

    final static int CONTACT_PICKER = 0;

    private MainView mMainView;
    // We shouldn't do it in the production code, we need to cache the currencies somewhere else.
    static List<String> sCurrencies;


    public MainPresenterImpl(MainView mainView) {
        mMainView = mainView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (mMainView == null) {
            return;
        }
        // Fetch all available currencies when the activity is invoked.
        // It's better to have a currency helper class holds all these values and share the data across the whole application.
        // It's also worth considering caching the data to avoid unnecessary network request.
        if (sCurrencies == null) {
            BusProvider.getInstance().post(new WorldRemitService.RetrieveCurrenciesEvent());
        } else {
            mMainView.setCurrencySpinner(sCurrencies);
        }
    }

    @Override
    public void onResume() {
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mMainView == null) {
            return;
        }
        if (resultCode == Activity.RESULT_OK && requestCode == CONTACT_PICKER) {
            Uri result = data.getData();
            mMainView.setRecipient(ContactHelper.retrieveContactName(mMainView.getContext(), result));
        }
    }

    @Override
    public void onSendAmountEditorActionDone(String recipient, String sendAmount, String sendCurrency, String receiveAmount, String receiveCurrency) {
        sendCurrencyCalculationRequest(recipient, sendAmount, sendCurrency, receiveAmount, receiveCurrency);
    }

    @Override
    public void onItemSelected(String recipient, String sendAmount, String sendCurrency, String receiveAmount, String receiveCurrency) {
        sendCurrencyCalculationRequest(recipient, sendAmount, sendCurrency, receiveAmount, receiveCurrency);
    }

    @Override
    public void onContactPickerClicker() {
        if (mMainView == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mMainView.getActivity().startActivityForResult(intent, CONTACT_PICKER);
    }

    @Override
    public void onSendButtonClicked(String recipient, String sendAmount, String sendCurrency, String receiveAmount, String receiveCurrency) {
        if (mMainView == null) {
            return;
        }
        if (validateFields(OPERATION_SEND_MONEY, recipient, sendAmount, sendCurrency, receiveAmount, receiveCurrency)) {

            // Disable the button to avoid duplicate request for the same transaction.
            mMainView.setSendButtonEnabled(false);
            Transaction transaction = new Transaction(recipient, sendAmount, sendCurrency, receiveAmount, receiveCurrency);
            BusProvider.getInstance().post(new WorldRemitService.SendMoneyEvent(transaction));
        }
    }

    @Override
    public void removeView(MainView mainView) {
        mMainView = null;
    }


    public void sendCurrencyCalculationRequest(String recipient, String sendAmount, String sendCurrency, String receiveAmount, String receiveCurrency) {
        if (mMainView == null) {
            return;
        }
        // we need to verify the required fields have been set properly for this operation.
        if (validateFields(OPERATION_CALCULATE_CURRENCY, recipient, sendAmount, sendCurrency, receiveAmount, receiveCurrency)) {
            // reset receive amount view
            // TODO: MOVE TO string.xml
            mMainView.setReceiveAmount("Calculating");
            // TODO: Catch the NumberFormatException and handle the error.
            float amount = Float.valueOf(sendAmount);
            // TODO: we need to check if the currency code is a supported ISO 4217 currency code
            BusProvider.getInstance().post(new WorldRemitService.CalculateCurrencyEvent(amount, sendCurrency, receiveCurrency));

        }
    }


    @Subscribe
    public void retriveCurriesOnSuccess(WorldRemitService.RetrieveCurrenciesOnSuccessEvent event) {
        sCurrencies = event.getCurrencies();
        if (sCurrencies != null && mMainView != null) {
            mMainView.setCurrencySpinner(sCurrencies);
        }
    }

    @Subscribe
    public void CalculateCurrencyOnSuccess(WorldRemitService.CalculateCurrencyOnSuccessEvent event) {
        if (mMainView == null) {
            return;
        }

        Transaction transaction = event.getTransaction();
        // If the current fields have been set correctly.
        if (!validateFields(OPERATION_CALCULATE_CURRENCY, mMainView.getRecipient(), mMainView.getSendAmount(), mMainView.getSendCurrency(), mMainView.getReceiveAmount(), mMainView.getReceiveCurrency())) {
            return;
        }

        // Check if the fields have been changed since last calculation request.
        if (transaction != null && transaction.getSendAmount() == Float.valueOf(mMainView.getSendAmount()) &&
                transaction.getSendCurrency().equals(mMainView.getSendCurrency()) &&
                transaction.getReceiveCurrency().equals(mMainView.getReceiveCurrency())) {

            mMainView.setReceiveAmount(String.valueOf(transaction.getReceiveAmount()));
        }
    }

    @Subscribe
    public void SendMoneyOnSuccess(WorldRemitService.SendMoneyOnSuccess event) {
        if (mMainView == null) {
            return;
        }
        // enable the send button
        mMainView.setSendButtonEnabled(true);
        showConfirmDialog(event.getTransaction());
    }

    @Subscribe
    public void onRequestFailure(WorldRemitService.RequestOnFailureEvent event) {
        if (mMainView == null) {
            return;
        }
        // TODO: We need to specify how to handle the error.
        mMainView.setSendButtonEnabled(true);
    }


    private void showConfirmDialog(Transaction transaction) {
        if (mMainView == null) {
            return;
        }
        // Move the message title and copy to String.xml for localisation
        AlertDialog.Builder builder = new AlertDialog.Builder(mMainView.getContext())
                .setMessage(transaction.toString())
                .setTitle("Transaction confirmed")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        mMainView.showDialog(dialog);
    }

    // It's a hack for the Unit test, we should remove it and do it properly in the production code.
    public void setCurrencies(List<String> currencies) {
        sCurrencies = currencies;
    }

    /**
     * Validate the required fields for the given operation and
     * display error message to end user if any error occurs.
     * <p/>
     * TODO: It's better to move this method to another domain layer which holds all business rules.
     *
     * @param operation
     * @return return true if all required fields have been set properly, false if at least one required field is failed to pass the verification.
     */
    public boolean validateFields(int operation, String recipient, String sendAmount, String sendCurrency, String receiveAmount, String receiveCurrency) {

        boolean result = true;
        switch (operation) {
            // all fields must be set for this operation.
            case OPERATION_SEND_MONEY:

                if (TextUtils.isEmpty(recipient)) {
                    result = false;
                    // TODO: move the copy to String.xml for localisation.
                    showToast("Please select the recipient!");
                }

                // send amount, send currency and target currency must be set for this operation.
            case OPERATION_CALCULATE_CURRENCY:
                if (sCurrencies == null) {
                    return false;
                }

                if (TextUtils.isEmpty(receiveCurrency)) {
                    result = false;
                    // TODO: move the copy to String.xml for localisation.
                    showToast("Please select target currency!");
                }

                if (TextUtils.isEmpty(sendCurrency)) {
                    result = false;
                    // TODO: move the copy to String.xml for localisation.
                    showToast("Please select send currency!");
                }

                if (TextUtils.isEmpty(sendAmount)) {
                    result = false;
                    // TODO: move the copy to String.xml for localisation.
                    showToast("Please input the amount!");
                }

                try {
                    float sendAmountFloatValue = Float.valueOf(sendAmount);
                    if (sendAmountFloatValue < 0) {
                        result = false;
                        // TODO: move the copy to String.xml for localisation.
                        showToast("Please input valid amount!");
                    }
                } catch (Exception exception) {
                    result = false;
                    // TODO: move the copy to String.xml for localisation.
                    showToast("Please input valid amount!");
                }


        }
        return result;

    }

    private void showToast(String message) {
        if (mMainView != null) {
            mMainView.showToast(message);
        }
    }

}
