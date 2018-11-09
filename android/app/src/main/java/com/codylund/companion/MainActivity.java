package com.codylund.companion;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.GregorianCalendar;


public class MainActivity extends AppCompatActivity implements IToolbarController, CheckpointsFragment.CheckpointsFragmentListener {

    private static final String TAG = MainActivity.class.getName();

    // Pinging service stuff
    private Intent mAlarmIntent = null;
    private PendingIntent mPendingIntent = null;
    private AlarmManager mAlarmManager = null;

    // Toolbar stuff
    private Toolbar mToolbar;

    // Fragment stuff
    private FragmentManager mFragmentManager;
    private CheckpointsFragment mCheckpointsFragment;
    private NewCheckpointFragment mNewCheckpointFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAlarmIntent = new Intent(this, AlarmService.class);
        mPendingIntent = PendingIntent.getService(this, 0, mAlarmIntent, 0);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, new GregorianCalendar().getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES/3, mPendingIntent);
        mFragmentManager = getSupportFragmentManager();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    public void showNewCheckpointWizard() {
        mNewCheckpointFragment = NewCheckpointFragment.newInstance();

        // Add the checkpoints checkpoint maker fragment
        final FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.fragment, mNewCheckpointFragment);
        transaction.addToBackStack(NewCheckpointFragment.class.getName());
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        if (mFragmentManager.getBackStackEntryCount() <= 0)
            this.setDisplayHome(false);
        return true;
    }

    @Override
    public void setDisplayHome(boolean enabled) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
        getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
    }

    @Override
    public void setActionIcon(int id) {

    }
}
