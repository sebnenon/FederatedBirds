package fr.sio.ecp.federatedbirds.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import fr.sio.ecp.federatedbirds.ApiClient;
import fr.sio.ecp.federatedbirds.R;

/**
 * Created by snenon on 23/01/16.
 * This is inspired from logintaskfragment, and permits to treat the upload in parallel
 */
// TODO comment

public class UploadAvatarTaskFragment extends DialogFragment {
    private Bitmap mBitmap;

    public void setArguments(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        AsyncTaskCompat.executeParallel(
                new UploadAvatarTaskFragment.UploadAvatarTask()
        );
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.upload_progress));
        return dialog;
    }

    private class UploadAvatarTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                return ApiClient.getInstance(getContext()).uploadAvatar(mBitmap).login;
            } catch (IOException e) {
                Log.e(LoginActivity.class.getSimpleName(), "Upload failed", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                getActivity().finish();
                startActivity(MainActivity.newIntent(getContext()));
            } else {
                Toast.makeText(getContext(), R.string.upload_failed, Toast.LENGTH_SHORT).show();
            }
            dismiss();
        }

    }

}
