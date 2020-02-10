import java.util.*;

public class QLearningAgent {

	//player pieces
	final char[] PLAYERS = {'X', 'O'};
	//set of all visited states
	HashMap<String, State> visitedStates;
	State previousState;
	StateAction previousStateAction;
	Double previousReward;
	char[] ticTacToeBoard;
	Random rn = new Random();
	double epsilon;
	double discount;

	//constructor
	public QLearningAgent(double epsilon, double discount){
		this.visitedStates = new HashMap<String, State>();
		this.ticTacToeBoard = new char[9];
		for(int i = 0; i < ticTacToeBoard.length; i++)
			ticTacToeBoard[i] = (char)i;
		this.epsilon = epsilon;
		this.discount = discount;		
	}

	//purpose: play several games of tic-tac-toe (self play)
	//parameters: int iterations (number of games to play
	//returns: none
	public void train(int iterations){
		for(int i = 0; i < iterations; i++){
			simulateGame();
		}
	}
	
	//purpose: self play tic-tac-toe until terminal state reached
	//parameters: none
	//returns: none
	private void simulateGame(){
		int move = 0;
		while(true){
			updateAgent(PLAYERS[move%2]);
			if(this.previousStateAction == null)
				break;
					
			//perform action to get next state
			ticTacToeBoard[this.previousStateAction.pos] = PLAYERS[(move)%2];
			move++;
		}
		
		//reset game
		for(int i = 0; i < ticTacToeBoard.length; i++)
			ticTacToeBoard[i] = (char)i;
		this.previousState = null;
	}
	
	//purpose: choose next action (epsilon greedy). Update Q value for previous state-action pair
	//parameters: none
	//returns: none
	private void updateAgent(char player){
		StateAction nextStateAction = null;
		//check if current state has been visited, if not, add it to map of visited states
		String currentStateSequence = new String(ticTacToeBoard);
		State currentState = visitedStates.get(currentStateSequence);
		//if current state hasn't been visited, create it add it to table of visited states
		if(currentState == null){
			currentState = new State(currentStateSequence);
			visitedStates.put(currentStateSequence, currentState);
		}
		//if current state isn't terminal, get next action (based on epsilon greedy policy)
		if(!currentState.isTerminal)
			nextStateAction = currentState.getNextStateAction(this.epsilon);
		
		//if stored previous state isn't null...
		if(this.previousState != null){
			//increment state-action frequency and calculate learning rate
			double alpha = 1.0/++this.previousStateAction.frequency;
			//negative of current state utility used for previous state update (because the utility for current player is opposite for opponent)
			double currentStateActionUtility = currentState.isTerminal ? currentState.reward: -nextStateAction.Q; 
			double previousStateActionUtility = this.previousStateAction.Q;
			//update previous state utility (via temporal-difference equation)
			this.previousStateAction.Q = (double)(previousStateActionUtility+ alpha*(previousReward+discount*currentStateActionUtility - previousStateActionUtility));
			
			//sort the action states for the previous state
			Collections.sort(this.previousState.stateActionArrayList);			
		}
		this.previousStateAction = nextStateAction;
		this.previousState = currentState;
		this.previousReward = currentState.reward;
	}
	
	
	//purpose: pick best move while playing actual game
	//parameters: char[] board
	//returns: int  game status (-1 is a loss, 0 is a tie, 1 is a win, and 2 means the game is ongoing) 
	public int takeTurn(char[] board, int move){
		String stateSequence = new String(board);
		//look for state, create new state if it's never been seen before
		State state = this.visitedStates.get(stateSequence) != null ? this.visitedStates.get(stateSequence): new State(stateSequence);
		
		//see if this is a terminal state, if it is return victor
		if(state.isTerminal)
			return (int) -state.reward;
		
		//remove highest utility state action for this state
		StateAction highestSA = state.stateActionArrayList.get(state.stateActionArrayList.size() - 1);
		board[highestSA.pos] = PLAYERS[move%2];
		
		
		//see if terminal state reached after move
		stateSequence = new String(board);
		//look for state, create new state if it's never been seen before
		state = this.visitedStates.get(stateSequence) != null ? this.visitedStates.get(stateSequence): new State(stateSequence);
		
		//see if this is a terminal state, if it is return victor
		if(state.isTerminal)
			return (int) state.reward;

		return 2;
	}
}