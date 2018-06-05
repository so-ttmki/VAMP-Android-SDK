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
import jp.supership.vamp.VAMPGetCountryCodeListener;
import jp.supership.vamp.VAMPPrivacySettings;
import jp.supership.vamp.VAMPTargeting;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // テストモード設定（収益が発生しないテスト広告を表示する設定）
        // ＜対象：AdMob, AppLovin, maio, nend（manifestに記載が必要）, UnityAds＞
        // リリースする際は必ずコメントアウトしてください
//        VAMP.setTestMode(true);

        // デバッグモード設定（デバッグモードで実行する）
        // ＜対象：AppVador,AppLovin,UnityAds＞
        // リリースする際は必ずコメントアウトしてください
        VAMP.setDebugMode(true);

        // ターゲティング設定（AdMob、nend）
//        VAMP.setTargeting(new VAMPTargeting()
//            .setGender(VAMPTargeting.Gender.FEMALE)
//            .setBirthday(new GregorianCalendar(1980, Calendar.DECEMBER, 20).getTime()));

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

        // アドネットワークSDK 初期化メディエーション
        // initializeAdnwSDKを使う場合は、初期化が終わる前にAD画面へ遷移してloadしないようご注意ください。
        // ├ ステータス設定。デフォルトAUTO
        //    VAMPInitializeState.AUTO	接続環境によって、WEIGHTとALL設定を自動的に切り替える（Wi-Fi:ALL、キャリア回線:WEIGHT）
        //    VAMPInitializeState.WEIGHT	配信比率が高いものをひとつ初期化する
        //    VAMPInitializeState.ALL	全アドネットワークを初期化する
        //    VAMPInitializeState.WIFIONLY	Wi-Fi接続時のみ全アドネットワークを初期化する
        // └ アドネットワークのSDKを初期化する間隔（秒数）
        //   duration:秒単位で指定する。最小4秒、最大60秒。デフォルトは10秒。（対象:AppLovin、maio、UnityAds）
//        VAMP.initializeAdnwSDK(this, VAMPAdActivity.VAMP_AD_ID);     // デフォルト用
//        VAMP.initializeAdnwSDK(this, VAMPAdActivity.VAMP_AD_ID, VAMP.VAMPInitializeState.AUTO, 10);  // カスタム用
    }

}
