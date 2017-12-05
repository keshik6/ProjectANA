package sutdcreations.classes;

import java.util.ArrayList;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Subject {

    String subjectCode;
    String subjectTitle;
    ArrayList<Topic> topics = new ArrayList<>();
    boolean isLive;

    public Subject(){
        //default constructor for Firebase
    }

    public Subject(String subjectCode, String subjectTitle) {
        this.subjectCode = subjectCode;
        this.subjectTitle = subjectTitle;
    }

    public void addTopic(Topic topic) {
        topic.setKey(getKey()+" "+topic.getTitle());
        topics.add(topic);
    }

    //get database key, for ease in retrieving value from database
    public String getKey(){
        return subjectCode.replace(".","");
    }

    //public getters for Firebase
    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public ArrayList<Topic> getTopics() {
        return topics;
    }

    public boolean isLive() {
        return isLive;
    }

    public boolean getIsLive(){return isLive;}

    public void setIsLive(boolean isLive) {
        this.isLive = isLive;
    }

    public void toggleLive() {
        if (isLive){
            isLive = false;
        }
        else isLive=true;
    }

}