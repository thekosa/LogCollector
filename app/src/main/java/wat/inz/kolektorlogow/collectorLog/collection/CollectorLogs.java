package wat.inz.kolektorlogow.collectorLog.collection;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import wat.inz.kolektorlogow.DAO.FirestoreLogDAO;
import wat.inz.kolektorlogow.collectorLog.log.CollectorLog;
import wat.inz.kolektorlogow.collectorLog.log.FirestoreLog;
import wat.inz.kolektorlogow.collectorLog.modifiers.CollectorLogsFilter;
import wat.inz.kolektorlogow.collectorLog.modifiers.CollectorLogsSort;

public @Data class CollectorLogs {
    private List<CollectorLog> logsList;

    public CollectorLogs() {
        logsList = new ArrayList<>();
    }

    public void generateLogs(BufferedReader stream, FirebaseFirestore dbConnection) {
        try {
            int logCount = 0;
            FirestoreLogDAO firestoreLogDAO = new FirestoreLogDAO(dbConnection);
            for (String logLine = stream.readLine(); logLine != null; logLine = stream.readLine()) {
                CollectorLog log = new CollectorLog(logLine);
                if (!log.isEmpty()) {
                    //Limit of 2000 logs, because of device memory, with adb setting there could be a lot more than 2000 logs
                    if (logCount++ < 2000) {
                        logsList.add(log);
                    }
                    firestoreLogDAO.saveLog(new FirestoreLog(log));
                }
            }
        } catch (IOException e) {
            String err = "Błąd odczytu logów z konsoli. ";
            Log.e("CollectorLogs.generateLogs", err, e);
            System.err.println(err + e);
        }
    }

    public List<CollectorLog> filterOutLogs(CollectorLogsFilter collectorLogsFilter) {
        List<CollectorLog> logsListCopy = new ArrayList<>();
        for (CollectorLog log : logsList) {
            if (log.isCorrect(collectorLogsFilter)) {
                logsListCopy.add(log);
            }
        }
        return logsListCopy;
    }

    public void sortOutLogs(CollectorLogsSort collectorLogsSort) {
        logsList = collectorLogsSort.sort(logsList);
    }

    public void destroyLogsList() {
        logsList.clear();
    }
}