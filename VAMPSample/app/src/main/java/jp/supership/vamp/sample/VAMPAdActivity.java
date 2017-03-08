package jp.supership.vamp.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import jp.supership.vamp.AdvancedListener;
import jp.supership.vamp.VAMP;
import jp.supership.vamp.VAMPError;
import jp.supership.vamp.VAMPListener;

public class VAMPAdActivity extends BaseActivity {
    public static final String VAMP_AD_ID = "*****";    // 広告枠ID

    private VAMP vamp;
    private Button showButton;

    @Override
    protected void onCreateLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vamp_ad);
        setTitle(R.string.vamp_ad);

        vamp = VAMP.getVampInstance(this, VAMP_AD_ID);
        vamp.setVAMPListener(new AdListener());     // VAMPListenerをセット
        vamp.setAdvancedListner(new AdvListener()); // AdvancedListnerをセット

        // load button
        Button load = (Button) findViewById(R.id.button_load);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLog("click load button.");
                if (!vamp.isReady()) {
                    // 再生する動画を準備する
                    vamp.load();
                } else {
                    addLog("already loaded.");
                    showButton.setEnabled(true);
                }
            }
        });

        // show button
        showButton = (Button) findViewById(R.id.button_show);
        showButton.setEnabled(false);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLog("click show button.");
                if (vamp.isReady()) {
                    showButton.setEnabled(false);
                    // 動画の準備が完了していた場合
                    // 動画を再生する
                    vamp.show();
                }
            }
        });

        // 設定されているid表示
        TextView vamp_id = (TextView) findViewById(R.id.vamp_id);
        if (VAMP_AD_ID != null && VAMP_AD_ID.length() > 0) {
            vamp_id.setText("ID:" + VAMP_AD_ID);
        } else {
            vamp_id.setText("ID:設定なし");
        }

        // log
        mLogView = (TextView) findViewById(R.id.logs);
    }

    private class AdListener implements VAMPListener {
        @Override
        public void onReceive(String placementId, String adnwName) {
            // 動画表示の準備完了
            addLog("onReceive(" + adnwName + ")");
            showButton.setEnabled(true);
        }

        @Override
        public void onFail(String placementId, VAMPError error) {
            // 動画準備or表示失敗
            addLog("onFail() " + error);
            showButton.setEnabled(false);
        }

        @Override
        public void onComplete(String placementId, String adnwName) {
            // 動画再生正常終了（インセンティブ付与可能）
            addLog("onComplete(" + adnwName + ")", Color.BLUE);
        }

        @Override
        public void onClose(String placementId, String adnwName) {
            // 動画プレーヤーやエンドカードが表示終了
            // ＜注意：ユーザキャンセルなども含むので、インセンティブ付与はonCompleteで判定すること＞
            addLog("onClose(" + adnwName + ")", Color.MAGENTA);
        }

        @Override
        public void onExpired(String placementId) {
            // 有効期限オーバー
            // ＜注意：onReceiveを受けてからの有効期限が切れました。showするには再度loadを行う必要が有ります＞
            addLog("onExpired()", Color.MAGENTA);
            showButton.setEnabled(false);
        }
    }

    private class AdvListener implements AdvancedListener {
        @Override
        public void onLoadStart(String placementId, String adnwName) {
            // 優先順位順にアドネットワークごとの広告取得を開始
            addLog("onLoadStart(" + adnwName + ")");
        }

        @Override
        public void onLoadResult(String placementId, boolean success, String adnwName, String message) {
            // アドネットワークごとの広告取得結果
            addLog("onLoadResult(" + adnwName + ") " + message);
        }
    }
}
