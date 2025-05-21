package dashboard.Model;

/**
 * Represents a detailed transformation record from the detail sheets.
 */
public class TransformationRecord {
    private final String transformationId;
    private final String transformationName;
    private final String itemId;
    private final String itemRev;
    private final String process;
    private final String processDetails;
    private final String input;
    private final String output;
    private final String status;
    private final String decision;

    public TransformationRecord(String transformationId, String transformationName, String itemId, String itemRev,
                                String process, String processDetails, String input, String output,
                                String status, String decision) {
        this.transformationId = transformationId;
        this.transformationName = transformationName;
        this.itemId = itemId;
        this.itemRev = itemRev;
        this.process = process;
        this.processDetails = processDetails;
        this.input = input;
        this.output = output;
        this.status = status;
        this.decision = decision;
    }

    public String getTransformationId() { return transformationId; }
    public String getTransformationName() { return transformationName; }
    public String getItemId() { return itemId; }
    public String getItemRev() { return itemRev; }
    public String getProcess() { return process; }
    public String getProcessDetails() { return processDetails; }
    public String getInput() { return input; }
    public String getOutput() { return output; }
    public String getStatus() { return status; }
    public String getDecision() { return decision; }
}
