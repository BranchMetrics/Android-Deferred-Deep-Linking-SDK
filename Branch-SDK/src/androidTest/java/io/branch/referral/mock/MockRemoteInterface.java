package io.branch.referral.mock;

import android.util.Log;

import org.json.JSONObject;

import io.branch.referral.BranchTest;
import io.branch.referral.network.BranchRemoteInterface;

import static io.branch.referral.Defines.RequestPath.GetCPID;
import static io.branch.referral.Defines.RequestPath.GetCreditHistory;
import static io.branch.referral.Defines.RequestPath.GetURL;
import static io.branch.referral.Defines.RequestPath.RegisterInstall;
import static io.branch.referral.Defines.RequestPath.RegisterOpen;

public class MockRemoteInterface extends BranchRemoteInterface {
    @Override
    public BranchResponse doRestfulGet(String url) throws BranchRemoteException {
        try {
            Thread.sleep(BranchTest.TEST_TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("benas", "doRestfulGet, url: " + url);
        return new BranchResponse(pathForSuccessResponse(url), 200);
    }

    @Override
    public BranchResponse doRestfulPost(String url, JSONObject payload) throws BranchRemoteException {
        try {
            Thread.sleep(BranchTest.TEST_TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("benas", "doRestfulPost, url: " + url + ", payload: " + payload);
        return new BranchResponse(pathForSuccessResponse(url), 200);
    }

    public static String pathForSuccessResponse(String url) {
        if (url.contains(GetURL.getPath())) {
            return "{\"url\":\"https://bnc.lt/a/key_live_testing_only?channel=facebook&type=0&duration=0&source=android&data=eyJzb3VyY2UiOiJhbmRyb2lkIn0%3D\"}";
        } else if (url.contains(GetCreditHistory.getPath())) {
            return "[]";
        } else if (url.contains(RegisterInstall.getPath()) || url.contains(RegisterOpen.getPath())) {
            return "{\"session_id\":\"880938553235373649\",\"identity_id\":\"880938553226608667\",\"link\":\"https://branchster.test-app.link?%24identity_id=880938553226608667\",\"data\":\"{\\\"+clicked_branch_link\\\":false,\\\"+is_first_session\\\":false}\",\"device_fingerprint_id\":\"867130134518497054\"}";
        } else if (url.contains(GetCPID.getPath())) {
            return "{\"user_data\":{\"cross_platform_id\":\"afb3e7f98b18dc6c90ebaeade4dbc6cac67fbb8e3f34e9cd8217490bf8f24b1f\",\"past_cross_platform_ids\":[],\"prob_cross_platform_ids\":[],\"developer_identity\":\"880938553226608667\"}}";
        } else {
            return "{}";
        }
    }
}