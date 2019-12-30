package zw.co.paynow.responses;

import zw.co.paynow.constants.ApplicationConstants;
import zw.co.paynow.constants.TransactionStatus;
import zw.co.paynow.exceptions.InvalidIntegrationException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;

/**
 * This class is a wrapper around the status response received from Paynow
 * after checking the current status of a transaction
 */
public class StatusResponse extends PaynowResponse {

    /**
     * The transaction’s reference on the merchant site, which should be unique to the transaction.
     */
    private final String merchantReference;

    /**
     * The transaction’s reference from Paynow, which is unique to the transaction at Paynow.
     */
    private final String paynowReference;

    /**
     * Amount of the transaction, in RTGS$/USD
     */
    private final BigDecimal amount;

    /**
     * Whether the payment has been completed i.e. the amount has been paid.
     */
    private final boolean paid;

    /**
     * StatusResponse constructor.
     * <br>
     * Read through the raw response content received from Paynow, and set response values
     *
     * @param response Raw response content received from Paynow
     * @throws InvalidIntegrationException Thrown if Paynow reports that user used an invalid integration
     */
    public StatusResponse(Map<String, String> response)  {

        rawResponseContent = response;

        if (!rawResponseContent.containsKey(ApplicationConstants.ERROR)) {
            requestSuccess = true;
        } else {
            requestSuccess = false;
        }

        if (rawResponseContent.containsKey(ApplicationConstants.STATUS)) {
            String rawStatus = rawResponseContent.get(ApplicationConstants.STATUS);
            status = TransactionStatus.getTransactionStatus(rawStatus);

            paid = rawResponseContent.get(ApplicationConstants.STATUS).equalsIgnoreCase(TransactionStatus.PAID.getResponseString());
        } else {
            paid = false;

            status = TransactionStatus.UNDEFINED;
        }

        if (rawResponseContent.containsKey(ApplicationConstants.AMOUNT)) {
            amount = new BigDecimal(rawResponseContent.get(ApplicationConstants.AMOUNT));
        } else {
            //set a default value of zero
            amount = new BigDecimal(0);
        }

        if (rawResponseContent.containsKey(ApplicationConstants.REFERENCE)) {
            merchantReference = rawResponseContent.get(ApplicationConstants.REFERENCE);
        } else {
            merchantReference = "";
        }

        if (rawResponseContent.containsKey(ApplicationConstants.PAYNOWREFERENCE)) {
            paynowReference = rawResponseContent.get(ApplicationConstants.PAYNOWREFERENCE);
        } else {
            paynowReference = "";
        }

        if (requestSuccess) {
            return;
        }

        if (rawResponseContent.containsKey(ApplicationConstants.ERROR)) {
            fail(rawResponseContent.get(ApplicationConstants.ERROR));
        }

    }

    /**
     * Method to return the poll url
     *
     * @return Returns the poll url
     */
    public final String pollUrl() {
        return rawResponseContent.containsKey(ApplicationConstants.POLLURL) ? rawResponseContent.get(ApplicationConstants.POLLURL) : "";
    }

    /**
     * The hash value generated
     *
     * @return Returns the hash
     */
    public final String hash() {
        return rawResponseContent.containsKey(ApplicationConstants.HASH) ? rawResponseContent.get(ApplicationConstants.HASH) : "";
    }

    //GETTER METHODS
    public String getMerchantReference() {
        return merchantReference;
    }

    public String getPaynowReference() {
        return paynowReference;
    }

    public BigDecimal getAmount() {
        return BigDecimal.valueOf(amount.doubleValue()).round(new MathContext(2, RoundingMode.HALF_UP));
    }

    public boolean paid() {
        return isPaid();
    }

    public boolean isPaid() {
        return paid;
    }
    //END OF GETTER METHODS

}