package Lab_3;

/**
 * Created by Aphex on 13.05.2016.
 */
public abstract class AbstractFunction<Input,Output> {
    Output out;
    Input inp;
    public abstract Output apply (Input input);
}
