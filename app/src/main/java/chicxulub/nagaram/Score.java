package chicxulub.nagaram;

/**
 * Created by trollham on 10/15/15.
 */
public class Score {
    String name;
    int score;
    public Score(String name, int score){
        setName(name);
        setScore(score);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
}
