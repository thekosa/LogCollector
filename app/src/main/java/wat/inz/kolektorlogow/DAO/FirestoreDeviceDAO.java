package wat.inz.kolektorlogow.DAO;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import wat.inz.kolektorlogow.meta.FirestoreDevice;

public class FirestoreDeviceDAO {
    private final FirestoreDevice device;
    private final CollectionReference collectionReference;

    public FirestoreDeviceDAO(FirebaseFirestore connection, FirestoreDevice device) {
        this.device = device;
        this.collectionReference = connection.collection("devices-registry");
        System.out.println(collectionReference);
    }

    /**
     * if device exists return 1
     * <br>
     * if device does NOT exist return 0
     * <br>
     * otherwise return -1 - error with connection
     */
    public int ifDeviceExist() {
        final int[] exist = {-1};
        CountDownLatch latch = new CountDownLatch(1);
        collectionReference
                .whereEqualTo("identifier", device.getIdentifier())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    exist[0] = !queryDocumentSnapshots.isEmpty() ? 1 : 0;
                    latch.countDown();
                }).addOnFailureListener(e -> {
                    exist[0] = -1;
                    latch.countDown();
                });
        try {
            if (wait(latch, 30)) {
                exist[0] = -1;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return exist[0];
    }

    public boolean registerDevice() {
        final boolean[] success = {false};
        CountDownLatch latch = new CountDownLatch(1);
        collectionReference
                .add(device)
                .addOnSuccessListener(a -> {
                    Log.d("OgnistyMagazyn", "Urządzenie o nazwie " + device.getName() + " zostało zarejestrowane w bazie");
                    success[0] = true;
                    latch.countDown();
                })
                .addOnFailureListener(a -> {
                    Log.e("OgnistyMagazyn", "Urządzenie o nazwie " + device.getName() + " NIE zostało zarejestrowane w bazie");
                    success[0] = false;
                    latch.countDown();
                });
        try {
            if (!wait(latch, 30)) {
                success[0] = false;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return success[0];
    }

    public int findAllDevices() {
        final int[] devicesCount = {0};
        CountDownLatch latch = new CountDownLatch(1);
        collectionReference
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    devicesCount[0] = queryDocumentSnapshots.size();
                    latch.countDown();
                })
                .addOnFailureListener(e -> {
                    devicesCount[0] = -1;
                    latch.countDown();
                });
        try {
            if (!wait(latch, 30)) {
                devicesCount[0] = -1;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return devicesCount[0];
    }

    private boolean wait(CountDownLatch latch, int seconds) throws InterruptedException {
        return latch.await(seconds, TimeUnit.SECONDS);
    }
}
