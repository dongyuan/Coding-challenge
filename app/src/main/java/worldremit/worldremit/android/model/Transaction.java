package worldremit.worldremit.android.model;

/**
 * A POPJ presents the transaction
 *
 * Created by ericyuan on 13/03/2015.
 */
public class Transaction {

    String recipient;
    float sendamount;
    String sendcurrency;
    float receiveamount;
    String receivecurrency;

    /**
     *
     * Constructs a new {@code Transaction}
     *
     * @param recipient
     * @param sendAmount
     * @param sendCurrency
     * @param receiveAmount
     * @param receiveCurrency
     */
    public Transaction(String recipient, float sendAmount, String sendCurrency, float receiveAmount, String receiveCurrency) {
        this.recipient = recipient;
        this.sendamount = sendAmount;
        this.sendcurrency = sendCurrency;
        this.receiveamount = receiveAmount;
        this.receivecurrency = receiveCurrency;
    }

    /**
     *
     * Constructs a new {@code Transaction} by given {@link String} values.
     * We need to catch the {@link NumberFormatException}
     *
     * @param recipient
     * @param sendAmount
     * @param sendCurrency
     * @param receiveAmount
     * @param receiveCurrency
     */
    public Transaction(String recipient, String sendAmount, String sendCurrency, String receiveAmount, String receiveCurrency) {
        this.recipient = recipient;
        // need to catch the exception in the caller
        this.sendamount = Float.valueOf(sendAmount);
        this.sendcurrency = sendCurrency;
        this.receiveamount = Float.valueOf(receiveAmount);
        this.receivecurrency = receiveCurrency;
    }


    public float getSendAmount() {
        return sendamount;
    }

    public String getSendCurrency() {
        return sendcurrency;
    }

    public float getReceiveAmount() {
        return receiveamount;
    }

    public String getReceiveCurrency() {
        return receivecurrency;
    }

    public String getRecipient() {
        return recipient;
    }

    public String toString(){
        return new StringBuilder("transaction:")
                .append("\nrecipient:"+recipient)
                .append("\nsend amount:"+sendamount)
                .append("\nsend currency:"+sendcurrency)
                .append("\nreceive amount:"+receiveamount)
                .append("\nreceive currency:"+receivecurrency)
                .toString();
    }
}
