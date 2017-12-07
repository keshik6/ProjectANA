package sutdcreations.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Subject {

    String subjectCode;
    String subjectTitle;
    ArrayList<Topic> topics = new ArrayList<>();
    static ArrayList<Topic> allTopics = new ArrayList<>();
    boolean isLive;
    static Map<String,Integer> totalStudentsByCourse = new HashMap<>();
    int totalStudents = 0;

    public Subject(){
        //default constructor for Firebase
    }

    public Subject(String subjectCode, String subjectTitle) {
        this.subjectCode = subjectCode;
        this.subjectTitle = subjectTitle;
        totalStudentsByCourse.put(subjectCode,0);
    }

    public void addTopic(Topic topic) {
        topic.setKey(getKey()+" "+topic.getTitle());
        topics.add(topic);
        allTopics.add(topic);
    }

    public void addStudent(String key){
        if (totalStudentsByCourse.containsKey(key)){
            totalStudentsByCourse.put(key,totalStudentsByCourse.get(key) + 1);
        }
        else{
            totalStudentsByCourse.put(key,1);
        }
    }

    public static double getTotalStudents(String key){
        if (totalStudentsByCourse.containsKey(key)){
            return totalStudentsByCourse.get(key);
        }
        return -1;

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