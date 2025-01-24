package wat.inz.kolektorlogow.DAO;

import static org.junit.Assert.assertTrue;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import wat.inz.kolektorlogow.meta.FirestoreDevice;

public class FirestoreDeviceDAOTest {
    private FirestoreDevice mockDevice = Mockito.mock(FirestoreDevice.class);
    private FirebaseFirestore connection = FirebaseFirestore.getInstance();

//    @Before
//    public void setUp() throws Exception {
//        Mockito.when(mockDevice.getIdentifier()).thenReturn("59397c212175f7d9");
//        //before this test, needed is delete document of a device from "devices-registry" collection,
//        //ofcourse if there is the tested device
//    }
//
//    @Test
//    public void ifDeviceExists() {
//        FirestoreDeviceDAO dao = new FirestoreDeviceDAO(connection, mockDevice);
//        dao.ifDeviceNotExist(() -> assertTrue(true));
//    }
//
//    @Test
//    public void registerDevice() {
//        FirestoreDeviceDAO dao = new FirestoreDeviceDAO(connection,
//                new FirestoreDevice(InstrumentationRegistry.getInstrumentation().getTargetContext()));
//        dao.registerDevice();
//        dao.ifDeviceNotExist(Assert::fail);
//    }
}