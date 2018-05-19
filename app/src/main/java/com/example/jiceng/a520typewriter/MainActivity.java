package com.example.jiceng.a520typewriter;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.VideoView;

/**
 * 打字机效果的TextView
 * Created by jichunzeng on 2018/5/19.
 */
public class MainActivity extends AppCompatActivity {
    TypeTextView mTypeTextView;
    private ScrollView scrollView;
    private VideoView main_videoview;
    String TEST_DATA = " //自从看到你之后就爱上了单纯的你/\n" +
            "        long love = 0;\n" +
            "        //时光在增加，爱你的心也在升温/\n" +
            "        for (long time = 1 ; time<love ; ++love, ++time){\n" +
            "            //如果你不爱我，我将会等到时间的尽头，独自心碎/\n" +
            "            if (love < 0) break;\n" +
            "        }\n" +
            "        //抽象出我的爱注入你的心中/\n" +
            "        abstract class MyLove implements YourHeart {\n" +
            "            //你若愿意/\n" +
            "            String MyLove(String I_ do) {\n" +
            "                //我便爱你直至永久/\n" +
            "                return \"Always and Forever\";\n" +
            "            }\n" +
            "        }";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        setContentView(R.layout.activity_main);
        mTypeTextView = (TypeTextView) findViewById(R.id.typeTxtId);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        mTypeTextView.setOnTypeViewListener(new TypeTextView.OnTypeViewListener() {
            @Override
            public void onTypeStart() {
                Log.d("", "onTypeStart");
            }
            //打字机效果的TextView

            @Override
            public void onTypeOver() {
                Log.d("", "onTypeOver");
            }
        });
        mTypeTextView.start(scrollView ,TEST_DATA);

        main_videoview = (VideoView) findViewById(R.id.main_videoview);
        main_videoview.setVideoURI(Uri.parse("android.resource://com.example.jiceng.a520typewriter/"+R.raw.video));
        main_videoview.start();
        main_videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        main_videoview.stopPlayback();
        mTypeTextView.stop();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        main_videoview.start();
        mTypeTextView.start(scrollView ,TEST_DATA);
    }
}
