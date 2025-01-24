package wat.inz.kolektorlogow.DAO;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.function.Consumer;

import wat.inz.kolektorlogow.meta.FirestoreDevice;

public class FirestoreDeviceDAO {
    private final FirestoreDevice device;
    private final CollectionReference collectionReference;

    public FirestoreDeviceDAO(FirebaseFirestore connection, FirestoreDevice device) {
        this.device = device;
        this.collectionReference = connection.collection("devices-registry");
    }

    /**
     * if device exists return 1
     * <br>
     * if device does NOT exist return 0
     * <br>
     * otherwise return -1 - error with connection
     */

    public void ifDeviceExist(Consumer<Integer> callback) {
        collectionReference
                .whereEqualTo("identifier", device.getIdentifier())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> callback.accept(!queryDocumentSnapshots.isEmpty() ? 1 : 0))
                .addOnFailureListener(e -> callback.accept(-1));
    }

    public void registerDevice(Consumer<Boolean> callback) {
        collectionReference
                .add(device)
                .addOnSuccessListener(a -> {
                    Log.d("OgnistyMagazyn", "Urządzenie o nazwie " + device.getName() + " zostało zarejestrowane w bazie");
                    callback.accept(true);

                })
                .addOnFailureListener(a -> {
                    Log.e("OgnistyMagazyn", "Urządzenie o nazwie " + device.getName() + " NIE zostało zarejestrowane w bazie");
                    callback.accept(false);
                });
    }

    public void findAllDevices(Consumer<Integer> callback) {
        collectionReference
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> callback.accept(queryDocumentSnapshots.size()))
                .addOnFailureListener(e -> callback.accept(-1));
    }
}
