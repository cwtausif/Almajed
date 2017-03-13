package com.almajedgarage.almajedgarage.customer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.almajedgarage.almajedgarage.GlobCar;
import com.almajedgarage.almajedgarage.R;
import com.squareup.picasso.Picasso;

public class CustomerCarFullDetailActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageOne,imageTwo,imageThree,expandedImageView,expandedImageViewTwo,expandedImageThree;
    TextView fullShowStatus,commentTV,chargesTV,carnoTV,customerTV,cantactTV;
    int id;
    String status,description,charges,carno,customer,contact,imageone,imagetwo,imagethree;
    Button goBackBtn;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_car_full_detail);

              Bundle receivedCarId = getIntent().getExtras();

        status = receivedCarId.getString("status");
        description = receivedCarId.getString("description");
        charges = receivedCarId.getString("charges");
        carno = receivedCarId.getString("carno");
        customer = receivedCarId.getString("customer");
        contact = receivedCarId.getString("contact");

        imageone = receivedCarId.getString("imageone");
        imagetwo = receivedCarId.getString("imagetwo");
        imagethree = receivedCarId.getString("imagethree");

        Log.d("response CFDA: ",receivedCarId+"");

        //region getviews
        fullShowStatus = (TextView) findViewById(R.id.fullShowStatus);
        commentTV = (TextView) findViewById(R.id.commentTV);
        chargesTV = (TextView) findViewById(R.id.chargesTV);
        carnoTV = (TextView) findViewById(R.id.carnoTV);
        customerTV = (TextView) findViewById(R.id.customerTV);
        cantactTV = (TextView) findViewById(R.id.cantactTV);
        goBackBtn = (Button) findViewById(R.id.goBackBtn);
        expandedImageView = (ImageView) findViewById(R.id.expanded_image);
        expandedImageViewTwo = (ImageView) findViewById(R.id.expanded_imagetwo);
        expandedImageThree = (ImageView) findViewById(R.id.expanded_imagethree);
        goBackBtn.setOnClickListener(this);

        //imageviews
        imageOne = (ImageView) findViewById(R.id.imageOne);
        imageTwo = (ImageView) findViewById(R.id.imageTwo);
        imageThree = (ImageView) findViewById(R.id.imageThree);
        //endregion

        //Set On Click Listener
        imageOne.setOnClickListener(this);
        imageTwo.setOnClickListener(this);
        imageThree.setOnClickListener(this);


        //setviews
        fullShowStatus.setText(status);
        commentTV.setText(description);
        commentTV.setKeyListener(null);
        chargesTV.setText(charges);
        chargesTV.setKeyListener(null);
        carnoTV.setText(carno);
        carnoTV.setKeyListener(null);
        customerTV.setText(customer);
        customerTV.setKeyListener(null);
        cantactTV.setText(contact);
        cantactTV.setKeyListener(null);

        //Set Images
        if (imageone.length()>0){
            Picasso.with(getApplicationContext()).load(GlobCar.base_url+imageone).into(imageOne);
            Picasso.with(getApplicationContext()).load(GlobCar.base_url+imageone).into(expandedImageView);
        }
        if (imagetwo.length()>0){
            Picasso.with(getApplicationContext()).load(GlobCar.base_url+imagetwo).into(imageTwo);
            Picasso.with(getApplicationContext()).load(GlobCar.base_url+imagetwo).into(expandedImageViewTwo);
        }
        if (imagethree.length()>0){
            Picasso.with(getApplicationContext()).load(GlobCar.base_url+imagethree).into(imageThree);
            Picasso.with(getApplicationContext()).load(GlobCar.base_url+imagethree).into(expandedImageThree);
        }

        //hide keyboard
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }


    @Override
    public void onClick(View view) {
        if (view == goBackBtn){
            finish();
        } else if (view == imageOne){
           // Toast.makeText(getApplicationContext(),"Imag One",Toast.LENGTH_SHORT).show();
            try{
                zoomImageFromThumb(view);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else if (view == imageTwo){
          //  Toast.makeText(getApplicationContext(),"Image Two",Toast.LENGTH_SHORT).show();
            try{
                zoomImageFromThumbTwo(view);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else if (view == imageThree){
            //Toast.makeText(getApplicationContext(),"Image Three",Toast.LENGTH_SHORT).show();
            try{
                zoomImageFromThumbThree(view);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void zoomImageFromThumb(final View thumbView) {

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }


        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();


        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);


        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {

            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {

            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }


        //thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);


        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);


        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
    private void zoomImageFromThumbTwo(final View thumbView) {

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }



        //expandedImageView.setImageResource(imageResId);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();


        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);


        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {

            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {

            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }


        //thumbView.setAlpha(0f);
        expandedImageViewTwo.setVisibility(View.VISIBLE);


        expandedImageViewTwo.setPivotX(0f);
        expandedImageViewTwo.setPivotY(0f);


        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageViewTwo, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageViewTwo, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageViewTwo, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageViewTwo,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        final float startScaleFinal = startScale;
        expandedImageViewTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageViewTwo, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageViewTwo,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageViewTwo,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageViewTwo,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageViewTwo.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageViewTwo.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
    private void zoomImageFromThumbThree(final View thumbView) {

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }


        //expandedImageView.setImageResource(imageResId);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();


        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);


        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {

            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {

            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }


        // thumbView.setAlpha(0f);
        expandedImageThree.setVisibility(View.VISIBLE);


        expandedImageThree.setPivotX(0f);
        expandedImageThree.setPivotY(0f);


        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageThree, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageThree, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageThree, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageThree,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        final float startScaleFinal = startScale;
        expandedImageThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageThree, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageThree,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageThree,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageThree,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageThree.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageThree.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
