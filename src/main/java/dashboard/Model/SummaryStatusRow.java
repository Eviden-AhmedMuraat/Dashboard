package dashboard.Model;

/**
 * Wrapper class for showing a row in the KPI detail table.
 */
public class SummaryStatusRow {
    private final String transformationId;
    private final String transformationName;
    private final int successCount;
    private final int warningCount;
    private final int errorCount;

    public SummaryStatusRow(String transformationId, String transformationName,
                            int successCount, int warningCount, int errorCount) {
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
