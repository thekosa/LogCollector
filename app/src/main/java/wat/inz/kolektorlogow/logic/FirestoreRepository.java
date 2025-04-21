package wat.inz.kolektorlogow.logic;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import wat.inz.kolektorlogow.DAO.FirestoreDeviceDAO;
import wat.inz.kolektorlogow.DAO.FirestoreLogDAO;
import wat.inz.kolektorlogow.core.log.FirestoreLog;
import wat.inz.kolektorlogow.meta.FirestoreDevice;

public class FirestoreRepository {
    private final FirebaseFirestore dbConnection;
    private final FirestoreLogDAO firestoreLogDAO;

    public FirestoreRepository(FirebaseFirestore instance) {
        dbConnection = instance;
        firestoreLogDAO = new FirestoreLogDAO(dbConnection);
    }

    public void saveFirestoreLog(FirestoreLog log, Runnable callback) {
        firestoreLogDAO.saveLog(log, callback);
    }

    public void checkDeviceExist(Context context) {
        FirestoreDeviceDAO deviceDAO = new FirestoreDeviceDAO(dbConnection, new FirestoreDevice(context));
        deviceDAO.ifDeviceExist(result -> {
            if (result == 0) {
                deviceDAO.registerDevice(this::doNothing);
            } else if (result == -1) {
                Toast.makeText(context, "Sprawdź połączenie z internetem", Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean checkOrdinalNumber(Context context) {
        final boolean[] checkResult = {false};
        new FirestoreLogDAO(dbConnection).findMaxOrdinalNumber(result -> {
            if (result == -1) {
                Toast.makeText(context, "Sprawdź połączenie z internetem", Toast.LENGTH_LONG).show();
                checkResult[0] = false;
            } else {
                FirestoreLog.setStaticOrdinalNumber(result);
                checkResult[0] = true;
            }
        });
        return checkResult[0];
    }

    private void doNothing(Object object) {
    }
}
