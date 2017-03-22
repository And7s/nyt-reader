package com.example.andre.nytreader;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity  {
    private final static String API_URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private final static String API_KEY = "9e72a512e2114f0486b34318cd134041";

    private NewsAdapter adapter;
    private ArrayList<Doc> docs;
    private ProgressBar pbLoading;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FilterCriteria();
        setContentView(R.layout.activity_main);
        docs = new ArrayList<>();
        RecyclerView rvNews = (RecyclerView) findViewById(R.id.rvNews);
        //LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        final StaggeredGridLayoutManager mLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        rvNews.setLayoutManager(mLayoutManager);
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);

        adapter = new NewsAdapter(docs);
        rvNews.setAdapter(adapter);
        rvNews.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int[] lastVisibleItemPositions = mLayoutManager.findLastVisibleItemPositions(null);

                int itemsCount = mLayoutManager.getItemCount();

                int lastVisibleItem = 0;
                for (int itemPosition : lastVisibleItemPositions) {
                    if (lastVisibleItem < itemPosition) {
                        lastVisibleItem = itemPosition;
                    }
                }

                if (lastVisibleItem + 2 >= itemsCount && ! isLoading && FilterCriteria.hasMorePages) {
                    Log.d("atend", "isatend");
                    isLoading = true;
                    FilterCriteria.page++;

                    Handler handler = new Handler();

                    final Runnable r = new Runnable() {
                        public void run() {
                            load();
                        }
                    };
                    handler.post(r);
                }
            }
        });
        load();
    }

    public void openFilter(View v) {
        FilterDialog fd = new FilterDialog();
        fd.show(getSupportFragmentManager(), "filterDialog");
    }

    public void search(View v) {
        FilterCriteria.page = 1;
        TextView tvSearch = (TextView) findViewById(R.id.tvSearch);
        FilterCriteria.searchTerm = tvSearch.getText().toString();
        Log.d("will trigger a search", FilterCriteria.searchTerm);
        load();
    }

    public void load() {
        pbLoading.setVisibility(View.VISIBLE);
        isLoading = true;   // flag to indicate currently is a request

        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("http", "fire");
        String url = API_URL + "?api-key=" + API_KEY;
        if (FilterCriteria.searchTerm.length() != 0) {
            url += "&q=" + FilterCriteria.searchTerm;
        }
        // append begin date yyyymmdd
        url += "&begin_date="+FilterCriteria.startYear +
                (FilterCriteria.startMonth < 10 ? "0" : "") + FilterCriteria.startMonth +
                (FilterCriteria.startDay < 10 ? "0" : "") + FilterCriteria.startDay;
        // sort
        url += "&sort=" + (FilterCriteria.sort == 0 ? "newest": "oldest");
        // news desks
        url += "&fq=news_desk:(" + (FilterCriteria.includeSports ? "\"Sports\" " : "") +
                (FilterCriteria.includeArts? "\"Arts\" " : "") +
                (FilterCriteria.includeFashion? "\"Fashion\"": "") + ")";
        // pagination
        url += "&page=" + FilterCriteria.page;
        Log.d("will trigger", url);
        if (FilterCriteria.page == 1) docs.clear(); // first page reset, other add at end
        adapter.notifyDataSetChanged();
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                pbLoading.setVisibility(View.GONE);
                isLoading = false;
                try {
                    JSONObject response = responseBody.getJSONObject("response");
                    JSONObject meta = response.getJSONObject("meta");
                    int hits = meta.getInt("hits"),
                            time = meta.getInt("time"),
                            offset = meta.getInt("offset");
                    FilterCriteria.hasMorePages = hits > 10 * FilterCriteria.page;
                    JSONArray arr_docs = response.getJSONArray("docs");
                    for (int i = 0; i < arr_docs.length(); i++) {

                        Doc doc = new Doc(arr_docs.getJSONObject(i));
                        docs.add(doc);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(int i, Header[] h, Throwable t, JSONObject j) {
                pbLoading.setVisibility(View.GONE);
                isLoading = false;
            }
        };
        client.get(url, handler);
    }

}
