package com.comp313_002.crimestalker.Activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.comp313_002.crimestalker.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    // Creating a drawerLayout object to manipulate the sidebar menu
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Setting up toolbar and action bar elements
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // Mapping mDrawerLayout to the XML layout drawer_layout
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Creating and mapping a NavigationView object to the nav_view element in the view
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Setting a listener for the navigationView variable
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // Close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Switch statement to control which option is clicked on the navigation view
                        switch (menuItem.getItemId()){

                            // It is executed when the Crime Map option is clicked
                            case R.id.drawerMapItem: {
                                Toast.makeText(HomeActivity.this, "Launching map Activity", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(HomeActivity.this, CrimeMapActivity.class));
                                break;
                            }
                            // It is executed when the Report Crime option is clicked
                            case R.id.drawerReportCrime:{
                                startActivity(new Intent(HomeActivity.this, ReportCrimeActivity.class));
                                break;
                            }
                            // It is executed when the Crime History option is clicked
                            case R.id.drawerCrimeHistory:{
                                startActivity(new Intent(HomeActivity.this, CrimeHistoryActivity.class));
                                break;
                            }
                            // It is executed when the Read Crime option is clicked
                            case R.id.drawerReadCrime:{
                                startActivity(new Intent(HomeActivity.this, ReadCrime.class));
                                break;
                            }
                        }

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
    }

    /*
    @Author: Kenny Perroni
    This method is called once the home menu button is clicked
    This will open the drawer or sidebar menu to show the options or services of the app
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Getting the item clicked
        switch (item.getItemId()) {
            // If it is the home that was clicked then...
            case android.R.id.home:
                // Open the drawer
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    @Author: Kenny Perroni
    This method is executed when the logout label is clicked on the navigation view bar
    This method logs out the current user and returns them to the Login Activity
     */
    public void logout(View v){
        // Showing a message
        Toast.makeText(HomeActivity.this, "Logging out", Toast.LENGTH_LONG).show();
        // Using firebase's authentication service to log out user
        FirebaseAuth.getInstance().signOut();
        // Redirecting user to the Login Activity
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }

    public void test(View view){
        startActivity(new Intent(getApplicationContext(), CrimeHistoryActivity.class));
    }
}
