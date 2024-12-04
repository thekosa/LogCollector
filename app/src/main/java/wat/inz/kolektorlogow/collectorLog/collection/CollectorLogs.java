package wat.inz.kolektorlogow.collectorLog.collection;

import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import wat.inz.kolektorlogow.collectorLog.log.CollectorLog;
import wat.inz.kolektorlogow.collectorLog.log.FirestoreLog;
import wat.inz.kolektorlogow.collectorLog.modifiers.CollectorLogsFilter;
import wat.inz.kolektorlogow.collectorLog.modifiers.CollectorLogsSort;
import wat.inz.kolektorlogow.main.MainActivity;

public @Data class CollectorLogs {
    private List<CollectorLog> logsList;

    public CollectorLogs() {
        logsList = new ArrayList<>();
    }

    public void generateLogs(BufferedReader stream) {
        try {
            // int i = 0;
            for (String logLine = stream.readLine(); logLine != null; logLine = stream.readLine()) {
                CollectorLog log = new CollectorLog(logLine);
                if (!log.isEmpty()) {
                    /*
                    if (++i >= 10) {
                        //Log.e("CollectorLogs.generateLogs", "Za dużo logów.");
                        return;
                    }
                    */
                    logsList.add(log);
                    saveLog(new FirestoreLog(log));
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

    private void saveLog(FirestoreLog log) {
        //to jest device id, tu można wpisać takie id np, unikalny dla każdego użytkownika na danym urządzeniu
        //String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        //numer seryjny urządzenia, ale potrzebny root
        //String serial = Build.getSerial();
        MainActivity.db
                .collection(Build.MANUFACTURER + " " + Build.MODEL)
                .add(log)
                .addOnSuccessListener((a) -> Log.d("OgnistyMagazyn", "Log o tagu " + log.getTag() + " zapisany"))
                .addOnFailureListener((a) -> Log.e("OgnistyMagazyn", "Log o tagu" + log.getTag() + " nie zapisany"));
    }
}