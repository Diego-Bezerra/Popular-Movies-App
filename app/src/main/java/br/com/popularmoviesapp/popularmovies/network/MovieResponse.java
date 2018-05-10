package br.com.popularmoviesapp.popularmovies.network;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieResponse implements Parcelable {

    private static final String ID_PARAM = "id";
    private static final String ORIGINAL_TITLE_PARAM = "original_title";
    private static final String POSTER_PATH_PARAM = "poster_path";
    private static final String OVERVIEW_PARAM = "overview";
    private static final String VOTE_AVERAGE_PARAM = "vote_average";
    private static final String RELEASE_DATE_PARAM = "release_date";

    private int id;
    public String originalTitle;
    public String posterPath;
    public String overview;
    public double voteAverage;
    public Date releaseDate;

//    public MovieResponse() {
//    }

    public MovieResponse(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            id = Integer.valueOf(jsonObject.getString(ID_PARAM));
            originalTitle = jsonObject.getString(ORIGINAL_TITLE_PARAM);
            posterPath = MovieApiService.getImageThumbPath(jsonObject.getString(POSTER_PATH_PARAM));
            overview = jsonObject.getString(OVERVIEW_PARAM);
            voteAverage = Double.valueOf(jsonObject.getString(VOTE_AVERAGE_PARAM));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            releaseDate = sdf.parse(jsonObject.getString(RELEASE_DATE_PARAM));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private MovieResponse(Parcel in) {
        id = in.readInt();
        originalTitle = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
        releaseDate = new Date(in.readLong());
    }

    @SuppressWarnings("unused")
    public static final Creator<MovieResponse> CREATOR = new Creator<MovieResponse>() {
        @Override
        public MovieResponse createFromParcel(Parcel in) {
            return new MovieResponse(in);
        }

        @Override
        public MovieResponse[] newArray(int size) {
            return new MovieResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeLong(releaseDate.getTime());
    }
}
