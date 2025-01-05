package wat.inz.kolektorlogow.DAO;

import com.google.firebase.firestore.FirebaseFirestore;

import wat.inz.kolektorlogow.meta.FirestoreDevice;

//this is stub
public class FirestoreDeviceDAO {
    private final FirebaseFirestore connection;
    private final FirestoreDevice device;
    private final String registryName = "devices-registry";

    public FirestoreDeviceDAO(FirebaseFirestore connection, FirestoreDevice device) {
        this.connection = connection;
        this.device = device;
    }

    public void ifDeviceExists(Runnable callback) {
    }

    public void registerDevice() {
    }
}
