package com.hok.hokfocus;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;
import java.util.*;


class exampleEdit extends AppCompatActivity{
    private EditText editText = findViewById(R.id.editTextTime);
    public void changeText() {
        editText.setText("this worked");
    }
}
public class MainActivity extends AppCompatActivity {

//    private static final String CLIENT_ID = System.getenv("SPOTIFY_CLIENT_ID");
    private boolean timerButtonClicked= false;


    CountDownTimer countDownTimerInstance;


    private static final String REDIRECT_URI = "hokfocus://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    EditText timeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Editable	getEditableText()
        //Return the text that TextView is displaying as an Editable object.
        this.timeEditText = findViewById(R.id.editTextTime);

    }

    @Override
    protected void onStart() {
        // TODO Get input from the user as to Hours:Minutes:Seconds
        // TODO refractor code so that api call to spotify does not start automatically
        // TODO add functionality to make api calls that allow user to choose music or playlist
        super.onStart();
        // We will start writing our code here.
        // Set the connection parameters
        ApplicationInfo ai = null;
        try {
            ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Bundle bundle = ai.metaData;
        String myApiKey = bundle.getString("keyValue");
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(myApiKey)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    private void connected() {
        // Then we will write some more code here.
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });

    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);

    }

    private void clearState(Bundle savedInstanceState) {
        savedInstanceState.clear();
    }

    public void onClick(View view) throws ParseException {
        if(!this.timerButtonClicked) {
            System.out.println("timer is starting 0o0o0o0o0o0o");
            this.timerButtonClicked = true;
            this.countDownTimerInstance = new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                    timeEditText.setText("seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    timeEditText.setText("done!");
                }
            }.start();
            onStart();

        }
        else {
            this.timerButtonClicked = false;
            System.out.println("we're stopping the timer");
            timeEditText.setText("00:00:00");
            onStop();
            this.countDownTimerInstance.cancel();
            this.countDownTimerInstance = null;
        }
    }

    private void beginCountdown() {

        EditText editText = findViewById(R.id.editTextTime);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                editText.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                editText.setText("done!");
            }
        }.start();

    }
    private void enterCountdown(EditText editText) {
        //editTextTime

    }

}