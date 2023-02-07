package wat.inz.kolektorlogow;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.awt.Button;
import java.util.List;

import javax.swing.text.View;

import jdk.internal.net.http.common.Log;
import wat.inz.kolektorlogow.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button refreshList;
    private List<Log> logList = null;
    private TextView logListTextView;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.refreshList.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
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
            stringBuilder.append(log.getLogString()).append("\n");
        }

        logListTextView.setText("Lista logów: \n\n" + stringBuilder);
    }

    private void init() {
        refreshList = findViewById(R.id.refreshList);
        logListTextView = findViewById(R.id.logList);

        refreshList.setOnClickListener(this);
    }
}