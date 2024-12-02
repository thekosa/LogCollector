package wat.inz.kolektorlogow;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuBinderWrapper;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button refreshList;
    private TableLayout tableLayout;
    private String logcatCommand;
    private CollectorLogs collectorLogs;
    private CollectorLogs collectorLogsFiltered;
    private DrawerLayout drawerLayout;
    private ImageButton settingsButton;
    private NavigationView settingsBar;
    private Button saveFiltersButton;
    private Spinner prioritySpinner;
    private EditText tagInput;
    private EditText pidInput;
    private EditText tidInput;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch adbSwitch;
    private CollectorLogsFilter logsListFilter;
    private Map<String, String> priorityMap;
    private Button buttonShizukuConnect;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        buttonShizukuConnect = findViewById(R.id.button_Shizuku_connect);

        settingsBar = findViewById(R.id.navigation_view);
        saveFiltersButton = settingsBar.findViewById(R.id.save_button);
        prioritySpinner = settingsBar.findViewById(R.id.priority_spinner);
        tagInput = settingsBar.findViewById(R.id.tag_input);
        pidInput = settingsBar.findViewById(R.id.pid_input);
        tidInput = settingsBar.findViewById(R.id.tid_input);

        drawerLayout = findViewById(R.id.drawer_layout);
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setEnabled(false);
        refreshList = findViewById(R.id.refreshList);
        tableLayout = findViewById(R.id.tableLayout);
        adbSwitch = findViewById(R.id.adb_switch);

        logcatCommand = "logcat -d";
        collectorLogs = new CollectorLogs();
        collectorLogsFiltered = new CollectorLogs();
        logsListFilter = new CollectorLogsFilter(null, null, null, null);

        saveFiltersButton.setOnClickListener(this);

        priorityMap = new HashMap<>();
        priorityMap.put("Verbose", "V");
        priorityMap.put("Debug", "D");
        priorityMap.put("Info", "I");
        priorityMap.put("Warning", "W");
        priorityMap.put("Error", "E");
        priorityMap.put("Fatal", "F");
        priorityMap.put("*", null);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonShizukuConnect.getId()) {
            // Shizuku.addBinderReceivedListener(() -> {
            if (Shizuku.pingBinder()) {
                Toast.makeText(this, "Shizuku is ready", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Shizuku is not ready", Toast.LENGTH_SHORT).show();
            }
            // });
            if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(0);
                Toast.makeText(this, "Shizuku permission is not ok", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Shizuku permission is ok", Toast.LENGTH_SHORT).show();
            }
/*
            if (Shizuku.pingBinder()) {
                Toast.makeText(this, "Shizuku is connected", Toast.LENGTH_SHORT).show();
                if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Shizuku permission is not ok", Toast.LENGTH_SHORT).show();
                    Shizuku.requestPermission(0);// Wywołanie żądania uprawnień
                }
            } else {
                Toast.makeText(this, "Shizuku is not connected", Toast.LENGTH_SHORT).show();
            }
*/
        }
        //Przycisk Odświeżania listy logów
        if (v.getId() == refreshList.getId()) {
            //executeLogcatWithShizuku();
            refreshLogList();
            collectorLogsFiltered.setLogsList(collectorLogs.filterOutLogs(logsListFilter));
            buildLogsListTableLayout();
            if (!settingsButton.isEnabled()) {
                settingsButton.setEnabled(true);
                settingsButton.setAlpha(1.0f);
                settingsButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500));
            }
        }
        //Przycisk Wyświetlania belki filtrów
        if (v.getId() == settingsButton.getId()) {
            Toast.makeText(this, "Filter", Toast.LENGTH_SHORT).show();
            drawerLayout.openDrawer(GravityCompat.END);
        }
        //Przycisk Zapisywania filtrów
        if (v.getId() == saveFiltersButton.getId()) {
            logsListFilter.setFilter(tagInput.getText().toString(),
                    priorityMap.get(prioritySpinner.getSelectedItem().toString()),
                    pidInput.getText().toString(),
                    tidInput.getText().toString());
            collectorLogsFiltered.setLogsList(collectorLogs.filterOutLogs(logsListFilter));
            buildLogsListTableLayout();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(settingsBar);
        }
        //Switch komendy adb
        if (v.getId() == adbSwitch.getId()) {
            logcatCommand = adbSwitch.isChecked() ? "adb logcat -d" : "logcat -d";
            Toast.makeText(this, logcatCommand, Toast.LENGTH_SHORT).show();
        }
    }
    //todo: sortownie w silniku
    //todo: opcja wyświetlania kolumn w silniku
    //todo: zrobić dwie listy logów, jedna pierwotna zmieniana tylko w momencie odświeżenia listy, druga filtrowana, zmieniana podłóg potrzeb
    //todo: naprawić filtrowanie po wszystkich priorytetach

    private void buildLogsListTableLayout() {
        resetTableLayout();
        for (CollectorLog log : collectorLogsFiltered.getLogsList()) {
            TableRow row = new TableRow(this);
            for (String rowElement : log.getRow()) {
                row.addView(createNewCellTextView(rowElement, false, log.getColor()));
            }
            tableLayout.addView(row);
        }
    }

    private void resetTableLayout() {
        tableLayout.removeAllViews();
        TableRow row = new TableRow(this);
        row.addView(createNewCellTextView("Date", true));
        row.addView(createNewCellTextView("Time", true));
        row.addView(createNewCellTextView("PID", true));
        row.addView(createNewCellTextView("TID", true));
        row.addView(createNewCellTextView("Priority", true));
        row.addView(createNewCellTextView("Tag", true));
        row.addView(createNewCellTextView("Message", true));
        tableLayout.addView(row);

    }

    private void generateCommand(boolean adbSwitch, String tag, String priority) {
        StringBuilder command = new StringBuilder();
        if (adbSwitch) {
            command.append("adb ");
        }
        command.append(logcatCommand);
        boolean priorityIsNotUndefined = priority.compareTo("null") != 0 && priority.compareTo("*") != 0;
        if (tag != null && priorityIsNotUndefined) {
            command.append(tag).append(":").append(priority).append(" *:S");
        } else if (tag != null && priority.compareTo("*") == 0) {
            command.append(tag).append(":").append("V");
        } else if (tag != null && priority.compareTo("null") == 0) {
            command.append("|grep ").append(tag);
        } else if (tag == null && priorityIsNotUndefined) {
            command.append("|grep ").append(priority);
        }
        logcatCommand = command.toString();
    }

    private TextView createNewCellTextView(String text, boolean bold, int color) {
        TextView cell = createNewCellTextView(text, bold);
        cell.setTextColor(color);
        return cell;
    }

    private TextView createNewCellTextView(String text, boolean bold) {
        TextView cell = new TextView(this);
        cell.setText(text);
        cell.setTypeface(null, bold ? Typeface.BOLD : Typeface.NORMAL);
        cell.setPadding(8, 8, 8, 8);
        return cell;
    }

    private void executeLogcatWithShizuku() {
        try {
            // Sprawdzenie, czy Shizuku jest gotowe
            if (!Shizuku.pingBinder()) {
                Toast.makeText(this, "Shizuku is not ready", Toast.LENGTH_SHORT).show();
                return;
            }

            // Wywołanie komendy w procesie z uprawnieniami
            IBinder binder = Shizuku.getBinder();
            ShizukuBinderWrapper wrapper = new ShizukuBinderWrapper(Shizuku.getBinder());
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();

            // Przygotowanie polecenia logcat
            String command = logcatCommand;
//            data.writeStringArray(command.split(" "));
            data.writeString(command);
            System.out.println(data.readString());
            //  ShizukuRemoteProcess process = new ShizukuRemoteProcess();
            wrapper.transact(binder.FIRST_CALL_TRANSACTION, data, reply, 0);
            // Wywołanie Shizuku binder
            // binder.transact(IBinder.FIRST_CALL_TRANSACTION, data, reply, 0);
//            Shizuku.transactRemote(data, reply, 0);

            String result = reply.readString();
//            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            // collectorLogs.destroyLogsList();
            //  collectorLogs.generateLogs(reply.readString());
            Shizuku.UserServiceArgs asd = new Shizuku.UserServiceArgs(this.getComponentName());

            System.out.println(reply);
            System.out.println(result);
            System.out.println(Shizuku.getUid());
            data.recycle();
            reply.recycle();
        } catch (Exception e) {
            String err = "Error executing logcat with Shizuku.";
            Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
            Log.e("Shizuku", err, e);
            System.err.println(err + e);
        }
    }


    private void refreshLogList() {
        try {
            Process process = Shizuku.newProcess(new String[]{"logcat", "-d", "|head"}, null, null);
            ;
            //  Process process = Runtime.getRuntime().exec(logcatCommand);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            collectorLogs.destroyLogsList();
            collectorLogs.generateLogs(bufferedReader);
            bufferedReader.close();
        } catch (IOException e) {
            String err = "Błąd polecenia logcat. ";
            Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
            Log.e(this.getPackageName(), err, e);
            System.err.println(err + e);
        }
    }
}