package wat.inz.kolektorlogow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button refreshList;
    private TableLayout tableLayout;
    private String logcatCommand;
    private CollectorLogs collectorLogs;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logcatCommand = "logcat -d";
        collectorLogs = new CollectorLogs();
        refreshList = findViewById(R.id.refreshList);
        refreshList.setOnClickListener(this);
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
    }

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
            collectorLogs.generateLogs(bufferedReader);
            bufferedReader.close();
        } catch (IOException e) {
            String err = "Błąd polecenia logcat. ";
            Log.e(this.getPackageName(), err, e);
            System.err.println(err + e);
        }
    }
}