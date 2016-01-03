package ua.com.skident.englishwords;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by Skident on 07.11.2015.
 */

public class cDBHelper extends SQLiteOpenHelper
{
    private static String m_DbPath = "/data/data/ua.com.skident.englishwords/databases/";
    private static String m_DbName = "english_words.db";

    // Tables
    private static String m_TableWords              = "Words";
    private static String m_TableIrregularVerbs     = "IrregularVerbs";
    private static String m_TablePatterns           = "Patterns";
    private static String m_TableSections           = "Sections";
    private static String m_TableThematics          = "Thematics";

    // Db variables
    private final Context m_Context;
    private SQLiteDatabase m_db;
    private Boolean m_isOpened;

    private static cDBHelper instance = null;

    enum eWordsStatus
    {
        known,
        unknown,
        all
    }

    enum eWordType
    {
        type_irregular,
        type_common
    }


    /**
     * @brief returns instance on cDBHelper class
     * @param context
     * @throws SQLException
     */
    public static cDBHelper getInstance(Context context) throws SQLException
    {
        if(instance == null)
            instance = new cDBHelper(context);
        return instance;
    }

    /**
     * @brief Constructor - init object and open database
     * @param context
     */
    private cDBHelper(Context context) throws SQLException
    {
        super(context, m_DbName, null, 1); // init parent
        m_Context = context;
        m_isOpened = false;

        openDataBase();
    }

    /**
     * @brief Create new database if it doesn't exist
     * @throws IOException
     */
    private void createDataBase() throws IOException
    {
        boolean dbExist = checkDataBase();

        if (!dbExist)
        {
            // create new database for replace it in future
            this.getReadableDatabase();

            try
            {
                copyDataBase();
            }
            catch (IOException e)
            {
                throw new Error("Error copying database");
            }
        }
    }


    /**
     * @brief Check if database already exists.
     * @return true - exists, false - not exists
     */
    private boolean checkDataBase()
    {
        SQLiteDatabase checkDB = null;

        try
        {
            String myPath = m_DbPath + m_DbName;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch(SQLiteException e)
        {
            //база еще не существует
        }

        if(checkDB != null)
            checkDB.close();

//        return false;
        return checkDB != null ? true : false;
    }

    /**
     * @brief Copy DB from folder assets to internal app folder on flash drive
     * @throws IOException
     */
    private void copyDataBase() throws IOException
    {
        //Open local DB as stream
//        InputStream myInput = m_Context.getAssets().open(m_DbName);
        InputStream myInput = m_Context.getAssets().open(m_DbName);

        //Path to new DB
        String outFileName = m_DbPath + m_DbName;
        OutputStream myOutput = null;
        try
        {
            //Open empty DB
            myOutput = new FileOutputStream(outFileName);
        }
        catch (Exception ex)
        {
            Log.e("dbg", ex.getMessage());
        }

        //Copying databases
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0)
        {
            myOutput.write(buffer, 0, length);
        }

        //закрываем потоки
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * @brief Open local DB
     * @throws SQLException
     */
    public void openDataBase() throws SQLException
    {
        // DB already opened
        if (m_isOpened)
            return;

        try
        {
            createDataBase();
            String myPath = m_DbPath + m_DbName;
            m_db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            m_isOpened = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            Log.e("dbg", "Open database error");
        }
    }

    @Override
    public synchronized void close()
    {
        if(m_db != null)
            m_db.close();
        super.close();
        m_isOpened = false;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }




    // SPECIAL METHODS BLOCKS

    /**
     * @brief Select words from table
     * @return Map [key - orogonal, value - translate]
     */
    public cIrrVerbContainer getIrregularVerbs(eWordsStatus status)
    {
        // Container for irregular verbs
        cIrrVerbContainer container = new cIrrVerbContainer();

        // prepare query
        String query = "SELECT _id, infinitive, past_simple, past_participle, translate, is_known " +
                        "FROM " + m_TableIrregularVerbs;
        switch (status)
        {
            case known:
                query += " WHERE is_known == 'true'";
                break;

            case unknown:
                query += " WHERE is_known != 'true' OR is_known is null";
                break;

            default:
                break;
        }

        Cursor res =  m_db.rawQuery(query, null); // do query
        res.moveToFirst();

        // select records one by one
        while(res.isAfterLast() == false)
        {
            String id               = res.getString(0);
            String infinitive       = res.getString(1);
            String pastSimple       = res.getString(2);
            String pastParticiple   = res.getString(3);
            String translate        = res.getString(4);
            String isKnown          = res.getString(5);
            if (isKnown == null)
                isKnown = "false";

            container.put(new cIrregularVerb(id, infinitive, pastSimple, pastParticiple, translate, isKnown));

            res.moveToNext();
        }

        return container;
    }



    /**
     * @brief Select words from table
     * @return Map [key - orogonal, value - translate]
     */
    public cWordContainer getWords(eWordsStatus type)
    {
        cWordContainer mapWords = new cWordContainer(); // first - oroginal, second - translate

        String query = "SELECT _id, original, translate, isKnown FROM " + m_TableWords;
        switch (type)
        {
            case known:
                query += " WHERE isKnown == 'true'";
                break;

            case unknown:
                query += " WHERE isKnown != 'true' OR isKnown is null";
                break;

            default:
                break;
        }

//        query += " LIMIT 100";
        Cursor res =  m_db.rawQuery(query, null);
        res.moveToFirst();

        while(res.isAfterLast() == false)
        {
            cSimpleWord word = new cSimpleWord(); // new object for every word
            word.id             = res.getInt(0);
            word.original      = res.getString(1);
            word.translate     = res.getString(2);
            String isKnown     = res.getString(3);
            if (isKnown != null && isKnown.equals("true"))
                word.isKnown = true;
            else
                word.isKnown = false;

            mapWords.put(word);
            res.moveToNext();
        }

        return mapWords;
    }

    /**
     * @brief Select words from table
     * @return Map [key - thema, value - id]
     */
    public TreeMap<String, String> getThematics()
    {
        TreeMap<String, String> thematics = new TreeMap<>();

        String query = "SELECT _id, thematic FROM " + m_TableThematics;
        Cursor res =  m_db.rawQuery(query, null);
        res.moveToFirst();

        while(res.isAfterLast() == false)
        {
            String id =  res.getString(0);
            String thematic = res.getString(1);
            thematics.put(thematic, id);
            res.moveToNext();
        }

        return thematics;
    }


    /**
     * @brief Select sections from table
     * @return Map [key - id, value - section name]
     */
//    public TreeMap<String, String> getSections()
    public Vector<String> getSections()
    {
//        TreeMap<String, String> sections = new TreeMap<>();
        Vector<String> sections = new Vector<>();

        String query = "SELECT _id, name FROM " + m_TableSections;
        Cursor res =  m_db.rawQuery(query, null);
        res.moveToFirst();

        while(res.isAfterLast() == false)
        {
            String id =  res.getString(0);
            String section = res.getString(1);
//            sections.put(section, id);
            sections.add(section);
            res.moveToNext();
        }

        return sections;
    }

    private String getDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date date = new Date();
        String sDate = dateFormat.format(date).toString();
        return sDate;
    }


    public void setVerbState(String id, Boolean state)
    {
//        String date = getDate();
        String query = "UPDATE " + m_TableIrregularVerbs +" SET is_known='" + state.toString() + "' WHERE _id == " + id;
        Log.i("Query: ", query);
        Cursor cur = m_db.rawQuery(query, null);
        cur.moveToFirst();
    }

    public void setSimpleWordState(Integer id, Boolean state)
    {
        String date = getDate();
        String query = "UPDATE " + m_TableWords +" SET isKnown='"+state.toString()+"', learnt_date = '"+date+"' " +
                "WHERE _id = '"+id.toString()+"'";

        Cursor cur = m_db.rawQuery(query, null);
        cur.moveToFirst();
    }

    public Integer getTotalCount(eWordType type)
    {
        String query = "SELECT COUNT(_id) FROM " + m_TableWords;
        if (type == eWordType.type_irregular)
            query = "SELECT COUNT(_id) FROM " + m_TableIrregularVerbs;

        Cursor res =  m_db.rawQuery(query, null);
        res.moveToFirst();
        Integer total = res.getInt(0);
        return total;
    }

    public Integer getKnownCount(eWordType type)
    {
        String query = "SELECT COUNT(_id) FROM " + m_TableWords + " WHERE isKnown == 'true'";
        if (type == eWordType.type_irregular)
            query = "SELECT COUNT(_id) FROM " + m_TableIrregularVerbs + " WHERE is_known == 'true'";

        Cursor res =  m_db.rawQuery(query, null);
        res.moveToFirst();
        Integer total = res.getInt(0);
        return total;
    }

    public Integer getUnknownCount(eWordType type)
    {
        String query = "SELECT COUNT(_id) FROM " + m_TableWords + " WHERE isKnown != 'true' OR isKnown is null";
        if (type == eWordType.type_irregular)
            query = "SELECT COUNT(_id) FROM " + m_TableIrregularVerbs + " WHERE is_known != 'true' OR is_known is null";

        Cursor res =  m_db.rawQuery(query, null);
        res.moveToFirst();
        Integer total = res.getInt(0);
        return total;
    }

    public Boolean addVerb(cIrregularVerb verb)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put("infinitive",         verb.getData(cIrregularVerb.eTags.infinitive));
        initialValues.put("past_simple",        verb.getData(cIrregularVerb.eTags.pastSimple));
        initialValues.put("past_participle",    verb.getData(cIrregularVerb.eTags.pastParticiple));
        initialValues.put("translate", verb.getData(cIrregularVerb.eTags.translate));
        initialValues.put("is_known", "false");
        long id = m_db.insert(m_TableIrregularVerbs, null, initialValues);

        return (id == -1 ? false : true);
    }

    public Boolean addSimpleWord(Pair<String, String> word)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put("thematic_id",    "0");
        initialValues.put("original",       word.first);
        initialValues.put("translate",      word.second);
        initialValues.put("isKnown",        "false");
        initialValues.put("isFavorite", "false");

        long id = m_db.insert(m_TableWords, null, initialValues);

        return (id == -1 ? false : true);
    }

    public Boolean addPattern(String title, String form, String desc, String examples)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put("title",          title);
        initialValues.put("form",           form);
        initialValues.put("description",    desc);
        initialValues.put("examples",       examples);

        long id = m_db.insert(m_TablePatterns, null, initialValues);

        return (id == -1 ? false : true);
    }



    /**
     * @brief Select all patterns from table
     * @return Map [key - id, value - pattern name]
     */
    public TreeMap<String, String> getPatterns()
    {
        TreeMap<String, String> patternsList = new TreeMap<>();

        String query = "SELECT _id, title FROM " + m_TablePatterns;
        Cursor res =  m_db.rawQuery(query, null);
        res.moveToFirst();

        while(res.isAfterLast() == false)
        {
            String id =  res.getString(0);
            String pattern = res.getString(1);
            patternsList.put(pattern, id);
            res.moveToNext();
        }

        return patternsList;
    }

    public cPattern getConcretePattern(String patternId)
    {
        cPattern pattern = new cPattern();

        String query = "SELECT _id, title, form, description, examples FROM " + m_TablePatterns
                + " WHERE _id = "+patternId+" LIMIT 1";
        Cursor res =  m_db.rawQuery(query, null);
        res.moveToFirst();

        while(res.isAfterLast() == false)
        {
            pattern.id =  res.getInt(0);
            pattern.name = res.getString(1);
            pattern.form = res.getString(2);
            pattern.desc = res.getString(3);
            pattern.examples = res.getString(4);
            res.moveToNext();
        }

        return pattern;
    }

}