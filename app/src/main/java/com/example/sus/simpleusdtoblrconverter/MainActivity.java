package com.example.sus.simpleusdtoblrconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    double rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseActualRates parseActualRates = new ParseActualRates();
        parseActualRates.execute();
        rate = parseActualRates.actualRate;
    }

    public float convertUsdToByr(float usd) {
        return (float) (usd * rate);
    }

    public float convertByrToUsd(float byr) {
        return (float) (byr / rate);
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
}
