package ua.com.skident.englishwords;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.util.Pair;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.sql.SQLException;

public class WordsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
{
    DBHelper m_db;

    WordsContainer m_words;
    CheckBox m_use_random;
    Button m_button_next;
    Button m_button_prev;
    Button m_button_know_it;

    TextView m_view_original;
    TextView m_view_translate;
    TextView m_view_words_total;
    TextView m_view_words_known;
    TextView m_view_words_unknown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setupActionBar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        m_view_original = (TextView) findViewById(R.id.view_original);
        m_view_translate = (TextView) findViewById(R.id.view_translate);
        m_view_words_total = (TextView) findViewById(R.id.words_total);
        m_view_words_known = (TextView) findViewById(R.id.words_known);
        m_view_words_unknown = (TextView) findViewById(R.id.words_unknown);

        m_button_prev.setOnClickListener(this);
        m_button_next.setOnClickListener(this);
        m_button_know_it.setOnClickListener(this);
        m_use_random.setOnClickListener(this);

        try
        {
            m_db = DBHelper.getInstance(this);
            m_words = m_db.getWords(DBHelper.eWords.unknown);
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
        Pair<String, String> pair = null;
        switch(v.getId()) {
            case R.id.button_prev:
                pair = m_words.getPair(WordsContainer.eStep.step_prev);
                break;

            case R.id.button_next:
                pair = m_words.getPair(WordsContainer.eStep.step_next);
                break;

            case R.id.button_know_it:
                m_words.delCurrPair();
                pair = m_words.getPair(WordsContainer.eStep.step_next);

                String original = (String) m_view_original.getText();
                String translate = (String) m_view_translate.getText();
                m_db.markAsKnown(original, translate);

                UpdateStat();

//                Snackbar.make(v, "Added to known", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                break;

            case R.id.UseRandom:
                if (m_words != null)
                {
                    m_words.setRandom(m_use_random.isChecked());
                }
                break;
        }

        if (pair != null) {
            m_view_original.setText(pair.first);
            m_view_translate.setText(pair.second);
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
                intent = new Intent(WordsActivity.this, IrregularVerbsActivity.class);
                break;
            case R.id.nav_add_words:
//                 intent = new Intent(SelectThematicActivity.this, SettingsActivity.class);
                break;

            case R.id.nav_settings:
                intent = new Intent(WordsActivity.this, SettingsActivity.class);
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
