package ua.com.skident.englishwords;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.sql.SQLException;

public class AddActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
{
    private cDBHelper m_db = null;
    private DrawerLayout    drawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_toolbar);
        String sectionName = getIntent().getExtras().getString("section_name");

        LinearLayout layout = null;
        switch (sectionName)
        {
            case "Words":
                layout = (LinearLayout) findViewById(R.id.new_irregular_verb);
                layout.setVisibility(View.INVISIBLE);

                layout = (LinearLayout) findViewById(R.id.new_word);
                layout.setVisibility(View.VISIBLE);

                layout = (LinearLayout) findViewById(R.id.new_pattern);
                layout.setVisibility(View.INVISIBLE);

                toolbar.setTitle(R.string.title_add_word);
                break;

            case "Irregular Verbs":
                layout = (LinearLayout) findViewById(R.id.new_irregular_verb);
                layout.setVisibility(View.VISIBLE);

                layout = (LinearLayout) findViewById(R.id.new_word);
                layout.setVisibility(View.INVISIBLE);

                layout = (LinearLayout) findViewById(R.id.new_pattern);
                layout.setVisibility(View.INVISIBLE);

                toolbar.setTitle(R.string.title_add_verb);
                break;

            case "Patterns":
                layout = (LinearLayout) findViewById(R.id.new_irregular_verb);
                layout.setVisibility(View.INVISIBLE);

                layout = (LinearLayout) findViewById(R.id.new_word);
                layout.setVisibility(View.INVISIBLE);

                layout = (LinearLayout) findViewById(R.id.new_pattern);
                layout.setVisibility(View.VISIBLE);

                toolbar.setTitle(R.string.title_add_pattern);
                break;
        }
        Log.i("Sected name: ", sectionName);

//        toolbar.inflateMenu(R.menu.menu_irregular); // todo change

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_add);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Button buttonAdd = null;
        buttonAdd = (Button) findViewById(R.id.button_add_irregular);
        buttonAdd.setOnClickListener(this);

        buttonAdd = (Button) findViewById(R.id.button_add_word);
        buttonAdd.setOnClickListener(this);

        buttonAdd = (Button) findViewById(R.id.button_add_pattern);
        buttonAdd.setOnClickListener(this);

        try
        {
            m_db = cDBHelper.getInstance(this);
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
            case R.id.button_add_word:
                addSimpleWord();
                break;

            case R.id.button_add_irregular:
                addIrregularVerb();
                break;

            case R.id.button_add_pattern:
                addPattern();
                break;
        }
    }

    private void addSimpleWord()
    {
        String info = "";
        EditText vOriginal         = (EditText) findViewById(R.id.word_original);
        EditText vTranslate         = (EditText) findViewById(R.id.word_translate);

        // get data
        String original       = vOriginal.getText().toString();
        String translate       = vTranslate.getText().toString();

        // check are all fields filled
        if (original.isEmpty() || translate.isEmpty())
        {
            info = "Please fill all fields";
        }
        else
        {
            // Insert into DB
            Pair<String, String> simpleWord = new Pair<>(original, translate);
            Boolean res = m_db.addSimpleWord(simpleWord);

            // check result of insert
            if (res) {
                info = "Word added sucessfully";
                // clear after successfully adding
                vOriginal.setText("");
                vTranslate.setText("");
            } else {
                info = "Error: Word doesn't added. Please check all fields!";
            }
        }

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, info, duration).show();
    }

    private void addIrregularVerb()
    {
        String info = "";

        // get handles
        EditText vInfinitive        = (EditText) findViewById(R.id.infinitive);
        EditText vPastSimple        = (EditText) findViewById(R.id.past_simple);
        EditText vPastParticiple    = (EditText) findViewById(R.id.past_participle);
        EditText vTranslate         = (EditText) findViewById(R.id.verb_translate);

        // get data
        String infinitive       = vInfinitive.getText().toString();
        String pastSimple       = vPastSimple.getText().toString();
        String pastParticiple   = vPastParticiple.getText().toString();
        String translate        = vTranslate.getText().toString();

        // check are all fields filled
        if (infinitive.isEmpty() || pastSimple.isEmpty() || pastParticiple.isEmpty() || translate.isEmpty())
        {
            info = "Please fill all fields";
        }
        else {
            // Insert into DB
            cIrregularVerb verb = new cIrregularVerb("0", infinitive, pastSimple, pastParticiple, translate, "false");
            Boolean res = m_db.addVerb(verb);

            // check result of insert
            if (res) {
                info = "Verb added sucessfully";
                // clear after successfully adding
                vInfinitive.setText("");
                vPastSimple.setText("");
                vPastParticiple.setText("");
                vTranslate.setText("");
            } else {
                info = "Error: Verb doesn't added. Please check all fields!";
            }
        }

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, info, duration).show();
    }

    private void addPattern()
    {
        String info = "";

        // get handles
        EditText vTitle         = (EditText) findViewById(R.id.pattern_title);
        EditText vForm          = (EditText) findViewById(R.id.pattern_form);
        EditText vDesc          = (EditText) findViewById(R.id.pattern_desc);
        EditText vExamples      = (EditText) findViewById(R.id.pattern_examples);

        // get data
        String title            = vTitle.getText().toString();
        String form             = vForm.getText().toString();
        String desc             = vDesc.getText().toString();
        String examples         = vExamples.getText().toString();

        // check are all fields filled
        if (title.isEmpty() || form.isEmpty() || desc.isEmpty() || examples.isEmpty())
        {
            info = "Please fill all fields";
        }
        else
        {
            // Insert into DB
            Boolean res = m_db.addPattern(title, form, desc, examples);

            // check result of insert
            if (res) {
                info = "cPattern added sucessfully";
                // clear after successfully adding
                vTitle.setText("");
                vForm.setText("");
                vDesc.setText("");
                vExamples.setText("");
            } else {
                info = "Error: cPattern doesn't added. Please check all fields!";
            }
        }

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, info, duration).show();
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
                intent = new Intent(AddActivity.this, WordsActivity.class);
                break;
            case R.id.nav_irregular_verb:
                intent = new Intent(AddActivity.this, IrregularVerbsActivity.class);
                break;
            case R.id.nav_add_words:
                 intent = new Intent(AddActivity.this, AddChooseSectionActivity.class);
                break;
            case R.id.nav_settings:
                intent = new Intent(AddActivity.this, SettingsActivity.class);
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

