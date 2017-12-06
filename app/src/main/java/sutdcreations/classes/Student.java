package sutdcreations.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Student extends User {
    Map<String,Integer> questionMap = new HashMap<>();  //Subjects and noof questions asked
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
        for (Integer s: answerMap.values()){
            replies_count += s;
        }
        return replies_count;
    }

    //Calculate grades for the courses individually using mean and standard deviation
    //In fact we are getting the average of the z scores of questions and answers

    public void calculatescore(){
        Map<String,Integer> qnsForCourses = new HashMap<>();
        for (Topic t: Subject.topics){
            if (questionMap.containsKey(t.getKey())){
                if (qnsForCourses.containsKey(t.getKey())){
                    qnsForCourses.put(t.getKey(),qnsForCourses.get(t.getKey())+t.getQuestions().size());
                }
                else{
                    qnsForCourses.put(t.getKey(),t.getQuestions().size());
                }
            }

        }

        for (String courseCode: questionMap.keySet()){
            double mean = (qnsForCourses.get(courseCode))/(Subject.getTotalStudents(courseCode));
            double val = questionMap.get(courseCode) - mean;
            scoresQn.put(courseCode,val);
        }

        //Get answer score
        Map<String,Integer> ansForCourses = new HashMap<>();
        for (Topic t: Subject.topics){
            if (answerMap.containsKey(t.getKey())){
                ArrayList<Question> qns = t.getQuestions();
                int answers = 0;
                for (Question q: qns){
                    answers += q.getAnswersTotal();
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
            finalGrade.put(courseCode,(scoresAns.get(courseCode)+ scoresQn.get(courseCode))/2.0);
        }
        //All the answers are stored in the finalGrade hash map
    }

}