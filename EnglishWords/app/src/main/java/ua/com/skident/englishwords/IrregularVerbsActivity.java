package ua.com.skident.englishwords;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.sql.SQLException;

public class IrregularVerbsActivity extends AppCompatActivity
        implements
        View.OnClickListener,
        Toolbar.OnMenuItemClickListener,
        CompoundButton.OnCheckedChangeListener

{
    private cDBHelper.eWordsStatus  m_currState             = cDBHelper.eWordsStatus.unknown;
    private DrawerLayout            m_drawer                = null;
    private Toolbar                 m_toolbar               = null;

    private cDBHelper               m_db                    = null;
    private cIrrVerbContainer       m_words                 = null;

    // buttons
    private CheckBox                m_is_random             = null;
    private CheckBox                m_is_known              = null;
    private Button                  m_button_next           = null;
    private Button                  m_button_prev           = null;

    // handles to text views with words
    private TextView                m_view_verb             = null;
    private TextView                m_view_verb_translate   = null;
    private TextView                m_view_past_simple      = null;
    private TextView                m_view_past_participle  = null;

    // handles for counters
    private TextView                m_view_words_total      = null;
    private TextView                m_view_words_known      = null;
    private TextView                m_view_words_unknown    = null;

    private GestureDetector gestureDetector = null;

    private Integer m_count_total = 0;
    private Integer m_count_unknown = 0;
    private Integer m_count_known = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irregular_verbs);

        // Setup the m_toolbar
        m_toolbar = (Toolbar) findViewById(R.id.irregular_toolbar);
        m_toolbar.setTitle(R.string.title_verbs_unknown);
        m_toolbar.inflateMenu(R.menu.menu_irregular);
        m_toolbar.setOnMenuItemClickListener(this);

        setSupportActionBar(m_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        m_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Get handles to widgets
        RadioButton radio_unknown = (RadioButton) findViewById(R.id.radio_unknown);
        RadioButton radio_known = (RadioButton) findViewById(R.id.radio_known);

        m_is_random             = (CheckBox) findViewById(R.id.check_random);
        m_is_known              = (CheckBox) findViewById(R.id.check_known);

        m_button_prev           = (Button) findViewById(R.id.button_prev);
        m_button_next           = (Button) findViewById(R.id.button_next);

        m_view_verb             = (TextView) findViewById(R.id.view_verb);
        m_view_past_simple      = (TextView) findViewById(R.id.view_past_simple);
        m_view_past_participle  = (TextView) findViewById(R.id.view_past_participle);
        m_view_verb_translate   = (TextView) findViewById(R.id.view_verb_translate);

        m_view_words_total      = (TextView) findViewById(R.id.view_verbs_total);
        m_view_words_known      = (TextView) findViewById(R.id.view_verbs_known);
        m_view_words_unknown    = (TextView) findViewById(R.id.view_verbs_unknown);

        m_button_prev.setOnClickListener(this);
        m_button_next.setOnClickListener(this);
        m_is_random.setOnClickListener(this);
        m_is_known.setOnClickListener(this);
        radio_known.setOnCheckedChangeListener(this);
        radio_unknown.setOnCheckedChangeListener(this);

        try
        {
            m_db = cDBHelper.getInstance(this);
            m_words = m_db.getIrregularVerbs(m_currState);

            Log.i("Count: ", m_words.size().toString());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
        {
            m_is_random.callOnClick();
            m_button_next.callOnClick();
        }

        UpdateStatistic();
        gestureDetector = new GestureDetector(this, new SwipeGestureDetector());
    }


    // Toolbar menuitem click listener
    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_word_settings:
                Log.d("DBG", "User wants set-up this activity");
                break;
        }
        return true;
    }


    // click listener for all activity's widgets
    @Override
    public void onClick(View v)
    {
        // Check which element was clicked
        cIrregularVerb verb = null;
        switch(v.getId())
        {
            case R.id.button_prev:
                verb = m_words.get(cIrrVerbContainer.eStep.step_prev);
                break;

            case R.id.button_next:
                verb = m_words.get(cIrrVerbContainer.eStep.step_next);
                break;

            case R.id.check_known:
                Boolean bKnown = m_is_known.isChecked();
                verb = m_words.get(cIrrVerbContainer.eStep.step_none);
                String id = verb.getData(cIrregularVerb.eTags.id);
                m_db.setVerbState(id, bKnown);

                if (bKnown) {
                    m_count_unknown--;
                    m_count_known++;
                }else{
                    m_count_unknown++;
                    m_count_known--;
                }

                if (m_currState == cDBHelper.eWordsStatus.all)
                {
                    m_words.setCurrVerbKnownState(bKnown);
                }
                else
                {
                    m_words.delCurrVerb();
                    verb = m_words.get(cIrrVerbContainer.eStep.step_next);
                }

                UpdateStatistic();

                break;

            case R.id.check_random:
                boolean bRandom = m_is_random.isChecked();
                if (m_words != null)
                    m_words.setRandom(bRandom);
                verb = m_words.get(cIrrVerbContainer.eStep.step_next);
                break;
        }

        if (verb != null)
        {
            Boolean state = true;
            String sState = verb.getData(cIrregularVerb.eTags.isKnown);
            if (sState.equals("false"))
                state = false;

            m_is_known.setChecked(state);
            m_view_verb.setText(verb.getData(cIrregularVerb.eTags.infinitive));
            m_view_past_simple.setText(verb.getData(cIrregularVerb.eTags.pastSimple));
            m_view_past_participle.setText(verb.getData(cIrregularVerb.eTags.pastParticiple));
            m_view_verb_translate.setText(verb.getData(cIrregularVerb.eTags.translate));
        }
        else
        {
            m_view_verb.setText("");
            m_view_past_simple.setText("");
            m_view_past_participle.setText("");
            m_view_verb_translate.setText("");

            Context context = getApplicationContext();
            CharSequence text = "You have no words in this mode. Please select another mode in menu";
            m_view_past_simple.setText(text);
        }
    }

    private void UpdateStatistic()
    {
        if (m_count_total <= 0 && m_count_unknown <= 0 && m_count_known <= 0)
        {
            m_count_total = m_db.getTotalCount(cDBHelper.eWordType.type_irregular);
            m_count_unknown = m_db.getUnknownCount(cDBHelper.eWordType.type_irregular);
            m_count_known = m_db.getKnownCount(cDBHelper.eWordType.type_irregular);
        }

        m_view_words_total.setText(m_count_total.toString());
        m_view_words_known.setText(m_count_known.toString());
        m_view_words_unknown.setText(m_count_unknown.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        Integer id = item.getItemId();
//
//        Log.i("Debug", id.toString());
//
//        if (id == android.R.id.home)
//        {
//            finish();
//            return true;
//        }
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings)
//        {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Integer title_id = R.string.title_words_unknown;
        if (isChecked == false)
            return;

        switch (buttonView.getId()) {
            case R.id.radio_unknown:
                Log.i("Debug", "UNKNOWN SELECTED");
                title_id = R.string.title_verbs_unknown;
                m_currState = cDBHelper.eWordsStatus.unknown;
                break;
            case R.id.radio_known:
                Log.i("Debug", "KNOWN SELECTED");
                title_id = R.string.title_verbs_known;
                m_currState = cDBHelper.eWordsStatus.known;
                break;
        }

        if (m_toolbar != null)
            m_toolbar.setTitle(title_id);
        m_words = m_db.getIrregularVerbs(m_currState);
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

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    IrregularVerbsActivity.this.onLeftSwipe();

                    // Right swipe
                } else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    IrregularVerbsActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }
}
