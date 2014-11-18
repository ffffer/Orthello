import java.util.ArrayList;

public class Board
{
    public int[][] board= new int[8][8];
    //public int sum=0;

    //sets starting board formation, 1= white, -1=black, 0=empty
    public Board()
    {
        for (int[] a: board)
        {
            for (int i = 0; i < a.length; i++)
            {
                a[i]=0;
            }
        }
        board[3][3]=1;
        board[4][4]=1;
        board[3][4]=-1;
        board[4][3]=-1;
    }
    
    public Board(int[][] myBoard)
    {
        board=copyBoard(myBoard);
    }
    
    /** print move "col row" **/

    //takes in coordinates of a move (i,j) just made, the color (int) of the player who made the move, and the validity array of the move
    //updates board accordingly, placing tile and flipping appropriate tiles
    public void updateBoard(int i, int j, int color, int[] validity)
    {
        board[i][j]=color;
        if (validity[0]==1)
        {
            for(int a = i-1; a>=0;a--)
            {
                if (board[a][j]==(-1*color))
                    board[a][j]=color;
                else if (board[a][j]==color)
                    a=-1;
                else
                {
                    //System.out.println("error has occured, move reported is not actually valid in the north direction");
                }
            }
        }
        
        if (validity[1]==1)
        {
            int b = j +1;
            for(int a = i-1; a>=0 && b<=7; a--, b++)
            {

                if (board[a][b]==(-1*color))
                    board[a][b]=color;
                else if (board[a][b]==color)
                    a=-1;
                else
                {
                    //System.out.println("error has occured, move reported is not actually valid in the northeast direction");
                }
            }
        }
        
        if (validity[2]==1)
        {
            for(int b = j+1; b <= 7;b++)
            {
                if (board[i][b]==(-1*color))
                    board[i][b]=color;
                else if (board[i][b]==color)
                    b=8;
                else
                {
                    //System.out.println("error has occured, move reported is not actually valid in the east direction");
                }
            }
        }
        
        if (validity[3]==1)
        {
            int b = j +1;
            for(int a = i+1; a<=7 && b<=7;a++,b++)
            {

                if (board[a][b]==(-1*color))
                    board[a][b]=color;
                else if (board[a][b]==color)
                    b=8;
                else
                {
                    //System.out.println("error has occured, move reported is not actually valid in the southeast direction");
                }
            }
        }
        
        if (validity[4]==1)
        {
            for (int k=(i+1); k<8; k++)
            {
                if (board[k][j]==(-1*color))
                    board[k][j]=color;
                else if (board[k][j]==color)
                    k=8;
                else
                {
                    //System.out.println("error has occured, move reported is not actually valid in the south direction");
                }
            }
        }
        
        if (validity[5]==1)
        {
            int l=j-1;
            for (int k=(i+1); k<8 && l>=0; k++, l--)
            {
                if (board[k][l]==(-1*color))
                    board[k][l]=color;
                else if (board[k][l]==color)
                    k=8;
                else
                {
                    //System.out.println("error has occured, move reported is not actually valid in the southwest direction");
                }
            }
        }
        
        if (validity[6]==1)
        {
            for (int l=(j-1); l>=0; l--)
            {
                if (board[i][l]==(-1*color))
                    board[i][l]=color;
                else if (board[i][l]==color)
                    l=-1;
                else
                {
                    //System.out.println("error has occured, move reported is not actually valid in the west direction");
                }
            }
        }
        
        if (validity[7]==1)
        {
            int l=j-1;
            for (int k=(i-1); k>=0 && l>=0; k--, l--)
            {
                if (board[k][l]==(-1*color))
                    board[k][l]=color;
                else if (board[k][l]==color)
                    k=-1;
                else
                {
                    //System.out.println("error has occured, move reported is not actually valid in the northwest direction");
                }
            }
        }
    }
    
    public Board moveResult(Move m, int color)
    {
        Board child= new Board(board);
        child.updateBoard(m.coords[0], m.coords[1], color, m.validity);
        return child;
    }
    
    public Move[] getMoves(int color)
    {
        ArrayList<Move> moves= new ArrayList<Move>();
        for (int i=0; i<8; i++)
        {
            for (int j=0; j<8; j++)
            {
                int[] validity= getValidity(i, j, color);
                boolean valid=false;
                for (int k : validity)
                {
                    if (k==1)
                        valid=true;
                }
                if (valid)
                    moves.add(new Move(i,j,validity));
            }
        }
        
        if (moves.size()==0)
            return null;
        
        Move[] res= new Move[moves.size()];
        
        for (int i=0; i<res.length; i++)
        {
            res[i]= moves.get(i);
        }
        
        if (res.length==0)
            return null;
        
        return res;
    }
    
    public double evaluate()
    {
        double sum=0.0;
        for (int[] a: board)
        {
            for (int b: a)
            {
                sum+=(b/100);
            }
        }
        
        Move[] c=getMoves(Shell.globalColor);
        if (c!=null)
            sum+=c.length;
        
        Move[] d= getMoves(-1*Shell.globalColor);
        if (d!=null)
            sum-=d.length;
        
        if (board[0][0]==Shell.globalColor)
            sum+=10-1/100;
        if (board[0][7]==Shell.globalColor)
            sum+=10-1/100;
        if (board[7][0]==Shell.globalColor)
            sum+=10-1/100;
        if (board[7][7]==Shell.globalColor)
            sum+=10-1/100;

        if (board[0][0]==-1*Shell.globalColor)
            sum-=10-1/100;
        if (board[0][7]==-1*Shell.globalColor)
            sum-=10-1/100;
        if (board[7][0]==-1*Shell.globalColor)
            sum-=10-1/100;
        if (board[7][7]==-1*Shell.globalColor)
            sum-=10-1/100;
        
        return sum;
    }

    //tells if a move is valid.  move is given by (i,j), player move is for is given by color.
    //returns an array of ints telling whether a move in valid in each direction. 1=valid, 0=invalid
    //the directions are [north, northeast, east, southeast, south, southwest, west, northwest].  
    public int[] getValidity(int i, int j, int color)
    {
        int[] res= {0,0,0,0,0,0,0,0};

        if (board[i][j]!=0)
            return res;

        res[0]=checkNorth(i, j, color);
        res[1]=checkNorthEast(i, j, color);
        res[2]=checkEast(i, j, color);
        res[3]=checkSouthEast(i, j, color);
        res[4]=checkSouth(i, j, color);
        res[5]=checkSouthWest(i, j, color);
        res[6]=checkWest(i, j, color);
        res[7]=checkNorthWest(i, j, color);

        return res;

    }

    public int checkNorth(int i, int j, int color)
    {
        if (i == 0)
            return 0;

        for(int a = i-1; a>=0;a--)
        {
            if (board[a][j]==color)
            {
                if (a == i-1)
                    return 0;
                else if (a< i-1)
                    return 1;
            }
            if (board[a][j]==0)
                return 0;
        }
        return 0;
    }

    public int checkNorthEast(int i, int j, int color)
    {
        if (i == 0 || j==7)
            return 0;

        int b = j +1;
        for(int a = i-1; a>=0 && b<=7; a--, b++)
        {

            if (board[a][b]==color)
            {
                if (a == i-1 )
                    return 0;
                else if (a< i-1)
                    return 1;
            }
            if (board[a][b]==0)
                return 0;
        }
        return 0;
    }

    public int checkEast(int i, int j, int color)
    {
        if (j == 7)
            return 0;

        for(int b = j+1; b <= 7;b++)
        {
            if (board[i][b]==color)
            {
                if (b == j+1)
                    return 0;
                else if (b > j + 1)
                    return 1;
            }
            if (board[i][b]==0)
                return 0;
        }
        return 0;
    }

    public int checkSouthEast(int i, int j, int color)
    {
        if (i == 7 || j==7)
            return 0;

        int b = j +1;
        for(int a = i+1; a<=7&&b<=7;a++,b++)
        {

            if (board[a][b]==color)
            {
                if (a == i+1 )
                    return 0;
                else if (a > i+1)
                    return 1;
            }
            if (board[a][b]==0)
                return 0;
        }
        return 0;
    }

    public int checkSouth(int i, int j, int color)
    {
        if (i==7)
            return 0;

        for (int k=(i+1); k<8; k++)
        {
            if (board[k][j]==(color))
            {
                if (k>(i+1))
                    return 1;
                if (k==(i+1))
                    return 0;
            }
            if (board[k][j]==0)
                return 0;
        }
        return 0;
    }

    public int checkSouthWest(int i, int j, int color)
    {
        if (i==7)
            return 0;
        if (j==0)
            return 0;

        int l=j-1;
        for (int k=(i+1); k<8 && l>=0; k++, l--)
        {
            if (board[k][l]==(color))
            {
                if (k>(i+1))
                    return 1;
                if (k==(i+1))
                    return 0;
            }
            if (board[k][l]==0)
                return 0;
        }
        return 0;
    }

    public int checkWest(int i, int j, int color)
    {
        if (j==0)
            return 0;

        for (int l=(j-1); l>=0; l--)
        {
            if (board[i][l]==(color))
            {
                if (l<(j-1))
                    return 1;
                if (l==(j-1))
                    return 0;
            }
            if (board[i][l]==0)
                return 0;
        }
        return 0;
    }

    public int checkNorthWest(int i, int j, int color)
    {
        if (i==0)
            return 0;
        if (j==0)
            return 0;

        int l=j-1;
        for (int k=(i-1); k>=0 && l>=0; k--, l--)
        {
            if (board[k][l]==(color))
            {
                if (k<(i-1))
                    return 1;
                if (k==(i-1))
                    return 0;
            }
            if (board[k][l]==0)
                return 0;
        }
        return 0;
    }
    /*
    public void print()
    {
        System.out.println("Board: \n");
        for (int[] a : board)
        {
            for (int b: a)
            {
                System.out.print(b+", ");
            }
            System.out.print("\n");
        }
    }
    */
    public static int[][] copyBoard(int[][] board)
    {
        int[][] res=new int[8][8];
        
        for (int i=0; i<8; i++)
        {
            for (int j=0; j<8; j++)
            {
                res[i][j]=board[i][j];
            }
        }
        return res;
    }

}
















