package wat.inz.kolektorlogow;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.evrencoskun.tableview.TableView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button refreshList;
    private List<CollectorLog> logList;
    private TextView logListTextView;
    private ScrollView scrollViewLogs;
    private TableLayout tableLayout;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshList = findViewById(R.id.refreshList);
        logListTextView = findViewById(R.id.logList);
        refreshList.setOnClickListener(this);
        scrollViewLogs = findViewById(R.id.scrollViewList);
        logList = new ArrayList<>();
        tableLayout = findViewById(R.id.tableLayout);
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
        if (v.getId() == R.id.refreshList) {
            refreshLogList();
            //Log.i("MainActivity.onClick", "stworzyłem liste logów!");
            for (CollectorLog log : logList) {
                TableRow row = new TableRow(this);
                List<String> rowData = log.getRow();
                for (String rowElement : rowData) {
                    row.addView(createNewTextView(rowElement));
                }
                tableLayout.addView(row);
            }
        }
    }

    private TextView createNewTextView(String text) {
        TextView cell = new TextView(this);
        cell.setText(text);
        cell.setPadding(8, 8, 8, 8);
        return cell;
    }

    @SuppressLint("SetTextI18n")
    private void refreshLogList() {
        logListTextView.setText("Lista logów: \n\n" + getAdbLogCat());
        scrollViewLogs.post(new Runnable() {
            @Override
            public void run() {
                scrollViewLogs.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private String getAdbLogCat() {
        try {
            StringBuilder logBuilder = new StringBuilder();
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String filename = String.valueOf(new Date().getTime());
            CollectorLog collectorLog;
            br.readLine();
            for (String logLine = br.readLine(); logLine != null; logLine = br.readLine()) {
                // logBuilder.append(logLine).append("\n\n");
                //todo ogarnąć żeby nie zapisywał pustych linii
                logList.add(new CollectorLog(logLine));
            }
            br.close();
            return logBuilder.toString();
        } catch (IOException e) {
            Log.e(this.getPackageName(), "Błąd odczytu logcat.", e);
            String err = "Cos jest nie tak" + e;
            System.err.println(err);
            return err;
        }
    }
}