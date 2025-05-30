package dashboard.Model;

/**
 * Summary of a single transformation's result counts.
 * Based entirely on the 'summary' tab from the Excel import.
 */
public class TransformationSummary {
    private final String transformationId;
    private final String transformationName;
    private final int successCount;
    private final int warningCount;
    private final int errorCount;

    public TransformationSummary(String transformationId, String transformationName, int successCount, int warningCount, int errorCount) {
        this.transformationId = transformationId;
        this.transformationName = transformationName;
        this.successCount = successCount;
        this.warningCount = warningCount;
        this.errorCount = errorCount;
    }

    public String getTransformationId() { return transformationId; }
    public String getTransformationName() { return transformationName; }
    public int getSuccessCount() { return successCount; }
    public int getWarningCount() { return warningCount; }
    public int getErrorCount()   { return errorCount; }
}
