package zw.co.paynow.responses;

import zw.co.paynow.constants.ApplicationConstants;
import zw.co.paynow.constants.TransactionStatus;
import zw.co.paynow.exceptions.InvalidIntegrationException;

import java.util.Map;

/**
 * This class is a wrapper around the response received from Paynow
 * after initiating a transaction
 */
public abstract class InitResponse extends PaynowResponse {

    /**
     * The poll url to check the status of a transaction.
     */
    protected final String pollUrl;

    /**
     * The hash value generated
     */
    protected final String hash;

    /**
     * InitResponse constructor.
     * <br>
     * Read through the raw response content received from Paynow, and set response values
     *
     * @param response Raw response content received from Paynow
     * @throws InvalidIntegrationException Thrown if Paynow reports that user used an invalid integration
     */
    public InitResponse(Map<String, String> response)  {

        rawResponseContent = response;

        if (rawResponseContent.containsKey(ApplicationConstants.STATUS)) {
            requestSuccess = (rawResponseContent.get(ApplicationConstants.STATUS).equalsIgnoreCase(TransactionStatus.OK.getResponseString()));
        } else {
            requestSuccess = false;
        }

        if (rawResponseContent.containsKey(ApplicationConstants.STATUS)) {
            String rawStatus = rawResponseContent.get(ApplicationConstants.STATUS);
            status = TransactionStatus.getTransactionStatus(rawStatus);
        } else {
            status = TransactionStatus.UNDEFINED;
        }

        if (rawResponseContent.containsKey(ApplicationConstants.HASH)) {
            hash = rawResponseContent.get(ApplicationConstants.HASH);
        } else {
            hash = "";
        }

        if (rawResponseContent.containsKey(ApplicationConstants.POLLURL)) {
            pollUrl = rawResponseContent.get(ApplicationConstants.POLLURL);
        } else {
            pollUrl = "";
        }

        if (requestSuccess) {
            return;
        }

        if (rawResponseContent.containsKey(ApplicationConstants.ERROR)) {
            fail(rawResponseContent.get(ApplicationConstants.ERROR));
        }

    }

    public String getHash() {
        return hash;
    }

    public String getPollUrl() {
        return pollUrl;
    }

    public String pollUrl() {
        return getPollUrl();
    }

}