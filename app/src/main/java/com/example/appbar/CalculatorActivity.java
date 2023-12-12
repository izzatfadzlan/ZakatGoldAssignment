package com.example.appbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.math.BigDecimal;

public class CalculatorActivity extends AppCompatActivity {

    private EditText goldWeight;
    private EditText goldValue;
    private TextView goldWeightminUrufff;
    private TextView zakatPayables;
    private TextView totalZkt;
    private RadioGroup goldTypeRadioGroup;
    private RadioButton goldTypeKeep;
    private RadioButton goldTypeWear;

    Toolbar calculatorToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);

        calculatorToolbar = findViewById(R.id.calculator_toolbar);

        setSupportActionBar(calculatorToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Calculator");

        goldWeight = findViewById(R.id.gold_weight_edit_text);
        goldValue = findViewById(R.id.gold_value_edit_text);
        goldWeightminUrufff = findViewById(R.id.total_value_text_view);
        zakatPayables = findViewById(R.id.zakat_payable_text_view);
        totalZkt = findViewById(R.id.total_zakat_text_view);
        goldTypeRadioGroup = findViewById(R.id.gold_type_radio_group);
        goldTypeKeep = findViewById(R.id.gold_type_keep);
        goldTypeWear = findViewById(R.id.gold_type_wear);

        Button calculateButton = findViewById(R.id.calculate_button);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateZakat();
            }
        });

        Button resetButton = findViewById(R.id.reset_button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return true;
    }

    private void calculateZakat() {
        String goldWeightStr = goldWeight.getText().toString().trim();
        String goldValueStr = goldValue.getText().toString().trim();
        String goldType;

        if (goldTypeKeep.isChecked()) {
            goldType = "keep";
        } else if (goldTypeWear.isChecked()) {
            goldType = "wear";
        } else {
            showErrorMessage("Please select gold type");
            return;
        }

        if (goldWeightStr.isEmpty() || goldValueStr.isEmpty()) {
            showErrorMessage("Please enter both gold weight and gold value");
            return;
        }

        try {
            BigDecimal goldWeight = new BigDecimal(goldWeightStr);
            BigDecimal goldValue = new BigDecimal(goldValueStr);

            BigDecimal goldWeightminUruf = goldWeight.subtract(new BigDecimal(85));
            BigDecimal zakatPayable = BigDecimal.ZERO;

            if (goldType.equals("keep")) {
                zakatPayable = goldWeightminUruf.max(BigDecimal.ZERO).multiply(goldValue);
            } else if (goldType.equals("wear")) {
                goldWeightminUruf = goldWeight.subtract(new BigDecimal(200));
                zakatPayable = goldWeightminUruf.max(BigDecimal.ZERO).multiply(goldValue);
            }

            BigDecimal totalZakat = zakatPayable.multiply(new BigDecimal(0.025)).setScale(2, BigDecimal.ROUND_HALF_UP);

            goldWeightminUrufff.setText(goldWeightminUruf.toString());
            zakatPayables.setText("RM" + zakatPayable.toString());
            totalZkt.setText("RM" + totalZakat.toString());

        } catch (NumberFormatException e) {
            showErrorMessage("Invalid input. Please enter valid numbers for gold weight and gold value");
        }
    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void resetFields() {
        goldWeight.setText("");
        goldValue.setText("");
        goldWeightminUrufff.setText("");
        zakatPayables.setText("");
        totalZkt.setText("");

        goldTypeRadioGroup.clearCheck();
    }
}