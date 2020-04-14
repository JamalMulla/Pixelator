package com.jmulla.scannerx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.cameraview.controls.Control;
import com.otaliastudios.cameraview.controls.Engine;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.filter.BaseFilter;
import com.otaliastudios.cameraview.filter.Filter;
import com.otaliastudios.cameraview.filter.Filters;
import com.otaliastudios.cameraview.filters.BlackAndWhiteFilter;
import com.otaliastudios.cameraview.filters.DuotoneFilter;
import com.otaliastudios.cameraview.filters.TemperatureFilter;
import com.otaliastudios.cameraview.filters.TintFilter;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;

import java.io.File;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final CameraView camera = findViewById(R.id.cameraView);
    camera.setLifecycleOwner(this);
    camera.setEngine(Engine.CAMERA1);
    Engine engine = camera.getEngine();

    final ImageView imageView = findViewById(R.id.takenView);
    final Button btnTakePic = findViewById(R.id.btn_take_pic);
    final Button btnReturn = findViewById(R.id.btn_return);
    final Button btnChange = findViewById(R.id.btn_change);
    final Button btnFlip = findViewById(R.id.btn_flip);

    final boolean[] facingBack = {true};
    
    camera.setFacing(Facing.BACK);
    camera.addCameraListener(new CameraListener() {
      @Override
      public void onPictureTaken(PictureResult result) {
        // Picture was taken!
        // If planning to show a Bitmap, we will take care of
        // EXIF rotation and background threading for you...

        final int maxWidth = result.getSize().getWidth();
        final int maxHeight = result.getSize().getHeight();


        System.out.println("Max Width: " + maxWidth + " Max Height: " + maxHeight);

        result.toBitmap(maxWidth, maxHeight, new BitmapCallback() {
          @Override
          public void onBitmapReady(@Nullable Bitmap bitmap) {
            //show taken image?
            if (bitmap != null) {
              int width = bitmap.getWidth();
              int height = bitmap.getHeight();
              System.out.println("Width: " + width + " Height: " + height);
            }
            imageView.setMaxHeight(maxHeight);
            imageView.setMaxWidth(maxWidth);
            imageView.setImageBitmap(bitmap);



            imageView.setVisibility(View.VISIBLE);
            camera.setVisibility(View.GONE);
            btnTakePic.setVisibility(View.GONE);
            btnReturn.setVisibility(View.VISIBLE);
          }
        });

        // If planning to save a file on a background thread,
        // just use toFile. Ensure you have permissions.

//        result.toFile(file, new FileCallback() {
//          @Override
//          public void onFileReady(@Nullable File file) {
//
//          }
//        });

        // Access the raw data if needed.
        byte[] data = result.getData();
      }


      @Override
      public void onVideoTaken(VideoResult result) {
        // A Video was taken!
      }

      // And much more
    });

    final PixelFilter pixelFilter = new PixelFilter();

    camera.setFilter(pixelFilter);


    btnChange.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        pixelFilter.selectColorPalette(0);
      }
    });


    btnTakePic.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        camera.takePictureSnapshot();
      }
    });

    btnFlip.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (facingBack[0]){
          camera.setFacing(Facing.FRONT);
          facingBack[0] = false;
        } else {
          camera.setFacing(Facing.BACK);
          facingBack[0] = true;
        }
      }
    });


    btnReturn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        imageView.setImageBitmap(null);
        imageView.setVisibility(View.GONE);
        camera.setVisibility(View.VISIBLE);
        btnTakePic.setVisibility(View.VISIBLE);
        btnReturn.setVisibility(View.GONE);
      }
    });


  }
}
