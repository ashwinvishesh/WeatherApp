package com.ashwinvishesh.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String result = "";
    String weathermain, weatherdesc, weathertemp;
    Button searchbt;
    EditText cityEt;
    TextView resultTv;


    public class DownloaderTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "NOTFOUND";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("NOTFOUND")) {
                Toast.makeText(MainActivity.this, "City Not Found", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject jsonReply = new JSONObject(s);
                    String weatherinfo = jsonReply.getString("weather");
                    JSONObject jsonMain = jsonReply.getJSONObject("main");
                    JSONArray arr = new JSONArray(weatherinfo);


                    weathermain = arr.getJSONObject(0).getString("main");
                    weatherdesc = arr.getJSONObject(0).getString("description");
                    weathertemp = jsonMain.getString("temp");


                    Log.e("weather main ", weathermain);
                    Log.e("weather desc ", weatherdesc);
                    Log.e("weather temp ", weathertemp);

                    resultTv.setText("Weather main : "+weathermain+"\nDescription : "+weatherdesc+"\nTemperature : "+weathertemp+" degree C");
                }
                catch (Exception bae)
                {
                    bae.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchbt = findViewById(R.id.searchbt);
        cityEt = findViewById(R.id.cityEt);
        resultTv = findViewById(R.id.textView);

        searchbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloaderTask dtask = new DownloaderTask();
                dtask.execute("https://openweathermap.org/data/2.5/weather?q=" + cityEt.getText().toString() + "&appid=b6907d289e10d714a6e88b30761fae22");
            }
        });


    }
}
