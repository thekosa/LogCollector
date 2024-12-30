package wat.inz.kolektorlogow.DAO;

import android.os.Build;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import wat.inz.kolektorlogow.collectorLog.log.FirestoreLog;

public class FirestoreLogDAO {
    private FirebaseFirestore connection;

    public FirestoreLogDAO(FirebaseFirestore connection) {
        this.connection = connection;
    }

    public void saveLog(FirestoreLog log) {
        //to jest device id, tu można wpisać takie id np, unikalny dla każdego użytkownika na danym urządzeniu
        //String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        connection
                .collection(Build.MANUFACTURER + " " + Build.MODEL)
                .add(log)
                .addOnSuccessListener(a -> Log.d("OgnistyMagazyn", "Log o tagu " + log.getTag() + " zapisany"))
                .addOnFailureListener(a -> Log.e("OgnistyMagazyn", "Log o tagu" + log.getTag() + " nie zapisany"));
    }

    public void setOrdinalNumber(Runnable callback) {
        String deviceName = Build.MANUFACTURER + " " + Build.MODEL;

        CollectionReference collectionRef = connection.collection(deviceName);
        Query query = collectionRef
                .orderBy("ordinalNumber", Query.Direction.DESCENDING)
                .limit(1);

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {
                FirestoreLog.setStaticOrdinalNumber(0);
            } else {
                Object maxON = queryDocumentSnapshots.getDocuments().get(0).get("ordinalNumber");
                FirestoreLog.setStaticOrdinalNumber(maxON == null ? 0 : (long) maxON);
            }
            if (callback != null) {
                callback.run();
            }
        }).addOnFailureListener(e -> {
            Log.e("OgnistyMagazyn", "Błąd podczas pobierania maksymalnej wartości liczby porządkowej z kolekcji" + deviceName, e);
            if (callback != null) {
                callback.run();
            }
        });
    }
}
