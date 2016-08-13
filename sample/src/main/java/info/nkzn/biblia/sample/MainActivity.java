package info.nkzn.biblia.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import info.nkzn.biblia.rakuten.SearchParams;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText etTitle;
    private EditText etAuthor;
    private EditText etPublisherName;
    private EditText etIsbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTitle = (EditText) findViewById(R.id.et_title);
        etAuthor = (EditText) findViewById(R.id.et_author);
        etPublisherName = (EditText) findViewById(R.id.et_publisher_name);
        etIsbn = (EditText) findViewById(R.id.et_isbn);

        findViewById(R.id.btn_search).setOnClickListener(v -> search());
    }

    private void search() {
        SearchParams.Builder builder = new SearchParams.Builder();

        if (hasValue(etTitle)) {
            builder.title(etTitle.getText().toString());
        }

        if (hasValue(etAuthor)) {
            builder.author(etAuthor.getText().toString());
        }

        if (hasValue(etPublisherName)) {
            builder.publisherName(etPublisherName.getText().toString());
        }

        if (hasValue(etIsbn)) {
            builder.isbn(etIsbn.getText().toString());
        }

        SearchParams searchParams = builder.build();

        Log.d(TAG, new Gson().toJson(searchParams));

        if (!searchParams.isValid()) {
            Toast.makeText(this, "必須項目が入力されていません", Toast.LENGTH_LONG).show();
            return;
        }

        startActivity(SearchResultActivity.createIntent(this, searchParams));
    }

    private boolean hasValue(EditText editText) {
        return !TextUtils.isEmpty(editText.getText().toString());
    }
}
