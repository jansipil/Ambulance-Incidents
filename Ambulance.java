import java.util.EnumMap;

class Ambulance{

	private int speed;
	private State state;
	public enum State{
		IDLE,
		TOINCIDENT,
		ONSITE,
		TOHOSPITAL,
		TRANSFER
	};

	private EnumMap<State, String> states = new EnumMap<State, String>(State.class);

	//pretyy useless class in the end so player around with enums and maps
	public Ambulance(int speed){
		this.speed = speed;
		State state = State.IDLE;
		//states = new EnumMap<State, String>()
		states.put(State.IDLE, " is waiting idly");
		states.put(State.TOINCIDENT, " is on its way to incident ");
		states.put(State.ONSITE, " is on incident ");
		states.put(State.TOHOSPITAL, " is on its way back to hospital from incident ");
		states.put(State.TRANSFER, " is trasferring patient to hospital from incident ");
	}

	public String getState(){
		return states.get(state);
	}

	public void setState(State state){
		this.state = state;
	}

	public int getSpeed(){
		return speed;
	}


}