package net.along.dragonflyfm.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.along.dragonflyfm.R;
import net.along.dragonflyfm.fragment.AnalyzeFragment;
import net.along.dragonflyfm.fragment.RadioFragment;
import net.along.dragonflyfm.fragment.SearchesFragment;
import net.along.fragonflyfm.util.PlayerActivity;


/**
 * @author 陈泰龙
 */
public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private FloatingActionButton mActionButton;
    private AnalyzeFragment mAnalyzeFragment;
    private RadioFragment mRadioFragment;
    private SearchesFragment mSearchesFragment;
    private BottomNavigationView button;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        mAnalyzeFragment = new AnalyzeFragment();
        mRadioFragment = new RadioFragment();
        mSearchesFragment = new SearchesFragment();
        button = findViewById(R.id.button_rg_tab_bar);
        button.setOnNavigationItemSelectedListener(navListener);
        button.getMenu().getItem(1).setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,mSearchesFragment).commit();
        mActionButton = findViewById(R.id.to_playing);
        mActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
            startActivity(intent);
        });
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener= menuItem -> {
        switch (menuItem.getItemId()){
            case R.id.nav_menu_find:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,mSearchesFragment).commit();
                break;
            case R.id.nav_menu_album:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,mRadioFragment).commit();
                break;
            case R.id.nav_menu_analyse:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,mAnalyzeFragment).commit();
                break;
        }
        return true;
    };

    @Override
    public void onBackPressed() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }
}
