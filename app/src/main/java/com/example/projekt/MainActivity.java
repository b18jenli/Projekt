package com.example.projekt;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Cats> katter=new ArrayList<>();
    ArrayList<String> katt = new ArrayList();


    private class FetchData extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {
            // These two variables need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a Java string.
            String jsonStr = null;

            try {
                // Construct the URL for the Internet service
                URL url = new URL("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=b18jenli");
                //URL url = new URL("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");

                // Create the request to the PHP-service, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return "[]";
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a lot easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return "[]";
                }
                jsonStr = buffer.toString();
                return jsonStr;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in
                // attempting to parse it.
                return "[]";
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Network error", "Error closing stream", e);
                    }
                }
            }
        }
        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            String s1= o;
            Log.d("Jennas log","Returned:'"+o+"'");


            try {
                // Ditt JSON-objekt som Java
                Log.d("jennas log", "okej1");
                JSONArray cats = new JSONArray(s1);

                //JSONObject json1 = new JSONObject(s);
                Log.d("jennas log", "okej2");
                // När vi har ett JSONObjekt kan vi hämta ut dess beståndsdelar
                // JSONArray a = json1.getJSONArray("location");
                for (int i = 0; i < cats.length(); i++) {
                    JSONObject json1 = cats.getJSONObject(i);
                    String location = json1.getString("location");
                    Log.d("jennas log", "" + location);
                    String name = json1.getString("name");
                    Log.d("jennas log", "" + name);
                    String category = json1.getString("category");
                    Log.d("jennas log", "" + category);
                    String auxdata = json1.getString("auxdata");
                    Log.d("jennas log", "" + auxdata);
                    int cost = json1.getInt("cost");
                    Log.d("jennas log", "" + cost);

                    Cats m1 = new Cats(name, location, category, auxdata, cost);

                    katter.add(m1);
                    Log.d("jennas log", "okej3");
                }
            } catch(Exception e){
                Log.d("jennas log", "E:" + e.getMessage());
            }

            Log.d("jennas log","katt is ok");

            for (int i=0; i<katter.size();i++){
                katt.add(katter.get(i).getName());
            }

            String temp="";
            for (int i=0; i<katt.size();i++){
                temp+= katt.get(i) + " ";
            }
            Log.d("jennas log", "katt has: " + temp);



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.katt_item, R.id.list_item, katt);
            ListView lista= findViewById(R.id.listview);
            lista.setAdapter(adapter);

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), katter.get(position).info(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new FetchData().execute();
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
        if (id == R.id.action_about) {
            katter.clear();
            ArrayList<String> katt2= new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.katt_item, R.id.list_item, katt2);
            ListView lista= findViewById(R.id.listview);
            lista.setAdapter(adapter);

            WebView xwebView = (WebView) findViewById(R.id.WebView_Dash);
            WebSettings webSettings = xwebView.getSettings();
            ((WebSettings) webSettings).setJavaScriptEnabled(true);
            xwebView.loadUrl("file:///android_asset/about.html");
            return true;
        }
        if (id == R.id.action_refresh) {
            katter.clear();
            katt.clear();
            new FetchData().execute();

            WebView mwebView = (WebView) findViewById(R.id.WebView_Dash);
            WebSettings webSettings = mwebView.getSettings();
            ((WebSettings) webSettings).setJavaScriptEnabled(true);
            mwebView.loadUrl("");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.katt_item, R.id.list_item, katt);
            ListView lista= findViewById(R.id.listview);
            lista.setAdapter(adapter);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
