package wat.inz.kolektorlogow;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Data;

public @Data class CollectorLog {
    private String date;
    private String time;
    private String pid;
    private String tid;
    private String priority;
    private String tag;
    private String message;
    private int color;

    public CollectorLog(String logLine) {
        setLog(logLine);
    }

    public void setLog(String logLine) {
        if (logLine.contains("--------- beginning") || logLine.contains("Zabieram się za pracę nad logiem")) {
            return;
        }
        Log.i("CollectorLog.setLog", "Zabieram się za pracę nad logiem: " + logLine);
        String[] parts = logLine.split("\\s+|(?<=:)\\s", 6);
        this.date = parts[0];
        this.time = parts[1];
        this.pid = parts[2];
        this.tid = parts[3];
        this.priority = parts[4];
        String[] tagMessage = parts[5].split("[:\\s]", 2);
        this.tag = tagMessage[0];
        this.message = tagMessage[1];
        switch (Objects.requireNonNull(priority)) {
            case "V":
                color = Color.GRAY;
                break;
            case "D":
                color = Color.GREEN;
                break;
            case "I":
                color = Color.BLUE;
                break;
            case "W":
                color = Color.YELLOW;
                break;
            case "E":
                color = Color.RED;
                break;
            case "F":
                color = Color.MAGENTA;
                break;
            default:
                color = Color.BLACK;
                break;
        }
    }

    public List<String> getRow() {
        List<String> row = new ArrayList<>();
        row.add(date);
        row.add(time);
        row.add(pid);
        row.add(tid);
        row.add(priority);
        row.add(tag);
        row.add(message);
        return row;
    }

    public boolean isEmpty() {
        return date == null && time == null && pid == null && tid == null && priority == null && tag == null && message == null;
    }

    public boolean isCorrect(CollectorLogsFilter filter) {
        return (filter.getTagFilter() == null || Objects.equals(filter.getTagFilter(), tag))
                && (filter.getPriorityFilter() == null || Objects.equals(filter.getPriorityFilter(), priority))
                && (filter.getPidFilter() == null || Objects.equals(filter.getPidFilter(), pid))
                && (filter.getTidFilter() == null || Objects.equals(filter.getTidFilter(), tid));
    }
}
