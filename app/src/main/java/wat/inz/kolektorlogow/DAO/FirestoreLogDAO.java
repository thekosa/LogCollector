
package wat.inz.kolektorlogow.DAO;

import android.os.Build;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.function.Consumer;

import wat.inz.kolektorlogow.core.log.FirestoreLog;

public class FirestoreLogDAO {
    private final CollectionReference collectionReference;
    private final String deviceName = Build.MANUFACTURER + " " + Build.MODEL;

    public FirestoreLogDAO(FirebaseFirestore connection) {
        this.collectionReference = connection.collection(deviceName);
    }

    public void saveLog(FirestoreLog log) {
        collectionReference
                .add(log)
                .addOnSuccessListener(a -> Log.d("OgnistyMagazyn", "Log o tagu " + log.getTag() + " zapisany"))
                .addOnFailureListener(a -> Log.e("OgnistyMagazyn", "Log o tagu " + log.getTag() + " nie zapisany"));
    }

    public long findMaxOrdinalNumber(Consumer<Long> callback) {
        final long[] maxOrdinalNumber = {5};

        collectionReference
                .orderBy("ordinalNumber", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Object maxNumber = queryDocumentSnapshots.getDocuments().get(0).get("ordinalNumber");
                    if (maxNumber != null) {
                        callback.accept((long) maxNumber);
                    } else {
                        callback.accept((long) 0);
                    }
                }).addOnFailureListener(e -> {
                    Log.e("OgnistyMagazyn", "Błąd podczas pobierania maksymalnej wartości liczby porządkowej z kolekcji" + deviceName, e);
                    callback.accept((long) -1);
                });

        return maxOrdinalNumber[0];
    }
}