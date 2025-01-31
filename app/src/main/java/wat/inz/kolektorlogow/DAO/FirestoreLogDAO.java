
package wat.inz.kolektorlogow.DAO;

import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.function.Consumer;

import wat.inz.kolektorlogow.core.log.FirestoreLog;

public class FirestoreLogDAO {
    private final CollectionReference collectionReference;
    private final String deviceName;

    public FirestoreLogDAO(FirebaseFirestore connection) {
        this.deviceName = Build.MANUFACTURER + " " + Build.MODEL;
        this.collectionReference = connection.collection(deviceName);
    }

    public FirestoreLogDAO(FirebaseFirestore connection, String deviceName) {
        this.deviceName = deviceName;
        this.collectionReference = connection.collection(this.deviceName);
    }

    public void saveLog(FirestoreLog log, @Nullable Runnable callback) {
        collectionReference
                .add(log)
                .addOnSuccessListener(a -> {
                    Log.d("OgnistyMagazyn", "Log o tagu " + log.getTag() + " zapisany");
                    if (callback != null) {
                        callback.run();
                    }
                })
                .addOnFailureListener(a -> {
                    Log.e("OgnistyMagazyn", "Log o tagu " + log.getTag() + " nie zapisany");
                    if (callback != null) {
                        callback.run();
                    }
                });
    }

    public void findMaxOrdinalNumber(Consumer<Long> callback) {
        collectionReference
                .orderBy("ordinalNumber", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        Long maxNumber = queryDocumentSnapshots.getDocuments().get(0).getLong("ordinalNumber");
                        if (maxNumber != null) {
                            callback.accept(maxNumber);
                        }
                    } else {
                        callback.accept((long) 0);
                    }
                }).addOnFailureListener(e -> {
                    Log.e("OgnistyMagazyn", "Błąd podczas pobierania maksymalnej wartości liczby porządkowej z kolekcji" + deviceName, e);
                    callback.accept((long) -1);
                });
    }
}