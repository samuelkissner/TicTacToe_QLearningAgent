import java.util.*;
public class State {
	
	//String array to represent state of board. Sequence of 'X', 'O'
	String sequence;
	final char BLANK = '_';
	//character used to indicate draw game
	final char DRAW = 'd';
	//State Action data structure
	ArrayList<StateAction> stateActionArrayList;
	//reward
	double reward;
	Random random;
	
	//terminal state fields
	boolean isTerminal;

	
	//constructor
	public State(String s){
		this.sequence = s;
		this.random = new Random();
		
		//check to see if terminal
		char c = this.terminalStateCheck();
		this.isTerminal = c != this.BLANK ? true : false;
		if(this.isTerminal){
			this.reward = c != this.DRAW ? 1.0 : 0.0;
		}
		
		//if not terminal, create all state actions for this state
		else{
			this.reward = -.04;
			this.stateActionArrayList = new ArrayList<StateAction>();
			this.initializeStateActions();
		}
	}
	
	//purpose: mark as terminal and set reward if this is a terminal state. Also indicate which player wins
	//parameters: none
	//returns: none
	private char terminalStateCheck(){
		this.isTerminal = false;
		//check each row(3)
		for(int i = 0; i < sequence.length(); i+=3){
			if(sequence.charAt(i) == sequence.charAt(i+1) && sequence.charAt(i) == sequence.charAt(i+2)){
					return sequence.charAt(i);
			}		
		}
		//check each column(3)
		for(int i = 0; i < 3; i++){
			if(sequence.charAt(i) == sequence.charAt(i+3) && sequence.charAt(i) == sequence.charAt(i+6)){
					return sequence.charAt(i);
			}	
		}
		//check each diagonal(2)
		if(sequence.charAt(0) == sequence.charAt(4) && sequence.charAt(0) == sequence.charAt(8)
				|| sequence.charAt(2) == sequence.charAt(4) && sequence.charAt(2) == sequence.charAt(6)){
				return sequence.charAt(4);
		}
		//check for empty spaces (if any, the game isn't terminal)
		for(int i = 0; i < sequence.length(); i++){
				if(sequence.charAt(i) != 'X' && sequence.charAt(i) != 'O')
						return this.BLANK;
		}		
		//return draw if no empty spaces and no winner
		return this.DRAW;
	}
	
	//purpose: create all state actions for this state
	//parameters: none
	//returns: none
	public void initializeStateActions(){
		for(int i = 0; i< this.sequence.length(); i++){
			if(sequence.charAt(i) != 'X' && sequence.charAt(i) != 'O'){
				StateAction sa = new StateAction(i);
				this.stateActionArrayList.add(sa);
			}		
		}
	}
	
	//purpose: get next state action based on epsilon greedy policy
	//parameters: none
	//returns StateAction nextStateAction
	public StateAction getNextStateAction(double epsilon){
		StateAction nextStateAction = null;
		//pick a random number
		double rn = random.nextDouble();
		//pick min state (will be opponents max state) action if random number is greater than epsilon
		if(rn > epsilon){
			nextStateAction = this.stateActionArrayList.get(this.stateActionArrayList.size() - 1);
		}
		//pick random state action if random number is less than or equal to epsilon
		else{
			nextStateAction = this.stateActionArrayList.get(random.nextInt(this.stateActionArrayList.size()));
		}
		return nextStateAction;
	}
	
}
