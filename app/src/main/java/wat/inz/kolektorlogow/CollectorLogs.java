package wat.inz.kolektorlogow;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public @Data class CollectorLogs {
    private List<CollectorLog> logsList;

    public CollectorLogs() {
        logsList = new ArrayList<>();
    }

    public void generateLogs(BufferedReader stream) {
        try {
            for (String logLine = stream.readLine(); logLine != null; logLine = stream.readLine()) {
                CollectorLog log = new CollectorLog(logLine);
                if (!log.isEmpty()) {
                    logsList.add(log);
                }
            }
        } catch (IOException e) {
            String err = "Błąd odczytu logów z konsoli. ";
            Log.e("CollectorLogs.generateLogs", err, e);
            System.err.println(err + e);
        }
    }

    public void generateLogs(BufferedReader stream, CollectorLogsFilter filter) {
        try {
            for (String logLine = stream.readLine(); logLine != null; logLine = stream.readLine()) {
                CollectorLog log = new CollectorLog(logLine);
                if (!log.isEmpty() && (filter.isNull() || log.isCorrect(filter))) {
                    logsList.add(log);
                }
            }
        } catch (IOException e) {
            String err = "Błąd odczytu logów z konsoli. ";
            Log.e("CollectorLogs.generateLogs", err, e);
            System.err.println(err + e);
        }
    }

    public void filterOutLogs(CollectorLogsFilter filter) {
        List<CollectorLog> logsListCopy = new ArrayList<>();
        for (CollectorLog log : logsList) {
            if (log.isCorrect(filter)) {
                logsListCopy.add(log);
            }
        }
        logsList = logsListCopy;
    }

    public int size() {
        return logsList.size();
    }

    public void destroyLogsList() {
        logsList.clear();
    }
}
