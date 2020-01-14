import java.util.List;
import java.util.ArrayList;
import javafx.util.Pair;

class Hospital{

	private int locX;
	private int locY;
	private int ambulances;
	private int maxAmbs;
	private int incidents;
	private List<Long> timeList;
	private boolean finished;

	public Hospital(int locX, int locY, int ambulances, int incidents){
		this.locX = locX;
		this.locY = locY;
		this.ambulances = ambulances;
		this.incidents = incidents;
		maxAmbs = ambulances;
		timeList = new ArrayList<>();
		finished = false;
	}

	public int getX(){
		return locX;
	}
	public int getY(){
		return locY;
	}
	public int getAmbs(){
		return ambulances;
	}
	public int getMaxAmbs(){
		return maxAmbs;
	}
	public int getIncidents(){
		return incidents;
	}

	//parameter is either positive(1) or negative (-1)
	//if positive, ambulance has come back from incident (+1 ambulance)
	//if negative, incident needs ambulance, check if theres one available (-1 ambulance)
	//returns true if all is ok and false if there are no ambulances left in hospital
	public synchronized boolean changeAmb(int number){
		if(number < 0 && ambulances == 0){
			return false;
		}
		ambulances += number;
		return true;
	}

	//decrease incident count by one in this iteration
	public synchronized boolean decIncident(){
		if(incidents == 0){
			return false;
		}
		incidents-=1;
		return true;
	}

	//keeps tab of times spent on incidents
	public synchronized void addTimeToList(long time){
		timeList.add(time);
	}

	//calculates average and total times
	public Pair<Long,Long> calculateTimes(){
		int divide = 0;
		long average = 0;
		for (long temp : timeList) {
			average += temp;
			divide++;
		}
		Pair<Long,Long> pair = new Pair<>(average, (average/divide));
		return pair;
	}

	public boolean getFinished(){
		return finished;
	}

	public void setFinished(){
		if(incidents == 0){
			finished = true;
		}
	}

	


}