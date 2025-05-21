package dashboard.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory static storage for summaries and records.
 * This is cleared and repopulated on each Excel import.
 */
public class TransformationDataStore {

    private static final List<TransformationSummary> summaries = new ArrayList<>();
    private static final List<TransformationRecord> records = new ArrayList<>();

    public static void addSummary(TransformationSummary summary) {
        summaries.add(summary);
    }

    public static List<TransformationSummary> getSummaries() {
        return summaries;
    }

    public static void clearAllSummaries() {
        summaries.clear();
    }

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
