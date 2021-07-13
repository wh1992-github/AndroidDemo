package com.example.custom;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.custom.util.BitmapUtils;
import com.example.custom.widget.RoundImageView1;
import com.example.custom.widget.RoundImageView2;
import com.example.custom.widget.RoundImageView3;

public class CustomImageViewActivity extends AppCompatActivity {

    private ImageView mCircleView;
    private ImageView mRoundRectView;
    private RoundImageView1 mRoundIv1;
    private RoundImageView2 mRoundIv2;
    private RoundImageView3 mRoundIv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_imageview);
        mRoundIv1 = findViewById(R.id.round_iv1);
        mRoundIv2 = findViewById(R.id.round_iv2);
        mRoundIv3 = findViewById(R.id.round_iv3);
        mCircleView = findViewById(R.id.circle_iv);
        mRoundRectView = findViewById(R.id.round_rect_iv);

        loadImage(mRoundIv1);
        loadImage(mRoundIv2);
        loadImage(mRoundIv3);
        mCircleView.setImageBitmap(BitmapUtils.createCircleView(BitmapFactory.decodeResource(getResources(), R.drawable.ic_default)));
        mRoundRectView.setImageBitmap(BitmapUtils.createRoundRectView(BitmapFactory.decodeResource(getResources(), R.drawable.ic_default)));
    }

    private void loadImage(ImageView imageView) {
        Glide.with(this)
                .asBitmap()
                .load(R.drawable.ic_default)
                .apply(RequestOptions.noAnimation().transforms(new CenterCrop())
                        .override(300, 300)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .skipMemoryCache(false))
                .into(imageView);
    }
}