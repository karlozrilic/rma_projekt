package zrilic.rmaprojekt;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

public class DetaljiActivity extends AppCompatActivity {

    private Song song;
    private ImageView songThumb;
    private TextView songTitle;
    private TextView artist;
    private TextView album;
    private TextView genre;
    private TextView description;
    private YouTubeThumbnailView youTubeThumbnailView;
    private ImageView youtubeLogo;
    private TextView youtubeError;
    private String ytLink;
    private Button ytButton;

    private ScrollView scrollView;
    int imageHeight = 0;
    private LinearLayout linearLayout;

    private boolean detectedScroll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalji);

        SlidrConfig config = new SlidrConfig.Builder()
                .position(SlidrPosition.LEFT)
                .scrimColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .scrimStartAlpha(0.3f)
                .scrimEndAlpha(0f)
                //.edge(true)
                //.edgeSize(1f)
                .build();

        Slidr.attach(this, config);

        Intent intent = getIntent();
        song = (Song) intent.getSerializableExtra("song");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(song.getStrTrack());
            actionBar.setSubtitle(song.getStrArtist());
        }

        songThumb = findViewById(R.id.trackThumb);
        songTitle = findViewById(R.id.song);
        artist = findViewById(R.id.artist);
        album = findViewById(R.id.album);
        genre = findViewById(R.id.genre);
        description = findViewById(R.id.description);

        scrollView = findViewById(R.id.scr);
        scrollView.setSmoothScrollingEnabled(true);
        linearLayout = findViewById(R.id.ll);

        songTitle.setText(String.valueOf(song.getStrTrack()));
        artist.setText(String.valueOf(song.getStrArtist()));
        album.setText(String.valueOf(song.getStrAlbum()));
        genre.setText(String.valueOf(song.getStrGenre()));

        description.setText(String.valueOf(song.getStrDescription()));
        description.setMovementMethod(new ScrollingMovementMethod());

        Glide.with(this).load(song.getStrTrackThumb()).into(songThumb);

        ytLink = song.getStrMusicVid().split("=")[1];
        ytButton = findViewById(R.id.yt_btn);

        youtubeLogo = findViewById(R.id.youtubeLogo);
        youtubeError = findViewById(R.id.youtubeError);

        youTubeThumbnailView = findViewById(R.id.youtubePlayerView);
        youTubeThumbnailView.setTag(ytLink);
        youTubeThumbnailView.animate();
        youTubeThumbnailView.initialize(Constants.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(ytLink);
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                        youtubeLogo.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.yt_ply, null));
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        youtubeError.setText("Can't load thumbnail");
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                youtubeError.setText("Youtube Thumbnail Initialization Failure (It could be because you don't have YouTube app installed)");
            }

        });

        youTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetaljiActivity.this, YoutubePlayerActivity.class);
                intent.putExtra("video_id", ytLink);
                startActivity(intent);
            }
        });

        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageHeight = songThumb.getMeasuredHeight();
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                final int scrollY = scrollView.getScrollY(); // For ScrollView
                if (scrollY >= imageHeight && !isDetectedScroll()) {
                    setDetectedScroll(true);
                    ValueAnimator va = ValueAnimator.ofInt(imageHeight, 0);
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int val = (Integer) animation.getAnimatedValue();
                            LinearLayout.LayoutParams par = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                            par.height = val;
                            linearLayout.setLayoutParams(par);
                        }
                    });
                    va.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            LinearLayout.LayoutParams par = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                            par.height = 0;
                            linearLayout.setLayoutParams(par);
                        }
                    });
                    va.setDuration(500);
                    va.start();
                } else if (scrollY <= 0 && detectedScroll) {
                    ValueAnimator va = ValueAnimator.ofInt(0, imageHeight);
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int val = (Integer) animation.getAnimatedValue();
                            LinearLayout.LayoutParams par = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                            par.height = val;
                            linearLayout.setLayoutParams(par);
                        }
                    });
                    va.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            LinearLayout.LayoutParams par = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                            par.height = imageHeight;
                            linearLayout.setLayoutParams(par);
                            scrollView.scrollTo(0, 0);
                            setDetectedScroll(false);
                        }
                    });
                    va.setDuration(500);
                    va.start();
                }
            }
        });
        ytButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(song.getStrMusicVid()));
                startActivity(i);
            }
        });
    }

    public boolean isDetectedScroll() {
        return detectedScroll;
    }

    public void setDetectedScroll(boolean detectedScroll) {
        this.detectedScroll = detectedScroll;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}