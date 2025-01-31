package wat.inz.kolektorlogow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;

import androidx.test.annotation.UiThreadTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.scottyab.rootbeer.RootBeer;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import rikka.shizuku.Shizuku;
import wat.inz.kolektorlogow.DAO.FirestoreDeviceDAOTest;
import wat.inz.kolektorlogow.core.collection.CollectorLogs;
import wat.inz.kolektorlogow.core.log.CollectorLog;
import wat.inz.kolektorlogow.core.modifiers.CollectorLogsFilter;
import wat.inz.kolektorlogow.main.MainActivity;

@RunWith(AndroidJUnit4.class)
public class ComponentsTests {
    @Test
    public void gatherLogsIntoList() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            MainActivity mainActivity = new MainActivity();
            BufferedReader bufferedReader;
            try {
                bufferedReader = mainActivity.gatherLogs();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            CollectorLogs collectorLogs = new CollectorLogs();
            collectorLogs.generateLogs(bufferedReader, null);
            assertFalse(collectorLogs.getLogsList().isEmpty());
        });
    }

    @Test
    public void androidPersmissionsCheck() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            RootBeer mockRootBeer = mock(RootBeer.class);
            when(mockRootBeer.isRooted()).thenReturn(true);
            MainActivity mainActivity = new MainActivity();
            when(Shizuku.checkSelfPermission()).thenReturn(PackageManager.PERMISSION_GRANTED);
            mainActivity.refreshPermissions();
            assertEquals("root", mainActivity.getPermissionLevelTextView().getText());
        });
    }

    @Test
    public void filterOutLogs() {
        CollectorLogsFilter collectorLogsFilter = new CollectorLogsFilter(null, "E", null, null);
        CollectorLogs collectorLogs = new CollectorLogs();
        ArrayList<CollectorLog> logsList = new ArrayList<>();
        logsList.add(new CollectorLog("2025-01-22 15:53:09.515 1219 1811 E error:error log"));
        logsList.add(new CollectorLog("2025-01-22 15:53:09.515 1219 1811 F fatal:fatal log"));
        collectorLogs.setLogsList(logsList);
        assertEquals(1, collectorLogs.filterOutLogs(collectorLogsFilter).size());
    }
}
