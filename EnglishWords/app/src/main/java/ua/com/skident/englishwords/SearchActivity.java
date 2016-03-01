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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
{
    private Vector<String>          m_sections      = new Vector<>();
    private ListView                m_view_results  = null;
    private Button                  m_button_search = null;
    private EditText                m_field_search  = null;
    private DrawerLayout            m_drawer        = null;
    private cDBHelper               m_db                    = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        toolbar.setTitle(R.string.title_section_list);

        // setup drawer
        m_drawer = (DrawerLayout) findViewById(R.id.search_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, m_drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        m_drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, new ArrayList(m_sections));

        m_view_results = (ListView) findViewById(R.id.list_of_results);
        m_view_results.setAdapter(arrayAdapter); // Here, you set the data in your ListView

        m_button_search = (Button) findViewById(R.id.button_search);
        m_field_search  = (EditText) findViewById(R.id.field_search);

        m_button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = m_field_search.getText().toString();
                m_db.findWord(query);
            }
        });

        try
        {
            m_db = cDBHelper.getInstance(this);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }


        // Register the ListView  for Context menu
        //registerForContextMenu(m_view_results);

        m_view_results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;

                String sectionName = ((TextView) view).getText().toString();
                if (sectionName == "Words") {
                    intent = new Intent(SearchActivity.this, WordsActivity.class);
                } else if (sectionName == "Irregular verbs") {
                    intent = new Intent(SearchActivity.this, IrregularVerbsActivity.class);
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
                intent = new Intent(SearchActivity.this, WordsActivity.class);
                break;
            case R.id.nav_irregular_verb:
                intent = new Intent(SearchActivity.this, IrregularVerbsActivity.class);
                break;
            case R.id.nav_add_words:
                 intent = new Intent(SearchActivity.this, SearchActivity.class);
                break;

            case R.id.nav_settings:
                intent = new Intent(SearchActivity.this, SettingsActivity.class);
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

