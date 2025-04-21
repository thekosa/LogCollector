package wat.inz.kolektorlogow.logic;

import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.scottyab.rootbeer.RootBeer;

import java.io.IOException;

import rikka.shizuku.Shizuku;

public class PermissionManager {
    private boolean ADBcheck = false;

    public void setADBcheck(boolean ADB) {
        this.ADBcheck = ADB;
    }

    public boolean getADBcheck() {
        return ADBcheck;
    }

    public boolean isRooted(Context context) {
        RootBeer rootBeer = new RootBeer(context);
        return rootBeer.isRooted();
    }

    public boolean isADB() {
        return Shizuku.pingBinder() && Shizuku.getUid() == 2000;
    }

    public void checkShizuku(Context context) {
        try {
            if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(0);
            }
        } catch (IllegalStateException e) {
            Toast.makeText(context, "Włącz Shizuku", Toast.LENGTH_SHORT).show();
        }
    }

    //source: best answer added by Alex Mamo
    //https://stackoverflow.com/questions/52279144/how-to-verify-if-user-has-network-access-and-show-a-pop-up-alert-when-there-isn
    public boolean isNetworkAvailable() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = process.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
