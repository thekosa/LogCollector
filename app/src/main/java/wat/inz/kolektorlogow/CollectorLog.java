package wat.inz.kolektorlogow;

import android.util.Log;

import lombok.Data;

public @Data class CollectorLog {
    private String date;
    private String time;
    private String pid;
    private String tid;
    private String priority;
    private String tag;
    private String message;

    public CollectorLog(String logLine) {
        setLog(logLine);
    }

    public CollectorLog() {
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
    }
}
