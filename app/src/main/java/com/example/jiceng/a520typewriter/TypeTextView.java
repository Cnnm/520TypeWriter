package com.example.jiceng.a520typewriter;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 打字机效果的TextView
 * Created by jichunzeng on 2018/5/19.
 */

public class TypeTextView extends android.support.v7.widget.AppCompatTextView {
    private Context mContext = null;
    private MediaPlayer mMediaPlayer = null;
    private String mShowTextString = null;
    private Timer mTypeTimer = null;
    private OnTypeViewListener mOnTypeViewListener = null;
    private static final int TYPE_TIME_DELAY = 30;
    private int mTypeTimeDelay = TYPE_TIME_DELAY; // 打字间隔
    private ScrollView scrollView;


    public TypeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initTypeTextView( context );
    }

    public TypeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeTextView( context );
    }

    public TypeTextView(Context context) {
        super(context);
        initTypeTextView( context );
    }

    public void setOnTypeViewListener( OnTypeViewListener onTypeViewListener ){
        mOnTypeViewListener = onTypeViewListener;
    }

    public void start( ScrollView scrollView ,final String textString ){
        this.scrollView = scrollView;
        start( textString, TYPE_TIME_DELAY );
    }

    public void start( final String textString, final int typeTimeDelay ){
        if( TextUtils.isEmpty( textString ) || typeTimeDelay < 0 ){
            return;
        }
        post( new Runnable( ) {
            @Override
            public void run() {
                mShowTextString = textString;
                mTypeTimeDelay = typeTimeDelay;
                setText( "" );
                startTypeTimer( );
                if( null != mOnTypeViewListener ){
                    mOnTypeViewListener.onTypeStart( );
                }
            }
        });
    }

    public void stop( ){
        stopTypeTimer( );
        stopAudio();
    }

    private void initTypeTextView( Context context ){
        mContext = context;
    }

    private void startTypeTimer( ){
        stopTypeTimer( );
        mTypeTimer = new Timer( );
        mTypeTimer.schedule( new TypeTimerTask(), mTypeTimeDelay );
    }

    private void stopTypeTimer( ){
        if( null != mTypeTimer ){
            mTypeTimer.cancel( );
            mTypeTimer = null;
        }
    }

    private void startAudioPlayer() {
        stopAudio();
        playAudio( R.raw.type_in );
    }

    private void playAudio( int audioResId ){
        try{
            stopAudio( );
            mMediaPlayer = MediaPlayer.create( mContext, audioResId );
            mMediaPlayer.start( );
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

    private void stopAudio( ){
        if( mMediaPlayer != null && mMediaPlayer.isPlaying( ) ){
            mMediaPlayer.stop( );
            mMediaPlayer.release( );
            mMediaPlayer = null;
        }
    }

    class TypeTimerTask extends TimerTask {
        @Override
        public void run() {
            post(new Runnable( ) {
                @Override
                public void run() {
                    if( getText( ).toString( ).length( ) < mShowTextString.length( ) ){
                        Log.d("打字机",mShowTextString.substring(0, getText( ).toString( ).length( ) + 1 ));
                        setText(switchtext(mShowTextString.substring(0, getText( ).toString( ).length( ) + 1 )));
                        startAudioPlayer();
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
                        startTypeTimer( );
                    }else{
                        stopTypeTimer( );
                        if( null != mOnTypeViewListener ){
                            mOnTypeViewListener.onTypeOver( );
                        }
                    }
                }
            });
        }
    }

    private SpannableStringBuilder switchtext(String text) {
        // 关键字正则表达式规则
        String regExs = "^this|private|class|implements|abstract|extends|new|null|for|if|break|return|char|foreach|long$";
        // 编译正则表达式
        Pattern patterns = Pattern.compile(regExs);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        SpannableStringBuilder styles = new SpannableStringBuilder(text);
        Matcher matchers = patterns.matcher(text);
        // 查找字符串中是否有匹配正则表达式的字符/字符串
        while (matchers.find()) {
            int i = 1;

            styles.setSpan(new ForegroundColorSpan(Color.parseColor("#e2bb68")), matchers.start(), matchers.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            i++;
        }
        String regEx = "//[^/]+/|Always and Forever";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        SpannableStringBuilder style = new SpannableStringBuilder(styles);
        Matcher matcher = pattern.matcher(styles);
        // 查找字符串中是否有匹配正则表达式的字符/字符串
        while (matcher.find()) {
            int i = 1;

            style.setSpan(new ForegroundColorSpan(Color.parseColor("#56a84b")), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            i++;
        }
        String reg = "love|I_ do|time";
        // 编译正则表达式
        Pattern pat = Pattern.compile(reg);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        SpannableStringBuilder sty = new SpannableStringBuilder(style);
        Matcher mat = pat.matcher(style);
        // 查找字符串中是否有匹配正则表达式的字符/字符串
        while (mat.find()) {
            int i = 1;
            sty.setSpan(new ForegroundColorSpan(Color.parseColor("#896e98")), mat.start(), mat.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            i++;
        }
        return sty;
    }


    public interface OnTypeViewListener{
        public void onTypeStart( );
        public void onTypeOver( );
    }

}
