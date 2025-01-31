package wat.inz.kolektorlogow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.concurrent.CountDownLatch;

import wat.inz.kolektorlogow.DAO.FirestoreDeviceDAO;
import wat.inz.kolektorlogow.meta.FirestoreDevice;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegrationDeviceDAOTests {
    private FirestoreDeviceDAO firestoreDeviceDAO;

    @Before
    public void setUp() {
        FirebaseFirestore connection = FirebaseFirestore.getInstance();
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirestoreDevice firestoreDevice = new FirestoreDevice(context);
        firestoreDeviceDAO = new FirestoreDeviceDAO(connection, firestoreDevice);
    }

    @Test
    public void test1_beforeRegisterDeviceToFirestore() {
        CountDownLatch latch = new CountDownLatch(1);
        firestoreDeviceDAO.ifDeviceExist(result -> {
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
    public void test2_registerDeviceToFirestore() {
        CountDownLatch latch = new CountDownLatch(1);
        firestoreDeviceDAO.registerDevice(result -> {
            assertTrue(result);
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            fail();
        }
    }

    @Test
    public void test3_afterRegisterDeviceToFirestore() {
        CountDownLatch latch = new CountDownLatch(1);
        firestoreDeviceDAO.ifDeviceExist(result -> {
            assertEquals(1, (int) result);
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            fail();
        }
    }
}