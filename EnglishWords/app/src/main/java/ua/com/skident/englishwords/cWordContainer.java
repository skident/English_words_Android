package ua.com.skident.englishwords;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by skident on 11/14/15.
 */
public class cWordContainer
{
    public enum eStep
    {
        step_none,
        step_prev,
        step_next
    };

    private Boolean m_random = false;
    private LinkedList<cSimpleWord> myWords = null;
    private ListIterator<cSimpleWord> Iterator = null;
    private cSimpleWord currWord = null;
    private eStep prevStep = eStep.step_none;


    public cWordContainer()
    {
        myWords = new LinkedList<cSimpleWord>();
    }

    public void put(cSimpleWord word)
    {
        myWords.add(word);
    }

    public Integer size()
    {
        return myWords.size();
    }

    public void removeCurrWord()
    {
        Iterator.remove();
    }

    public void setKnownState(Boolean state)
    {
        currWord.isKnown = state;
        Iterator.set(currWord);
    }

    public cSimpleWord get(eStep currStep)
    {
        if (size() == 0)
            return null;

        if (Iterator == null)
            Iterator = myWords.listIterator();

        cSimpleWord pair = null;

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
                currWord = pair = Iterator.next();

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
                currWord = pair = Iterator.previous();

                break;

            case step_none:
                pair = currWord;
                currStep = prevStep; // HACK for next step
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
}
