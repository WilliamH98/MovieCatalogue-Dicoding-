package com.dicoding.MovieCatalogue.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.dicoding.MovieCatalogue.R;
import com.dicoding.MovieCatalogue.db.DatabaseContractTV;
import com.dicoding.MovieCatalogue.db.TVShowsUpdateService;
import com.dicoding.MovieCatalogue.model.TVShows;
import com.dicoding.MovieCatalogue.model.TVShowsFavorite;
import com.dicoding.MovieCatalogue.widget.FavoriteTVWidget;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import jp.wasabeef.picasso.transformations.BlurTransformation;

import static com.dicoding.MovieCatalogue.activity.MainActivity.onFavorite;
import static com.dicoding.MovieCatalogue.fragment.TVShowsTabFragment.genresList;

public class TVShowsDetailsActivity extends AppCompatActivity {
    private ImageView mShowsPosterDetails, mShowsPosterDetails2;
    private TextView mShowsTitle, mShowsYear, mShowsLanguage, mImdbScore, mDescription, mTVShowsGenre;
    private RatingBar mRatingScore;
    private Toolbar mToolbar;
    private ToggleButton mShowsFavorite;
    public static final String EXTRA_TV_SHOWS = "extra_tv_Shows";
    private String URI_EXTRA_TV;
    public TVShows tvShowsGlb;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);
    private Realm realm;
    public static Uri mUri;
    public StringBuilder finalGenre = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshows_details);

        mShowsPosterDetails = findViewById(R.id.iv_shows_poster_details);
        mShowsPosterDetails2 = findViewById(R.id.iv_shows_poster_details2);
        mShowsTitle = findViewById(R.id.tv_shows_title);
        mShowsYear = findViewById(R.id.tv_shows_year);
        mShowsLanguage = findViewById(R.id.tv_shows_language);
        mImdbScore = findViewById(R.id.tv_score);
        mDescription = findViewById(R.id.tv_description);
        mTVShowsGenre = findViewById(R.id.tv_shows_genre);

        if (onFavorite == 0) {
            tvShowsGlb = getIntent().getParcelableExtra(EXTRA_TV_SHOWS);
        } else if (onFavorite == 1) {
            URI_EXTRA_TV = "Details Intent TV";
            tvShowsGlb = getIntent().getParcelableExtra(URI_EXTRA_TV);
        }

        mRatingScore = findViewById(R.id.rtb_score);
        mToolbar = findViewById(R.id.tb_shows_details);
        mShowsFavorite = findViewById(R.id.tb_shows_favorite);
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
        final RealmResults<TVShowsFavorite> checkIsFavorite = realm.where(TVShowsFavorite.class).contains("tvShowsTitle", tvShowsGlb.getTvShowsTitle()).findAll();
        TVShowsFavorite tvShowsFavorite = realm.where(TVShowsFavorite.class).contains("tvShowsTitle", tvShowsGlb.getTvShowsTitle()).findFirst();

        if (tvShowsFavorite != null)
            mUri = DatabaseContractTV.CONTENT_URI
                    .buildUpon()
                    .appendPath(String.valueOf(tvShowsFavorite.getId()))
                    .build();

        String score = tvShowsGlb.getTvShowsImdbScore();
        String imageURL = "https://image.tmdb.org/t/p/w342/";
        String language = tvShowsGlb.getTvShowsOriginalLanguage();
        Locale loc = new Locale(language);
        String languageNameConvert = loc.getDisplayLanguage(loc);

        if (tvShowsGlb.getTvShowsPoster() == null) {
            Picasso.get()
                    .load(R.drawable.no_image)
                    .fit()
                    .centerCrop()
                    .transform(new BlurTransformation(getApplicationContext(), 1, 1))
                    .into(mShowsPosterDetails);

            Picasso.get().load(R.drawable.no_image).fit().centerCrop().into(mShowsPosterDetails2);
        } else {
            imageURL = imageURL.concat(tvShowsGlb.getTvShowsPoster());
            Picasso.get()
                    .load(imageURL)
                    .transform(new BlurTransformation(getApplicationContext(), 1, 1))
                    .into(mShowsPosterDetails);

            Picasso.get().load(imageURL).into(mShowsPosterDetails2);
        }

        mShowsTitle.setText(tvShowsGlb.getTvShowsTitle());

        if (tvShowsGlb.getTvShowsYear().equals(""))
            mShowsYear.setText("");
        else
            mShowsYear.setText(tvShowsGlb.getTvShowsYear().substring(0, 4));

        mShowsLanguage.setText(languageNameConvert);
        mImdbScore.setText(score);

        mDescription.setText(tvShowsGlb.getTvShowsDescription());

        if (onFavorite == 0) {
            StringBuilder genre = new StringBuilder();
            for (int i = 0; i < tvShowsGlb.getGenre().length; i++) {
                if (i == 3) {
                    genre = new StringBuilder(genre.substring(0, genre.length() - 2));
                    break;
                }

                for (int j = 0; j < genresList.size(); j++) {
                    if (tvShowsGlb.getGenre()[i] == genresList.get(j).getId())
                        if (i < tvShowsGlb.getGenre().length - 1)
                            genre.append(genresList.get(j).getName()).append(", ");
                        else
                            genre.append(genresList.get(j).getName());
                }
            }

            finalGenre = genre;
            mTVShowsGenre.setText(genre.toString());
        } else if (onFavorite == 1) {
            assert tvShowsFavorite != null;
            mTVShowsGenre.setText(tvShowsFavorite.getGenre());
        }

        if (!score.equals("_")) {
            float parseScore = Float.parseFloat(score);
            mRatingScore.setRating(parseScore / 2);
        } else {
            mRatingScore.setRating((float) 0);
        }

        if (checkIsFavorite.size() != 0) {
            mShowsFavorite.setChecked(true);
            mShowsFavorite.setBackgroundResource(R.drawable.ic_favorite);
        }

        mShowsFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mShowsFavorite.setChecked(true);
                buttonView.startAnimation(buttonClick);
                mShowsFavorite.setBackgroundResource(R.drawable.ic_favorite);

                ContentValues values = new ContentValues(8);
                values.put(DatabaseContractTV.TVShowsColumns.TV_SHOWS_TITLE, tvShowsGlb.getTvShowsTitle());
                values.put(DatabaseContractTV.TVShowsColumns.TV_SHOWS_YEAR, tvShowsGlb.getTvShowsYear());
                values.put(DatabaseContractTV.TVShowsColumns.TV_SHOWS_SCORE, tvShowsGlb.getTvShowsImdbScore());
                values.put(DatabaseContractTV.TVShowsColumns.TV_SHOWS_DESCRIPTION, tvShowsGlb.getTvShowsDescription());
                values.put(DatabaseContractTV.TVShowsColumns.TV_SHOWS_VOTE_COUNT, tvShowsGlb.getTvShowsVoteCount());
                values.put(DatabaseContractTV.TVShowsColumns.TV_SHOWS_POSTER, tvShowsGlb.getTvShowsPoster());
                values.put(DatabaseContractTV.TVShowsColumns.TV_SHOWS_LANGUAGE, tvShowsGlb.getTvShowsOriginalLanguage());
                values.put(DatabaseContractTV.TVShowsColumns.TV_SHOWS_GENRE, finalGenre.toString());

                TVShowsUpdateService.insertNewTVShowsFav(this, values);
            } else {
                TVShowsFavorite tvShowsFavoriteDelete = realm.where(TVShowsFavorite.class).contains("tvShowsTitle", tvShowsGlb.getTvShowsTitle()).findFirst();

                assert tvShowsFavoriteDelete != null;
                mUri = DatabaseContractTV.CONTENT_URI
                        .buildUpon()
                        .appendPath(String.valueOf(tvShowsFavoriteDelete.getId()))
                        .build();

                mShowsFavorite.setChecked(false);
                buttonView.startAnimation(buttonClick);
                mShowsFavorite.setBackgroundResource(R.drawable.ic_favorite_border);
                TVShowsUpdateService.deleteTVShowsFav(this, mUri);

                if(onFavorite == 1){
                    finish();
                }
            }

            Context context = getApplicationContext();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, FavoriteTVWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
        });
    }
}
