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

public class VAMPMultiAdActivity extends BaseActivity implements VAMPListener, AdvancedListener {

    private static final String VAMP_MULTIAD_ID1 = "*****"; // 広告枠ID1を設定してください
    private static final String VAMP_MULTIAD_ID2 = "*****"; // 広告枠ID2を設定してください

    private VAMP vamp1;
    private VAMP vamp2;

    @Override
    protected void onCreateLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vamp_multi_ad);
        setTitle(R.string.vamp_multi);

        vamp1 = VAMP.getVampInstance(this, VAMP_MULTIAD_ID1);
        vamp1.setVAMPListener(this);                // VAMPListenerをセット
        vamp1.setAdvancedListner(this);             // AdvancedListnerをセット

        vamp2 = VAMP.getVampInstance(this, VAMP_MULTIAD_ID2);
        vamp2.setVAMPListener(this);                // VAMPListenerをセット
        vamp2.setAdvancedListner(this);             // AdvancedListnerをセット

        // load button
        Button load1 = (Button) findViewById(R.id.button_load1);
        load1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLog(getIndex(VAMP_MULTIAD_ID1), "click load button.");
                if (!vamp1.isReady()) {
                    // 再生する動画を準備する
                    vamp1.load();
                } else {
                    addLog(getIndex(VAMP_MULTIAD_ID1), "already loaded.");
                }
            }
        });
        Button load2 = (Button) findViewById(R.id.button_load2);
        load2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLog(getIndex(VAMP_MULTIAD_ID2), "click load button.");
                if (!vamp2.isReady()) {
                    // 再生する動画を準備する
                    vamp2.load();
                } else {
                    addLog(getIndex(VAMP_MULTIAD_ID2), "already loaded.");
                }
            }
        });

        // show button
        Button show1 = (Button) findViewById(R.id.button_show1);
        show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLog(getIndex(VAMP_MULTIAD_ID1), "click show button.");
                if (vamp1.isReady()) {
                    // 動画の準備が完了していた場合
                    // 動画を再生する
                    vamp1.show();
                }
            }
        });
        Button show2 = (Button) findViewById(R.id.button_show2);
        show2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLog(getIndex(VAMP_MULTIAD_ID2), "click show button.");
                if (vamp2.isReady()) {
                    // 動画の準備が完了していた場合
                    // 動画を再生する
                    vamp2.show();
                }
            }
        });

        // 設定されているid表示
        TextView video_id1 = (TextView) findViewById(R.id.vamp_id1);
        if (VAMP_MULTIAD_ID1.length() > 0) {
            video_id1.setText("ID:" + VAMP_MULTIAD_ID1);
        } else {
            video_id1.setText("ID:設定なし");
        }
        TextView video_id2 = (TextView) findViewById(R.id.vamp_id2);
        if (VAMP_MULTIAD_ID2.length() > 0) {
            video_id2.setText("ID:" + VAMP_MULTIAD_ID2);
        } else {
            video_id2.setText("ID:設定なし");
        }

        // log
        mLogView = (TextView) findViewById(R.id.logs);
    }

    private int getIndex(String placementId) {
        if (VAMP_MULTIAD_ID1.length() > 0 || VAMP_MULTIAD_ID2.length() > 0) {
            if (VAMP_MULTIAD_ID1.equals(placementId)) {
                return 1;
            } else if (VAMP_MULTIAD_ID2.equals(placementId)) {
                return 2;
            }
        }
        return -1;
    }

    private void addLog(int index, String message) {
        addLog("[" + index + "]" + message);
    }

    private void addLog(int index, String message, int color) {
        addLog("[" + index + "]" + message, color);
    }

    @Override
    public void onReceive(String placementId, String adnwName) {
        // 動画表示の準備完了
        addLog(getIndex(placementId), "onReceive(" + placementId + ":" + adnwName + ")");
    }

    @Override
    public void onFail(String placementId, VAMPError error) {
        // 動画準備or表示失敗
        addLog(getIndex(placementId), "onFail(" + placementId + ") " + error);
    }

    @Override
    public void onComplete(String placementId, String adnwName) {
        // 動画再生正常終了（インセンティブ付与可能）
        addLog(getIndex(placementId), "onComplete(" + placementId + ":" + adnwName + ")", Color.BLUE);
    }

    @Override
    public void onClose(String placementId, String adnwName) {
        // 動画プレーヤーやエンドカードが表示終了
        // ＜注意：ユーザキャンセルなども含むので、インセンティブ付与はonCompleteで判定すること＞
        addLog(getIndex(placementId), "onClose(" + placementId + ":" + adnwName + ")", Color.MAGENTA);
    }

    @Override
    public void onExpired(String placementId) {
        // 有効期限オーバー
        // ＜注意：onReceiveを受けてからの有効期限が切れました。showするには再度loadを行う必要が有ります＞
        addLog(getIndex(placementId), "onExpired(" + placementId + ") ", Color.MAGENTA);
    }

    @Override
    public void onLoadStart(String placementId, String adnwName) {
        // 優先順位順にアドネットワークごとの広告取得を開始
        addLog(getIndex(placementId), "onLoadStart(" + placementId + ":" + adnwName + ")");
    }

    @Override
    public void onLoadResult(String placementId, boolean success, String adnwName, String message) {
        // アドネットワークごとの広告取得結果
        addLog(getIndex(placementId), "onLoadResult(" + placementId + ":" + adnwName + ") " + message);
    }
}

