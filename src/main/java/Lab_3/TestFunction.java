package Lab_3;

/**
 * Created by Aphex on 13.05.2016.
 */
public class TestFunction extends AbstractFunction<Integer,String>{
    public String apply(Integer input){
        return Integer.toString(Integer.toString(input).hashCode());
    }
}
