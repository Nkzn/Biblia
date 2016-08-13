package info.nkzn.biblia.sample;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.nkzn.biblia.Biblia;
import info.nkzn.biblia.rakuten.Book;
import info.nkzn.biblia.rakuten.SearchParams;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SearchResultActivity extends AppCompatActivity {

    private static String TAG = "SearchResultActivity";
    private static String EXTRA_SEARCH_PARAMS = "searchParams";

    public static Intent createIntent(Context context, SearchParams searchParams) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra(EXTRA_SEARCH_PARAMS, searchParams);
        return intent;
    }

    RecyclerView rvBooks;
    ContentLoadingProgressBar progressBar;

    private final CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvBooks = (RecyclerView) findViewById(R.id.rv_books);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);

        SearchParams params = (SearchParams) getIntent().getSerializableExtra(EXTRA_SEARCH_PARAMS);
        if (params == null) {
            throw new IllegalArgumentException("Use SearchResultActivity.createIntent(SearchParams)");
        }

        search(params);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (subscriptions.isUnsubscribed()) {
            subscriptions.unsubscribe();
        }
        super.onDestroy();
    }

    void search(SearchParams params) {
        Subscription subscription = Biblia.client().search(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSearchFinished, this::onError);

        subscriptions.add(subscription);
    }

    private void onSearchFinished(List<Book> books) {
        Log.d(TAG, "onSearchFinished");
        BookAdapter adapter = new BookAdapter(books, this);
        rvBooks.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvBooks.setLayoutManager(linearLayoutManager);

        progressBar.setVisibility(View.GONE);
        rvBooks.setVisibility(View.VISIBLE);
    }

    private void onError(Throwable throwable) {
        Log.e(TAG, "onError", throwable);
    }

    private static class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {

        final List<Book> books;
        final Context context;
        final LayoutInflater inflater;

        public BookAdapter(List<Book> books, Context context) {
            this.books = books;
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BookViewHolder(inflater.inflate(R.layout.adapter_book, parent, false));
        }

        @Override
        public void onBindViewHolder(BookViewHolder holder, int position) {
            Book book = books.get(position);

            holder.title.setText(book.getTitle());
            holder.subTitle.setText(book.getAuthor());

            Picasso.with(context)
                    .load(book.getSmallImageUrl())
                    .into(holder.icon);
        }

        @Override
        public int getItemCount() {
            return books.size();
        }
    }

    private static class BookViewHolder extends RecyclerView.ViewHolder {
        final ImageView icon;
        final TextView title;
        final TextView subTitle;

        public BookViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.icon);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            subTitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
        }
    }
}
