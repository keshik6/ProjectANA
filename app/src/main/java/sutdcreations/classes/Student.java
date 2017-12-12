package sutdcreations.classes;

/**
 * Created by Beng Haun on 2/12/2017.
 */
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student extends User {
    double participation;
    int student_id;
    int replies_count;
    int questions_count;
    Map<String,Integer> questionsMap = new HashMap<>();  //Subjects and no of questions asked
    Map<String,Integer> answersMap = new HashMap<>();    //Subjects and answers added
    Map<String,Double> scoresQn  = new HashMap<>();
    Map<String,Double> scoresAns  = new HashMap<>();
    Map<String,Double> finalGrade = new HashMap<>();
    ArrayList<String> notifications = new ArrayList<>(); //list of notifications for student

    public Student(){
        super();
        //public constructor for Firebase
    }
    public Student(String email_add, String uid) {
        super(email_add, uid);
    }

    public void addSubject(Subject subject){
        subjects.add(subject);
        //initialize subject
        questionsMap.put(subject.getKey(),0);
        answersMap.put(subject.getKey(),0);
        subject.addStudent(subject.getKey());
    }



    public void addNotification(String notification){
        Log.i("notifDebug", "Notification added");
        notifications.add(notification);
    }

    public void removeNotification(String notification){
        notifications.remove(notification);
    }

    //Getter and setter for questions_count
    public int getTotalQuestionsAsked(){
        questions_count = 0;
        for (Integer s: questionsMap.values()){
            questions_count += s;
        }
        return questions_count;
    }

    public int getTotalReplies(){
        replies_count = 0;
        for (Integer s: answersMap.values()) {
            replies_count += s;
        }
        return replies_count;
    }

    //Calculate grades for the courses individually using mean and standard deviation
    //In fact we are getting the average of the z scores of questions and answers
    String gradeTeacher = "";
    public String calculateScore(ArrayList<Topic> allTopics,String type) {
        Map<String, Integer> qnsForCourses = new HashMap<>();
        for (Topic t : allTopics) {
            String key = t.getKey().split(" ")[0];
            if (questionsMap.containsKey(key)) {
                if (qnsForCourses.containsKey(key)){
                    qnsForCourses.put(key, qnsForCourses.get(key) + t.getQuestions().size());
                }

                else{
                    qnsForCourses.put(key, t.getQuestions().size());
                }
            }
        }

        for (String courseCode : questionsMap.keySet()) {
            //double mean = (qnsForCourses.get(courseCode)) / (Subject.getTotalStudents(courseCode));
            double val = questionsMap.get(courseCode);
            scoresQn.put(courseCode, val);
            gradeTeacher += courseCode.substring(0,2) + "." + courseCode.substring(2,courseCode.length()) + ":\t";
            gradeTeacher += questionsMap.get(courseCode);

            if (!courseCode.equals(questionsMap.keySet().equals(courseCode))){
                gradeTeacher += "\n";
            }
        }


        //Get answer score
        Map<String,Integer> ansForCourses = new HashMap<>();
        for (Topic t: allTopics){
            String key = t.getKey().split(" ")[0];
            if (answersMap.containsKey(key)){
                ArrayList<Question> qns = t.getQuestions();
                int answers = 0;
                for (Question q: qns){
                    answers += q.getAnswersTotal();
                }

                if (ansForCourses.containsKey(key)){
                    ansForCourses.put(key,ansForCourses.get(key)+ answers);
                }
                else{
                    ansForCourses.put(key,answers);
                }
            }
        }

        gradeTeacher += "|";
        for (String courseCode: answersMap.keySet()){
            Log.i("keshik",String.valueOf(ansForCourses.get(courseCode)));
            //double mean = (ansForCourses.get(courseCode))/(Subject.getTotalStudents(courseCode));
            double val = answersMap.get(courseCode);
            scoresAns.put(courseCode,val);
            gradeTeacher += courseCode.substring(0,2) + "." + courseCode.substring(2,courseCode.length()) + ":\t";
            gradeTeacher += answersMap.get(courseCode);
            if (!courseCode.equals(answersMap.keySet().equals(courseCode))){
                gradeTeacher += "\n";
            }
        }


        //Calculate final grade
        for (String courseCode: answersMap.keySet()){
            finalGrade.put(courseCode,(scoresAns.get(courseCode) + scoresQn.get(courseCode))/2.0);
        }
        //All the answers are stored in the finalGrade hash map


        //Update this to beng haun's code
        String gradeStd = "";

        for (String courseCode: finalGrade.keySet()){
            gradeStd += courseCode.substring(0,2) + "." + courseCode.substring(2,courseCode.length()) + ":\t";
            if (finalGrade.get(courseCode) >= 15){
                gradeStd += "A";
            }
            else if (finalGrade.get(courseCode) >= 10){
                gradeStd += "B";
            }
            else if (finalGrade.get(courseCode) >= 5){
                gradeStd += "C";
            }
            else{
                gradeStd += "D";
            }

            if (!courseCode.equals(finalGrade.keySet().equals(courseCode))){
                gradeStd += "\n";
            }
        }

        if (type.equals("Teacher")){
            return gradeTeacher;
        }
        return gradeStd;
    }

    //public getters and setters for Firebase
    public double getParticipation(){return participation;}
    public Map<String, Integer> getQuestionMap() {
        return questionsMap;
    }

    public void setQuestionMap(Map<String, Integer> questionMap) {
        this.questionsMap = questionMap;
    }

    public Map<String, Integer> getAnswerMap() {
        return answersMap;
    }

    public void setAnswerMap(Map<String, Integer> answerMap) {
        this.answersMap = answerMap;
    }

    public Map<String, Double> getScoresQn() {
        return scoresQn;
    }

    public void setScoresQn(Map<String, Double> scoresQn) {
        this.scoresQn = scoresQn;
    }

    public Map<String, Double> getScoresAns() {
        return scoresAns;
    }

    public void setScoresAns(Map<String, Double> scoresAns) {
        this.scoresAns = scoresAns;
    }

    public Map<String, Double> getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Map<String, Double> finalGrade) {
        this.finalGrade = finalGrade;
    }

    public Map<String, Integer> getQuestionsMap() {
        return questionsMap;
    }

    public void setQuestionsMap(Map<String, Integer> questionsMap) {
        this.questionsMap = questionsMap;
    }

    public Map<String, Integer> getAnswersMap() {
        return answersMap;
    }

    public void setAnswersMap(Map<String, Integer> answersMap) {
        this.answersMap = answersMap;
    }

    public int getReplies_count() {

        return replies_count;
    }

    public void setReplies_count(int replies_count) {
        this.replies_count = replies_count;
    }

    public int getQuestions_count() {
        return questions_count;
    }

    public void setQuestions_count(int questions_count) {
        this.questions_count = questions_count;
    }

    public ArrayList<String> getNotifications() {

        return notifications;
    }

    public void setNotifications(ArrayList<String> notifications) {
        this.notifications = notifications;
    }

    public int getStudent_id(){
        return student_id;
    }

    public void setStudent_id(int student_id){
        this.student_id = student_id;
    }



}