package com.dsrnative.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;

/**
 * Runs in the ":restart" process (see the manifest) so it survives the game process being killed. It starts
 * GameActivity in a fresh task, then kills the game process and itself.
 */
public class RestartActivity extends Activity {
    static final String EXTRA_PID = "pid";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        final int oldPid = getIntent().getIntExtra(EXTRA_PID, -1);
        if (oldPid > 0 && oldPid != Process.myPid()) Process.killProcess(oldPid);
        // Give the system a moment to tear the old process down before the new one starts.
        getWindow().getDecorView().postDelayed(() -> {
            Intent i = new Intent(this, GameActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            Process.killProcess(Process.myPid());
        }, 350);
    }
}
