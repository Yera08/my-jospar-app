package kz.yerzhan.jospar.SaveLang;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {

    private Context context;
    private SharedPreferences sharedPreferences;
    public LanguageManager(Context ctx){
        context = ctx;
        sharedPreferences = context.getSharedPreferences("LANG", Context.MODE_PRIVATE);

    }
    public void updateResource(String code){
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        setLang(code);
    }
    public void setLang(String code){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", code);
        editor.apply();
    }
    public String getLang(){
       return sharedPreferences.getString("language", "ru");
    }
}


























