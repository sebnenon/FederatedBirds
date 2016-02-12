package fr.sio.ecp.federatedbirds.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.auth.TokenManager;

// added followers to menu
public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkUserLogin();

        setContentView(R.layout.activity_main);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Fragment fragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_container, fragment)
                                .commit();
                        return true;
                    case R.id.followed:
                        fragment = new UserFollowedFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_container, fragment)
                                .commit();
                        return true;
                    case R.id.followers:
                        // new case
                        fragment = new UserFollowersFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_container, fragment)
                                .commit();
                        return true;
                    case R.id.settings:
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        return true;
                }
                ((DrawerLayout) findViewById(R.id.drawer)).closeDrawer(navigationView);
                return false;
            }

        });

        if (savedInstanceState == null) {
            HomeFragment fragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
        }

    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                (DrawerLayout) findViewById(R.id.drawer),
                toolbar,
                R.string.open_menu,
                R.string.close_menu
        );
        mDrawerToggle.syncState();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkUserLogin();
    }

    private void checkUserLogin() {
        if (TokenManager.getUserToken(this) == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

}
