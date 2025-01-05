package wat.inz.kolektorlogow.DAO;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import wat.inz.kolektorlogow.collectorLog.log.FirestoreLog;

//this is stub
public class FirestoreLogDAO {
    private final FirebaseFirestore connection;

    public FirestoreLogDAO(FirebaseFirestore connection) {
        this.connection = connection;
    }

    public void saveLog(FirestoreLog log) {
        Log.d("OgnistyMagazyn", "Log o tagu " + log.getTag() + " powiedzmy, że został zapisany");
    }

    public void setOrdinalNumber(Runnable callback) {
        FirestoreLog.setStaticOrdinalNumber(2137);
        callback.run();
    }
}