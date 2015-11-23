package ua.com.skident.englishwords;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by skident on 11/14/15.
 */
public class WordsContainer {
    public enum eStep
    {
        step_none,
        step_prev,
        step_next
    };

    private boolean m_random = false;
    private LinkedList<Pair<String, String>> myWords;
    private ListIterator<Pair<String, String>> Iterator;
    private eStep prevStep = eStep.step_none;


    public WordsContainer()
    {
        myWords = new LinkedList<Pair<String, String>>();
    }

    public void put(String original, String translate)
    {
        myWords.add(new Pair<String, String>(original, translate));
    }

    public int size()
    {
        return myWords.size();
    }

    public void delCurrPair()
    {
        Iterator.remove();
    }

    public Pair<String, String> getPair(eStep currStep)
    {
        if (size() == 0)
            throw new IndexOutOfBoundsException("Have no words");

        if (Iterator == null)
            Iterator = myWords.listIterator();

        Pair<String, String> pair = null;

        switch (currStep) {
            case step_next:
                // Hack: because of incorrect iterator behaviour
                if (Iterator.hasNext())
                {
                    if (prevStep == eStep.step_prev)
                        Iterator.next();
                }

                if (!Iterator.hasNext())
                    Iterator = myWords.listIterator();
                pair = Iterator.next();

                break;

            case step_prev:
                // Hack: because of incorrect iterator behaviour
                if (Iterator.hasPrevious())
                {
                    if (prevStep == eStep.step_next)
                        Iterator.previous();
                }

                if (!Iterator.hasPrevious())
                    Iterator = myWords.listIterator(myWords.size());
                pair = Iterator.previous();

                break;

            default:
                throw new IllegalArgumentException();
        }

        prevStep = currStep;
        return pair;
    }


    public void setRandom(boolean random)
    {
        m_random = random;
        if (m_random)
            Collections.shuffle(myWords);
        Iterator = myWords.listIterator();
    }

    public ArrayList<String> GetOriginals()
    {
        ArrayList<String> set = new ArrayList<String>();
        for (Integer i = 0; i < myWords.size(); i++)
            set.add(myWords.get(i).first.toString());

        return set;
    }
}
