package wat.inz.kolektorlogow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import wat.inz.kolektorlogow.DAO.FirestoreLogDAO;
import wat.inz.kolektorlogow.core.log.CollectorLog;
import wat.inz.kolektorlogow.core.log.FirestoreLog;

public class IntegrationLogsDAOTests {
    @Test
    public void saveLog() {
        CountDownLatch latch = new CountDownLatch(1);
        FirestoreLog.setStaticOrdinalNumber(7312);
        String deviceName = "Zamockowane Urządzenie";
        FirestoreLogDAO firestoreLogDAO = new FirestoreLogDAO(FirebaseFirestore.getInstance(), deviceName);
        CollectorLog log = new CollectorLog("2025-01-22 15:53:09.515 1219 1811 E error:error log");
        firestoreLogDAO.saveLog(new FirestoreLog(log));

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail();
        }

        firestoreLogDAO.findMaxOrdinalNumber(result -> {
            assertEquals(7313, (long) result);
            latch.countDown();
        });

        try {
            if (!latch.await(30, TimeUnit.SECONDS)) {
                fail();
            }
        } catch (InterruptedException e) {
            fail();
        }
    }
}
