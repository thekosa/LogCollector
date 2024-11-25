package wat.inz.kolektorlogow;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

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
    private CollectorLogsFilter collectorLogsFilter;
    private CollectorLogsSort collectorLogsSort;
    private Map<String, String> priorityMap;
    private Spinner spinnerSortColumnName;
    private Spinner spinnerSortDirection;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

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
        refreshList = findViewById(R.id.refreshList);
        tableLayout = findViewById(R.id.tableLayout);
        adbSwitch = findViewById(R.id.adb_switch);

        logcatCommand = "logcat -d -v year";
        collectorLogs = new CollectorLogs();
        collectorLogsFiltered = new CollectorLogs();
        collectorLogsFilter = new CollectorLogsFilter(null, null, null, null);
        collectorLogsSort = new CollectorLogsSort("Date & Time", true);

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
        //Przycisk Odświeżania listy logów
        if (v.getId() == refreshList.getId()) {
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
        //Switch komendy adb
        if (v.getId() == adbSwitch.getId()) {
            logcatCommand = adbSwitch.isChecked() ? "adb logcat -d -v year" : "logcat -d -v year";
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
        row.addView(createNewCellTextView("Date & Time", true));
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

    private void refreshLogList() {
        try {
            Process process = Runtime.getRuntime().exec(logcatCommand);
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