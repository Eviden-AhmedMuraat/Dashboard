package dashboard.Model;

public class TransformationSummary {
    private final String transformationId;
    private final String transformationName;
    private int successCount;
    private int warningCount;
    private int errorCount;

    public TransformationSummary(String transformationId, String transformationName) {
        this.transformationId = transformationId;
        this.transformationName = transformationName;
        this.successCount = 0;
        this.warningCount = 0;
        this.errorCount = 0;
    }

    public void increment(String status) {
        switch (status.toLowerCase()) {
            case "success" -> successCount++;
            case "warning" -> warningCount++;
            case "error"   -> errorCount++;
            default -> {} // onbekende status wordt genegeerd
        }
    }

    public String getTransformationId() {
        return transformationId;
    }

    public String getTransformationName() {
        return transformationName;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getWarningCount() {
        return warningCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public int getTotalCount() {
        return successCount + warningCount + errorCount;
    }

    @Override
    public String toString() {
        return transformationId + " (" + transformationName + ") -> " +
                "Success: " + successCount + ", Warning: " + warningCount + ", Error: " + errorCount;
    }
}
