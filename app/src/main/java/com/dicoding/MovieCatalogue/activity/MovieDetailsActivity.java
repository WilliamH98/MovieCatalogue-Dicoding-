package com.dicoding.MovieCatalogue.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.dicoding.MovieCatalogue.R;
import com.dicoding.MovieCatalogue.db.DatabaseContract;
import com.dicoding.MovieCatalogue.db.MovieUpdateService;
import com.dicoding.MovieCatalogue.model.Movies;
import com.dicoding.MovieCatalogue.model.MoviesFavorite;
import com.dicoding.MovieCatalogue.widget.FavoriteMovieWidget;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import jp.wasabeef.picasso.transformations.BlurTransformation;

import static com.dicoding.MovieCatalogue.activity.MainActivity.onFavorite;
import static com.dicoding.MovieCatalogue.fragment.MovieTabFragment.genresList;

public class MovieDetailsActivity extends AppCompatActivity {
    private ImageView mMoviePosterDetails, mMoviePosterDetails2;
    private TextView mMovieTitle, mMovieYear, mMovieLanguage, mImdbScore, mDescription, mMovieGenre;
    private RatingBar mRatingScore;
    private Toolbar mToolbar;
    private ToggleButton mMovieFavorite;
    public static String EXTRA_MOVIES = "extra_movies";
    private String URI_EXTRA;
    public Movies moviesGlb;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);
    private Realm realm;
    public static Uri mUri;
    public StringBuilder finalGenre = new StringBuilder();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mMoviePosterDetails = findViewById(R.id.iv_movie_poster_details);
        mMoviePosterDetails2 = findViewById(R.id.iv_movie_poster_details2);
        mMovieTitle = findViewById(R.id.tv_movie_title);
        mMovieYear = findViewById(R.id.tv_movie_year);
        mMovieLanguage = findViewById(R.id.tv_movie_language);
        mImdbScore = findViewById(R.id.tv_score);
        mDescription = findViewById(R.id.tv_description);
        mRatingScore = findViewById(R.id.rtb_score);
        mToolbar = findViewById(R.id.tb_mov_details);
        mMovieFavorite = findViewById(R.id.tb_movie_favorite);
        mMovieGenre = findViewById(R.id.tv_movie_genre);

        if (onFavorite == 0) {
            moviesGlb = getIntent().getParcelableExtra(EXTRA_MOVIES);
        } else if (onFavorite == 1) {
            URI_EXTRA = "Details Intent";
            moviesGlb = getIntent().getParcelableExtra(URI_EXTRA);
        }

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(v -> {
            v.startAnimation(buttonClick);
            finish();
        });

        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        setData();
    }

    private void setData() {
        final RealmResults<MoviesFavorite> checkIsFavorite = realm.where(MoviesFavorite.class).contains("movieTitle", moviesGlb.getMovieTitle()).findAll();
        MoviesFavorite moviesFavorite = realm.where(MoviesFavorite.class).contains("movieTitle", moviesGlb.getMovieTitle()).findFirst();

        if (moviesFavorite != null)
            mUri = DatabaseContract.CONTENT_URI
                    .buildUpon()
                    .appendPath(String.valueOf(moviesFavorite.getId()))
                    .build();

        String score = moviesGlb.getMovieImdbScore();
        String imageURL = "https://image.tmdb.org/t/p/w342/";
        String language = moviesGlb.getMovieOriginalLanguage();
        Locale loc = new Locale(language);
        String languageNameConvert = loc.getDisplayLanguage(loc);

        if (moviesGlb.getMoviePoster() == null) {
            Picasso.get()
                    .load(R.drawable.no_image)
                    .fit()
                    .centerCrop()
                    .transform(new BlurTransformation(getApplicationContext(), 1, 1))
                    .into(mMoviePosterDetails);

            Picasso.get().load(R.drawable.no_image).fit().centerCrop().into(mMoviePosterDetails2);
        } else {
            imageURL = imageURL.concat(moviesGlb.getMoviePoster());
            Picasso.get()
                    .load(imageURL)
                    .transform(new BlurTransformation(getApplicationContext(), 1, 1))
                    .into(mMoviePosterDetails);

            Picasso.get().load(imageURL).into(mMoviePosterDetails2);
        }

        mMovieTitle.setText(moviesGlb.getMovieTitle());

        if (moviesGlb.getMovieYear().equals(""))
            mMovieYear.setText("");
        else
            mMovieYear.setText(moviesGlb.getMovieYear().substring(0, 4));

        mMovieLanguage.setText(languageNameConvert);
        mImdbScore.setText(score);
        mDescription.setText(moviesGlb.getMovieDescription());

        if (onFavorite == 0) {
            StringBuilder genre = new StringBuilder();
            for (int i = 0; i < moviesGlb.getGenre().length; i++) {
                if (i == 3) {
                    genre = new StringBuilder(genre.substring(0, genre.length() - 2));
                    break;
                }

                for (int j = 0; j < genresList.size(); j++) {
                    if (moviesGlb.getGenre()[i] == genresList.get(j).getId())
                        if (i < moviesGlb.getGenre().length - 1)
                            genre.append(genresList.get(j).getName()).append(", ");
                        else
                            genre.append(genresList.get(j).getName());
                }
            }

            finalGenre = genre;
            mMovieGenre.setText(genre.toString());
        } else if (onFavorite == 1) {
            assert moviesFavorite != null;
            mMovieGenre.setText(moviesFavorite.getGenre());
        }

        if (!score.equals("_")) {
            float parseScore = Float.parseFloat(score);
            mRatingScore.setRating(parseScore / 2);
        } else {
            mRatingScore.setRating((float) 0);
        }

        if (checkIsFavorite.size() != 0) {
            mMovieFavorite.setChecked(true);
            mMovieFavorite.setBackgroundResource(R.drawable.ic_favorite);
        }

        mMovieFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mMovieFavorite.setChecked(true);
                buttonView.startAnimation(buttonClick);
                mMovieFavorite.setBackgroundResource(R.drawable.ic_favorite);

                ContentValues values = new ContentValues(8);
                values.put(DatabaseContract.MovieColumns.MOVIE_TITLE, moviesGlb.getMovieTitle());
                values.put(DatabaseContract.MovieColumns.MOVIE_YEAR, moviesGlb.getMovieYear());
                values.put(DatabaseContract.MovieColumns.MOVIE_SCORE, moviesGlb.getMovieImdbScore());
                values.put(DatabaseContract.MovieColumns.MOVIE_DESCRIPTION, moviesGlb.getMovieDescription());
                values.put(DatabaseContract.MovieColumns.MOVIE_VOTE_COUNT, moviesGlb.getMovieVoteCount());
                values.put(DatabaseContract.MovieColumns.MOVIE_POSTER, moviesGlb.getMoviePoster());
                values.put(DatabaseContract.MovieColumns.MOVIE_LANGUAGE, moviesGlb.getMovieOriginalLanguage());
                values.put(DatabaseContract.MovieColumns.MOVIE_GENRE, finalGenre.toString());
                MovieUpdateService.insertNewMovieFav(this, values);
            } else {
                MoviesFavorite moviesFavoriteDelete = realm.where(MoviesFavorite.class).contains("movieTitle", moviesGlb.getMovieTitle()).findFirst();

                assert moviesFavoriteDelete != null;
                mUri = DatabaseContract.CONTENT_URI
                        .buildUpon()
                        .appendPath(String.valueOf(moviesFavoriteDelete.getId()))
                        .build();

                mMovieFavorite.setChecked(false);
                buttonView.startAnimation(buttonClick);
                mMovieFavorite.setBackgroundResource(R.drawable.ic_favorite_border);
                MovieUpdateService.deleteMovieFav(this, mUri);
                if(onFavorite == 1){
                    finish();
                }
            }

            Context context = getApplicationContext();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, FavoriteMovieWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
        });
    }
}
