package wat.inz.kolektorlogow.main;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.scottyab.rootbeer.RootBeer;

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


public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore dbConnection;
    private Button refreshLogListButton;
    private TableLayout logsListTableLayout;
    private String logcatCommand;
    private CollectorLogs collectorLogs;
    private CollectorLogs collectorLogsFiltered;
    private DrawerLayout drawerLayout;
    private ImageButton settingsButton;
    private NavigationView settingsBarNavigationView;
    private Spinner priorityFilterSpinner;
    private EditText tagFilterEditText;
    private EditText pidFilterEditText;
    private EditText tidFilterEditText;
    private boolean ifADB = false;
    private CollectorLogsFilter collectorLogsFilter;
    private CollectorLogsSort collectorLogsSort;
    private Map<String, String> priorityMap;
    private Spinner columnSortSpinner;
    private Spinner directionSortSpinner;
    private CheckBox dateTimeColumnVisibilityCheckBox;
    private CheckBox pidColumnVisibilityCheckBox;
    private CheckBox tidColumnVisibilityCheckBox;
    private CheckBox priorityColumnVisibilityCheckBox;
    private CheckBox tagColumnVisibilityCheckBox;
    private CheckBox messageColumnVisibilityCheckBox;
    private TextView permissionLevelTextView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        permissionLevelTextView = findViewById(R.id.permission_level_textview);
        settingsBarNavigationView = findViewById(R.id.settings_bar_navigation_view);
        priorityFilterSpinner = settingsBarNavigationView.findViewById(R.id.priority_filter_spinner);
        tagFilterEditText = settingsBarNavigationView.findViewById(R.id.tag_filter_edittext);
        pidFilterEditText = settingsBarNavigationView.findViewById(R.id.pid_filter_edittext);
        tidFilterEditText = settingsBarNavigationView.findViewById(R.id.tid_filter_edittext);
        columnSortSpinner = settingsBarNavigationView.findViewById(R.id.column_sort_spinner);
        directionSortSpinner = settingsBarNavigationView.findViewById(R.id.direction_sort_spinner);

        drawerLayout = findViewById(R.id.drawer_layout);
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setEnabled(false);
        refreshLogListButton = findViewById(R.id.refresh_log_list_button);
        refreshLogListButton.setEnabled(false);
        logsListTableLayout = findViewById(R.id.logs_list_table_layout);
        dateTimeColumnVisibilityCheckBox = findViewById(R.id.datetime_column_visibility_checkbox);
        pidColumnVisibilityCheckBox = findViewById(R.id.pid_column_visibility_checkbox);
        tidColumnVisibilityCheckBox = findViewById(R.id.tid_column_visibility_checkbox);
        priorityColumnVisibilityCheckBox = findViewById(R.id.priority_column_visibility_checkbox);
        tagColumnVisibilityCheckBox = findViewById(R.id.tag_column_visibility_checkbox);
        messageColumnVisibilityCheckBox = findViewById(R.id.message_column_visibility_checkbox);
        dateTimeColumnVisibilityCheckBox.setChecked(true);
        pidColumnVisibilityCheckBox.setChecked(true);
        tidColumnVisibilityCheckBox.setChecked(true);
        priorityColumnVisibilityCheckBox.setChecked(true);
        tagColumnVisibilityCheckBox.setChecked(true);
        messageColumnVisibilityCheckBox.setChecked(true);

        logcatCommand = "logcat -d -v year";
        collectorLogs = new CollectorLogs();
        collectorLogsFiltered = new CollectorLogs();
        collectorLogsFilter = new CollectorLogsFilter(null, null, null, null);
        collectorLogsSort = new CollectorLogsSort("Date & Time", true);

        priorityMap = new HashMap<>();
        priorityMap.put("Verbose", "V");
        priorityMap.put("Debug", "D");
        priorityMap.put("Info", "I");
        priorityMap.put("Warning", "W");
        priorityMap.put("Error", "E");
        priorityMap.put("Fatal", "F");
        priorityMap.put("*", null);

        dbConnection = FirebaseFirestore.getInstance();
        new FirestoreLogDAO(dbConnection).setOrdinalNumber(() -> refreshLogListButton.setEnabled(true));

        refreshPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPermissions();
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

    @SuppressLint("SetTextI18n")
    public void refreshPermissions() {
        shizukuCheckPermission();
        RootBeer rootBeer = new RootBeer(this);
        if (rootBeer.isRooted()) {
            permissionLevelTextView.setTextColor(Color.RED);
            permissionLevelTextView.setText("root");
            ifADB = false;
        } else if (Shizuku.pingBinder() && Shizuku.getUid() == 2000) {
            permissionLevelTextView.setTextColor(Color.GREEN);
            permissionLevelTextView.setText("ADB");
            ifADB = true;
        } else {
            permissionLevelTextView.setTextColor(Color.BLUE);
            permissionLevelTextView.setText("Zwykły użytkownik");
            ifADB = false;
        }
    }

    public void onRefreshLogsListButtonClick(View view) {
        refreshPermissions();
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

    public void onSettingsButtonClick(View view) {
        Toast.makeText(this, "Filter", Toast.LENGTH_SHORT).show();
        drawerLayout.openDrawer(GravityCompat.END);
    }

    public void onSaveFiltersButtonClick(View view) {
        collectorLogsSort.setColumnName(columnSortSpinner.getSelectedItem().toString());
        collectorLogsSort.setDirection(directionSortSpinner.getSelectedItem().toString().equals("Ascendingly"));
        collectorLogs.sortOutLogs(collectorLogsSort);
        collectorLogsFilter.setFilter(
                tagFilterEditText.getText().toString(),
                priorityMap.get(priorityFilterSpinner.getSelectedItem().toString()),
                pidFilterEditText.getText().toString(),
                tidFilterEditText.getText().toString()
        );
        collectorLogsFiltered.setLogsList(collectorLogs.filterOutLogs(collectorLogsFilter));
        buildLogsListTableLayout();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        drawerLayout.closeDrawer(settingsBarNavigationView);
    }

    public void onSelectAllColumnsVisibilityButtonClick(View view) {
        dateTimeColumnVisibilityCheckBox.setChecked(true);
        pidColumnVisibilityCheckBox.setChecked(true);
        tidColumnVisibilityCheckBox.setChecked(true);
        priorityColumnVisibilityCheckBox.setChecked(true);
        tagColumnVisibilityCheckBox.setChecked(true);
        messageColumnVisibilityCheckBox.setChecked(true);
    }

    private void shizukuCheckPermission() {
        try {
            if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(0);
            }
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Włącz Shizuku", Toast.LENGTH_SHORT).show();
        }
    }

    private void buildLogsListTableLayout() {
        resetTableLayout();
        for (CollectorLog log : collectorLogsFiltered.getLogsList()) {
            TableRow row = new TableRow(this);
            for (String rowElement : log.getRow(
                    dateTimeColumnVisibilityCheckBox.isChecked(),
                    pidColumnVisibilityCheckBox.isChecked(),
                    tidColumnVisibilityCheckBox.isChecked(),
                    priorityColumnVisibilityCheckBox.isChecked(),
                    tagColumnVisibilityCheckBox.isChecked(),
                    messageColumnVisibilityCheckBox.isChecked())) {
                row.addView(createNewCellTextViewBody(rowElement, log.getColor()));
            }
            logsListTableLayout.addView(row);
        }
    }

    private void resetTableLayout() {
        logsListTableLayout.removeAllViews();
        TableRow row = new TableRow(this);
        if (dateTimeColumnVisibilityCheckBox.isChecked()) {
            row.addView(createNewCellTextView("Date & Time", true));
        }
        if (pidColumnVisibilityCheckBox.isChecked()) {
            row.addView(createNewCellTextView("PID", true));
        }
        if (tidColumnVisibilityCheckBox.isChecked()) {
            row.addView(createNewCellTextView("TID", true));
        }
        if (priorityColumnVisibilityCheckBox.isChecked()) {
            row.addView(createNewCellTextView("Priority", true));
        }
        if (tagColumnVisibilityCheckBox.isChecked()) {
            row.addView(createNewCellTextView("Tag", true));
        }
        if (messageColumnVisibilityCheckBox.isChecked()) {
            row.addView(createNewCellTextView("Message", true));
        }
        logsListTableLayout.addView(row);
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
            if (ifADB) {
                process = Shizuku.newProcess(logcatCommand.split(" "), null, null);
            } else {
                process = Runtime.getRuntime().exec(logcatCommand);
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            collectorLogs.destroyLogsList();
            collectorLogs.generateLogs(bufferedReader, dbConnection);
            bufferedReader.close();

            Runtime.getRuntime().exec("logcat -c");
        } catch (IOException e) {
            String err = "Błąd polecenia logcat. ";
            Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
            Log.e(this.getPackageName(), err, e);
            System.err.println(err + e);
        }
    }
}