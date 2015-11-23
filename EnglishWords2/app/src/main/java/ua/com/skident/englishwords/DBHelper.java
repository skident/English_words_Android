package ua.com.skident.englishwords;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Skident on 07.11.2015.
 */

public class DBHelper extends SQLiteOpenHelper
{
    private static String m_DbPath = "/data/data/ua.com.skident.englishwords/databases/";
    private static String m_DbName = "english_words.db";
    private static String m_TableWords   = "Words";
    private static String m_TableIrregularVerbs   = "IrregularWords";
    private static String m_TableThematics   = "Thematics";
    private final Context m_Context;
    private SQLiteDatabase m_db;
    private Boolean m_isOpened;
    private static DBHelper instance = null;

    enum eWords
    {
        known,
        unknown,
        all
    }


    public static DBHelper getInstance(Context context) throws SQLException {
        if(instance == null)
            instance = new DBHelper(context);
        return instance;
    }

    /**
     * @brief Constructor
     * @param context
     */
    private DBHelper(Context context) throws SQLException {
        super(context, m_DbName, null, 1);
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

        return false;
//        return checkDB != null ? true : false;
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
    public IrregularVerbsContainer getIrregularVerbs(eWords type)
    {
        IrregularVerbsContainer mapWords = new IrregularVerbsContainer(); // first - oroginal, second - translate

        String query = "SELECT _id, infinitive, past_simple, past_participle, translate FROM " + m_TableIrregularVerbs;
        switch (type)
        {
            case known:
                query += " WHERE is_known == 1";
                break;

            case unknown:
                query += " WHERE is_known != 1 OR is_known is null";
                break;

            default:
                break;
        }

        Cursor res =  m_db.rawQuery(query, null);
        res.moveToFirst();

        while(res.isAfterLast() == false)
        {
            String id =  res.getString(0);
            String infinitive = res.getString(1);
            String pastSimple =  res.getString(2);
            String pastParticiple =  res.getString(3);
            String translate = res.getString(4);

            mapWords.put(new IrregularVerb(id, infinitive, pastSimple, pastParticiple, translate));

            res.moveToNext();
        }

        return mapWords;
    }



    /**
     * @brief Select words from table
     * @return Map [key - orogonal, value - translate]
     */
    public WordsContainer getWords(eWords type)
    {
        WordsContainer mapWords = new WordsContainer(); // first - oroginal, second - translate

        String query = "SELECT original, translate, isKnown FROM " + m_TableWords;
        switch (type)
        {
            case known:
                query += " WHERE isKnown == 1";
                break;

            case unknown:
                query += " WHERE isKnown != 1 OR isKnown is null";
                break;

            default:
                break;
        }

        Cursor res =  m_db.rawQuery(query, null);
        res.moveToFirst();

        while(res.isAfterLast() == false)
        {
            String original =  res.getString(0);
            String translate = res.getString(1);
            Integer isKnown = res.getInt(2);
            mapWords.put(original, translate);
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

    private String getDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date date = new Date();
        String sDate = dateFormat.format(date).toString();
        return sDate;
    }


    public void markAsKnown(String original, String translate)
    {
        String date = getDate();
        String query = "UPDATE " + m_TableWords +" SET isKnown=1, learnt_date = '"+date+"' " +
                "WHERE original = '"+original+"' AND  translate = '"+translate+"'";

        Cursor cur = m_db.rawQuery(query, null);
        cur.moveToFirst();
    }

    public Integer getTotal()
    {
        Cursor res =  m_db.rawQuery( "SELECT COUNT(_id) FROM " + m_TableWords, null );
        res.moveToFirst();
        Integer total = res.getInt(0);
        return total;
    }

    public Integer getKnown()
    {
        Cursor res =  m_db.rawQuery( "SELECT COUNT(_id) FROM " + m_TableWords + " WHERE isKnown == 1", null );
        res.moveToFirst();
        Integer total = res.getInt(0);
        return total;
    }

    public Integer getUnknown()
    {
        Cursor res =  m_db.rawQuery("SELECT COUNT(_id) FROM " + m_TableWords + " WHERE isKnown != 1 OR isKnown is null", null );
        res.moveToFirst();
        Integer total = res.getInt(0);
        return total;
    }

    public void AddColumn()
    {
//        String query = "alter table " + m_TableWords + " add column learnt_date TEXT";
//        Cursor res =  m_db.rawQuery(query, null);
//        res.moveToFirst();
//
//        query = "SELECT sql FROM sqlite_master WHERE tbl_name = 'Words' AND type = 'table'";
//        res =  m_db.rawQuery(query, null);
//        res.moveToFirst();
//        String table = res.getString(0);
//        Log.i("debug", table);
    }
}