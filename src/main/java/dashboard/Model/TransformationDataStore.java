package dashboard.Model;

import java.util.ArrayList;
import java.util.List;

public class TransformationDataStore {

    private static final List<TransformationRecord> records = new ArrayList<>();
    private static final List<TransformationSummary> summaries = new ArrayList<>();

    // Summary-methoden
    public static void addSummary(TransformationSummary summary) {
        summaries.add(summary);
    }

    public static List<TransformationSummary> getSummaries() {
        return summaries;
    }

    public static void clearAllSummaries() {
        summaries.clear();
    }

    public static TransformationSummary getOrCreateSummary(String id, String name) {
        for (TransformationSummary s : summaries) {
            if (s.getTransformationId().equals(id)) {
                return s;
            }
        }
        TransformationSummary newSummary = new TransformationSummary(id, name);
        summaries.add(newSummary);
        return newSummary;
    }

    // Record-methoden
    public static void addRecord(TransformationRecord record) {
        records.add(record);
    }

    public static List<TransformationRecord> getRecords() {
        return records;
    }

    public static void clearAll() {
        records.clear();
    }
}
