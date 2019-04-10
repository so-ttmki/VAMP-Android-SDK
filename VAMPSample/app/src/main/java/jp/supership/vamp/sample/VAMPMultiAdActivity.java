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

    private static final String VAMP_MULTIAD_ID1 = "59756"; // 広告枠ID1を設定してください
    private static final String VAMP_MULTIAD_ID2 = "*****"; // 広告枠ID2を設定してください

    private VAMP vamp1;
    private VAMP vamp2;

    @Override
    protected void onCreateLayout(Bundle savedInstanceState) {
        setContentView(R.layout.activity_vamp_multi_ad);
        setTitle(R.string.vamp_multi);

        vamp1 = VAMP.getVampInstance(this, VAMP_MULTIAD_ID1);
        vamp1.setVAMPListener(this);
        vamp1.setAdvancedListner(this);

        vamp2 = VAMP.getVampInstance(this, VAMP_MULTIAD_ID2);
        vamp2.setVAMPListener(this);
        vamp2.setAdvancedListner(this);

        findViewById(R.id.button_load1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 広告の取得を開始
                vamp1.load();
                addLog(getIndex(VAMP_MULTIAD_ID1), "[LOAD1] load()");
            }
        });

        findViewById(R.id.button_load2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 広告の取得を開始
                vamp2.load();
                addLog(getIndex(VAMP_MULTIAD_ID2), "[LOAD2] load()");
            }
        });

        findViewById(R.id.button_show1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 広告の表示準備ができているか確認
                if (vamp1.isReady()) {
                    // 広告を表示
                    vamp1.show();
                    addLog(getIndex(VAMP_MULTIAD_ID1), "[SHOW1] show()");
                } else {
                    addLog(getIndex(VAMP_MULTIAD_ID1), "[SHOW1] isReady:false");
                }
            }
        });

        findViewById(R.id.button_show2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (vamp2.isReady()) {
                    // 動画の準備が完了していた場合
                    // 動画を再生する
                    vamp2.show();
                    addLog(getIndex(VAMP_MULTIAD_ID2), "[SHOW2] show()");
                } else {
                    addLog(getIndex(VAMP_MULTIAD_ID2), "[SHOW2] isReady:false");
                }
            }
        });

        ((TextView) findViewById(R.id.vamp_id1)).setText("ID:" + VAMP_MULTIAD_ID1);
        ((TextView) findViewById(R.id.vamp_id2)).setText("ID:" + VAMP_MULTIAD_ID2);

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
        // 広告表示の準備完了
//        addLog(getIndex(placementId), "onReceive(" + placementId + ":" + adnwName + ")");
        // v3.0〜　onLoadResult:successで判定する
    }

    @Override
    public void onFailedToLoad(VAMPError error, String placementId) {
        // 広告取得失敗
        // 広告が取得できなかったときに通知されます。
        // 例）在庫が無い、タイムアウトなど
        // @see https://github.com/AdGeneration/VAMP-Android-SDK/wiki/VAMP-Android-API-Errors
        addLog(getIndex(placementId), "onFailedToLoad(" + placementId + ") " + error, Color.RED);

        if (error == VAMPError.NO_ADSTOCK) {
            // 在庫が無いので、再度loadをしてもらう必要があります。
            // 連続で発生する場合、時間を置いてからloadをする必要があります。
        } else if (error == VAMPError.NO_ADNETWORK) {
            // アドジェネ管理画面でアドネットワークの配信がONになっていない、
            // またはEU圏からのアクセスの場合(GDPR)発生します。
        } else if (error == VAMPError.NEED_CONNECTION) {
            // ネットワークに接続できない状況です。
            // 電波状況をご確認ください。
        } else if (error == VAMPError.MEDIATION_TIMEOUT) {
            // アドネットワークSDKから返答が得られず、タイムアウトしました。
        }
    }

    @Override
    public void onFailedToShow(VAMPError error, String placementId) {
        // 広告表示失敗
        // show実行したが、何らかの理由で広告表示が失敗したときに通知されます。
        // 例）ユーザーが広告再生を途中でキャンセルなど
        addLog(getIndex(placementId), "onFailedToShow(" + placementId + ") " + error, Color.RED);

        if (error == VAMPError.USER_CANCEL) {
            // ユーザが広告再生を途中でキャンセルしました。
        }
    }

    @Override
    public void onComplete(String placementId, String adnwName) {
        // インセンティブ付与OK
        // インセンティブ付与が可能になったタイミング（動画再生完了時、またはエンドカードを閉じたタイミング）で通知
        // アドネットワークによって通知タイミングが異なる
        addLog(getIndex(placementId), "onComplete(" + placementId + ":" + adnwName + ")", Color.BLUE);
    }

    @Override
    public void onClose(String placementId, String adnwName) {
        // 動画プレーヤーやエンドカードが表示終了
        // ＜注意：ユーザキャンセルなども含むので、インセンティブ付与はonCompleteで判定すること＞
        addLog(getIndex(placementId), "onClose(" + placementId + ":" + adnwName + ")", Color.BLACK);
    }

    @Override
    public void onExpired(String placementId) {
        // 有効期限オーバー
        // ＜注意：onReceiveを受けてからの有効期限が切れました。showするには再度loadを行う必要が有ります＞
        addLog(getIndex(placementId), "onExpired(" + placementId + ") ", Color.RED);
    }

    @Override
    public void onLoadStart(String placementId, String adnwName) {
        // 優先順にアドネットワークごとの広告取得を開始
        addLog(getIndex(placementId), "onLoadStart(" + placementId + ":" + adnwName + ")");
    }

    @Override
    public void onLoadResult(String placementId, boolean success, String adnwName, String message) {
        // アドネットワークを１つずつ呼び出した結果、広告在庫が取得できたかをsuccessフラグで確認
        if (success) {
            addLog(getIndex(placementId), "onLoadResult(" + placementId + ":" + adnwName + ")", Color.BLACK);
        } else {
            // 失敗しても、次のアドネットワークがあれば、広告取得を試みます。
            // 最終的に全てのアドネットワークの広告在庫が無ければ
            // onFailedToLoadのNO_ADSTOCKが通知されます。
            addLog(getIndex(placementId), "onLoadResult(" + placementId + ":" + adnwName + ") " + message, Color.RED);
        }
    }
}

