package tk.st0pcha.cookie_clicker;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {
    private int cookies = 0;
    private TextView cookiesText;

    private TextView upgradeAmount;

    private TextView upgradePriceText;

    private TextView notEnoughCookies;

    private TextView cookiesPerSecond;

    private boolean musicStatus = true;

    private int addCookie;

    private int upgradeLevel = 0;

    private int upgradePrice = 50;

    private int cps = 0;

    private Random random = new Random();

    private MediaPlayer player;

    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView cookie = findViewById(R.id.cookie);
        ImageView upgrade = findViewById(R.id.btnUpgrade);
        ImageView music = findViewById(R.id.btnMusic);

        cookiesText = findViewById(R.id.cookies);
        upgradeAmount = findViewById(R.id.upgradeAmount);
        notEnoughCookies = findViewById(R.id.notEnoughCookies);
        upgradePriceText = findViewById(R.id.upgradePrice);
        cookiesPerSecond = findViewById(R.id.cookiesPerSecond);

        notEnoughCookies.setVisibility(View.INVISIBLE);

        // gachi music :)
        player = MediaPlayer.create(this, R.raw.music);
        player.setLooping(true);
        player.setVolume(15, 15);
        player.start();

        music.setOnClickListener(v -> {
            musicStatus = !musicStatus;

            if (musicStatus) {
                player.start();
                player.setLooping(true);
                music.setImageResource(R.drawable.play);
            } else {
                player.pause();
                music.setImageResource(R.drawable.mute);
            }
        });

        upgrade.setOnClickListener(v -> {
            if (!(upgradeLevel >= 16)) {
                if (cookies >= upgradePrice) {
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.cookie_animation);
                    animation.setAnimationListener(new CookieAnimation() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            cookieAnimation("-", upgradePrice);
                            upgradeLevel++;
                            cookies -= upgradePrice;
                            upgradePrice += Math.round(upgradeLevel * 500 + 15 * 3 / 1.5);
                            upgradeAmount.setText(upgradeLevel + "/16");

                            upgradePriceText.setText(upgradePrice + "c");
                            cookiesText.setText(cookies + "c");
                        }
                    });
                    v.startAnimation(animation);
                } else {
                    notEnoughCookies.setVisibility(View.VISIBLE);
                    v.postDelayed(() -> notEnoughCookies.setVisibility(View.INVISIBLE), 1000);

                }
            }
        });

        cookie.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.cookie_animation);
            animation.setAnimationListener(new CookieAnimation() {
                @Override
                public void onAnimationStart(Animation animation) {
                    cookieClick();
                }
            });
            v.startAnimation(animation);
        });
    }

    @SuppressLint("SetTextI18n")
    private void cookieClick() {
        if (upgradeLevel != 0) {
            addCookie = (int) Math.round(ThreadLocalRandom.current().nextInt(1, 3 + 1)
                    + (upgradeLevel / 1.5));
        } else {
            addCookie = 1;
        }

        // very awful version of cps :\

        cps++;
        cookiesPerSecond.setText(cps + "c/s");

        cookiesPerSecond.postDelayed(() -> {
            cps = 0;
            cookiesPerSecond.setText(cps + "c/s");
        }, 1000);

        cookies += addCookie;
        cookiesText.setText(cookies + "c");
        cookieAnimation("+", addCookie);
    }

    @SuppressLint({"SetTextI18n", "RtlHardcoded"})
    private void cookieAnimation(String symbol, int i) {
        final Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER | Gravity.LEFT, random.nextInt(500) + 100, random.nextInt(500) - 300);
        toast.setDuration(Toast.LENGTH_SHORT);
        TextView textView = new TextView(this);
        textView.setText(symbol + i + "c");
        textView.setTextSize(40f);
        textView.setTextColor(Color.BLACK);
        toast.setView(textView);

        CountDownTimer countDownTimer = new CountDownTimer(500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            @Override
            public void onFinish() {
                toast.cancel();
            }
        };
        toast.show();
        countDownTimer.start();
    }
}