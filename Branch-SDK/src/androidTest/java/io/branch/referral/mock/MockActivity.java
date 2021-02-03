package io.branch.referral.mock;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONObject;
import org.junit.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class MockActivity extends Activity {
    private static final String TAG = "MockActivity";
    public String state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = "created";
    }

    @Override
    public void onStart() {
        super.onStart();
        state = "started";
        if (Branch.getInstance() != null) {
            final CountDownLatch testCallbackInvoked = new CountDownLatch(1);
            Branch.sessionBuilder(this).withCallback(new Branch.BranchReferralInitListener() {
                @Override
                public void onInitFinished(@Nullable JSONObject referringParams, @Nullable BranchError error) {
                    // this isn't really a test, just makes sure that we are indeed using `MockRemoteInterface` and getting success responses
                    Log.d(TAG, "onInitFinished, referringParams: " + referringParams + ", error: " + error);
                    Assert.assertNotNull(referringParams);
                    Assert.assertNull(error);
                    testCallbackInvoked.countDown();
                }
            }).withData(getIntent() == null ? null : getIntent().getData()).init();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        testCallbackInvoked.await(5000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        Assert.fail("testCallbackInvoked failed");
                    }
                }
            }).start();
        } else {
            Log.d(TAG, "MockActivity started but sdk not initialized.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        state = "resumed";
    }

    @Override
    public void onPause() {
        super.onPause();
        state = "paused";
    }

    @Override
    public void onStop() {
        super.onStop();
        state = "stopped";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        state = "destroyed";
    }
}
