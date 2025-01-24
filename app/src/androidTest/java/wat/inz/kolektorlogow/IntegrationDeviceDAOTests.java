package wat.inz.kolektorlogow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import wat.inz.kolektorlogow.DAO.FirestoreDeviceDAO;
import wat.inz.kolektorlogow.meta.FirestoreDevice;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegrationDeviceDAOTests {
    private FirebaseFirestore connection;
    private Context context;
    private FirestoreDevice firestoreDevice;
    private FirestoreDeviceDAO firestoreDeviceDAO;

    @Before
    public void setUp() {
        connection = FirebaseFirestore.getInstance();
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        firestoreDevice = new FirestoreDevice(context);
        firestoreDeviceDAO = new FirestoreDeviceDAO(connection, firestoreDevice);
    }

    @Test
    public void test1_beforeRegisterDeviceToFirestore() throws InterruptedException {
        assertEquals(1, firestoreDeviceDAO.findAllDevices());
    }

    @Test
    public void test2_registerDeviceToFirestore() throws InterruptedException {
        assertTrue(firestoreDeviceDAO.registerDevice());
    }

    @Test
    public void test3_afterRegisterDeviceToFirestore() {
        assertEquals(2, firestoreDeviceDAO.findAllDevices());
    }
}