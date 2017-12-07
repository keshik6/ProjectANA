package sutdcreations.classes;

/**
 * Created by Beng Haun on 2/12/2017.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student extends User {
    double participation;
    Map<String,Integer> questionMap = new HashMap<>();  //Subjects and no of questions asked
    Map<String,Integer> answerMap = new HashMap<>();    //Subjects and answers added
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
        questionMap.put(subject.getKey(),0);
        answerMap.put(subject.getKey(),0);
        subject.addStudent(subject.getKey());
    }

    //Getter and setter for questions_count
    public int getTotalQuestionsAsked(){
        int questions_count = 0;
        for (Integer s: questionMap.values()){
            questions_count += s;
        }
        return questions_count;
    }

    public int getTotalReplies(){
        int replies_count = 0;
        for (Integer s: answerMap.values()) {
            replies_count += s;
        }
        return replies_count;
    }

    //Calculate grades for the courses individually using mean and standard deviation
    //In fact we are getting the average of the z scores of questions and answers


    public void calculateScore() {
        Map<String, Integer> qnsForCourses = new HashMap<>();
        for (Topic t : Subject.allTopics) {
            if (questionMap.containsKey(t.getKey())) {
                if (qnsForCourses.containsKey(t.getKey())) {
                    qnsForCourses.put(t.getKey(), qnsForCourses.get(t.getKey()) + t.getQuestions().size());
                }

                else{
                    qnsForCourses.put(t.getKey(), t.getQuestions().size());
                }
            }
        }

        for (String courseCode : questionMap.keySet()) {
            double mean = (qnsForCourses.get(courseCode)) / (Subject.getTotalStudents(courseCode));
            double val = questionMap.get(courseCode) - mean;
            scoresQn.put(courseCode, val);
        }


        //Get answer score
        Map<String,Integer> ansForCourses = new HashMap<>();
        for (Topic t: Subject.allTopics){
            if (answerMap.containsKey(t.getKey())){
                ArrayList<Question> qns = t.getQuestions();
                int answers = 0;
                for (Question q: qns){
                    answers = q.getAnswersTotal();
                }

                if (ansForCourses.containsKey(t.getKey())){
                    ansForCourses.put(t.getKey(),ansForCourses.get(t.getKey())+ answers);
                }
                else{
                    ansForCourses.put(t.getKey(),answers);
                }
            }
        }

        for (String courseCode: answerMap.keySet()){
            double mean = (ansForCourses.get(courseCode))/(Subject.getTotalStudents(courseCode));
            double val = answerMap.get(courseCode) - mean;
            scoresAns.put(courseCode,val);
        }

        //Calculate final grade
        for (String courseCode: answerMap.keySet()){
            finalGrade.put(courseCode,(scoresAns.get(courseCode) + scoresQn.get(courseCode))/2.0);
        }
        //All the answers are stored in the finalGrade hash map
     }

    //public getters and setters for Firebase
    public double getParticipation(){return participation;}
    public Map<String, Integer> getQuestionMap() {
        return questionMap;
    }

    public void setQuestionMap(Map<String, Integer> questionMap) {
        this.questionMap = questionMap;
    }

    public Map<String, Integer> getAnswerMap() {
        return answerMap;
    }

    public void setAnswerMap(Map<String, Integer> answerMap) {
        this.answerMap = answerMap;
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