package com.example.computesmartphonedisplacementapi;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TextView tvMsg1;
	private TextView tvMsg2;
	private SensorManager sensorMgr;
	private SensorEventListener listener;
	private float[] linearAccelerationValue;
	private float[] GyroscopeValue;
	private Sensor sensor;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorMgr = (SensorManager)getSystemService(SENSOR_SERVICE); 
        tvMsg1 = (TextView)findViewById(R.id.tvMsg1);
        tvMsg2 = (TextView)findViewById(R.id.tvMsg2);
        linearAccelerationValue = new float[3];
        GyroscopeValue = new float[3];
        
        listener = new SensorEventListener() { 
    		@Override
    		public void onAccuracyChanged(Sensor sensor, int accuracy) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void onSensorChanged(SensorEvent event) {
    			// TODO Auto-generated method stub
    			
    			StringBuilder sensorInfo = new StringBuilder(); 
    			
    			switch (event.sensor.getType()) { 
				case Sensor.TYPE_LINEAR_ACCELERATION: 
					sensor = event.sensor;
					
					sensorInfo.append("sensor Name: " + sensor.getName() + "\n"); 
	    			sensorInfo.append("sensor Type: " + sensor.getType() + "\n"); 
	    			sensorInfo.append("used power: " + sensor.getPower() + " mA\n"); 
	    			sensorInfo.append("values: \n"); 
	    			linearAccelerationValue = event.values.clone();
	    			for (int i = 0; i < linearAccelerationValue.length; i++) 
	    				sensorInfo.append("-values[" + i + "] = " + linearAccelerationValue[i] + "\n");
	    			tvMsg1.setText(sensorInfo);
	    			
					break; 
				case Sensor.TYPE_GYROSCOPE: 
					sensor = event.sensor;
					
					sensorInfo.append("sensor Name: " + sensor.getName() + "\n"); 
					sensorInfo.append("sensor Type: " + sensor.getType() + "\n"); 
	    			sensorInfo.append("used power: " + sensor.getPower() + " mA\n"); 
	    			sensorInfo.append("values: \n"); 
	    			GyroscopeValue = event.values.clone();
	    			for (int i = 0; i < GyroscopeValue.length; i++) 
	    				sensorInfo.append("-values[" + i + "] = " + GyroscopeValue[i] + "\n");
	    			tvMsg2.setText(sensorInfo);
					
					break; 
				default: 
					break; 
    			} 

    			
    		} 
    	};
    	
    	
    }
    
	protected void onResume() { 
		super.onResume(); 
		sensorMgr.registerListener(listener, sensorMgr.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_UI); 
		sensorMgr.registerListener(listener, sensorMgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_UI); 
	}
	protected void onPause() { 
		super.onPause(); 
		sensorMgr.unregisterListener(listener); 
	} 
   

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
