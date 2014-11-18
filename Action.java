public class Action
{
    public double state;
    public Move move=null;
    
    public Action (double myState, Move myMove)
    {
        state=myState;
        move=myMove;
    }
    
    public Action()
    {}
    
    public int compareTo(double a)
    {
        if (state>a)
            return 1;
        if (a>state)
            return -1;
        else
            return 0;
    }
}

