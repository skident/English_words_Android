package ua.com.skident.englishwords;

import java.util.TreeMap;

/**
 * Created by skident on 11/22/15.
 */
public class cIrregularVerb {
    enum eTags
    {
        id,
        infinitive,
        pastSimple,
        pastParticiple,
        translate,
        isKnown
    }

    TreeMap<eTags, String> verb = new TreeMap<>();

    cIrregularVerb(String _id, String _infinitive, String _pastSimple, String _pastParticiple, String _translate, String _isKnown)
    {
        verb.put(eTags.id, _id);
        verb.put(eTags.infinitive, _infinitive);
        verb.put(eTags.pastParticiple, _pastParticiple);
        verb.put(eTags.pastSimple, _pastSimple);
        verb.put(eTags.translate, _translate);
        verb.put(eTags.isKnown, _isKnown);
    }

    String getData(eTags tag)
    {
        return verb.get(tag);
    }

    void setData(eTags tag, String value)
    {
        verb.put(tag, value);
    }
}
