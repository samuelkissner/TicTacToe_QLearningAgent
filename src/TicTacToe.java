import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;


public class TicTacToe {
	//player pieces
	final static char[] PLAYERS = {'X', 'O'};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		char choice = ' '; 
		int iterations = 1000;
		while(true){
			String line;
			int diff;
			while(true){
				try{
					System.out.print("Select difficulty (1-easy, 2-medium, 3-hard): ");
					line = input.readLine();
					if(line.length() > 1)
						throw new Exception();
					diff = Integer.parseInt(line);
					
					if(diff != 1 && diff != 2 && diff != 3)
						throw new Exception();
					break;
					
				}catch(Exception e){
					System.out.println("\nenter either 1 2 or 3: \n");
				}
			}
			
			if(diff == 1)
				iterations = 100;
			if(diff == 2)
				iterations = 1000;
			if(diff == 3)
				iterations = 10000;
			
			QLearningAgent qAgent = new QLearningAgent(.2,.9);
			qAgent.train(iterations);
			
			
			playTicTacToe(qAgent);
			
			while(true){
				System.out.print("Play Again? (y/n): ");
			
				try{
					line = input.readLine();
					if(line.length() > 1)
						throw new Exception();
					choice = line.charAt(0);
					if(choice != 'y' && choice != 'n')
						throw new Exception();
					break;
				
				}catch(Exception e){
					System.out.println("\n\nenter either 'y' or 'n'\n");
				}
			}
			if(choice == 'n')
				return;
			System.out.println("\n");
		}
	}
	
	//purpose: play a game of Tic Tac Toe against an AI agent
	//parameters: QLearningAgent qAgent (the ai openent) 
	//returns: none
	public static void playTicTacToe(QLearningAgent qAgent){
		Random rn = new Random();
		
			//ticTacToe board
			char[] ticTacToeBoard  = new char[9];
			for(int i = 0; i < ticTacToeBoard.length; i++)
				ticTacToeBoard[i] = (char) i;
			
			int player = rn.nextInt(2);
			//move counter and result variable
			int move = 0;
			int result = 2;
			//take turns making moves until terminal state
			while(result == 2){	
				if(move % 2 == player)
					//display board and prompt user to move
					getPlayerMove(ticTacToeBoard, player);
				else
					result = qAgent.takeTurn(ticTacToeBoard, move);
				move++;
			}
			System.out.println();
			printBoard(ticTacToeBoard);
			String resultMessage = "";
			if(result == -1)
				resultMessage ="You Win!";
			else if(result == 0)
				resultMessage = "Tie Game";
			else if(result == 1)
				resultMessage = "You lose";
			System.out.println();
			System.out.println(resultMessage);	
	}
	
	//purpose: get next user move
	//parameters: char[] board, int player
	//returns: none
	public static void getPlayerMove(char[] board, int player){
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		//print board
		System.out.println();
		printBoard(board);
		
		int move = -1;
		while(true){
			System.out.printf("\nEnter board position for next '%s' placement: ", PLAYERS[player]);
			
			try{
				move = validatePlayerMove(input.readLine(), board);
				break;
			}catch(Exception e){
				System.out.println("\nEnter a valid board position");
			}
		}	 
		board[move - 1] = PLAYERS[player];
	}
	
	public static void printBoard(char[] board){
		for(int i = 0; i < board.length; i += 3){
			System.out.print("|");
			for(int j = 0; j < 3; j++){
				if(board[i+j] != PLAYERS[0] && board[i+j] != PLAYERS[1])
					System.out.print((int)board[i+j]+1);
				else
					System.out.print(board[i+j]);
				System.out.print("|");
			}
			System.out.println();
		}
	}
	
	public static int validatePlayerMove(String input, char[] board) throws Exception{

			
		try{
			if(input.length() > 1)
				throw new Exception();
			int choice = Integer.parseInt(input);
			int boardLabel = (int)board[choice -1];
			if(boardLabel != choice -1)
				throw new Exception();
			
			return choice;
		}catch(Exception e){
			throw e;
		}	
	}
}
