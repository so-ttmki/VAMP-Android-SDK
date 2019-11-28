package jp.supership.vamp.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import jp.supership.vamp.VAMP;
import jp.supership.vamp.VAMPConfiguration;
import jp.supership.vamp.VAMPGetCountryCodeListener;

import jp.supership.vamp.VAMPGetLocationListener;
import jp.supership.vamp.VAMPLocation;
import jp.supership.vamp.VAMPPrivacySettings;
import jp.supership.vamp.VAMPTargeting;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // テストモード設定（収益が発生しないテスト広告を表示する設定）
        // ＜対象：AdMob, maio, nend（manifestに記載が必要）, UnityAds, Mintegral, MoPub, FAN＞
        // リリースする際は必ずコメントアウトしてください
        VAMP.setTestMode(true);

        // デバッグモード設定（デバッグモードで実行する）
        // ＜対象：AppLovin,UnityAds,Mintegral,MoPub,FAN＞
        // リリースする際は必ずコメントアウトしてください
        VAMP.setDebugMode(true);


        // ターゲティング設定（AdMob、nend、Mintegral）
//        VAMP.setTargeting(new VAMPTargeting()
//            .setGender(VAMPTargeting.Gender.FEMALE)
//            .setBirthday(new GregorianCalendar(1980, Calendar.DECEMBER, 20).getTime()));

        VAMPConfiguration vampConfiguration = VAMPConfiguration.getInstance();
        vampConfiguration.setPlayerCancelable(true);
        vampConfiguration.setPlayerAlertTitleText("動画を終了しますか？");
        vampConfiguration.setPlayerAlertBodyText("視聴途中でキャンセルすると報酬がもらえません");
        vampConfiguration.setPlayerAlertCloseButtonText("動画を終了");
        vampConfiguration.setPlayerAlertContinueButtonText("動画を再開");

        Button ad1Button = (Button) findViewById(R.id.button_vamp_ad1);
        ad1Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VAMPAdActivity.class));
            }
        });

        Button ad2Button = (Button) findViewById(R.id.button_vamp_ad2);
        ad2Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VAMPAd2Activity.class));
            }
        });

        Button ad3Button = (Button) findViewById(R.id.button_vamp_ad3);
        ad3Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VAMPAd3Activity.class));
            }
        });

        Button multiAdButton = (Button) findViewById(R.id.button_vamp_multi);
        multiAdButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, VAMPMultiAdActivity.class));
            }
        });

        Button infoButton = (Button) findViewById(R.id.button_info);
        infoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InfoActivity.class));
            }
        });

        // APP & VAMP SDK version
        StringBuffer buffer = new StringBuffer();
        PackageManager pm = getPackageManager();
        try {
            PackageInfo p_info = pm.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            buffer.append("APP v");
            buffer.append(p_info.versionName);
            buffer.append(" / ");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        buffer.append("SDK ");
        buffer.append(VAMP.SDKVersion());
        final String version = buffer.toString();

        final TextView sdkVerTextView = (TextView) findViewById(R.id.sdk_version);
        sdkVerTextView.setText(version);

        // 2桁の国コードを取得して、広告枠IDを切り替える
        VAMP.getLocation(this, new VAMPGetLocationListener() {

            @Override
            public void onLocation(VAMPLocation location) {
                StringBuffer buffer = new StringBuffer(version);
                buffer.append(" / ");
                if (location != null) {
                    buffer.append(location.getCountryCode());
                    if (!TextUtils.isEmpty(location.getRegion())) {
                        buffer.append("-" + location.getRegion());
                    }

                    String countryCode = location.getCountryCode();
                    String region = location.getRegion();

                    // アメリカ
                    if ("US".equals(countryCode)) {
                        // COPPA対象ユーザである場合はtrueを設定する

                        // VAMP.setChildDirected(true);

                        if ("CA".equals(region)) {
                            // カリフォルニア州 (California)
                            // CCPA(https://www.caprivacy.org/)
                        } else if ("NV".equals(region)) {
                            // ネバタ州 (Nevada)
                        }
                    }

                    // 日本
                    if ("JP".equals(countryCode)) {
                        if ("13".equals(region)) {
                            // 東京都
                        } else if ("27".equals(region)) {
                            // 大阪府
                        }
                    }
                }

                sdkVerTextView.setText(buffer.toString());
            }
        });

        VAMP.isEUAccess(this, new VAMPPrivacySettings.UserConsentListener() {

            @Override
            public void onRequired(boolean isRequired) {
                if (!isRequired) {
                    // Nothing to do
                    return;
                }

                new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Personalized Ads")
                    .setMessage("Accept?")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            VAMP.setUserConsent(VAMPPrivacySettings.ConsentStatus.ACCEPTED);
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            VAMP.setUserConsent(VAMPPrivacySettings.ConsentStatus.DENIED);
                        }
                    })
                    .create()
                    .show();
            }
        });
    }
}
