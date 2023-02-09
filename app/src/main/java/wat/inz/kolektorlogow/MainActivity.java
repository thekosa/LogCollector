package wat.inz.kolektorlogow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button refreshList;
    private List<Log> logList = new ArrayList<>();
    private TextView logListTextView;
    private ScrollView scrollViewLogs;
    private TextView commandLine;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshList = findViewById(R.id.refreshList);
        logListTextView = findViewById(R.id.logList);
        refreshList.setOnClickListener(this);
        scrollViewLogs = findViewById(R.id.scrollViewList);
commandLine=findViewById(R.id.commandLine);

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
        }
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
            String[] homeCommand = {"pwd"};
            String[] homeCommand2 = {"ls", "-al"};
            String[] pwdCommand = {"pwd"};
            String[] logcatCommand = {"logcat", "-d"};
            Process process = Runtime.getRuntime().exec(commandLine.getText().toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            for (String log = br.readLine(); log != null; log = br.readLine()) {
                logBuilder.append(log).append("\n\n");
            }
            br.close();
            return logBuilder.toString();
        } catch (IOException e) {
            String err = "Cos jest nie tak" + e;
            System.err.println(err);
            return err;
        }
    }
}