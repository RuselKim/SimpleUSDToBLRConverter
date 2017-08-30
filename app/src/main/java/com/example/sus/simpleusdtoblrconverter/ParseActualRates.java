package com.example.sus.simpleusdtoblrconverter;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sus on 30.08.2017.
 */

class ParseActualRates extends AsyncTask<Void,Void,String> {

    HttpURLConnection urlConnection;
    BufferedReader reader;
    String result;
    protected double actualRate;

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL("http://www.nbrb.by/API/ExRates/Rates/USD?ParamMode=2");
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line=reader.readLine())!= null){
                buffer.append(line);
            }

            result = buffer.toString();

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonObject = new JSONObject(s);
            actualRate = jsonObject.getDouble("Cur_OfficialRate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
