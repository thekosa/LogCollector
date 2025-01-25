package wat.inz.kolektorlogow.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;

import wat.inz.kolektorlogow.meta.FirestoreDevice;

public class FirestoreDeviceDAOTest {
    private FirestoreDevice mockDevice = Mockito.mock(FirestoreDevice.class);
    private FirebaseFirestore connection = FirebaseFirestore.getInstance();

    @Test
    public void ifDeviceExist() {
        Mockito.when(mockDevice.getIdentifier()).thenReturn("59397c212175f7d9");
        CountDownLatch latch = new CountDownLatch(1);
        FirestoreDeviceDAO dao = new FirestoreDeviceDAO(connection, mockDevice);
        dao.ifDeviceExist(result -> {
            assertEquals(0, (int) result);
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            fail();
        }
    }

    @Test
    public void registerDevice() {
        FirestoreDevice device = new FirestoreDevice("1234567890123456", "testowe rejestrowane");
        CountDownLatch latch = new CountDownLatch(1);
        FirestoreDeviceDAO dao = new FirestoreDeviceDAO(connection, device);
        dao.registerDevice(result ->
                dao.ifDeviceExist(out -> {
                    assertEquals(1, (int) out);
                    latch.countDown();
                }));

        try {
            latch.await();
        } catch (InterruptedException e) {
            fail();
        }
    }

    @Test
    public void findAllDevices() {
        CountDownLatch latch = new CountDownLatch(1);
        FirestoreDeviceDAO firestoreDeviceDAO = new FirestoreDeviceDAO(connection,
                new FirestoreDevice(InstrumentationRegistry.getInstrumentation().getTargetContext()));
        firestoreDeviceDAO.findAllDevices(result -> {
            assertTrue((int) result > 0);
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            fail();
        }
    }
}