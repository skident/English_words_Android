package ua.com.skident.englishwords;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.sql.SQLException;

public class ShowConcretePatternActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private cDBHelper m_db;
    private DrawerLayout drawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_concrete_pattern);

        Toolbar toolbar = (Toolbar) findViewById(R.id.show_concrete_pattern_toolbar);
        toolbar.setTitle(R.string.title_pattern);

        String patternId = getIntent().getExtras().getString("pattern_id");
        String patternName = getIntent().getExtras().getString("pattern_name");
        Log.i("Sected name: ", patternName);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_show_concrete_pattern);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView vName      = (TextView) findViewById(R.id.view_name);
        TextView vForm      = (TextView) findViewById(R.id.view_form);
        TextView vDesc      = (TextView) findViewById(R.id.view_desc);
        TextView vExamples  = (TextView) findViewById(R.id.view_examples);

        try
        {
            m_db = cDBHelper.getInstance(this);
            cPattern pattern = m_db.getConcretePattern(patternId);

            vName.setText(pattern.name);
            vForm.setText(pattern.form);
            vDesc.setText(pattern.desc);
            vExamples.setText(pattern.examples);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Integer id = item.getItemId();

        Log.i("Debug", id.toString());

        if (id == android.R.id.home)
        {
            finish();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = null;

        switch (id)
        {
            case R.id.nav_words:
                intent = new Intent(ShowConcretePatternActivity.this, WordsActivity.class);
                break;
            case R.id.nav_irregular_verb:
                intent = new Intent(ShowConcretePatternActivity.this, IrregularVerbsActivity.class);
                break;
            case R.id.nav_add_words:
                 intent = new Intent(ShowConcretePatternActivity.this, AddChooseSectionActivity.class);
                break;
            case R.id.nav_settings:
                intent = new Intent(ShowConcretePatternActivity.this, SettingsActivity.class);
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

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

