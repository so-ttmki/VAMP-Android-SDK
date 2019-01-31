package jp.supership.vamp.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import jp.supership.vamp.VAMP;
import jp.supership.vamp.VAMPConfiguration;
import jp.supership.vamp.VAMPGetCountryCodeListener;
import jp.supership.vamp.VAMPPrivacySettings;
import jp.supership.vamp.VAMPTargeting;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // テストモード設定（収益が発生しないテスト広告を表示する設定）
        // ＜対象：AdMob, AppLovin, maio, nend（manifestに記載が必要）, UnityAds, Mintegral, MoPub, FAN＞
        // リリースする際は必ずコメントアウトしてください
        VAMP.setTestMode(true);

        // デバッグモード設定（デバッグモードで実行する）
        // ＜対象：AppVador,AppLovin,UnityAds,Mintegral,MoPub,FAN＞
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
        }
        buffer.append("SDK ");
        buffer.append(VAMP.SDKVersion());
        final String version = buffer.toString();

        final TextView sdkVerTextView = (TextView) findViewById(R.id.sdk_version);
        sdkVerTextView.setText(version);

        // 2桁の国コードを取得して、広告枠IDを切り替える
        VAMP.getCountryCode(this, new VAMPGetCountryCodeListener() {

            @Override
            public void onCountryCode(String isoCode) {
                StringBuffer buffer = new StringBuffer(version);
                buffer.append(" / ");
                buffer.append(isoCode);

                sdkVerTextView.setText(buffer.toString());

//                if (isoCode.equals("US")) {
//                    // COPPA対象ユーザである場合はtrueを設定する
//                    VAMP.setCoppaChildDirected(true);
//                }
            }
        });

//        VAMP.isEUAccess(new VAMPPrivacySettings.UserConsentListener() {
//
//            @Override
//            public void onRequired(boolean isRequired) {
//                if (!isRequired) {
//                    // Nothing to do
//                    return;
//                }
//
//                new AlertDialog.Builder(MainActivity.this)
//                    .setTitle("Personalized Ads")
//                    .setMessage("Accept?")
//                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            VAMP.setUserConsent(VAMPPrivacySettings.ConsentStatus.ACCEPTED);
//                        }
//                    })
//                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            VAMP.setUserConsent(VAMPPrivacySettings.ConsentStatus.DENIED);
//                        }
//                    })
//                    .create()
//                    .show();
//            }
//        });
    }

}
