package wat.inz.kolektorlogow.meta;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FirestoreDeviceTest {
    private Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Test
    public void getName() {
        FirestoreDevice firestoreDevice = new FirestoreDevice(appContext);
        assertEquals("samsung SM-G781B", firestoreDevice.getName());
    }

    @Test
    public void getIdentifier() {
        FirestoreDevice firestoreDevice = new FirestoreDevice(appContext);
        assertEquals(16, firestoreDevice.getIdentifier().length());
    }
}