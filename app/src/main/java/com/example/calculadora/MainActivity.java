package com.example.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private TextView logTextView;
    private double memoryValue = 0.0;
    private String currentInput = "";
    private String operator = "";
    private double firstOperand = 0.0;

    private final LinkedList<String> operationLog = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);
        logTextView = findViewById(R.id.logTextView);

        setupNumericButtons();
        setupOperatorButtons();

        findViewById(R.id.buttonClear).setOnClickListener(v -> clear());
        findViewById(R.id.buttonEquals).setOnClickListener(v -> calculateResult());
        findViewById(R.id.buttonMemoryAdd).setOnClickListener(v -> addToMemory());
        findViewById(R.id.buttonMemorySubtract).setOnClickListener(v -> subtractFromMemory());
        findViewById(R.id.buttonMemoryClear).setOnClickListener(v -> clearMemory());
        findViewById(R.id.buttonMemoryRecall).setOnClickListener(v -> recallMemory());
        findViewById(R.id.buttonMemoryStore).setOnClickListener(v -> storeMemory());
    }

    private void setupNumericButtons() {
        int[] numericButtonIds = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9, R.id.button00
        };

        for (int id : numericButtonIds) {
            findViewById(id).setOnClickListener(this::onNumericButtonClick);
        }
    }

    private void setupOperatorButtons() {
        int[] operatorButtonIds = {
                R.id.buttonAdd, R.id.buttonSubtract,
                R.id.buttonMultiply, R.id.buttonDivide
        };

        for (int id : operatorButtonIds) {
            findViewById(id).setOnClickListener(this::onOperatorButtonClick);
        }
    }

    private void onNumericButtonClick(View view) {
        Button button = (Button) view;
        currentInput += button.getText().toString();
        resultTextView.setText(currentInput);
    }

    private void onOperatorButtonClick(View view) {
        if (!currentInput.isEmpty()) {
            try {
                firstOperand = Double.parseDouble(currentInput);
                Button button = (Button) view;
                operator = button.getText().toString();
                currentInput = "";
            } catch (NumberFormatException e) {
                resultTextView.setText("Error");
            }
        }
    }

    private void calculateResult() {
        if (!currentInput.isEmpty() && !operator.isEmpty()) {
            try {
                double secondOperand = Double.parseDouble(currentInput);
                double result = 0.0;
                String operation;

                switch (operator) {
                    case "+":
                        result = firstOperand + secondOperand;
                        break;
                    case "-":
                        result = firstOperand - secondOperand;
                        break;
                    case "*":
                        result = firstOperand * secondOperand;
                        break;
                    case "/":
                        if (secondOperand != 0) {
                            result = firstOperand / secondOperand;
                        } else {
                            resultTextView.setText("Error: Div/0");
                            currentInput = "";
                            return;
                        }
                        break;
                }

                operation = firstOperand + " " + operator + " " + secondOperand + " = " + result;
                resultTextView.setText(String.valueOf(result));
                currentInput = String.valueOf(result);
                operator = "";

                updateLog(operation);

            } catch (NumberFormatException e) {
                resultTextView.setText("Error");
                currentInput = "";
            }
        } else {
            resultTextView.setText("0");
        }
    }

    private void updateLog(String operation) {
        if (operationLog.size() >= 3) {
            operationLog.removeFirst();
        }
        operationLog.add(operation);

        StringBuilder logBuilder = new StringBuilder();
        for (String op : operationLog) {
            logBuilder.append(op).append("\n");
        }
        logTextView.setText(logBuilder.toString());
    }

    private void clear() {
        currentInput = "";
        operator = "";
        firstOperand = 0.0;
        resultTextView.setText("0");
    }

    private void addToMemory() {
        if (!currentInput.isEmpty()) {
            memoryValue += Double.parseDouble(currentInput);
            updateLog("M+ " + currentInput + " (Memoria: " + memoryValue + ")");
        }
    }

    private void subtractFromMemory() {
        if (!currentInput.isEmpty()) {
            memoryValue -= Double.parseDouble(currentInput);
            updateLog("M- " + currentInput + " (Memoria: " + memoryValue + ")");
        }
    }

    private void clearMemory() {
        memoryValue = 0.0;
        updateLog("MC (Memoria borrada)");
    }

    private void recallMemory() {
        resultTextView.setText(String.valueOf(memoryValue));
        updateLog("MR = " + memoryValue);
    }

    private void storeMemory() {
        if (!currentInput.isEmpty()) {
            memoryValue = Double.parseDouble(currentInput);
            updateLog("MS " + memoryValue);
        }
    }
}