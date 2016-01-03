package ua.com.skident.englishwords;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by skident on 11/14/15.
 */


public class cIrrVerbContainer {
    public enum eStep
    {
        step_none,
        step_prev,
        step_next
    };

    private boolean m_random = false;
    private LinkedList<cIrregularVerb> myWords;
    private ListIterator<cIrregularVerb> Iterator;
    private eStep prevStep = eStep.step_none;
    private cIrregularVerb currPair = null;


    public cIrrVerbContainer()
    {
        myWords = new LinkedList<cIrregularVerb>();
    }

    public void put(cIrregularVerb verb)
    {
        myWords.add(verb);
    }

    public Integer size()
    {
        return myWords.size();
    }

    public void delCurrVerb()
    {
        Iterator.remove();
    }

    public void setCurrVerbKnownState(Boolean state)
    {
        currPair.setData(cIrregularVerb.eTags.isKnown, state.toString());
        Iterator.set(currPair);
    }

    public cIrregularVerb get(eStep currStep)
    {
        if (size() == 0)
            return null;

        if (Iterator == null)
            Iterator = myWords.listIterator();

        cIrregularVerb pair = null;

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
                currPair = pair = Iterator.next();

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
                currPair = pair = Iterator.previous();

                break;

            case step_none:
                pair = currPair;
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
