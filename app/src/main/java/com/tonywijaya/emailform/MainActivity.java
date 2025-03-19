package com.tonywijaya.emailform;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button _kirimButton;
    private EditText _penerimaEditText, _subjekEditText, _pesanEditText;

    private ActivityResultLauncher<String> askPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermission = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (!isGranted) {
                        Toast.makeText(getApplicationContext(), "Aplikasi membutuhkan permission notificiation!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        initKirimButton();

        askNotificationPermission();
    }

    private void initKirimButton() {
        _kirimButton = findViewById(R.id.kirimButton);
        _penerimaEditText = findViewById(R.id.penerimaEditText);
        _subjekEditText = findViewById(R.id.subjekEditText);
        _pesanEditText = findViewById(R.id.pesanEditText);

        _kirimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationChannel channel = new NotificationChannel("twChannel", "TW", NotificationManager.IMPORTANCE_DEFAULT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(_subjekEditText.getText())
                        .setContentText(_penerimaEditText.getText() + " : " + _pesanEditText.getText())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setChannelId(channel.getId());

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.createNotificationChannel(channel);
                manager.notify(1, builder.build());
            }
        });
    }

    private void askNotificationPermission() {
        Log.d("*tw*", "Android version: " + Build.VERSION.SDK_INT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Log.d("*tw*", "Terpenuhi");

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                askPermission.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}