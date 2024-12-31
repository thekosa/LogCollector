package wat.inz.kolektorlogow.main;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import rikka.shizuku.Shizuku;
import wat.inz.kolektorlogow.DAO.FirestoreLogDAO;
import wat.inz.kolektorlogow.R;
import wat.inz.kolektorlogow.collectorLog.collection.CollectorLogs;
import wat.inz.kolektorlogow.collectorLog.log.CollectorLog;
import wat.inz.kolektorlogow.collectorLog.modifiers.CollectorLogsFilter;
import wat.inz.kolektorlogow.collectorLog.modifiers.CollectorLogsSort;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseFirestore dbConnection;
    private Button refreshListButton;
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
    private CollectorLogsFilter collectorLogsFilter;
    private CollectorLogsSort collectorLogsSort;
    private Map<String, String> priorityMap;
    private Spinner spinnerSortColumnName;
    private Spinner spinnerSortDirection;
    private Button buttonSelectAllVisibility;
    private CheckBox checkBoxDateTimeVisibility;
    private CheckBox checkBoxPidVisibility;
    private CheckBox checkBoxTidVisibility;
    private CheckBox checkBoxPriorityVisibility;
    private CheckBox checkBoxTagVisibility;
    private CheckBox checkBoxMassageVisibility;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        dbConnection = FirebaseFirestore.getInstance();

        settingsBar = findViewById(R.id.navigation_view);
        saveFiltersButton = settingsBar.findViewById(R.id.save_button);
        prioritySpinner = settingsBar.findViewById(R.id.priority_spinner);
        tagInput = settingsBar.findViewById(R.id.tag_input);
        pidInput = settingsBar.findViewById(R.id.pid_input);
        tidInput = settingsBar.findViewById(R.id.tid_input);
        spinnerSortColumnName = findViewById(R.id.column_sort);
        spinnerSortDirection = findViewById(R.id.direction_sort);

        drawerLayout = findViewById(R.id.drawer_layout);
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setEnabled(false);
        refreshListButton = findViewById(R.id.refreshList);
        refreshListButton.setEnabled(false);
        tableLayout = findViewById(R.id.tableLayout);
        adbSwitch = findViewById(R.id.adb_switch);
        buttonSelectAllVisibility = findViewById(R.id.button_select_all_visibility);
        checkBoxDateTimeVisibility = findViewById(R.id.checkbox_datetime_visibility);
        checkBoxPidVisibility = findViewById(R.id.checkbox_pid_visibility);
        checkBoxTidVisibility = findViewById(R.id.checkbox_tid_visibility);
        checkBoxPriorityVisibility = findViewById(R.id.checkbox_priority_visibility);
        checkBoxTagVisibility = findViewById(R.id.checkbox_tag_visibility);
        checkBoxMassageVisibility = findViewById(R.id.checkbox_massage_visibility);
        checkBoxDateTimeVisibility.setChecked(true);
        checkBoxPidVisibility.setChecked(true);
        checkBoxTidVisibility.setChecked(true);
        checkBoxPriorityVisibility.setChecked(true);
        checkBoxTagVisibility.setChecked(true);
        checkBoxMassageVisibility.setChecked(true);

        logcatCommand = "logcat -d -v year";
        collectorLogs = new CollectorLogs();
        collectorLogsFiltered = new CollectorLogs();
        collectorLogsFilter = new CollectorLogsFilter(null, null, null, null);
        collectorLogsSort = new CollectorLogsSort("Date & Time", true);

        saveFiltersButton.setOnClickListener(this);
        buttonSelectAllVisibility.setOnClickListener(this);

        priorityMap = new HashMap<>();
        priorityMap.put("Verbose", "V");
        priorityMap.put("Debug", "D");
        priorityMap.put("Info", "I");
        priorityMap.put("Warning", "W");
        priorityMap.put("Error", "E");
        priorityMap.put("Fatal", "F");
        priorityMap.put("*", null);

        new FirestoreLogDAO(dbConnection).setOrdinalNumber(() -> refreshListButton.setEnabled(true));
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
        //Przycisk Odświeżania listy logów
        if (v.getId() == refreshListButton.getId()) {
            refreshLogList();
            collectorLogs.sortOutLogs(collectorLogsSort);
            collectorLogsFiltered.setLogsList(collectorLogs.filterOutLogs(collectorLogsFilter));
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
            collectorLogsSort.setColumnName(spinnerSortColumnName.getSelectedItem().toString());
            collectorLogsSort.setDirection(spinnerSortDirection.getSelectedItem().toString().equals("Ascendingly"));
            collectorLogs.sortOutLogs(collectorLogsSort);
            collectorLogsFilter.setFilter(tagInput.getText().toString(),
                    priorityMap.get(prioritySpinner.getSelectedItem().toString()),
                    pidInput.getText().toString(),
                    tidInput.getText().toString());
            collectorLogsFiltered.setLogsList(collectorLogs.filterOutLogs(collectorLogsFilter));
            buildLogsListTableLayout();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(settingsBar);
        }
        //Switch ADB
        if (v.getId() == adbSwitch.getId()) {
            try {
                if (adbSwitch.isChecked()) {
                    if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                        Shizuku.requestPermission(0);
                        Toast.makeText(this, "Shizuku permission is not ok", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Shizuku permission is ok", Toast.LENGTH_SHORT).show();
                    }
                    if (Shizuku.pingBinder()) {
                        Toast.makeText(this, "Shizuku is ready", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Shizuku is not ready", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (IllegalStateException e) {
                Toast.makeText(this, "Włącz Shizuku", Toast.LENGTH_SHORT).show();
                adbSwitch.setChecked(false);
            }
        }
        //Przycisk zaznaczenia wszystkich kolumn
        if (v.getId() == buttonSelectAllVisibility.getId()) {
            checkBoxDateTimeVisibility.setChecked(true);
            checkBoxPidVisibility.setChecked(true);
            checkBoxTidVisibility.setChecked(true);
            checkBoxPriorityVisibility.setChecked(true);
            checkBoxTagVisibility.setChecked(true);
            checkBoxMassageVisibility.setChecked(true);
        }
    }

    private void buildLogsListTableLayout() {
        resetTableLayout();
        for (CollectorLog log : collectorLogsFiltered.getLogsList()) {
            TableRow row = new TableRow(this);
            for (String rowElement : log.getRow(
                    checkBoxDateTimeVisibility.isChecked(),
                    checkBoxPidVisibility.isChecked(),
                    checkBoxTidVisibility.isChecked(),
                    checkBoxPriorityVisibility.isChecked(),
                    checkBoxTagVisibility.isChecked(),
                    checkBoxMassageVisibility.isChecked())) {
                row.addView(createNewCellTextViewBody(rowElement, log.getColor()));
            }
            tableLayout.addView(row);
        }
    }

    private void resetTableLayout() {
        tableLayout.removeAllViews();
        TableRow row = new TableRow(this);
        if (checkBoxDateTimeVisibility.isChecked()) {
            row.addView(createNewCellTextView("Date & Time", true));
        }
        if (checkBoxPidVisibility.isChecked()) {
            row.addView(createNewCellTextView("PID", true));
        }
        if (checkBoxTidVisibility.isChecked()) {
            row.addView(createNewCellTextView("TID", true));
        }
        if (checkBoxPriorityVisibility.isChecked()) {
            row.addView(createNewCellTextView("Priority", true));
        }
        if (checkBoxTagVisibility.isChecked()) {
            row.addView(createNewCellTextView("Tag", true));
        }
        if (checkBoxMassageVisibility.isChecked()) {
            row.addView(createNewCellTextView("Message", true));
        }
        tableLayout.addView(row);
    }

    private TextView createNewCellTextViewBody(String text, int color) {
        TextView cell = createNewCellTextView(text, false);
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

    private void refreshLogList() {
        try {
            Process process;
            if (adbSwitch.isChecked()) {
                process = Shizuku.newProcess(logcatCommand.split(" "), null, null);
                Shizuku.newProcess(new String[]{"logcat", "-c"}, null, null);
            } else {
                process = Runtime.getRuntime().exec(logcatCommand);
                Runtime.getRuntime().exec("logcat -c");
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            collectorLogs.destroyLogsList();
            collectorLogs.generateLogs(bufferedReader, dbConnection);
            bufferedReader.close();
        } catch (IOException e) {
            String err = "Błąd polecenia logcat. ";
            Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
            Log.e(this.getPackageName(), err, e);
            System.err.println(err + e);
        }
    }
}