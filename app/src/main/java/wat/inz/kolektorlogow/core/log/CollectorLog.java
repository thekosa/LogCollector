package wat.inz.kolektorlogow.core.log;

import android.graphics.Color;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import wat.inz.kolektorlogow.core.modifiers.CollectorLogsFilter;

public @Data class CollectorLog {
    private CollectorLogDate dateTime;
    private String pid;
    private String tid;
    private String priority;
    private String tag;
    private String message;
    private int color;

    private static final HashMap<String, Integer> priorityMap = new HashMap<>();

    public CollectorLog(String logLine) {
        setLog(logLine);
        priorityMap.put("Verbose", Color.GRAY);
        priorityMap.put("Debug", Color.GREEN);
        priorityMap.put("Info", Color.BLUE);
        priorityMap.put("Warning", Color.YELLOW);
        priorityMap.put("Error", Color.RED);
        priorityMap.put("Fatal", Color.MAGENTA);
    }

    public void setLog(String logLine) {
        if (logLine.contains("--------- beginning")
                || logLine.contains("Zabieram się za pracę nad logiem")
                || logLine.contains("OgnistyMagazyn")) {
            return;
        }
        Log.i("CollectorLog.setLog", "Zabieram się za pracę nad logiem: " + logLine);
        String[] parts = logLine.split("\\s+|(?<=:)\\s", 6);
        setDateTime(parts[0], parts[1]);
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

    public List<String> getRow(boolean datetimeCheck, boolean pidCheck, boolean tidCheck, boolean priorityCheck, boolean tagCheck, boolean messageCheck) {
        List<String> row = new ArrayList<>();
        if (datetimeCheck) {
            row.add(dateTime.toString());
        }
        if (pidCheck) {
            row.add(pid);
        }
        if (tidCheck) {
            row.add(tid);
        }
        if (priorityCheck) {
            row.add(priority);
        }
        if (tagCheck) {
            row.add(tag);
        }
        if (messageCheck) {
            row.add(message);
        }
        return row;
    }

    public boolean isEmpty() {
        return dateTime == null && pid == null && tid == null && priority == null && tag == null && message == null;
    }

    public boolean isCorrect(CollectorLogsFilter filter) {
        return (filter.getTagFilter() == null || Objects.equals(filter.getTagFilter(), tag))
                && (filter.getPriorityFilter() == null || Objects.equals(filter.getPriorityFilter(), priority))
                && (filter.getPidFilter() == null || Objects.equals(filter.getPidFilter(), pid))
                && (filter.getTidFilter() == null || Objects.equals(filter.getTidFilter(), tid));
    }

    private void setDateTime(String date, String time) {
        try {
            this.dateTime = new CollectorLogDate(date + " " + time);
        } catch (ParseException e) {
            System.err.println("Błąd parsowania daty: " + e.getMessage());
            this.dateTime.setTime(0);
        }
    }
}
