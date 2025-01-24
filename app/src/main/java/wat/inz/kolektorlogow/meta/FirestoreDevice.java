package wat.inz.kolektorlogow.meta;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import lombok.Getter;
import lombok.Setter;


public @Getter @Setter class FirestoreDevice {
    private String name;
    private String identifier;

    @SuppressLint("HardwareIds")
    public FirestoreDevice(Context context) {
        this.name = Build.MANUFACTURER + " " + Build.MODEL;
        this.identifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
