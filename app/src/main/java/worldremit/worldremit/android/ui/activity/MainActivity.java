package worldremit.worldremit.android.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import worldremit.worldremit.android.R;
import worldremit.worldremit.android.presenter.MainPresenter;
import worldremit.worldremit.android.presenter.MainPresenterImpl;
import worldremit.worldremit.android.ui.view.MainView;


public class MainActivity extends ActionBarActivity implements MainView, AdapterView.OnItemSelectedListener {


    private final static String TAG = MainActivity.class.getSimpleName();

    /** UI Views**/
    private EditText mEditTextRecipient;
    private EditText mEditTextSendAmount;
    private EditText mEditTextReceiveAmount;
    private Spinner mSpinnerSendCurrency;
    private Spinner mSpinnerReceiveCurrency;
    private Button mSendButton;

    /** Main presenter **/
    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditTextRecipient = (EditText) findViewById(R.id.edit_text_recipient_name);
        mEditTextSendAmount = (EditText) findViewById(R.id.edit_text_send_amount);
        mEditTextReceiveAmount = (EditText) findViewById(R.id.edit_text_receive_amount);
        mSpinnerSendCurrency = (Spinner) findViewById(R.id.spinner_send_currency);
        mSpinnerReceiveCurrency = (Spinner) findViewById(R.id.spinner_receive_currency);
        mSendButton = (Button) findViewById(R.id.button_send);

        mEditTextSendAmount.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mEditTextSendAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mMainPresenter.onSendAmountEditorActionDone(getRecipient(), getSendAmount(), getSendCurrency(), getReceiveAmount(), getReceiveCurrency());
                }
                return false;
            }
        });

        mMainPresenter = new MainPresenterImpl(this);
        mMainPresenter.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMainPresenter.onPause();
    }

    public void onContactPickerClicked(View view) {
        mMainPresenter.onContactPickerClicker();
    }

    public void onSendButtonClicked(View view) {
        mMainPresenter.onSendButtonClicked(getRecipient(), getSendAmount(), getSendCurrency(), getReceiveAmount(), getReceiveCurrency());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mMainPresenter.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mMainPresenter.onItemSelected(getRecipient(), getSendAmount(), getSendCurrency(), getReceiveAmount(), getReceiveCurrency());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setPresenter(MainPresenter presenter) {
        if (mMainPresenter!=null){
            mMainPresenter.removeView(this);
        }
        mMainPresenter = presenter;
    }

    public void setCurrencySpinner(List<String> currencies){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to both spinners
        mSpinnerSendCurrency.setAdapter(adapter);
        mSpinnerReceiveCurrency.setAdapter(adapter);

        mSpinnerSendCurrency.setOnItemSelectedListener(this);
        mSpinnerReceiveCurrency.setOnItemSelectedListener(this);
    }

    @Override
    public void setRecipient(String recipient) {
        mEditTextRecipient.setText(recipient);
    }

    @Override
    public void setSendButtonEnabled(boolean buttonEnabled) {
        mSendButton.setEnabled(buttonEnabled);
    }

    @Override
    public void showDialog(Dialog dialog) {
        dialog.show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setReceiveAmount(String amount) {
        mEditTextReceiveAmount.setText(amount);
    }

    @Override
    public String getRecipient(){
        return mEditTextRecipient.getText().toString();
    }

    @Override
    public String getSendAmount(){
        return mEditTextSendAmount.getText().toString();
    }

    @Override
    public String getSendCurrency(){
        if (mSpinnerSendCurrency.getSelectedItem() == null){
            return "";
        } else {
            return mSpinnerSendCurrency.getSelectedItem().toString();
        }
    }

    @Override
    public String getReceiveAmount(){
        return mEditTextReceiveAmount.getText().toString();
    }

    @Override
    public String getReceiveCurrency(){
            if (mSpinnerReceiveCurrency.getSelectedItem() == null){
                return "";
            } else {
                return mSpinnerReceiveCurrency.getSelectedItem().toString();
            }
        }

}


