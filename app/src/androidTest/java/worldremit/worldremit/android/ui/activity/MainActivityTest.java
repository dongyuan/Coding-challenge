package worldremit.worldremit.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import worldremit.worldremit.android.R;
import worldremit.worldremit.android.presenter.MainPresenter;
import worldremit.worldremit.android.ui.view.MainView;

/**
 * Created by ericyuan on 14/03/2015.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private final static String TAG = MainActivityTest.class.getSimpleName();
    private MainActivity mMainActivity;
    /**
     * UI Views*
     */
    private EditText mEditTextRecipient;
    private EditText mEditTextSendAmount;
    private EditText mEditTextReceiveAmount;
    private Spinner mSpinnerSendCurrency;
    private Spinner mSpinnerReceiveCurrency;
    private Button mSendButton;

    /**
     * Creates an {@link ActivityInstrumentationTestCase2} that tests the {@link worldremit.worldremit.android.ui.activity.MainActivity}.
     */
    public MainActivityTest() {
        super(MainActivity.class);
        Log.i(TAG, "MainActivityTest");
    }

    @Override
    protected void setUp() throws Exception {
        Log.i(TAG, "setup");
        super.setUp();
        mMainActivity = getActivity();
        // ensure a valid handle to the activity has been returned
        assertNotNull(mMainActivity);

        mMainActivity.setPresenter(new MockMainPresenter(mMainActivity));

        mEditTextRecipient = (EditText) mMainActivity.findViewById(R.id.edit_text_recipient_name);
        mEditTextSendAmount = (EditText) mMainActivity.findViewById(R.id.edit_text_send_amount);
        mEditTextReceiveAmount = (EditText) mMainActivity.findViewById(R.id.edit_text_receive_amount);
        mSpinnerSendCurrency = (Spinner) mMainActivity.findViewById(R.id.spinner_send_currency);
        mSpinnerReceiveCurrency = (Spinner) mMainActivity.findViewById(R.id.spinner_receive_currency);
        mSendButton = (Button) mMainActivity.findViewById(R.id.button_send);

    }

    public void testPreconditions() {
        Log.i(TAG, "testPreconditions");
        assertNotNull(mEditTextRecipient);
        assertNotNull(mEditTextSendAmount);
        assertNotNull(mEditTextReceiveAmount);
        assertNotNull(mSpinnerSendCurrency);
        assertNotNull(mSpinnerReceiveCurrency);
        assertNotNull(mSendButton);
    }

    @UiThreadTest
    public void testSetReceiveAmount() {
        final String amount = "2000";

        getActivity().setReceiveAmount(amount);
        assertEquals("failed to set receive amount !", amount, mEditTextReceiveAmount.getText().toString());
    }

    public void testGetRecipient() {

        assertEquals("failed to get the correct recipient !", getActivity().getRecipient(), mEditTextRecipient.getText().toString());

    }

    public void testGetSendAmount() {

        assertEquals("failed to get the correct send amount !", getActivity().getSendAmount(), mEditTextSendAmount.getText().toString());

    }

    @UiThreadTest
    public void testGetSendCurrency() {

        if (mSpinnerReceiveCurrency.getSelectedItem() != null) {
            String currency = mSpinnerReceiveCurrency.getSelectedItem().toString();
            assertEquals("failed to get the correct send currency !", getActivity().getSendCurrency(), currency);
        }
    }

    public void testGetReceiveAmount() {

        assertEquals("failed to get the correct receive amount !", getActivity().getReceiveAmount(), mEditTextReceiveAmount.getText().toString());
    }

    @UiThreadTest
    public void testGetReceiveCurrency() {

        assertEquals("failed to get the correct receive currency !", getActivity().getReceiveCurrency(), mSpinnerReceiveCurrency.getSelectedItem().toString());
    }

    @UiThreadTest
    public void testSetCurrencySpinner() {

        String[] currency = {"TEST"};
        getActivity().setCurrencySpinner(Arrays.asList(currency));
        assertEquals("failed to set currency on the spinner !", getActivity().getSendCurrency(), "TEST");
    }

    @UiThreadTest
    public void testSetRecipient() {
        final String TEST_RECEIPIENT = "TEST_RECEIPIENT";

        getActivity().setRecipient(TEST_RECEIPIENT);
        assertEquals("failed to set recipient !", TEST_RECEIPIENT, mEditTextRecipient.getText().toString());
    }

    @UiThreadTest
    public void testSetSendButtonEnabled() {
        boolean TEST_BUTTON_ENABLED = true;
        getActivity().setSendButtonEnabled(TEST_BUTTON_ENABLED);

        assertTrue("failed to send the send money button!", mSendButton.isEnabled());

        boolean TEST_BUTTON_NOT_ENABLED = false;
        getActivity().setSendButtonEnabled(TEST_BUTTON_NOT_ENABLED);

        assertFalse("failed to send the send money button!", mSendButton.isEnabled());

    }

    class MockMainPresenter implements MainPresenter {
        MainView mMainView;

        MockMainPresenter(MainView mainView) {
            mMainView = mainView;
            final List<String> currencies = new ArrayList<>();
            currencies.add("GBP");
            currencies.add("USD");
            currencies.add("PHP");
            currencies.add("EUR");
            mainView.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMainView.setCurrencySpinner(currencies);
                }
            });

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {

        }

        @Override
        public void onResume() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {

        }

        @Override
        public void onSendAmountEditorActionDone(String recipient, String sendAmount, String sendCurrency, String receiveAmount, String receiveCurrency) {

        }

        @Override
        public void onItemSelected(String recipient, String sendAmount, String sendCurrency, String receiveAmount, String receiveCurrency) {

        }

        @Override
        public void onContactPickerClicker() {

        }

        @Override
        public void onSendButtonClicked(String recipient, String sendAmount, String sendCurrency, String receiveAmount, String receiveCurrency) {

        }

        @Override
        public void removeView(MainView mainView) {

        }

        @Override
        public boolean validateFields(int operation, String recipient, String sendAmount, String sendCurrency, String receiveAmount, String receiveCurrency) {
            return false;
        }
    }


}
