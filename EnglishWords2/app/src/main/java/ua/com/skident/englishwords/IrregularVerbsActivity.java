package ua.com.skident.englishwords;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.sql.SQLException;

public class IrregularVerbsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
{
    DBHelper m_db;

    IrregularVerbsContainer m_words;
    CheckBox m_use_random;
    Button m_button_next;
    Button m_button_prev;
    Button m_button_know_it;

    TextView m_view_verb;
    TextView m_view_verb_translate;
    TextView m_view_past_simple;
    TextView m_view_past_participle;

    TextView m_view_words_total;
    TextView m_view_words_known;
    TextView m_view_words_unknown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irregular_verbs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setupActionBar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_irregular);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get pointers to widgets
        m_use_random = (CheckBox) findViewById(R.id.UseRandom);

        m_button_prev = (Button) findViewById(R.id.button_prev);
        m_button_next = (Button) findViewById(R.id.button_next);
        m_button_know_it = (Button) findViewById(R.id.button_know_it);

        m_view_verb = (TextView) findViewById(R.id.view_verb);
        m_view_past_simple = (TextView) findViewById(R.id.view_past_simple);
        m_view_past_participle = (TextView) findViewById(R.id.view_past_participle);
        m_view_verb_translate = (TextView) findViewById(R.id.view_verb_translate);


        m_view_words_total = (TextView) findViewById(R.id.view_verbs_total);
        m_view_words_known = (TextView) findViewById(R.id.view_verbs_known);
        m_view_words_unknown = (TextView) findViewById(R.id.view_verbs_unknown);

        m_button_prev.setOnClickListener(this);
        m_button_next.setOnClickListener(this);
        m_button_know_it.setOnClickListener(this);
        m_use_random.setOnClickListener(this);

        try
        {
            m_db = DBHelper.getInstance(this);
            m_words = m_db.getIrregularVerbs(DBHelper.eWords.unknown);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            m_use_random.callOnClick();
            m_button_next.callOnClick();
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View v)
    {
        IrregularVerb pair = null;
        switch(v.getId()) {
            case R.id.button_prev:
                pair = m_words.get(IrregularVerbsContainer.eStep.step_prev);
                break;

            case R.id.button_next:
                pair = m_words.get(IrregularVerbsContainer.eStep.step_next);
                break;

            case R.id.button_know_it:
                m_words.delCurrPair();
                pair = m_words.get(IrregularVerbsContainer.eStep.step_next);

                String original = (String) m_view_verb.getText();
                String translate = (String) m_view_verb_translate.getText();
                m_db.markAsKnown(original, translate);

                UpdateStat();
                break;

            case R.id.UseRandom:
                if (m_words != null)
                {
                    m_words.setRandom(m_use_random.isChecked());
                }
                break;
        }

        if (pair != null) {
            m_view_verb.setText(pair.getData(IrregularVerb.eTags.infinitive));
            m_view_past_simple.setText(pair.getData(IrregularVerb.eTags.past_simple));
            m_view_past_participle.setText(pair.getData(IrregularVerb.eTags.past_participle));
            m_view_verb_translate.setText(pair.getData(IrregularVerb.eTags.translate));
        }
    }

    private void UpdateStat()
    {
        m_view_words_total.setText(m_db.getTotal().toString());
        m_view_words_known.setText(m_db.getKnown().toString());
        m_view_words_unknown.setText(m_db.getUnknown().toString());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = null;

        switch (id)
        {
            case R.id.nav_words:
                break;
            case R.id.nav_irregular_verb:
                intent = new Intent(IrregularVerbsActivity.this, IrregularVerbsActivity.class);
                break;
            case R.id.nav_add_words:
//                 intent = new Intent(SelectThematicActivity.this, SettingsActivity.class);
                break;

            case R.id.nav_settings:
                intent = new Intent(IrregularVerbsActivity.this, SettingsActivity.class);
                break;
            case R.id.nav_about:
//                 intent = new Intent(SelectThematicActivity.this, SettingsActivity.class);
                break;
            case R.id.nav_contacts:
//                 intent = new Intent(SelectThematicActivity.this, SettingsActivity.class);
                break;
            default:

        }

        if (intent != null)
            startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

