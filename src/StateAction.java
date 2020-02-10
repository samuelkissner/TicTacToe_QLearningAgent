public class StateAction implements Comparable <StateAction>  {
	
	int pos;
	//estimated utility for this state action pair
	double Q;
	//incremented every time this action is taken at this state
	int frequency;
	
	//constructor
	public StateAction(int pos){
		this.pos = pos;
		this.Q = 0.0;
		this.frequency = 0;
	}
	
	//method needed for Comparator interface
	public int compareTo(StateAction sa2){
		if(this.Q > sa2.Q)
			return 1;
		if(this.Q < sa2.Q)
			return -1;
		return 0;
	}	
}
