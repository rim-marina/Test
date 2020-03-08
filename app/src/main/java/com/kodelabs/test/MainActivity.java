package com.kodelabs.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeAdView;
import com.appodeal.ads.NativeCallbacks;
import com.appodeal.ads.NativeMediaView;
import com.appodeal.ads.RewardedVideoCallbacks;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    /**
     * Вызывается когда activity было создано
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListeners();
        Appodeal.setBannerViewId(R.id.appodealBannerView);
        Appodeal.setTesting(true);
        Appodeal.initialize(this, TAG, Appodeal.BANNER, true);
    }

    private void initView(){
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
    }

    private void initListeners(){
        btn1.setOnClickListener(view -> {
            int count = 1;
            public void onClick(View view) {
                 if (count <= 5) {
                     Toast.makeText(getApplicationContext(), "BANNER was clicked " + count + "times", Toast.LENGTH_SHORT).show();
                     Appodeal.show(MainActivity.this, Appodeal.BANNER_VIEW);
                     count++;
                 }
                 Appodeal.hide(this, Appodeal.BANNER_VIEW);}
        });

        btn2.setOnClickListener(view -> {
                    Appodeal.initialize((Activity) this, "appKey", Appodeal.INTERSTITIAL);
                    Appodeal.isLoaded(Appodeal.INTERSTITIAL);
                    Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
                        public void onInterstitialLoaded(boolean isPrecache) {
                            Log.d("Appodeal", "onInterstitialLoaded");
                            btn2.setEnabled(true);
                        }
                        @Override
                        public void onInterstitialFailedToLoad() {
                            btn2.setEnabled(false);
                            Toast.makeText(getApplicationContext(), "On Interstitial Failed To Load", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onInterstitialShown() {
                            Appodeal.show(MainActivity.this, Appodeal.INTERSTITIAL);
                        }
                        @Override
                        public void onInterstitialShowFailed() {
                            btn2.setEnabled(false);
                        }
                        @Override
                        public void onInterstitialClicked() {
                            onInterstitialShown();
                        }
                        @Override
                        public void onInterstitialClosed() {
                            Toast.makeText(getApplicationContext(), "On Interstitial Closed", Toast.LENGTH_SHORT).show();
                            try {
                                Thread.sleep(60000);
                            } catch (InterruptedException e) {
                            }
                        }
                        @Override
                        public void onInterstitialExpired()  {
                            btn2.setEnabled(false);
                            Toast.makeText(getApplicationContext(), "On Interstitial Expired", Toast.LENGTH_SHORT).show();
                        }
                    });

        btn3.setOnClickListener(new View.OnClickListener() {
            int count = 1;
            @Override
            public void onClick(View view) {
                Appodeal.isLoaded(Appodeal.REWARDED_VIDEO);
                Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
                    @Override
                    public void onRewardedVideoLoaded(boolean isPrecache) {
                        btn3.setEnabled(true);
                        Toast.makeText(getApplicationContext(), "On Rewarded Video Loaded", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onRewardedVideoFailedToLoad() {
                        btn3.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "On Rewarded Video Failed To Load", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onRewardedVideoShown() {
                        Toast.makeText(getApplicationContext(), "On Rewarded Video Shown", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onRewardedVideoShowFailed() {
                        Toast.makeText(getApplicationContext(), "On Rewarded Video Show Failed", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onRewardedVideoClicked() {
                        if (count <= 3) {
                            btn3.setEnabled(true);
                            Appodeal.show(MainActivity.this, Appodeal.REWARDED_VIDEO);
                            count++;
                        } else {
                            btn3.setEnabled(false);
                        }
                    }
                    @Override
                    public void onRewardedVideoFinished(double amount, String name) {
                        Toast.makeText(getApplicationContext(), "On Rewarded Video Finished. Reward: " + amount + " " + name, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onRewardedVideoClosed(boolean finished) {
                        Toast.makeText(getApplicationContext(), "On Rewarded Video Closed", Toast.LENGTH_SHORT).show();
                        btn3.setEnabled(false);
                    }
                    @Override
                    public void onRewardedVideoExpired() {
                        btn3.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "On Rewarded Video Closed,  finished: %s", Toast.LENGTH_SHORT).show();
                    }
                });



                btn4.setOnClickListener(view -> {
                    Appodeal.setAutoCache(Appodeal.NATIVE, false);
                    Appodeal.setTesting(true);
                    Appodeal.initialize(MainActivity.this, TAG, Appodeal.NATIVE, true);
                    Appodeal.isLoaded(Appodeal.NATIVE);
                        Appodeal.setNativeCallbacks(new NativeCallbacks() {
                            @Override
                            public void onNativeLoaded() {
                                showAd();
                            }

                            @Override
                            public void onNativeFailedToLoad() {
                                Toast.makeText(getApplicationContext(), "On Native Failed To Load", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNativeShown(NativeAd nativeAd) {
                                // Called when native ad is shown
                            }

                            @Override
                            public void onNativeShowFailed(NativeAd nativeAd) {
                                Toast.makeText(getApplicationContext(), "On Native Failed To Load", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNativeClicked(NativeAd nativeAd) {
                                // Called when native ads is clicked
                            }

                            @Override
                            public void onNativeExpired() {
                                // Called when native ads is expired
                            }

                            private void showAd() {
                                List<NativeAd> nativeAds = Appodeal.getNativeAds(1);
                                if (nativeAds.size() >= 3) {
                                    ConstraintLayout view = findViewById(R.id.nativeview);
                                    view.removeAllViews();
                                    NativeAd nativeAd = nativeAds.get(0);
                                    NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(this).inflate(R.layout.include_native_ads, null, false);

                                    TextView tvTitle = nativeAdView.findViewById(R.id.tv_title);
                                    tvTitle.setText(nativeAd.getTitle());
                                    nativeAdView.setTitleView(tvTitle);

                                    TextView tvDescription = nativeAdView.findViewById(R.id.tv_description);
                                    tvDescription.setText(nativeAd.getDescription());
                                    nativeAdView.setDescriptionView(tvDescription);

                                    Button ctaButton = nativeAdView.findViewById(R.id.b_cta);
                                    ctaButton.setText(nativeAd.getCallToAction());
                                    nativeAdView.setCallToActionView(ctaButton);

                                    NativeMediaView nativeMediaView = nativeAdView.findViewById(R.id.appodeal_media_view_content);
                                    nativeAdView.setNativeMediaView(nativeMediaView);

                                    View providerView = nativeAd.getProviderView(this);
                                    if (providerView != null) {
                                        if (providerView.getParent() != null && providerView.getParent() instanceof ViewGroup) {
                                            ((ViewGroup) providerView.getParent()).removeView(providerView);
                                        }
                                    }
                                    nativeAdView.setProviderView(providerView);
                                }
                            }
                        }Appodeal.show(MainActivity.this, Appodeal.NATIVE);
                    }
            )};


                /**
                 * Вызывается когда activity виден пользователю
                 * но еще не в фокусе
                 * */
                @Override
                protected void onStart () {
                    Log.d(TAG, "onStart: ");
                    MainActivity.super.onStart();
                }
                /**
                 * Вызывается когда activity находится на переднем плане
                 * */
                @Override
                protected void onResume () {
                    Log.d(TAG, "onResume: ");
                    MainActivity.super.onResume();
                }
                /**
                 * Вызывается когда activity теряет фокус
                 * */
                @Override
                protected void onPause () {
                    Log.d(TAG, "onPause: ");
                    MainActivity.super.onPause();
                }
                /**
                 * Вызывается когда activity переходин на задний план(или перекрыт)
                 *  */
                @Override
                protected void onStop () {
                    Log.d(TAG, "onStop: ");
                    MainActivity.super.onStop();
                }
                /**
                 * Вызывается когда activity уничтожается
                 * */
                @Override
                protected void onDestroy () {
                    Log.d(TAG, "onDestroy: ");
                    MainActivity.super.onDestroy();
                }
            }
        }
