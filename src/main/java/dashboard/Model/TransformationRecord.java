package dashboard.Model;

public class TransformationRecord {
    private String transformationId;
    private String transformationName;
    private String itemId;
    private String itemRev;
    private String process;
    private String processDetails;
    private String input;
    private String output;
    private String status;    // "Success", "Warning", "Error"
    private String decision;

    public TransformationRecord(String transformationId, String transformationName,
                                String itemId, String itemRev, String process, String processDetails,
                                String input, String output, String status, String decision) {
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

    // Getters
    public String getTransformationId() { return transformationId; }
    public String getTransformationName() { return transformationName; }
    public String getItemId() { return itemId; }
    public String getItemRev() { return itemRev; }
    public String getProcess() { return process; }
    public String getProcessDetails() { return processDetails; }
    public String getInput() { return input; }
    public String getOutput() { return output; }
    public String getDecision() { return decision; }
    public String getStatus() { return status; }
}
