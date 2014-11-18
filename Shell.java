
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;  
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Shell
{
    public static int globalColor=-1;
    public static int globalDepth=4;
    public static long globalTimelimit1 = (long) 0;
    public static long globalTimelimit2 = (long) 0;
    public static long startTime= (long) (0);
    
    Board board= new Board();
    public static void main(String[] args) throws IOException
    {
    
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(System.out));
    
        Board board= new Board();

        String start= reader.readLine();
        
        String[] startArray = {"","","","",""};
        
        int j=0;
        for (int i=0; i<start.length(); i++)
        {
            String a= Character.toString(start.charAt(i));
            if (!a.equals(" "))
                startArray[j]+=a;
            else
                j++;
        }

        if (startArray[1].equalsIgnoreCase("B"))
            globalColor=-1;
        if (startArray[1].equalsIgnoreCase("W"))
            globalColor=1;
    
        globalDepth= Integer.parseInt(startArray[2]);
        
        globalTimelimit1 = ((long) (Integer.parseInt(startArray[3]))*1000000L);
        
       	globalTimelimit2 = (long) (Integer.parseInt(startArray[4])*1000000L);
	
	if (globalTimelimit2!= (long) (0))
		globalTimelimit1=globalTimelimit2/60L;

        if (globalColor==1)
        {
            String line= reader.readLine();
            
            int[] move= new int[2];
            move[1]= Integer.parseInt(Character.toString(line.charAt(0)));
            move[0]= Integer.parseInt(Character.toString(line.charAt(2)));
            
            board.updateBoard(move[0], move[1], (-1*globalColor), board.getValidity(move[0], move[1], (-1*globalColor)));
            //board.print();
        }
        
        for (int i=1; i>0; i--)
        {
        startTime=System.nanoTime();

            if (board.getMoves(globalColor)!=null)
            {
            Action myAction=null;
            
            if (globalTimelimit1!= (long) (0))
            {
                int tempDepth=-1;
                Action tempAction=null;
                
                while ((System.nanoTime()-startTime)<=(globalTimelimit1-10000000L))
                {
                    
                    tempDepth+=1;
                    myAction=tempAction;
                    tempAction= AlphaBeta(board, tempDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, globalColor);
                    //System.out.println((System.nanoTime()-startTime)+ " "+ (globalTimelimit1-900000000));
                    
                }
            }
            if (!(globalTimelimit1!= (long) (0)))
            {
                
                myAction= AlphaBeta(board, globalDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, globalColor);
            }

                writer.write(myAction.move.coords[1]+ " "+ myAction.move.coords[0]+"\n");
                writer.flush();
                
                board.updateBoard(myAction.move.coords[0], myAction.move.coords[1], globalColor, board.getValidity(myAction.move.coords[0], myAction.move.coords[1], globalColor));
                i=2;
            }
            
        else
        {
            writer.write("pass\n");
            writer.flush();
        }

        //blank

            String line= reader.readLine();

        if (board.getMoves(-1*globalColor)!=null)
        {
            int[] move= new int[2];
            move[1]= Integer.parseInt(Character.toString(line.charAt(0)));
            move[0]= Integer.parseInt(Character.toString(line.charAt(2)));

            board.updateBoard(move[0], move[1], (-1*globalColor), board.getValidity(move[0], move[1], (-1*globalColor)));
            //board.print();
            i=2;
        }
        
        }
        //System.out.println("I have no more moves I think because Im a dumb robot");
        reader.close();
        writer.close();
    }
    
    public static Action AlphaBeta(Board state, int d, double alpha, double beta, int color)
    {
        //cutoff test:
        
        Move[] moves= state.getMoves(color);
        Move best= null;

    if (globalTimelimit1!=0 && (System.nanoTime()-startTime)>=(globalTimelimit1-10000000L))
        return (new Action(state.evaluate(), best));
        


        if (d<=0 || moves==null)
            return (new Action(state.evaluate(), null));
    
        
        
        
//      for (Move m: moves)
//      {
//          System.out.println(m.coords[0]+" "+m.coords[1]+", ");
//      }
//      System.out.println("\n\n");
        
        if (color==globalColor)
        {
            for (int i=0; i<moves.length; i++)
            {
                Board child= state.moveResult(moves[i], color);
                //child.print();
                //System.out.println("Alpha="+alpha+" Beta="+beta);
                Action value= AlphaBeta(child, d-1, alpha, beta, -1*color);
                if (value.compareTo(alpha)==1)
                {
                    alpha=value.state;
                    best=moves[i];
                }
                //System.out.println("Color= " + color+", "+"Depth= "+d+", "+"Move= " +moves[i].coords[1]+" "+ moves[i].coords[0]+ ",  value= "+ value.state+ ", Alpha= "+ alpha+ ", Beta= "+ beta);

                if (beta<=alpha)
                    i=moves.length+1;
            }
            return new Action(alpha, best);
        }
        else
        {
            for (int i=0; i<moves.length; i++)
            {
                Board child= state.moveResult(moves[i], color);
                //child.print();
                //System.out.println("Alpha="+alpha+" Beta="+beta);
                Action value= AlphaBeta(child, d-1, alpha, beta, -1*color);
                if (value.compareTo(beta)==-1)
                {
                    beta=value.state;
                    best=moves[i];
                }
               // System.out.println("Color= " + color+", "+"Depth= "+d+", "+ "Move= " +moves[i].coords[1]+" "+ moves[i].coords[0]+ ",  value= "+ value.state+ ", Alpha= "+ alpha+ ", Beta= "+ beta);

                if (beta<=alpha)
                    i=moves.length+1;
            }
            return new Action(beta, best);
        }
    }
}

