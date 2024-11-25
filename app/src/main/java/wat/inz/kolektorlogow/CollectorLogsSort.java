package wat.inz.kolektorlogow;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

public @Data class CollectorLogsSort {
    private String columnName;
    private boolean direction;
    private static final Map<String, Integer> priorityMap = new HashMap<>();
    private static final Map<String, Integer> priorityMapReversed = new HashMap<>();

    public CollectorLogsSort(String columnName, boolean direction) {
        priorityMap.put("V", 1);
        priorityMap.put("D", 2);
        priorityMap.put("I", 3);
        priorityMap.put("W", 4);
        priorityMap.put("E", 5);
        priorityMap.put("F", 6);
        priorityMapReversed.put("F", 1);
        priorityMapReversed.put("E", 2);
        priorityMapReversed.put("W", 3);
        priorityMapReversed.put("I", 4);
        priorityMapReversed.put("D", 5);
        priorityMapReversed.put("V", 6);
        this.columnName = columnName;
        this.direction = direction;
    }

    public String getDirectionString() {
        return direction ? "Ascendingly" : "Descendingly";
    }

    public List<CollectorLog> sort(List<CollectorLog> logsList){
        return sort(columnName, direction, logsList);
    }

    public List<CollectorLog> sort(String columnName, boolean direction, List<CollectorLog> logsList) {
        if (direction) {
            switch (columnName) {
                case "Date & Time":
                    logsList.sort(Comparator.comparing(CollectorLog::getDateTime));
                    break;
                case "PID":
                    logsList.sort(Comparator.comparing(CollectorLog::getPid));
                    break;
                case "TID":
                    logsList.sort(Comparator.comparing(CollectorLog::getTid));
                    break;
                case "Priority":
                    logsList.sort(Comparator.comparingInt(log -> priorityMap.get(log.getPriority().toUpperCase())));
                    break;
                case "Tag":
                    logsList.sort(Comparator.comparing(CollectorLog::getTag));
                    break;
            }
        } else {
            switch (columnName) {
                case "Date & Time":
                    logsList.sort(Comparator.comparing(CollectorLog::getDateTime).reversed());
                    break;
                case "PID":
                    logsList.sort(Comparator.comparing(CollectorLog::getPid).reversed());
                    break;
                case "TID":
                    logsList.sort(Comparator.comparing(CollectorLog::getTid).reversed());
                    break;
                case "Priority":
                    logsList.sort(Comparator.comparingInt(log -> priorityMapReversed.get(log.getPriority().toUpperCase())));
                    break;
                case "Tag":
                    logsList.sort(Comparator.comparing(CollectorLog::getTag).reversed());
                    break;
            }
        }
        return logsList;
    }
}