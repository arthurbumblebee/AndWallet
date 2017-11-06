package hu.ait.android.andwallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String CURRENT_BALANCE = "CURRENT_BALANCE";
    public static final String TOTAL_EXPENSES = "TOTAL_EXPENSES";
    public static final String TOTAL_INCOME = "TOTAL_INCOME";


    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.etRecordName)
    EditText etRecordName;
    @BindView(R.id.incomeExpenseToggleButton)
    ToggleButton incomeExpenseToggleButton;
    @BindView(R.id.layoutContent)
    LinearLayout layoutContent;
    @BindView(R.id.balance)
    TextView balanceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView balanceText = findViewById(R.id.balance);
        balanceText.setText(getString(R.string.balance, "0"));

        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public String computeBalance() {
        int balance = 0;
        for (int i = 0; i < layoutContent.getChildCount(); i++) {
            View record = layoutContent.getChildAt(i);
            TextView tvAmount = record.findViewById(R.id.tvAmount);

            if (record.findViewById(R.id.ivRecord).getTag().toString().equals("INCOME")) {
                balance += Integer.parseInt((tvAmount.getText()).toString());

            } else
                balance -= Integer.parseInt((tvAmount.getText()).toString());

        }

        return Integer.toString(balance);
    }

    public String computeTotalIncome() {
        int totalIncome = 0;
        for (int i = 0; i < layoutContent.getChildCount(); i++) {
            View record = layoutContent.getChildAt(i);
            TextView tvAmount = record.findViewById(R.id.tvAmount);

            if (record.findViewById(R.id.ivRecord).getTag().toString().equals("INCOME")) {
                totalIncome += Integer.parseInt((tvAmount.getText()).toString());
            }
        }
        return Integer.toString(totalIncome);

    }

    public String computeTotalExpenses() {
        int totalExpenses = 0;
        for (int i = 0; i < layoutContent.getChildCount(); i++) {
            View record = layoutContent.getChildAt(i);
            TextView tvAmount = record.findViewById(R.id.tvAmount);

            if (!record.findViewById(R.id.ivRecord).getTag().toString().equals("INCOME")) {
                totalExpenses += Integer.parseInt((tvAmount.getText()).toString());
            }
        }

        return Integer.toString(totalExpenses);

    }

    public void deleteAll() {
        if (layoutContent.getChildCount() > 0) {
            layoutContent.removeAllViews();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_summary:

                Intent intentResult = new Intent();
                intentResult.setClass(MainActivity.this, SummaryActivity.class);

                int currentBalance = Integer.valueOf(computeBalance());
                int totalExpenses = Integer.valueOf(computeTotalExpenses());
                int totalIncome = Integer.valueOf(computeTotalIncome());

                intentResult.putExtra(CURRENT_BALANCE, currentBalance);
                intentResult.putExtra(TOTAL_EXPENSES, totalExpenses);
                intentResult.putExtra(TOTAL_INCOME, totalIncome);

                startActivity(intentResult);

                break;

            case R.id.action_delete_all:
                deleteAll();
                balanceText.setText(getString(R.string.balance, "0"));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btnSave)
    public void savePressed() {

        try {
            if (!TextUtils.isEmpty(etRecordName.getText()) && !TextUtils.isEmpty(etAmount.getText())) {
                final View recordRow = getLayoutInflater().inflate(
                        R.layout.layout_money_row,
                        null, false);

                TextView tvName = recordRow.findViewById(R.id.tvName);
                TextView tvAmount = recordRow.findViewById(R.id.tvAmount);

                tvName.setText(etRecordName.getText().toString());
                tvAmount.setText(etAmount.getText().toString());

                ImageView ivRecord = recordRow.findViewById(R.id.ivRecord);
                if (incomeExpenseToggleButton.isChecked()) {
                    ivRecord.setImageResource(R.drawable.expense);
                    ivRecord.setTag(R.string.expense);
                }

                layoutContent.addView(recordRow, 0);

                String currentBalance = computeBalance();

                balanceText.setText(getString(R.string.balance, currentBalance));
                etAmount.setText("");
                etRecordName.setText("");
                incomeExpenseToggleButton.setChecked(false);
                etRecordName.requestFocus();

            } else {
                if (TextUtils.isEmpty(etRecordName.getText())) {
                    etRecordName.setError("The record name field cannot be empty");

                } else
                    etAmount.setError("The amount field cannot be empty");
            }

        } catch (NumberFormatException nf) {
            etRecordName.setError("WrongContent");
            nf.printStackTrace();
        }
    }
}
