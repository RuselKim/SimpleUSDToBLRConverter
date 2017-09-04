package com.example.sus.simpleusdtoblrconverter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private double rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public float convertUsdToByr(float usd) {
        return (float) (usd * rate);
    }

    public float convertByrToUsd(float byr) {
        return (float) (byr / rate);
    }

    public void onClickGetRates(View view) {
        ParseActualRates parseActualRates = new ParseActualRates();
        parseActualRates.execute();

    }

    public void onClickConvert(View view) {

        EditText inputEditText = (EditText) findViewById(R.id.editText_enterPrice);
        RadioButton radioButtonUsdToByr = (RadioButton) findViewById(R.id.rButton_usdToByr);
        RadioButton radioButtonByrToUsd = (RadioButton) findViewById(R.id.rButton_byrToUsd);
        TextView resultTextView = (TextView) findViewById(R.id.textView_result);

        if (inputEditText.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.toast_emptiInput,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        float inputValue = Float.parseFloat(inputEditText.getText().toString());

        if (radioButtonUsdToByr.isChecked()) {
            resultTextView.setText(String.valueOf(convertUsdToByr(inputValue)));
        } else if (radioButtonByrToUsd.isChecked()) {
            resultTextView.setText(String.valueOf(convertByrToUsd(inputValue)));
        }

    }

    class ParseActualRates extends AsyncTask<Void,Void,Double> {

        HttpURLConnection urlConnection;
        BufferedReader reader;
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.rates_progressBar);
        Button buttonGetRates = (Button) findViewById(R.id.button_getRates);
        protected double actualRate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            buttonGetRates.setEnabled(false);
        }

        @Override
        protected Double doInBackground(Void... voids) {
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

                JSONObject jsonObject = new JSONObject(buffer.toString());
                actualRate = jsonObject.getDouble("Cur_OfficialRate");

            } catch (java.io.IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return actualRate;
        }

        @Override
        protected void onPostExecute(Double s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "Actual rates = " + s, Toast.LENGTH_SHORT).show();
            buttonGetRates.setEnabled(true);
            rate = s;

        }
    }
}