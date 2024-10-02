package Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

interface Complaints {
    static Map<Integer, ArrayList<ArrayList<String>>> complaints = new HashMap<>(); //student id -> value[0](pending) and value[1](resolved)
    public void view_complaints();
}
