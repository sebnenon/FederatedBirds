package fr.sio.ecp.federatedbirds.app;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import fr.sio.ecp.federatedbirds.R;

/**
 * Created by SNenon
 * new class
 */
public class UploadAvatarActivity extends AppCompatActivity {

    // number of images to select
    private static final int PICK_IMAGE = 1;
    private ImageView image;
    private Button uploadButton;
    private Button cancelButton;
    private Bitmap bitmap;
    private Button selectImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadavatar);


        image = (ImageView) findViewById(R.id.uploadImage);
        uploadButton = (Button) findViewById(R.id.uploadButton);
        cancelButton = (Button) findViewById(R.id.cancelUpload);

        // Picture selection
        selectImageButton = (Button) findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImageFromGallery();

            }
        });

        // start upload
        uploadButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadAvatar(bitmap);
            }
        });

        // abort
        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Opens dialog picker, so the user can select image from the gallery. The
     * result is returned in onActivityResult
     */
    public void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE);
    }

    /**
     * Retrieves the result returned from selecting image, by invoking the method
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            decodeFile(picturePath);

        }
    }

    /**
     * The method decodes the image file to avoid out of memory issues. Sets the
     * selected image in to the ImageView.
     *
     * @param filePath
     */
    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        image.setImageBitmap(bitmap);
    }

    private void uploadAvatar(Bitmap bitmap) {
        // here is the call to the async task that will upload the picture
        UploadAvatarTaskFragment taskFragment = new UploadAvatarTaskFragment();
        taskFragment.setArguments(bitmap);
        taskFragment.show(getSupportFragmentManager(), "upload_avatar");
    }


}
