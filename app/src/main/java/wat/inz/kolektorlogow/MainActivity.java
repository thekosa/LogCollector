package wat.inz.kolektorlogow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button refreshList;
    private TableLayout tableLayout;
    private String logcatCommand;
    private CollectorLogs collectorLogs;
    private DrawerLayout drawerLayout;
    private ImageButton filterButton;
    private NavigationView navigationView;
    private Button saveButton;
    private Spinner prioritySpinner;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        navigationView = findViewById(R.id.navigation_view);
        saveButton = navigationView.findViewById(R.id.save_button);
        prioritySpinner = navigationView.findViewById(R.id.priority_spinner);

        drawerLayout = findViewById(R.id.drawer_layout);
        filterButton = findViewById(R.id.filterButton);
        refreshList = findViewById(R.id.refreshList);
        tableLayout = findViewById(R.id.tableLayout);

        logcatCommand = "logcat -d";
        collectorLogs = new CollectorLogs();

        saveButton.setOnClickListener(this);
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
        if (v.getId() == refreshList.getId()) {
            refreshLogList();
            for (CollectorLog log : collectorLogs.getLogsList()) {
                TableRow row = new TableRow(this);
                List<String> rowData = log.getRow();
                for (String rowElement : rowData) {
                    row.addView(createNewCellTextView(rowElement));
                }
                tableLayout.addView(row);
            }
        }
        if (v.getId() == filterButton.getId()) {
            Toast.makeText(this, "Filter", Toast.LENGTH_SHORT).show();
            drawerLayout.openDrawer(GravityCompat.END);
        }
        if (v.getId() == saveButton.getId()) {
            Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show();
            //todo: funkcjonalność switcha adb, w postaci dopisania adb do polecenia
            //todo: funkcjonalność tag + priority, w postaci dopisania tagu i priorytetu do polecenia razem z *"S, chyba że nie ma jednego z nich to dopisać grepa
            //todo: funkcjonalność pid i tid, w postaci wyfiltrowania listy logów po tych danych
            drawerLayout.closeDrawer(navigationView);
        }
    }
    //todo: sortownie

    private TextView createNewCellTextView(String text) {
        TextView cell = new TextView(this);
        cell.setText(text);
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