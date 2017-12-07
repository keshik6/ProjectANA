package sutdcreations.classes;

/**
 * Created by Beng Haun on 2/12/2017.
 */
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student extends User {
    int replies_count;
    int questions_count;
    double participation;
    Map<String,Integer> questionsMap = new HashMap<>();  //Subjects and no of questions asked
    Map<String,Integer> answersMap = new HashMap<>();    //Subjects and answers added
    Map<String,Double> scoresQn  = new HashMap<>();
    Map<String,Double> scoresAns  = new HashMap<>();
    Map<String,Double> finalGrade = new HashMap<>();

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

    public String calculateScore() {
        Map<String, Integer> qnsForCourses = new HashMap<>();
        for (Topic t : Subject.allTopics) {
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
            double mean = (qnsForCourses.get(courseCode)) / (Subject.getTotalStudents(courseCode));
            double val = questionsMap.get(courseCode) - mean;
            scoresQn.put(courseCode, val);
        }


        //Get answer score
        Map<String,Integer> ansForCourses = new HashMap<>();
        for (Topic t: Subject.allTopics){
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
                    ansForCourses.put(t.getKey(),answers);
                }
            }
        }

        for (String courseCode: answersMap.keySet()){
            double mean = (ansForCourses.get(courseCode))/(Subject.getTotalStudents(courseCode));
            double val = answersMap.get(courseCode) - mean;
            scoresAns.put(courseCode,val);
        }


        //Calculate final grade
        for (String courseCode: answersMap.keySet()){
            finalGrade.put(courseCode,(scoresAns.get(courseCode) + scoresQn.get(courseCode))/2.0);
        }
        //All the answers are stored in the finalGrade hash map


        //Update this to beng haun's code
        String grade = "";
        for (String courseCode: finalGrade.keySet()){
            grade += courseCode + "\t" + finalGrade.get(courseCode);
            if (!courseCode.equals(finalGrade.keySet().equals(courseCode))){
                grade += "\n";
            }
        }
        return grade;
     }

    //public getters and setters for Firebase
    public double getParticipation(){return participation;}
    public Map<String, Integer> getquestionsMap() {
        return questionsMap;
    }

    public void setquestionsMap(Map<String, Integer> questionsMap) {
        this.questionsMap = questionsMap;
    }

    public Map<String, Integer> getanswersMap() {
        return answersMap;
    }

    public void setanswersMap(Map<String, Integer> answersMap) {
        this.answersMap = answersMap;
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

}