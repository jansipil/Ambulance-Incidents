import java.lang.Math;
import java.util.concurrent.ThreadLocalRandom;
import java.time.Instant;
import java.time.Duration;


class Incident implements Runnable{

	private int locX;
	private int locY;
	private int incNum;
	private Hospital h;
	private int currentAmb;

	public Incident(Hospital h, int locX, int locY, int incNum){
		this.locX = locX;
		this.locY = locY;
		this.h = h;
		this.incNum = incNum;
	}

	public void run(){

		h.decIncident();

		//random speed for ambulance in this incident
		int speed = ThreadLocalRandom.current().nextInt(5, 15 + 1);
		// speed = 50; //uncomment if need for instant rescue for testing purposes
		Ambulance a = new Ambulance(speed);

		//take time
		Instant startTime = Instant.now();

		System.out.println("Incident " + incNum + " at location " + locX + "," + locY + " is waiting for ambulance");

		//check situation for ambulances
		while(!h.changeAmb(-1)){
			try{
				Thread.sleep(1000);
			}catch(InterruptedException ie){}
		}

		//this ambulances id
		currentAmb = h.getMaxAmbs()-h.getAmbs();

		a.setState(Ambulance.State.TOINCIDENT);
		System.out.println("Ambulance " + currentAmb + a.getState() + incNum);

		//calculate time to incident location from hospital location with current speed
		int ambTime = calculateTime(h.getX(),h.getY(),speed);
		try{
			Thread.sleep(ambTime*1000);
		}catch(InterruptedException ie){}

		//arrive on site but also instantly leave
		a.setState(Ambulance.State.ONSITE);
		System.out.println("Ambulance " + currentAmb + a.getState() + incNum);
		a.setState(Ambulance.State.TOHOSPITAL);
		System.out.println("Ambulance " + currentAmb + a.getState() + incNum);

		//same time back from incident
        try{
			Thread.sleep(ambTime*1000);
		}catch(InterruptedException ie){}

		a.setState(Ambulance.State.TRANSFER);
		System.out.println("Ambulance " + currentAmb + a.getState() + incNum);
		
		//takes 10 seconds to transfer patient
		try{
			Thread.sleep(10000);
		}catch(InterruptedException ie){}

		//ambulance ready for next incident
		//incident finished, take time
		a.setState(Ambulance.State.IDLE);
		System.out.println("Ambulance " + currentAmb + a.getState());
		Instant finishTime = Instant.now();
		long timeElapsed = Duration.between(startTime, finishTime).toMillis();
		h.addTimeToList(timeElapsed);
		h.changeAmb(1);
		h.setFinished();
	}

	//initializer for thread
	public void newIncident(){
		Thread thread = new Thread(this);
		thread.start();
	}

	//calculates the times it takes to incident site
	public int calculateTime(int hospX, int hospY, int speed){

		double dist = Math.hypot(hospX-locX,hospY-locY);
		return (int) (dist/speed);


	}

}