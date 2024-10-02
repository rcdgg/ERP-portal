package Users;

import java.util.ArrayList;

public class Feedback<T, V> {
    public Feedback(T one, V two){
        if(one instanceof String && ((String) one).isEmpty()){
            one = (T) "-";
        }
        if(two instanceof String && ((String) two).isEmpty()){
            two = (V) "-";
        }
        feedback.add(one);
        feedback.add(two);
    }
    @Override
    public String toString(){
        return "Feedback(Rating/Comments): " + feedback;
    }
    ArrayList<Object> feedback = new ArrayList<>();
}
