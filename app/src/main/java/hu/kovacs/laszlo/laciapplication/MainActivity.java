package hu.kovacs.laszlo.laciapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    private TextView mMoneyCounter;
    private TextView mPassiveCounter;
    private TextView mActiveCounter;

    private Button mPassiveUpgradeButton;
    private Button mActiveUpgradeButton;

    private Long mMoney = new Long(0);

    private long mMoneyIncrementPassive = 5;
    private long mMoneyIncrementPassiveCost = 50;

    private long mMoneyIncrementActive = 2;
    private long mMoneyIncrementActiveCost = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mMoneyCounter = findViewById(R.id.money_counter);
        mPassiveCounter = findViewById(R.id.passive_counter);
        mActiveCounter = findViewById(R.id.active_counter);

        mActiveUpgradeButton = findViewById(R.id.active_upgrade_button);
        mPassiveUpgradeButton = findViewById(R.id.passive_upgrade_button);

        mActiveUpgradeButton.setText(
                getString(R.string.active_upgrade_button_text) + "(" + String.valueOf(
                        mMoneyIncrementActiveCost) + ")");
        mPassiveUpgradeButton.setText(
                getString(R.string.passive_upgrade_button_text) + "(" + String.valueOf(
                        mMoneyIncrementPassiveCost) + ")");


        mMoneyCounter.setText(String.valueOf(mMoney) + " $");
        mActiveCounter.setText(String.valueOf(mMoneyIncrementActive) + " $/click");
        mPassiveCounter.setText(String.valueOf(mMoneyIncrementPassive) + " $/s");


        Thread thread = new Thread() {

            @Override
            public void run() {

                while (!isInterrupted()) {

                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                synchronized (mMoney) {
                                    mMoney += mMoneyIncrementPassive;
                                }

                                mMoneyCounter.setText(String.valueOf(mMoney) + " $");
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        thread.start();

    }

    public void increaseMoney(View view) {
        synchronized (mMoney) {
            mMoney += mMoneyIncrementActive;
            mMoneyCounter.setText(String.valueOf(mMoney) + " $");
        }
    }

    public void increasePassiveIncome(View view) {
        if (mMoney >= mMoneyIncrementPassiveCost) {
            mMoney -= mMoneyIncrementPassiveCost;
            mMoneyIncrementPassive = calculatePassiveIncrement(mMoneyIncrementPassive);
            mMoneyIncrementPassiveCost = calculatePassiveIncrementCost(mMoneyIncrementPassiveCost);
            mPassiveCounter.setText(String.valueOf(mMoneyIncrementPassive) + " $/s");
            mPassiveUpgradeButton.setText(
                    getString(R.string.passive_upgrade_button_text) + "(" + String.valueOf(
                            mMoneyIncrementPassiveCost) + ")");

        }
        else {
            Toast.makeText(this, "You need to gain more money!", Toast.LENGTH_SHORT).show();
        }
    }

    public void increaseActiveIncome(View view) {
        if (mMoney >= mMoneyIncrementActiveCost) {
            mMoney -= mMoneyIncrementActiveCost;
            mMoneyIncrementActive = calculateActiveIncrement(mMoneyIncrementActive);
            mMoneyIncrementActiveCost = calculateActiveIncrementCost(mMoneyIncrementActiveCost);
            mActiveCounter.setText(String.valueOf(mMoneyIncrementActive) + " $/click");
            mActiveUpgradeButton.setText(
                    getString(R.string.active_upgrade_button_text) + "(" + String.valueOf(
                            mMoneyIncrementActiveCost) + ")");
        } else {
            Toast.makeText(this, "You need to gain more money!", Toast.LENGTH_SHORT).show();
        }
    }

    private long calculateActiveIncrement(long value){
        return value * 2;
    }

    private long calculateActiveIncrementCost(long value){
        return value * 3;
    }

    private long calculatePassiveIncrement(long value) {
        return value * 2;
    }

    private long calculatePassiveIncrementCost(long value) {
        return value * 3;
    }

}
