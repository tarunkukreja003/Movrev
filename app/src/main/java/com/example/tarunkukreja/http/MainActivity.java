package com.example.tarunkukreja.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tarunkukreja.http.models.MovieModel;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   ListView listView ;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this) ;
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Image is Loading");
        progressDialog.setCancelable(false);


        listView = (ListView)findViewById(R.id.listView) ;
        //for caching we use the following code
        // Create default options which will be used for every
//  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()

        .cacheInMemory(true)
                .cacheOnDisk(true)

        .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())

        .defaultDisplayImageOptions(defaultOptions)

        .build();
        ImageLoader.getInstance().init(config); // Do it on Application start

               new VideoListTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesData.txt");




    }

    private class VideoListTask extends AsyncTask<String, String, List<MovieModel>>{//the result of the type MovieModel


        HttpURLConnection urlConnection = null ;
        BufferedReader reader = null ;

        @Override
        protected List<MovieModel> doInBackground(String...params) {

            try {
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection() ;

                urlConnection.connect();
               //reader reads data stored in InputStream
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())) ;

                StringBuilder builder = new StringBuilder() ;

                String line = "";

                while((line = reader.readLine())!=null){


                    builder.append(line) ;

                }
                String finalJSON = builder.toString() ;

    //we can use online JSON viewer to view the JSON data in a structured way
                JSONObject parentObject = new JSONObject(finalJSON) ;
                JSONArray parentArray = parentObject.getJSONArray("movies") ;
    //since there are list of movies we need to create a list for movies as well
                List<MovieModel> movieList = new ArrayList<MovieModel>() ;
    //Gson connects our JSON to the model and hence we do not need to write the commented code
                Gson gson = new Gson() ;

                for(int i = 0 ; i<parentArray.length() ; i++) {
                    JSONObject subParent = parentArray.getJSONObject(i);

                   MovieModel model = gson.fromJson(subParent.toString(), MovieModel.class) ;
      /*              model.setMovie(subParent.getString("movie")) ;
                    model.setYear(subParent.getInt("year"));
                    model.setDirector(subParent.getString("director"));
                    model.setDuration(subParent.getString("duration"));
                    model.setRating((float) subParent.getDouble("rating"));
                    model.setTagline(subParent.getString("tagline"));
                    model.setStory(subParent.getString("story"));*/


               /*     List<Cast> castList = new ArrayList<>() ;

                    for(int j=0 ; j < subParent.getJSONArray("cast").length() ; j++){

                        JSONObject castObject = subParent.getJSONArray("cast").getJSONObject(j);
                        MovieModel.Cast cast = new MovieModel.Cast() ;
                        cast.setName(castObject.getString("name"));
                        castList.add(cast);

                    }*/

                  //  model.setCastList(castList);

                    movieList.add(model);
                }
                    return movieList;//if successful then return this




            } catch (Exception e){
                 e.printStackTrace();
            }
            finally {

                if(reader!=null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                if(urlConnection!=null)
                    urlConnection.disconnect();

            }

            return null; // if not successful then return this
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(List<MovieModel> res) {
            //the return statement from doInBackground is retrieved here
            super.onPostExecute(res);
            progressDialog.dismiss();

                MovieAdapter movieAdapter = new MovieAdapter(getApplicationContext(), R.layout.row, res);
                listView.setAdapter(movieAdapter);//the adapter is set on the listView

        }
    }

    public class MovieAdapter extends ArrayAdapter {

        int resource;
        LayoutInflater inflater;
        List<MovieModel> movieModelList;

        public MovieAdapter(Context context, int resource, List<MovieModel> objects) {
            super(context, resource, objects);
            this.resource = resource;
            movieModelList = objects;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        //getView method gets called many times and always it findsView By id which makes the listview sluggish
        //and is bad for memory so we make another class ViewHolder
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //getView gets called multiple times depending upon the number of items our view has
            //that is why we need position variable to store the position
            //convertView is null only before it has accessed the 1st item after that it is not null
            //as it holds the previous reference
            ViewHolder holder = null;
            if (convertView == null) {

                //convertView stores our actual view that we want
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);//resourse is the xml layout we want


                holder.imageIcon = (ImageView) convertView.findViewById(R.id.imageView);
                holder.movie = (TextView) convertView.findViewById(R.id.movie);
                holder.tagline = (TextView) convertView.findViewById(R.id.tagline);
                holder.year = (TextView) convertView.findViewById(R.id.year);
                holder.duration = (TextView) convertView.findViewById(R.id.duration);
                holder.director = (TextView) convertView.findViewById(R.id.director);
                holder.rating = (RatingBar) convertView.findViewById(R.id.rating_bar);
                holder.cast = (TextView) convertView.findViewById(R.id.cast);
                holder.story = (TextView) convertView.findViewById(R.id.story);
                convertView.setTag(holder);
            } else {
                //if the convetView has accessed 1st item and now it is not null then
                holder = (ViewHolder) convertView.getTag();

            }
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);

            // Then later, when you want to display image
            //its 1st parameter is the Image URL and the 2nd is the imageView
            //we also want a progress bar to show the loading of the image so we add a 3rd parameter
            ImageLoader.getInstance().displayImage(movieModelList.get(position).getImage(), holder.imageIcon, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                }
            }); // Default options will be used

            holder.movie.setText(movieModelList.get(position).getMovie());
            holder.tagline.setText(movieModelList.get(position).getTagline());
            holder.year.setText("Year : " + movieModelList.get(position).getYear());
            holder.duration.setText("Duration : " + movieModelList.get(position).getDuration());
            holder.director.setText("Director : " + movieModelList.get(position).getDirector());
            holder.rating.setRating(movieModelList.get(position).getRating() / 2);//generally the rating is out of 10 but we want a rating out of 5 so divide it by 2
            //Cast is an array so to set it we need a for loop to traverse the array
            StringBuilder stringBuilder = new StringBuilder();//we need a stringBuilder to store the cast and pass it into
            //cast textView
            for (MovieModel.Cast cast1 : movieModelList.get(position).getCastList()) {
                stringBuilder.append(cast1.getName() + ", ");
            }


            holder.cast.setText("Cast : " + stringBuilder);
            holder.story.setText("Storyline : " + movieModelList.get(position).getStory());


            return convertView;
        }

        class ViewHolder {

            private ImageView imageIcon;
            private TextView movie;
            private TextView tagline;
            private TextView year;
            private TextView duration;
            private TextView director;
            private RatingBar rating;
            private TextView cast;
            private TextView story;

        }

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
            if (id == R.id.action_refresh) {
                new VideoListTask().execute("http://jsonparsing.parseapp.com/jsonData/moviesData.txt") ;
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

}
