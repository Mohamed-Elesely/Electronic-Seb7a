package com.example.electronicseb7a;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.WindowInsetsController;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView welcome_text, counter_text; // TextView to display welcome message and counter value
    Button btn; // Button to increment the counter
    ImageView reset_button; // ImageView for reset button
    int counter = 0; // Counter variable to track the count

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make the activity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide the navigation bar and make the app full screen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // Check if the device is running on Android 11 (or later)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Hide the navigation bar using the WindowInsetsController (Android 11 and later)
            getWindow().getInsetsController().hide(WindowInsets.Type.navigationBars());
            getWindow().setDecorFitsSystemWindows(false);
        }

        setContentView(R.layout.activity_main);

        // Initialize UI elements
        btn = findViewById(R.id.btn);
        counter_text = findViewById(R.id.counter_text);
        counter_text.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_animation));
        reset_button = findViewById(R.id.reset_button);

        // Animation for button click
        final Animation alphaButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_button_animation);

        // Animation listener for button animation
        alphaButtonAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btn.clearAnimation(); // Clear the animation after it ends
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        // Button click listener
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter_text.setText("" + increaseCounter()); // Increment the counter and update the TextView
                animateCounterText(); // Animate the counter text
                if (counter == 1) {
                    showResetButton(); // Show the reset button when the counter reaches 1
                    reset_button.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_animation));
                }
                if (btn.getAnimation() == null) { // Start the animation only if it's not already running
                    btn.startAnimation(alphaButtonAnimation);
                }
            }
        });

        // Reset button click listener
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResetConfirmationDialog(); // Show the reset confirmation dialog
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemBars(); // Call the method to hide the system bars when the window gains focus
        }
    }

    private int increaseCounter() {
        return ++counter; // Increment the counter and return the updated value
    }

    private void animateCounterText() {
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);
        counter_text.startAnimation(scaleAnimation); // Apply the scale animation to the counter text
    }

    private void showResetButton() {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
        reset_button.setVisibility(View.VISIBLE); // Show the reset button
        reset_button.startAnimation(fadeInAnimation); // Apply the fade-in animation to the reset button
    }

    private void showResetConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Reset Confirmation")
                .setMessage("Are you sure you want to reset?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Reset the counter
                        counter = 0;
                        counter_text.setText("0"); // Update the counter text to display 0
                        reset_button.setVisibility(View.INVISIBLE); // Hide the reset button
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show(); // Show the reset confirmation dialog
    }

    private void hideSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Hide system bars using the WindowInsetsController (Android 11 and later)
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                getWindow().getDecorView().setOnApplyWindowInsetsListener((v, insets) -> {
                    insetsController.show(WindowInsets.Type.systemBars());
                    return insets;
                });
            }
        } else {
            // Hide system bars using SYSTEM_UI_FLAG_HIDE_NAVIGATION and SYSTEM_UI_FLAG_IMMERSIVE_STICKY (pre-Android 11)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
