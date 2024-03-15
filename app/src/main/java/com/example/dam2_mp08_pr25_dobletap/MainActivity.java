package com.example.dam2_mp08_pr25_dobletap;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.String.valueOf;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor sensor;
    SensorEventListener sensorListener;
    private long tiempoAnterior = 0;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView xValue = findViewById(R.id.xValue);
        TextView yValue = findViewById(R.id.yValue);
        TextView zValue = findViewById(R.id.zValue);

        sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                // Valors de l'acceleròmetre en m/s^2
                float xAcc = sensorEvent.values[0];
                float yAcc = sensorEvent.values[1];
                float zAcc = sensorEvent.values[2];

                xValue.setText(((Float)xAcc).toString());
                yValue.setText(((Float)yAcc).toString());
                zValue.setText(((Float)zAcc).toString());

                detectarDobleTap(zAcc);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                // Es pot ignorar aquesta CB de moment
            }
        };

        // Seleccionem el tipus de sensor (veure doc oficial)
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // registrem el Listener per capturar els events del sensor
        if( sensor!=null ) {
            sensorManager.registerListener(sensorListener,sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void detectarDobleTap(float zAcc) {
        long actual = SystemClock.uptimeMillis();
        long intervalo = actual - tiempoAnterior;

        if (zAcc < -9.5 && intervalo < 1200) {
            Toast.makeText(this, "Double Tap Detected!", Toast.LENGTH_SHORT).show();
            tiempoAnterior = 0; // Reset last tap time after detecting double tap
        } else if (zAcc < -9.5) {
            tiempoAnterior = actual;
        }
    }
}

