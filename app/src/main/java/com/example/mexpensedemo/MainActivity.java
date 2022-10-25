package com.example.mexpensedemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.mexpensedemo.adapter.RecyclerViewAdapter;
import com.example.mexpensedemo.model.Trip;
import com.example.mexpensedemo.model.TripViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mexpensedemo.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{
    protected NavController navController;
    private DrawerLayout drawerLayout;
    private ImageView ham_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerlayout);
        ham_menu = findViewById(R.id.ham_menu);

        ham_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.togglemenu);
        BottomNavigationView bottomMenu = findViewById(R.id.bottomMenu);

        bottomMenu.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeFragment:
                        NavigateToFragment(new HomeFragment());
                }
            }
        });


        NavHostFragment navHostFragment;

        navigationView.setItemIconTintList(null);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.frag_container);

        Log.d("log", "===>" + navHostFragment);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomMenu, navController);

    }
    public void NavigateBack() {
        navController.navigateUp();
    }

    public void NavigateToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, fragment).addToBackStack(null).commit();
    }
}