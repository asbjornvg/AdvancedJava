package assignment2;

public class AMonitor implements Monitor, Runnable {

	@Override
	public void pushReading(SensorReading sensorInput) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processReading(SensorReading sensorInput) {
		// TODO Auto-generated method stub
		//Note that you need to push computed discomfort levels to the registered
		//subscribers using the registerDiscomfortLevel method in Subscriber interface
		
	}

	@Override
	public void registerSubscriber(int discomfortLevel, Subscriber subscriber) {
		// TODO Auto-generated method stub
	}

	@Override
	public SensorReading getSensorReading() {
		// TODO Auto-generated method stub
		return null;
	}

	public void run() {
		SensorReading sensorInput = null;
		while(true) {
			sensorInput = getSensorReading();
			this.processReading(sensorInput);
		}
	}

}
