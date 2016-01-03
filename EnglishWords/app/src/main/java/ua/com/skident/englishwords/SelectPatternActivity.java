package ua.com.skident.englishwords;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

public class SelectPatternActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
{
    private cDBHelper m_db = null;
    private TreeMap<String, String> m_patterns = null;
    private ListView m_view_patterns = null;
    private DrawerLayout drawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pattern);

        Toolbar toolbar = (Toolbar) findViewById(R.id.select_pattern_toolbar);
        toolbar.inflateMenu(R.menu.menu_pattern);
        toolbar.setTitle(R.string.title_pattern_list);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_pattern_add:
                        Intent intent = new Intent(SelectPatternActivity.this, AddActivity.class);
                        intent.putExtra("section_name", "Patterns");
                        startActivity(intent);
                        break;

                    case R.id.menu_pattern_refresh:
                        UpdatePatternsList();
                        break;
                }
                return true;
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_select_pattern);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton refresh = (FloatingActionButton) findViewById(R.id.button_refresh);
        refresh.setOnClickListener(this);

        UpdatePatternsList();
    }

    private void UpdatePatternsList()
    {
        try
        {
            m_db = cDBHelper.getInstance(this);
            m_patterns = m_db.getPatterns();

            ArrayAdapter arrayAdapter =
                    new ArrayAdapter(this, android.R.layout.simple_list_item_1, new ArrayList(m_patterns.keySet()));

            m_view_patterns = (ListView) findViewById(R.id.list_patterns);
            m_view_patterns.setAdapter(arrayAdapter); // Here, you set the data in your ListView

            m_view_patterns.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(SelectPatternActivity.this, ShowConcretePatternActivity.class);

                    String pattern_name = ((TextView) view).getText().toString();
                    String pattern_id = m_patterns.get(pattern_name);

                    intent.putExtra("pattern_id", pattern_id);
                    intent.putExtra("pattern_name", pattern_name);
                    startActivity(intent);
                }
            });

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_refresh:
                UpdatePatternsList();
                break;

            default:
                break;
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
                intent = new Intent(SelectPatternActivity.this, WordsActivity.class);
                break;
            case R.id.nav_irregular_verb:
                intent = new Intent(SelectPatternActivity.this, IrregularVerbsActivity.class);
                break;
            case R.id.nav_add_words:
                 intent = new Intent(SelectPatternActivity.this, AddChooseSectionActivity.class);
                break;
            case R.id.nav_settings:
                intent = new Intent(SelectPatternActivity.this, SettingsActivity.class);
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

