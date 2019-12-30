package zw.co.paynow.responses;

import zw.co.paynow.constants.ApplicationConstants;
import zw.co.paynow.exceptions.InvalidIntegrationException;

import java.util.Map;

public class MobileInitResponse extends InitResponse {

    /**
     * The transaction’s reference from Paynow, which is unique to the transaction at Paynow.
     */
    private final String paynowReference;

    /**
     * The instructions to show to the customer how to complete the payment using mobile money.
     */
    protected final String instructions;

    /**
     * MobileInitResponse constructor.
     * <br>
     * Read through the raw response content received from Paynow, and set response values
     *
     * @param response Raw response content received from Paynow
     * @throws InvalidIntegrationException Thrown if Paynow reports that user used an invalid integration
     */
    public MobileInitResponse(Map<String, String> response) {
        super(response);

        if (rawResponseContent.containsKey(ApplicationConstants.INSTRUCTIONS)) {
            instructions = rawResponseContent.get(ApplicationConstants.INSTRUCTIONS);
        } else {
            instructions = "";
        }

        if (rawResponseContent.containsKey(ApplicationConstants.PAYNOWREFERENCE)) {
            paynowReference = rawResponseContent.get(ApplicationConstants.PAYNOWREFERENCE);
        } else {
            paynowReference = "";
        }
    }

    public String getPaynowReference() {
        return paynowReference;
    }

    public String getInstructions() {
        return instructions;
    }

    public String instructions() {
        return getInstructions();
    }

}
