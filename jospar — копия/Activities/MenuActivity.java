package kz.yerzhan.jospar.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import kz.yerzhan.jospar.SaveLang.AppCompat;
import kz.yerzhan.jospar.SaveLang.LanguageManager;
import kz.yerzhan.jospar.MenuActivities.FiveYearGoalsActivity;
import kz.yerzhan.jospar.MenuActivities.FourMonthPlanActivity;
import kz.yerzhan.jospar.MenuActivities.OneYearGoalsActivity;
import kz.yerzhan.jospar.MenuActivities.TenYearGoalsActivity;
import kz.yerzhan.jospar.R;

public class MenuActivity extends AppCompat {
    private Button goalsOfTenYearBtn, goalsOfFiveYearBtn, goalsOfOneYearBtn, fourMonthPlanBtn, btnLangKk, btnLangRu, developerBtn;
    private EditText nameUserET;
    private static final String PREFS_FILE = "Account";
    private static final String PREFS_NAME = "Name";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        goalsOfTenYearBtn = findViewById(R.id.goalsOfTenYearBtn);
        goalsOfFiveYearBtn = findViewById(R.id.goalsOfFiveYearBtn);
        goalsOfOneYearBtn = findViewById(R.id.goalsOfOneYearBtn);
        fourMonthPlanBtn = findViewById(R.id.fourMonthPlanBtn);
        nameUserET = findViewById(R.id.nameUserET);
        bottomNavigationView = findViewById(R.id.bottomNav);
        btnLangKk = findViewById(R.id.btnLangKk);
        btnLangRu = findViewById(R.id.btnLangRu);
        developerBtn = findViewById(R.id.developerBtn);

        LanguageManager languageManager = new LanguageManager(this);

        sharedPref = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        String name = sharedPref.getString(PREFS_NAME, "User");
        nameUserET.setText(name);

        btnLangKk.setOnClickListener(view -> {
            languageManager.updateResource("kk");
            recreate();
        });

        btnLangRu.setOnClickListener(view -> {
            languageManager.updateResource("ru");
            recreate();
        });

        bottomNavigationView.setSelectedItemId(R.id.menu);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();
            if (itemID == R.id.menu) {
                return true;
            } else if (itemID == R.id.main) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                return true;
            } else if (itemID == R.id.note) {
                startActivity(new Intent(getApplicationContext(), ToDoActivity.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                return true;
            }
            return false;
        });

        goalsOfTenYearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TenYearGoalsActivity.class));
            }
        });

        goalsOfFiveYearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FiveYearGoalsActivity.class));
            }
        });
        goalsOfOneYearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OneYearGoalsActivity.class));
            }
        });
        fourMonthPlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FourMonthPlanActivity.class));
            }
        });
        developerBtn.setOnClickListener(view -> {
            getTelegramDev(this);
        });


    }

    private void getTelegramDev(MenuActivity menuActivity) {
        Intent intent;

        try {
            try {
                menuActivity.getPackageManager().getPackageInfo("org.telegram.messenger", 0);
            } catch (PackageManager.NameNotFoundException e) {
                menuActivity.getPackageManager().getPackageInfo("org.thunderdog.challegram", 0);
            }
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=dilshod_software"));
            startActivity(intent);

        } catch (PackageManager.NameNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/+77779177223"));
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String name = nameUserET.getText().toString();
        // Сохранение в настройках
        editor = sharedPref.edit();
        editor.putString(PREFS_NAME, name);
        editor.apply();
    }

}





































