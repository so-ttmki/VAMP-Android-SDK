package jp.supership.vamp.sample;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.GregorianCalendar;

import jp.supership.vamp.VAMP;
import jp.supership.vamp.VAMPGetCountryCodeListener;
import jp.supership.vamp.VAMPTargeting;

public class MainActivity extends AppCompatActivity {
    private String mVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // テストモード設定（収益が発生しないテスト広告を表示する設定）
        // ＜対象：AppLovin,maio,UnityAds＞
        // リリースする際は必ずコメントアウトしてください
//        VAMP.setTestMode(true);

        // デバッグモード設定（デバッグモードで実行する）
        // ＜対象：AppVador,AppLovin,UnityAds＞
        VAMP.setDebugMode(true);

        // ターゲティング設定
        VAMP.setTargeting(new VAMPTargeting()
            .setGender(VAMPTargeting.Gender.FEMALE)
            .setBirthday(new GregorianCalendar(1980, 2, 20).getTime()));

        // VAMP AD
        Button vamp_ad = (Button) findViewById(R.id.button_vamp_ad);
        vamp_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VAMPAdActivity.class));
            }
        });

        // VAMP Multi AD
        Button vamp_multi_ad = (Button) findViewById(R.id.button_vamp_multi);
        vamp_multi_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, VAMPMultiAdActivity.class));
            }
        });

        // 端末情報表示
        Button info = (Button) findViewById(R.id.button_info);
        info.setOnClickListener(new View.OnClickListener() {
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
        mVersion = buffer.toString();

        TextView sdk_version = (TextView) findViewById(R.id.sdk_version);
        sdk_version.setText(mVersion);

        // CountryCode取得
        VAMP.getCountryCode(this, new VAMPGetCountryCodeListener() {
            @Override
            public void onCountryCode(String isoCode) {
                StringBuffer buffer = new StringBuffer(mVersion);
                buffer.append(" / ");
                buffer.append(isoCode);

                TextView sdk_version = (TextView) findViewById(R.id.sdk_version);
                sdk_version.setText(buffer.toString());
            }
        });

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
