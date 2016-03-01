package ua.com.skident.englishwords;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class SelectWordThematicActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
{
    cDBHelper m_db;
    TreeMap<String, String> m_thematics;
    ListView m_view_words_known;
    private DrawerLayout drawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_theme);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_thematic_list);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        try
        {
            m_db = cDBHelper.getInstance(this);
            m_thematics = m_db.getThematics();

            ArrayAdapter arrayAdapter =
                    new ArrayAdapter(this, android.R.layout.simple_list_item_1, new ArrayList(m_thematics.keySet()));


            m_view_words_known = (ListView) findViewById(R.id.known);
            if (m_view_words_known != null)
                m_view_words_known.setAdapter(arrayAdapter); // Here, you set the data in your ListView

            m_view_words_known.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(SelectWordThematicActivity.this, WordsActivity.class);

                    String thema = ((TextView) view).getText().toString();
                    String thema_id = m_thematics.get(thema);

                    intent.putExtra("thema_id", thema_id);
                    intent.putExtra("thema", thema);
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
    public void onClick(View v) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = null;

        switch (id)
        {
             case R.id.nav_words:
                 intent = new Intent(SelectWordThematicActivity.this, WordsActivity.class);
                 break;
             case R.id.nav_irregular_verb:
                 intent = new Intent(SelectWordThematicActivity.this, IrregularVerbsActivity.class);
                 break;
//            case R.id.nav_patterns:
//                intent = new Intent(SelectWordThematicActivity.this, SelectPatternActivity.class);
//                break;
            case R.id.nav_add_words:
                 intent = new Intent(SelectWordThematicActivity.this, SelectSectionActivity.class);
                 break;
             case R.id.nav_settings:
                 intent = new Intent(SelectWordThematicActivity.this, SettingsActivity.class);
                 break;
             case R.id.nav_about:
//                 intent = new Intent(SelectWordThematicActivity.this, SettingsActivity.class);
                 break;
             case R.id.nav_contacts:
//                 intent = new Intent(SelectWordThematicActivity.this, SettingsActivity.class);
                 break;
             default:

        }

        if (intent != null)
            startActivity(intent);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
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
}
