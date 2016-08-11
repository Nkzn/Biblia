package info.nkzn.biblia.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import info.nkzn.biblia.Biblia;
import info.nkzn.biblia.rakuten.Book;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText etKeyword;
    private Button btnSearch;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etKeyword = (EditText) findViewById(R.id.et_keyword);
        btnSearch = (Button) findViewById(R.id.btn_search);
        tvResult = (TextView) findViewById(R.id.tv_result);

        btnSearch.setOnClickListener(v -> search());
    }

    private void search() {
        String keyword = etKeyword.getText().toString();
        if (TextUtils.isEmpty(keyword)) {
            return;
        }

        Biblia.client().search(keyword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSearchFinished, this::onError);
    }

    private void onSearchFinished(List<Book> books) {
        tvResult.setText(books.toString());
    }

    private void onError(Throwable throwable) {
        tvResult.setText(String.format("Error:\n%s", throwable.getMessage()));
    }
}
