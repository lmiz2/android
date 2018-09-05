package com.example.songhyeonseok.drawing;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lmiz2 on 2017-08-29.
 */

public class DownFromDrive extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {

/*
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.driveframe);
        Button ok = (Button)findViewById(R.id.drive_ok);

        DriveContents driveContents;


        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this).addApi(Drive.API).addScope(Drive.SCOPE_FILE)
                .build();


        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, "tests.jpg"))
                .build();
        Drive.DriveApi.query(googleApiClient, query);


    }

    ResultCallback<DriveApi.DriveContentsResult> contentsOpenedCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        // display an error saying file can't be opened
                        return;
                    }
                    // DriveContents object contains pointers
                    // to the actual byte stream
                }
            };
    */

    private static final String TAG = "drive-quickstart";
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    private static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_RESOLUTION = 3;

    private GoogleApiClient mGoogleApiClient;
    private Bitmap mBitmapToSave;
    private InputStream inputStream;
    private String fileid;

    /*
     * Create a new file and save it to Drive.
*/
    private void saveFileToDrive() {
        // Start by creating a new contents, and setting a callback.
        Log.i(TAG, "Creating new contents.");
        final Bitmap image = mBitmapToSave;
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {

                    @Override
                    public void onResult(DriveApi.DriveContentsResult result) {
                        if (!result.getStatus().isSuccess()) {
                            return;
                        }


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Query query = new Query.Builder()
                                        .addFilter(Filters.eq(SearchableField.TITLE, "WCM0001.jpeg"))
                                        .build();
                                Drive.DriveApi.query(mGoogleApiClient, query)
                                        .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {

                                            @Override
                                            public void onResult(DriveApi.MetadataBufferResult result) {
                                                // Iterate over the matching Metadata instances in mdResultSet
                                                Metadata metadata = result.getMetadataBuffer().get(0);
                                                fileid = metadata.getDriveId().getResourceId();
                                                mBitmapToSave = BitmapFactory.decodeStream(read(fileid));
                                                Log.d("@@@@@@@",mBitmapToSave.toString());
                                            }
                                        });

                            }
                        }).start();


                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            // Create the API client and bind it to an instance variable.
            // We use this instance as the callback for connection and connection
            // failures.
            // Since no account name is passed, the user is prompted to choose.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        // Connect the client. Once connected, the camera is launched.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CAPTURE_IMAGE:
                // Called after a photo has been taken.
                if (resultCode == Activity.RESULT_OK) {
                    // Store the image data as a bitmap for writing later.
                    mBitmapToSave = (Bitmap) data.getExtras().get("data");
                }
                break;
            case REQUEST_CODE_CREATOR:
                // Called after a file is saved to Drive.
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "Image successfully saved.");
                    mBitmapToSave = null;
                    // Just start the camera again for another photo.
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                            REQUEST_CODE_CAPTURE_IMAGE);
                }
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "API client connected.");
        if (mBitmapToSave == null) {
            // This activity has no UI of its own. Just start the camera.
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                    REQUEST_CODE_CAPTURE_IMAGE);
            return;
        }
        saveFileToDrive();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    InputStream read(String id) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && id != null) try {
            DriveFile df = Drive.DriveApi.getFile(mGoogleApiClient, DriveId.decodeFromString(id));
            DriveApi.DriveContentsResult rslt = df.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null).await();
            InputStream inputStream;
            if ((rslt != null) && rslt.getStatus().isSuccess()) {
                DriveContents cont = rslt.getDriveContents();
                inputStream  = cont.getInputStream();
                cont.discard(mGoogleApiClient);    // or cont.commit();  they are equiv if READONLY
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

}
