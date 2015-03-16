package worldremit.worldremit.android.presenter;

import android.test.AndroidTestCase;

import java.util.ArrayList;


/**
 * Created by ericyuan on 14/03/2015.
 */
public class MainPresenterImplTest extends AndroidTestCase {

    MainPresenterImpl mMainPresenter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainPresenter = new MainPresenterImpl(null);
        mMainPresenter.setCurrencies(new ArrayList<String>());

    }

    public void testValidateFields() {
        // Test OPERATION_CALCULATE_CURRENCY
        String recipient, sendAmount, sendCurrency, receiveAmount, receiveCurrency;
        recipient = "Eric";
        sendAmount = "100";
        sendCurrency = "GBP";
        receiveAmount = "50";
        receiveCurrency = "USD";

        assertTrue("It's failed to validate the fields!", mMainPresenter.validateFields(MainPresenterImpl.OPERATION_CALCULATE_CURRENCY, recipient, sendAmount, sendCurrency, receiveAmount, receiveCurrency));

        sendAmount = "-1";
        assertFalse("It's failed to validate the fields!", mMainPresenter.validateFields(MainPresenterImpl.OPERATION_CALCULATE_CURRENCY, recipient, sendAmount, sendCurrency, receiveAmount, receiveCurrency));

        sendAmount = "ABC";
        assertFalse("It's failed to validate the fields!", mMainPresenter.validateFields(MainPresenterImpl.OPERATION_CALCULATE_CURRENCY, recipient, sendAmount, sendCurrency, receiveAmount, receiveCurrency));

        sendAmount = "100";
        sendCurrency = "";
        assertFalse("It's failed to validate the fields!", mMainPresenter.validateFields(MainPresenterImpl.OPERATION_CALCULATE_CURRENCY, recipient, sendAmount, sendCurrency, receiveAmount, receiveCurrency));

        sendCurrency = "GBP";
        receiveCurrency = "";
        assertFalse("It's failed to validate the fields!", mMainPresenter.validateFields(MainPresenterImpl.OPERATION_CALCULATE_CURRENCY, recipient, sendAmount, sendCurrency, receiveAmount, receiveCurrency));

        // Test OPERATION_SEND_MONEY
        recipient = "Eric";
        sendAmount = "100";
        sendCurrency = "GBP";
        receiveAmount = "50";
        receiveCurrency = "USD";

        assertTrue("It's failed to validate the fields!", mMainPresenter.validateFields(MainPresenterImpl.OPERATION_SEND_MONEY, recipient, sendAmount, sendCurrency, receiveAmount, receiveCurrency));

        recipient = "";
        assertFalse("It's failed to validate the fields!", mMainPresenter.validateFields(MainPresenterImpl.OPERATION_SEND_MONEY, recipient, sendAmount, sendCurrency, receiveAmount, receiveCurrency));


    }
}
