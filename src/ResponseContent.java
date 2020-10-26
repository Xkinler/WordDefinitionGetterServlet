import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

public class ResponseContent {

    // The status of the response
    // Possible values:
    // - 200 : The definition is successfully retrieved
    // - 404 : The word cannot be found in the dictionary
    @JSONField(ordinal = 1)
    private int status;

    // The word that is requested
    @JSONField(ordinal = 2)
    private String word;

    // The number of senses of this word
    @JSONField(ordinal = 3, name = "senses")
    private int senseNum;

    @JSONField(ordinal = 4, name = "allPos")
    private ArrayList<ArrayList<String>> allPartOfSpeech;

    // All definitions this word has
    @JSONField(name = "allDefs",ordinal = 5)
    private ArrayList<ArrayList<Definition>> allDefinitions;

    public ResponseContent(String word, ArrayList<ArrayList<String>> allPartOfSpeech, ArrayList<ArrayList<Definition>> allDefinitions) {
        this.allPartOfSpeech = allPartOfSpeech;
        this.status = 200;
        this.word = word;
        this.allDefinitions = allDefinitions;
        if (allDefinitions != null) {
            this.senseNum = allDefinitions.size();
        } else {
            this.senseNum = 0;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getSenseNum() {
        return senseNum;
    }

    public void setSenseNum(int senseNum) {
        this.senseNum = senseNum;
    }

    public ArrayList<ArrayList<String>> getAllPartOfSpeech() {
        return allPartOfSpeech;
    }

    public void setAllPartOfSpeech(ArrayList<ArrayList<String>> allPartOfSpeech) {
        this.allPartOfSpeech = allPartOfSpeech;
    }

    public ArrayList<ArrayList<Definition>> getAllDefinitions() {
        return allDefinitions;
    }

    public void setAllDefinitions(ArrayList<ArrayList<Definition>> allDefinitions) {
        this.allDefinitions = allDefinitions;
    }

    // This constructor is designed to be used only when status is 404,
    // !!DON'T USE THIS CONSTRUCTOR WHEN STATUS IS 200!!
    public ResponseContent(String word,int status) throws Exception {
        this(word, null, null);
        this.status = status;

        if (status == 200) {
            throw new Exception("Constructor ResponseContent(int status, String word) is not supposed to be used when status is 200");
        }

    }
}
