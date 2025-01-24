
package wat.inz.kolektorlogow.DAO;

import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

    public long findMaxOrdinalNumber() {
        final long[] maxOrdinalNumber = {-1};
            CountDownLatch latch = new CountDownLatch(1);
            collectionReference
                    .orderBy("ordinalNumber", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        Object maxNumber = queryDocumentSnapshots.getDocuments().get(0).get("ordinalNumber");
                        if (maxNumber != null) {
                            maxOrdinalNumber[0] = (long) maxNumber;
                        } else {
                            maxOrdinalNumber[0] = 0;
                        }
                        latch.countDown();
                    }).addOnFailureListener(e -> {
                        Log.e("OgnistyMagazyn", "Błąd podczas pobierania maksymalnej wartości liczby porządkowej z kolekcji" + deviceName, e);
                        maxOrdinalNumber[0] = -1;
                        latch.countDown();
                    });
            try {
                if (wait(latch, 30)) {
                    maxOrdinalNumber[0] = -1;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
        }
        return maxOrdinalNumber[0];
    }

    private boolean wait(CountDownLatch latch, int seconds) throws InterruptedException {
        return !latch.await(seconds, TimeUnit.SECONDS);
    }
}