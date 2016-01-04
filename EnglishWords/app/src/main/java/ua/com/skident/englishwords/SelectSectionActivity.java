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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class SelectSectionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
{
    private cDBHelper               m_db            = null;
    private Vector<String>          m_sections      = null;
    private ListView                m_view_sections = null;
    private DrawerLayout            m_drawer        = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_section);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_choose_section_toolbar);
        toolbar.setTitle(R.string.title_section_list);

        // setup drawer
        m_drawer = (DrawerLayout) findViewById(R.id.drawer_layout_add_choose_section);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, m_drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        m_drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try
        {
            m_db = cDBHelper.getInstance(this);
            m_sections = m_db.getSections();

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, new ArrayList(m_sections));

            m_view_sections = (ListView) findViewById(R.id.listSections);
            m_view_sections.setAdapter(arrayAdapter); // Here, you set the data in your ListView

            // Register the ListView  for Context menu
            registerForContextMenu(m_view_sections);

            m_view_sections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(SelectSectionActivity.this, AddActivity.class);

                    String section_name = ((TextView) view).getText().toString();

                    intent.putExtra("section_name", section_name);
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
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        String selected = m_sections.elementAt(info.position);
        Log.i("Debug", selected);

        CharSequence title = item.getTitle();

        if(title.equals(getResources().getString(R.string.context_menu_edit)))
        {

        }
        else if(title.equals(getResources().getString(R.string.context_menu_delete)))
        {

        }
        else
        {
            return false;
        }
        return true;
    }
    ///////////////////////////////////////////////////////////



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
                intent = new Intent(SelectSectionActivity.this, WordsActivity.class);
                break;
            case R.id.nav_irregular_verb:
                intent = new Intent(SelectSectionActivity.this, IrregularVerbsActivity.class);
                break;
            case R.id.nav_add_words:
                 intent = new Intent(SelectSectionActivity.this, SelectSectionActivity.class);
                break;

            case R.id.nav_settings:
                intent = new Intent(SelectSectionActivity.this, SettingsActivity.class);
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

        m_drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

