package com.example.songhyeonseok.drawing;

/**
 * Created by SongHyeonSeok on 2017-10-10.
 */
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.DriveContentsResult;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFile.DownloadProgressListener;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * An activity to illustrate how to open contents and listen
 * the download progress if the file is not already sync'ed.
 */
public class RetreiveContent extends BaseDemoActivity {

    final protected int GET_PICTUER_AT_G_DRIVE = 32322;
    private static final String TAG = "RetrieveFileWithProgressDialogActivity";

    /**
     * Request code to handle the result from file opening activity.
     */
    private static final int REQUEST_CODE_OPENER = 108;

    /**
     * Progress bar to show the current download progress of the file.
     */
    private ProgressBar mProgressBar;

    /**
     * File that is selected with the open file activity.
     */
    private DriveId mSelectedFileDriveId;

    private Bitmap result_bitmap;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_progress);
        //img = (ImageView)findViewById(R.id.img);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);
        mProgressBar.setMax(100);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);

        // If there is a selected file, open its contents.
        if (mSelectedFileDriveId != null) {
            open();
            return;
        }

        // Let the user pick an mp4 or a jpeg file if there are
        // no files selected by the user.
        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[]{"video/mp4", "image/png", "image/jpeg"})
                .build(getGoogleApiClient());
        try {
            startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);
        } catch (SendIntentException e) {
//            Log.w(TAG, "Unable to send intent", e);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_OPENER:
                if (resultCode == RESULT_OK) {
                    Log.d("@@@@@@@@@Data",data+"@@@@@@@@@@@@@@@@@");
                    mSelectedFileDriveId = (DriveId) data
                            .getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                } else {
                    Log.d("@@@@@@@@@2","1@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                    finish();
                }
                break;
            default:
                Log.d("@@@@@@@@@2","2@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void open() {
        mProgressBar.setProgress(0);
        DownloadProgressListener listener = new DownloadProgressListener() {
            @Override
            public void onProgress(long bytesDownloaded, long bytesExpected) {
                // Update progress dialog with the latest progress.
                int progress = (int) (bytesDownloaded * 100 / bytesExpected);
                mProgressBar.setProgress(progress);
            }
        };
        Drive.DriveApi.getFile(getGoogleApiClient(), mSelectedFileDriveId)
                .open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, listener)
                .setResultCallback(driveContentsCallback);
        mSelectedFileDriveId = null;
    }

    private ResultCallback<DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveContentsResult>() {
                @Override
                public void onResult(DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        showMessage("Error while opening the file contents");
                        return;
                    }
                    DriveContents driveContents = result.getDriveContents();
                    InputStream is = driveContents.getInputStream();
                    result_bitmap = BitmapFactory.decodeStream(is);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    result_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    Intent intent = new Intent();
                    intent.putExtra("picture",bytes);
                    setResult(Activity.RESULT_OK,intent);
                    RetreiveContent.this.finish();

                }
    };


}