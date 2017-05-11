package ua.com.skident.englishwords;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class WordsActivity extends AppCompatActivity
        implements
        View.OnClickListener,
        Toolbar.OnMenuItemClickListener,
        CompoundButton.OnCheckedChangeListener

{
    private GestureDetector gestureDetector;

    private cDBHelper.eWordsStatus m_currState = cDBHelper.eWordsStatus.unknown;
    private cDBHelper m_db;

    private cWordContainer m_words;
    private CheckBox m_is_random;
    private CheckBox m_is_known;

    private Button m_button_next;
    private Button m_button_prev;

    private TextView m_view_original;
    private TextView m_view_translate;
    private TextView m_view_words_total;
    private TextView m_view_words_known;
    private TextView m_view_words_unknown;

    private Toolbar toolbar = null;

    private Integer m_count_total = 0;
    private Integer m_count_unknown = 0;
    private Integer m_count_known = 0;

    private LinearLayout m_layout_learnt;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_words);

        // setup toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_words_unknown);
        toolbar.inflateMenu(R.menu.menu_simple_word);
        toolbar.setOnMenuItemClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Get pointers to widgets
        RadioButton radio_unknown = (RadioButton) findViewById(R.id.radio_unknown);
        RadioButton radio_known = (RadioButton) findViewById(R.id.radio_known);

        m_is_random = (CheckBox) findViewById(R.id.check_random);
        m_is_known = (CheckBox) findViewById(R.id.check_known);

        m_button_prev = (Button) findViewById(R.id.button_prev);
        m_button_next = (Button) findViewById(R.id.button_next);

        m_view_original = (TextView) findViewById(R.id.view_original);
        m_view_translate = (TextView) findViewById(R.id.view_translate);
        m_view_words_total = (TextView) findViewById(R.id.words_total);
        m_view_words_known = (TextView) findViewById(R.id.words_known);
        m_view_words_unknown = (TextView) findViewById(R.id.words_unknown);

        m_layout_learnt = (LinearLayout) findViewById(R.id.layout_learnt);

        // Set handlers for all buttons
        m_button_prev.setOnClickListener(this);
        m_button_next.setOnClickListener(this);
        m_is_known.setOnClickListener(this);
        m_is_random.setOnClickListener(this);
        radio_known.setOnCheckedChangeListener(this);
        radio_unknown.setOnCheckedChangeListener(this);

        try
        {
            m_db = cDBHelper.getInstance(this);
            m_words = m_db.getWords(cDBHelper.eWordsStatus.unknown);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            m_is_random.callOnClick();
            m_button_next.callOnClick();
        }

        UpdateStatistic();

        gestureDetector = new GestureDetector(this, new SwipeGestureDetector());
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_word_settings:
                Log.d("DBG", "User wants set-up this activity");
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v)
    {
        cSimpleWord word = null;
        switch(v.getId()) {
            case R.id.button_prev:
                word = m_words.get(cWordContainer.eStep.step_prev);
                break;

            case R.id.button_next:
                word = m_words.get(cWordContainer.eStep.step_next);
                break;

            case R.id.check_known:
                Boolean bKnown = m_is_known.isChecked();
                word = m_words.get(cWordContainer.eStep.step_none);

                if (bKnown) {
                    m_count_unknown--;
                    m_count_known++;
                }else{
                    m_count_unknown++;
                    m_count_known--;
                }


                m_db.setSimpleWordState(word.id, bKnown);
                if (m_currState == cDBHelper.eWordsStatus.all)
                {
                    m_words.setKnownState(bKnown);
                }
                else
                {
                    m_words.removeCurrWord();
                    word = m_words.get(cWordContainer.eStep.step_next);
                }

                UpdateStatistic();

                m_layout_learnt.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 10 seconds
                        m_layout_learnt.setVisibility(View.INVISIBLE);
                    }
                }, 400);



                break;

            case R.id.check_random:
                boolean bRandom = m_is_random.isChecked();
                if (m_words != null)
                    m_words.setRandom(bRandom);
                word = m_words.get(cWordContainer.eStep.step_next);
                break;
        }

        if (word != null)
        {
            m_is_known.setChecked(word.isKnown);
            m_view_original.setText(word.original);
            m_view_translate.setText(word.translate);
        }
        else
        {
            Context context = getApplicationContext();
            CharSequence text = "You have no words in this mode. Please select another mode in menu";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, text, duration).show();

            m_view_original.setText("");
            m_view_translate.setText("");
        }
    }

    private void UpdateStatistic()
    {
        if (m_count_total <= 0 && m_count_unknown <= 0 && m_count_known <= 0)
        {
            m_count_total = m_db.getTotalCount(cDBHelper.eWordType.type_common);
            m_count_unknown = m_db.getUnknownCount(cDBHelper.eWordType.type_common);
            m_count_known = m_db.getKnownCount(cDBHelper.eWordType.type_common);
        }

        m_view_words_total.setText(m_count_total.toString());
        m_view_words_known.setText(m_count_known.toString());
        m_view_words_unknown.setText(m_count_unknown.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Integer title_id = R.string.title_words_unknown;
        if (isChecked == false)
            return;

        switch (buttonView.getId()) {
            case R.id.radio_unknown:
                Log.i("Debug", "UNKNOWN SELECTED");
                title_id = R.string.title_words_unknown;
                m_currState = cDBHelper.eWordsStatus.unknown;
                break;
            case R.id.radio_known:
                Log.i("Debug", "KNOWN SELECTED");
                title_id = R.string.title_words_known;
                m_currState = cDBHelper.eWordsStatus.known;
                break;

        }

        if (toolbar != null)
            toolbar.setTitle(title_id);
        m_words = m_db.getWords(m_currState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            m_button_next.callOnClick();
        }
    }



    /////////////////////////////////////////////////////////
    ///////////////////////SWIPE LOGIN///////////////////////
    /////////////////////////////////////////////////////////

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onLeftSwipe() {
        // Do something
        Log.d("a", "swipte to left");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            m_button_prev.callOnClick();
        }
    }

    private void onRightSwipe() {
        // Do something
        Log.d("a", "swipte to right");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            m_button_next.callOnClick();
        }
    }

    private void onUpSwipe() {
        Log.d("a", "swipe UP");
    }

    private void onDownSwipe() {
        Log.d("a", "swipe DOWN");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            m_is_known.callOnClick();
        }
    }

    // Private class for gestures
    private class SwipeGestureDetector
            extends GestureDetector.SimpleOnGestureListener {
        // Swipe properties, you can change it to make the swipe
        // longer or shorter and speed
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            try
            {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();
                float diffY = e1.getY() - e2.getY();

                if (diffAbs > SWIPE_MAX_OFF_PATH && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
                {
                    // UP swipe
                    if (diffY > SWIPE_MIN_DISTANCE) {
                        WordsActivity.this.onUpSwipe();
                        // DOWN swipe
                    } else if (-diffY > SWIPE_MIN_DISTANCE){
                        WordsActivity.this.onDownSwipe();
                    } else
                        return false;
                    return true;
                }

                if (Math.abs(velocityX) <= SWIPE_THRESHOLD_VELOCITY)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE) {
                    WordsActivity.this.onLeftSwipe();
                // Right swipe
                } else if (-diff > SWIPE_MIN_DISTANCE) {
                    WordsActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }
}


