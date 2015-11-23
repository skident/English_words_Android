package ua.com.skident.englishwords;

import java.util.TreeMap;

/**
 * Created by skident on 11/22/15.
 */
public class IrregularVerb {
    enum eTags
    {
        id,
        infinitive,
        past_simple,
        past_participle,
        translate
    }

    TreeMap<eTags, String> verb = new TreeMap<>();

    IrregularVerb(String _id, String _infinitive, String _pastSimple, String _pastParticiple, String _translate)
    {
        verb.put(eTags.id, _id);
        verb.put(eTags.infinitive, _infinitive);
        verb.put(eTags.past_participle, _pastParticiple);
        verb.put(eTags.past_simple, _pastSimple);
        verb.put(eTags.translate, _translate);
    }

    String getData(eTags tag)
    {
        return verb.get(tag);
    }
}
