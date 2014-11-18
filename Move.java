public class Move
{
    int[] coords=null;
    int[] validity=null;
    
    public Move(int[] myCoords, int[] myValidity)
    {
        coords=myCoords;
        validity=myValidity;
    }
    
    public Move(int i, int j, int[] myValidity)
    {
        int[] myCoords= {i,j};
        coords=myCoords;
        validity=myValidity;
    }
}

