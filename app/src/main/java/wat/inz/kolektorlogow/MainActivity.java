package wat.inz.kolektorlogow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


import wat.inz.kolektorlogow.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button refreshList;
    private List<Log> logList = new ArrayList<>();
    private TextView logListTextView;
    private TextView helloView;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshList = findViewById(R.id.refreshList);
        logListTextView = findViewById(R.id.logList);
        helloView=findViewById(R.id.textView);
        refreshList.setOnClickListener(this);


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
        StringBuilder stringBuilder = new StringBuilder();
        for (Log log : logList) {
            stringBuilder.append(log.toString()).append("\n");
        }

        logListTextView.setText("Lista logów: \n\n" + stringBuilder);
    }

    private void init() {
        refreshList = findViewById(R.id.refreshList);
        logListTextView = findViewById(R.id.logList);
        helloView=findViewById(R.id.textView);
        refreshList.setOnClickListener(this);
    }
}