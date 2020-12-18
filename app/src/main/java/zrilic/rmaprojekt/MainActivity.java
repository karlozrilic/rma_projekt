package zrilic.rmaprojekt;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemClickInterface {

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private ShimmerFrameLayout shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.action_bar_title));
        }

        recyclerView = findViewById(R.id.lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shimmer = findViewById(R.id.shimmer);
        listAdapter = new ListAdapter(this);
        listAdapter.setItemClickInterface(this);

        recyclerView.setAdapter(listAdapter);

        String url = "https://rma-projekt-api.vercel.app/api/v1/list-songs";

        RESTTask task = new RESTTask();
        task.execute(url);
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmer.startShimmer();
    }

    @Override
    public void onPause() {
        shimmer.stopShimmer();
        super.onPause();
    }

    public class RESTTask extends AsyncTask<String, Void, List<Song>> {

        @Override
        protected List<Song> doInBackground(String... strings) {
            String adress = strings[0];
            try {
                URL url = new URL(adress);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("GET");
                http.setReadTimeout(15000);
                http.setConnectTimeout(15000);
                http.connect();
                InputStreamReader isr = new InputStreamReader(http.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                Odgovor odgovor = new Gson().fromJson(br, Odgovor.class);
                br.close();
                isr.close();
                Log.wtf("err", String.valueOf(odgovor.getSongs()));
                return odgovor.getSongs();
            } catch (MalformedURLException e) {
                Log.e("Problem adrese", e.getMessage());
            } catch (IOException e) {
                Log.e("Problem pristupa", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Song> songs) {
            // stop animating Shimmer and hide the layout
            if (songs != null) {
                shimmer.stopShimmer();
                shimmer.setVisibility(View.GONE);
            } else {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.shimmer), "Niste povezani na internet", Snackbar.LENGTH_INDEFINITE);
                View sbView = snackbar.getView();
                TextView textView = sbView.findViewById(R.id.snackbar_text);
                textView.setTextColor(Color.RED);
                snackbar.show();
            }
            listAdapter.setSongs(songs);
            listAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onItemClick(Song song) {
        Intent intent = new Intent(this, DetaljiActivity.class);
        intent.putExtra("song", song);
        startActivity(intent);
    }
}