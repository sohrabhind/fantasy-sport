package com.hindbyte.redfun.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.hindbyte.redfun.R;
import com.hindbyte.redfun.fragment.MatchesFragment;
import com.hindbyte.redfun.fragment.MyMatchesFragment;
import com.hindbyte.redfun.utils.SQLiteHandler;
import com.hindbyte.redfun.utils.SessionManager;

public class MatchesActivity extends AppCompatActivity {

    DrawerLayout drawer;
    TabLayout tabLayout;
    Fragment matches;
    Fragment myMatches;
    ViewPager viewPager;
    TextView actionBalance;
    LinearLayout actionAccount;
    LinearLayout actionRules;
    LinearLayout actionShare;
    LinearLayout actionRate;
    TextView winners;
    ViewPagerAdapter adapter;
    SessionManager session;
    SQLiteHandler db;
    String balance;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new SQLiteHandler(MatchesActivity.this);
        session = new SessionManager(MatchesActivity.this);
        if (!session.isLoggedIn()) {
            logoutUser();
        }
        HashMap<String, String> user = db.getUserDetails();
        balance = user.get("balance");
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        actionBalance = headerView.findViewById(R.id.action_balance);
        actionAccount = headerView.findViewById(R.id.action_account);
        actionRules = headerView.findViewById(R.id.action_rules);
        actionShare = headerView.findViewById(R.id.action_share);
        actionRate = headerView.findViewById(R.id.action_rate);
        actionBalance.setText("₹ " + balance);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabs);
        winners = findViewById(R.id.winners);
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        winners.setOnClickListener(view -> {
            Intent intent = new Intent(this, WinnersActivity.class);
            startActivity(intent);
        });
        iniMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void iniMenu() {
        actionAccount.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        });
        actionRules.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra("TITLE", "Rules");
            intent.putExtra("URL", "https://www.hindbyte.com/web/rules/");
            startActivity(intent);
        });
        actionShare.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Download Redfun App \nhttps://play.google.com/store/apps/details?id=" + getPackageName());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share via"));
        });
        actionRate.setOnClickListener(v ->{
            drawer.closeDrawer(GravityCompat.START);
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + getPackageName())));
            } catch (android.content.ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        matches = MatchesFragment.newInstance();
        adapter.addFragment(matches, "Matches");
        adapter.notifyDataSetChanged();

        myMatches = MyMatchesFragment.newInstance();
        adapter.addFragment(myMatches, "My Matches");
        adapter.notifyDataSetChanged();

        viewPager.setOffscreenPageLimit(tabLayout.getTabCount());
    }


    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        finishAffinity();
        Intent intent = new Intent(MatchesActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (tabLayout != null) {
            if (tabLayout.getSelectedTabPosition() == 0) {
                finishAffinity();
            } else {
                Objects.requireNonNull(tabLayout.getTabAt(0)).select();
            }
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
