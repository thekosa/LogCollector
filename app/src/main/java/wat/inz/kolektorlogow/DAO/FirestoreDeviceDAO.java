package wat.inz.kolektorlogow.DAO;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import wat.inz.kolektorlogow.meta.FirestoreDevice;

public class FirestoreDeviceDAO {
    private final FirebaseFirestore connection;
    private final FirestoreDevice device;
    private final String registryName = "devices-registry";

    public FirestoreDeviceDAO(FirebaseFirestore connection, FirestoreDevice device) {
        this.connection = connection;
        this.device = device;
    }

    public void ifDeviceExists(Runnable callback) {
        connection
                .collection(registryName)
                .whereEqualTo("identifier", device.getIdentifier())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        callback.run();
                    }
                });
    }

    public void registerDevice() {
        connection
                .collection(registryName)
                .add(device)
                .addOnSuccessListener(a -> Log.d("OgnistyMagazyn", "Urządzenie o nazwie " + device.getName() + " zostało zarejestrowane w bazie"))
                .addOnFailureListener(a -> Log.e("OgnistyMagazyn", "Urządzenie o nazwie " + device.getName() + " NIE zostało zarejestrowane w bazie"));
    }
}
