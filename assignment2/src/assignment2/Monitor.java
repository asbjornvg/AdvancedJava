package assignment2;

/**
 * Receives a stream of sensor data and notifies subscribers based on discomfort
 * levels
 * 
 * @author bonii
 * 
 */
public interface Monitor {

	/**
	 * Push a reading from a sensor. 
	 * 
	 * @param sensorInput
	 */
	public void pushReading(SensorReading sensorInput);

	/**
	 * Process a reading received from a sensor. 
	 * 
	 * @param sensorInput
	 * @return
	 */
	public void processReading(SensorReading sensorInput);

	/**
	 * Register a subscriber which wants to be notified when a discomfort level
	 * is exceeded
	 * 
	 * @param discomfortLevel
	 * @param subscriber
	 */
	public void registerSubscriber(int discomfortLevel, Subscriber subscriber);
	
	/**
	 * Gets the available sensor data, blocks if no sensor data is available
	 * @return
	 */
	public SensorReading getSensorReading()
		throws InterruptedException;

}
