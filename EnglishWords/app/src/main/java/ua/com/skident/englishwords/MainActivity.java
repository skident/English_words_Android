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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
{
    private Vector<String>          m_sections      = new Vector<>();
    private ListView                m_view_sections = null;
    private DrawerLayout            m_drawer        = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(R.string.title_section_list);

        // setup drawer
        m_drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, m_drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        m_drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        m_sections.add(0, "Words");
        m_sections.add(1, "Irregular verbs");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, new ArrayList(m_sections));

        m_view_sections = (ListView) findViewById(R.id.list_of_variants);
        m_view_sections.setAdapter(arrayAdapter); // Here, you set the data in your ListView

        // Register the ListView  for Context menu
        registerForContextMenu(m_view_sections);

        m_view_sections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;

                String sectionName = ((TextView) view).getText().toString();
                if (sectionName == "Words")
                {
                    intent = new Intent(MainActivity.this, WordsActivity.class);
                }
                else if (sectionName == "Irregular verbs")
                {
                    intent = new Intent(MainActivity.this, IrregularVerbsActivity.class);
                }

                startActivity(intent);
            }
            });
    }

    @Override
    public void onClick(View v)
    {

    }


    ///////////////////////////////////////////////////////////
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;

        menu.setHeaderTitle(m_sections.elementAt(info.position));
        menu.add(0, v.getId(), 0, R.string.context_menu_edit);
        menu.add(0, v.getId(), 0, R.string.context_menu_delete);
    }


    @Override
    public void onBackPressed()
    {
        if (m_drawer.isDrawerOpen(GravityCompat.START))
        {
            m_drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
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
                intent = new Intent(MainActivity.this, WordsActivity.class);
                break;
            case R.id.nav_search_item:
                intent = new Intent(MainActivity.this, SearchActivity.class);
                break;
            case R.id.nav_irregular_verb:
                intent = new Intent(MainActivity.this, IrregularVerbsActivity.class);
                break;
            case R.id.nav_add_words:
                 intent = new Intent(MainActivity.this, MainActivity.class);
                break;
            case R.id.nav_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
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

        m_drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

