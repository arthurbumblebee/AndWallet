package hu.ait.android.andwallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView totalIncome = findViewById(R.id.tvIncome);
        TextView totalExpenses = findViewById(R.id.tvExpenses);
        TextView currentBalance = findViewById(R.id.tvCurrentBalance);

        if (getIntent().hasExtra(MainActivity.TOTAL_EXPENSES) ||
                getIntent().hasExtra(MainActivity.TOTAL_INCOME) ||
                getIntent().hasExtra(MainActivity.CURRENT_BALANCE)
                ) {

            int expenses = getIntent().getIntExtra(MainActivity.TOTAL_EXPENSES, 0);
            int income = getIntent().getIntExtra(MainActivity.TOTAL_INCOME, 0);
            int balance = getIntent().getIntExtra(MainActivity.CURRENT_BALANCE, 0);

            totalExpenses.setText(getString(R.string.total_expenses, Integer.toString(expenses)));
            totalIncome.setText(getString(R.string.total_income, Integer.toString(income)));
            currentBalance.setText(getString(R.string.current_balance, Integer.toString(balance)));

        } else {
            totalExpenses.setText(getString(R.string.total_expenses, "0"));
            totalIncome.setText(getString(R.string.total_income, "0"));
            currentBalance.setText(getString(R.string.current_balance, "0"));
        }
    }
}
