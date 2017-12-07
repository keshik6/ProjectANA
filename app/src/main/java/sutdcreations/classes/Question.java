package sutdcreations.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Beng Haun on 2/12/2017.
 */

public class Question {

    Student asker;
    String title;
    String body;
    String key;
    int votes;
    ArrayList<Student> voted = new ArrayList<>();
    ArrayList<Answer> answers = new ArrayList<>();
    ArrayList<String> tags = new ArrayList<>();
    HashMap<String,String> animalMap = new HashMap<>();
    ArrayList<String> animalList = new ArrayList<>();
    boolean isClosed = false;
    boolean isLive = false;
    boolean feedback = false;

    public Question(){//default constructor for Firebase
    }

    public Question(String title, String body, ArrayList<String> tags, Student asker) {
        this.title = title;
        this.body = body;
        this.tags = tags;
        this.asker = asker;

        //get random animal to assign to asker, save assigned animal for this question to HashMap
        animalList.addAll(Arrays.asList("alligator", "anteater", "armadillo", "auroch", "axolotl", "badger", "bat", "beaver", "buffalo", "camel", "chameleon", "cheetah", "chipmunk", "chinchilla", "chupacabra", "cormorant", "coyote", "crow", "dingo", "dinosaur", "dog", "dolphin", "duck", "elephant", "ferret", "fox", "frog", "giraffe", "gopher", "grizzly", "hedgehog", "hippo", "hyena", "jackal", "ibex", "ifrit", "iguana", "kangaroo", "koala", "kraken", "lemur", "leopard", "liger", "lion", "llama", "manatee", "mink", "monkey", "moose", "narwhal", "nyan cat", "orangutan", "otter", "panda", "penguin", "platypus", "python", "pumpkin", "quagga", "rabbit", "raccoon", "rhino", "sheep", "shrew", "skunk", "slow loris", "squirrel", "tiger", "turtle", "walrus", "wolf", "wolverine", "wombat"));
        int randNum = ThreadLocalRandom.current().nextInt(0,animalList.size());
        String randAnimal = animalList.get(randNum);
        animalMap.put(asker.getUid(),randAnimal);
        animalList.remove(randAnimal);

        //add 1 to number of questions asked by student for this subject
        String courseCode = this.getKey().split(" ")[0];
        this.asker.questionMap.put(courseCode,this.asker.questionMap.get(courseCode)+1);
    }

    public void upVote(Student student) {
        votes+=1;
        voted.add(student);
    }

    public void downVote(Student student) {
        votes -= 1;
        voted.add(student);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);

        //assign random animal to answerer
        User answerer = answer.getAnswerer();
        int randNum = ThreadLocalRandom.current().nextInt(0,animalList.size());
        String randAnimal = animalList.get(randNum);
        animalMap.put(answerer.getUid(),randAnimal);
        animalList.remove(randAnimal);

        //add 1 to number of answers by student for this subject
        if (answer.answerer instanceof Student){
            Student student = (Student) answerer;
            String courseCode = this.getKey().split(" ")[0];
            student.answerMap.put(courseCode,student.answerMap.get(courseCode)+1);
        }
    }

    public void close() {
        isClosed = true;
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
    }

    //public getters and setters for Firebase

    public void setKey(String k){
        this.key = k;
    }

    public void setIsLive(boolean isLive) {
        this.isLive = isLive;
    }

    public void setFeedback(boolean fb){
        feedback = fb;
    }

    public void setAsker(Student user){
        asker = user;
    }

    public void setAnimalMap(HashMap<String, String> animalMap){
        this.animalMap = animalMap;
    }

    public void setAnimalList(ArrayList<String> animalList){
        this.animalList = animalList;
    }

    public HashMap<String, String> getAnimalMap() {
        return animalMap;
    }

    public ArrayList<String> getAnimalList() {
        return animalList;
    }

    public Student getAsker() {
        return asker;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getVotes() {
        return votes;
    }

    public ArrayList<Student> getVoted() {
        return voted;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public String getKey(){return key;}

    public boolean isLive() {
        return isLive;
    }

    public boolean getIsLive(){return isLive;}

    public boolean isFeedback() {
        return feedback;
    }

    public int getAnswersTotal(){
        return answers.size();
    }
}
