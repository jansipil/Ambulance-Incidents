import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.util.Pair;

class Main{

	private static Scanner reader = new Scanner(System.in);

	public static void main(String[] args){

		int iterAmount = 0;
		int city = 0;
		int x = 0;
		int y = 0;
		int ambs = 0;
		int incidents = 0;
		int speed = 0;
		int i;

		Incident inc;

		//stats for final
		Pair<Integer,Long> bestAverage = new Pair<>(0, Long.MAX_VALUE);
		Pair<Integer,Long> bestMax = new Pair<>(0, Long.MAX_VALUE);

		//stats per iteration
		List<Pair<Integer,Long>> iterationsMax = new ArrayList<>();
		List<Pair<Integer,Long>> iterationsAvg = new ArrayList<>();

		System.out.print("Give number of iterations: ");
		iterAmount = reader.nextInt();
		reader.nextLine();

		//will loop for amount of iterations given by user
		for(i=0;i<iterAmount;i++){

			System.out.println("\n---------------------------");
			System.out.println("Iteration " + (i+1) + " starts");
			System.out.println("---------------------------\n");

			//random city size
			city = ThreadLocalRandom.current().nextInt(50, 100 + 1);

			//random hospital location
			x = ThreadLocalRandom.current().nextInt((city * -1), city + 1);
			y = ThreadLocalRandom.current().nextInt((city * -1), city + 1);

			//random amount of ambulances and incidents
			ambs = ThreadLocalRandom.current().nextInt(1, 5 + 1);
			incidents = ThreadLocalRandom.current().nextInt(1, 10 + 1);

			System.out.println("City size: " + city + "x" + city);
			System.out.println("Hospital location: " + x + "," + y);
			System.out.println("Amount of incidents: " + incidents);
			System.out.println("Amount of ambulances: " + ambs);
			System.out.println();

			Hospital hospital = new Hospital(x,y,ambs,incidents);

			int currentIncidents = 0;
			int incidentNumber = 1;
			
			//loop until iteration is finished
			//launch new incidents in random interwals
			while(true){

				try{
					Thread.sleep(((ThreadLocalRandom.current().nextInt(5, 20 + 1)))*1000);
				}
				catch(InterruptedException ie){
					continue;
				}

				currentIncidents = hospital.getIncidents();
				if(currentIncidents > 0){
					x = ThreadLocalRandom.current().nextInt((city * -1), city + 1);
					y = ThreadLocalRandom.current().nextInt((city * -1), city + 1);

					inc = new Incident(hospital,x,y,incidentNumber);
					inc.newIncident();
					incidentNumber++;
				}
				//go to next iteration if finished
				else if(hospital.getAmbs() == hospital.getMaxAmbs() && hospital.getFinished()){
					break;
				}

				

			}
			System.out.println();

			//calculate stats for this iteration
			Pair<Long,Long> tempPair = hospital.calculateTimes();
			Pair<Integer,Long> max = new Pair<>(i+1, tempPair.getKey());
			iterationsMax.add(max);
			Pair<Integer,Long> avg = new Pair<>(i+1, tempPair.getValue());
			iterationsAvg.add(avg);

		}

		//calculate final stats
		for (Pair<Integer,Long> tempAvg : iterationsAvg) {
			if(bestAverage.getValue() > (tempAvg.getValue()) ){
				bestAverage = tempAvg;
			}
		}
		for (Pair<Integer,Long> tempMax : iterationsMax) {
			if(bestMax.getValue()>tempMax.getValue()){
				bestMax = tempMax;
			}
		}

		double bestAvgTime = bestAverage.getValue();
		double bestMaxTime = bestMax.getValue();
		//least time spent on average on iteration
		System.out.println("Best Average: Iteration " + bestAverage.getKey() + ", Time " + bestAvgTime/1000 + "s");
		//least time spent on total on iteration
		System.out.println("Best Max: Iteration " + bestMax.getKey() + ", Time " + bestMaxTime/1000 + "s");



	}

	
	
}