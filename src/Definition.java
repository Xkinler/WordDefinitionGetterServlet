import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

public class Definition {

    // The part of speech that this definition belongs to
    @JSONField(name = "pos", ordinal = 1)
    private String partOfSpeech;

    // All possible definitions for this part of speech
    @JSONField(name = "defs", ordinal = 2)
    private ArrayList<String> definitions;


    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public ArrayList<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(ArrayList<String> definitions) {
        this.definitions = definitions;
    }

    public Definition(String partOfSpeech, ArrayList<String> definitions) {
        this.partOfSpeech = partOfSpeech;
        this.definitions = definitions;
    }
}
