/*
 * Created by Christian Schabesberger on 02.08.16.
 * <p>
 * Copyright (C) Christian Schabesberger 2016 <chris.schabesberger@mailbox.org>
 * DownloadActivity.java is part of NewPipe.
 * <p>
 * NewPipe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * NewPipe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with NewPipe.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.yolomate.Zentube;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yolomate.Zentube.fragments.BackPressable;
import com.yolomate.Zentube.fragments.detail.VideoDetailFragment;
import com.yolomate.Zentube.util.ServiceHelper;
import com.yolomate.Zentube.util.StateSaver;
import com.yolomate.Zentube.util.ThemeHelper;

import org.schabi.newpipe.extractor.StreamingService;

import com.yolomate.Zentube.fragments.MainFragment;
import com.yolomate.Zentube.fragments.list.search.SearchFragment;
import com.yolomate.Zentube.util.Constants;
import com.yolomate.Zentube.util.NavigationHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final boolean DEBUG = !BuildConfig.BUILD_TYPE.equals("release");

    private ActionBarDrawerToggle toggle = null;

    /*//////////////////////////////////////////////////////////////////////////
    // Activity's LifeCycle
    //////////////////////////////////////////////////////////////////////////*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

        ThemeHelper.setTheme(this, ServiceHelper.getSelectedServiceId(this));

        super.onCreate(savedInstanceState);
        setContentView(com.yolomate.Zentube.R.layout.activity_main);

        if (getSupportFragmentManager() != null && getSupportFragmentManager().getBackStackEntryCount() == 0) {
            initFragments();
        }

        setSupportActionBar(findViewById(com.yolomate.Zentube.R.id.toolbar));
        setupDrawer();
        setupDrawerFooter();
    }

    private void setupDrawer() {
        final Toolbar toolbar = findViewById(com.yolomate.Zentube.R.id.toolbar);
        final DrawerLayout drawer = findViewById(com.yolomate.Zentube.R.id.drawer_layout);
        final NavigationView drawerItems = findViewById(com.yolomate.Zentube.R.id.navigation);

        //drawerItems.setItemIconTintList(null); // Set null to use the original icon
        drawerItems.getMenu().getItem(ServiceHelper.getSelectedServiceId(this)).setChecked(true);

        if (!BuildConfig.BUILD_TYPE.equals("release")) {
            toggle = new ActionBarDrawerToggle(this, drawer, toolbar, com.yolomate.Zentube.R.string.drawer_open, com.yolomate.Zentube.R.string.drawer_close);
            toggle.syncState();
            drawer.addDrawerListener(toggle);
            drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                private int lastService;

                @Override
                public void onDrawerOpened(View drawerView) {
                    lastService = ServiceHelper.getSelectedServiceId(MainActivity.this);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    if (lastService != ServiceHelper.getSelectedServiceId(MainActivity.this)) {
                        new Handler(Looper.getMainLooper()).post(MainActivity.this::recreate);
                    }
                }
            });

            drawerItems.setNavigationItemSelectedListener(item -> {
                if (item.getGroupId() == com.yolomate.Zentube.R.id.menu_services_group) {
                    drawerItems.getMenu().getItem(ServiceHelper.getSelectedServiceId(this)).setChecked(false);
                    ServiceHelper.setSelectedServiceId(this, item.getTitle().toString());
                    drawerItems.getMenu().getItem(ServiceHelper.getSelectedServiceId(this)).setChecked(true);
                }
                drawer.closeDrawers();
                return true;
            });
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    private void setupDrawerFooter() {
        ImageButton settings = findViewById(com.yolomate.Zentube.R.id.drawer_settings);
        ImageButton downloads = findViewById(com.yolomate.Zentube.R.id.drawer_downloads);
        ImageButton history = findViewById(com.yolomate.Zentube.R.id.drawer_history);

        settings.setOnClickListener(view -> NavigationHelper.openSettings(this) );
        downloads.setOnClickListener(view -> NavigationHelper.openDownloads(this));
        history.setOnClickListener(view -> NavigationHelper.openHistory(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            StateSaver.clearStateFiles();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean(Constants.KEY_THEME_CHANGE, false)) {
            if (DEBUG) Log.d(TAG, "Theme has changed, recreating activity...");
            sharedPreferences.edit().putBoolean(Constants.KEY_THEME_CHANGE, false).apply();
            // https://stackoverflow.com/questions/10844112/runtimeexception-performing-pause-of-activity-that-is-not-resumed
            // Briefly, let the activity resume properly posting the recreate call to end of the message queue
            new Handler(Looper.getMainLooper()).post(MainActivity.this::recreate);
        }

        if (sharedPreferences.getBoolean(Constants.KEY_MAIN_PAGE_CHANGE, false)) {
            if (DEBUG) Log.d(TAG, "main page has changed, recreating main fragment...");
            sharedPreferences.edit().putBoolean(Constants.KEY_MAIN_PAGE_CHANGE, false).apply();
            NavigationHelper.openMainActivity(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (DEBUG) Log.d(TAG, "onNewIntent() called with: intent = [" + intent + "]");
        if (intent != null) {
            // Return if launched from a launcher (e.g. Nova Launcher, Pixel Launcher ...)
            // to not destroy the already created backstack
            String action = intent.getAction();
            if ((action != null && action.equals(Intent.ACTION_MAIN)) && intent.hasCategory(Intent.CATEGORY_LAUNCHER)) return;
        }

        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (DEBUG) Log.d(TAG, "onBackPressed() called");

        Fragment fragment = getSupportFragmentManager().findFragmentById(com.yolomate.Zentube.R.id.fragment_holder);
        // If current fragment implements BackPressable (i.e. can/wanna handle back press) delegate the back press to it
        if (fragment instanceof BackPressable) {
            if (((BackPressable) fragment).onBackPressed()) return;
        }


        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else super.onBackPressed();
    }

    /**
     * Implement the following diagram behavior for the up button:
     * <pre>
     *              +---------------+
     *              |  Main Screen  +----+
     *              +-------+-------+    |
     *                      |            |
     *                      ▲ Up         | Search Button
     *                      |            |
     *                 +----+-----+      |
     *    +------------+  Search  |◄-----+
     *    |            +----+-----+
     *    |   Open          |
     *    |  something      ▲ Up
     *    |                 |
     *    |    +------------+-------------+
     *    |    |                          |
     *    |    |  Video    <->  Channel   |
     *    +---►|  Channel  <->  Playlist  |
     *         |  Video    <->  ....      |
     *         |                          |
     *         +--------------------------+
     * </pre>
     */
    private void onHomeButtonPressed() {
        // If search fragment wasn't found in the backstack...
        if (!NavigationHelper.tryGotoSearchFragment(getSupportFragmentManager())) {
            // ...go to the main fragment
            NavigationHelper.gotoMainFragment(getSupportFragmentManager());
        }
    }

    @SuppressLint("ShowToast")
    private void onHeapDumpToggled(@NonNull MenuItem item) {
        final boolean isHeapDumpEnabled = !item.isChecked();

        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean(getString(com.yolomate.Zentube.R.string.allow_heap_dumping_key), isHeapDumpEnabled).apply();
        item.setChecked(isHeapDumpEnabled);

        final String heapDumpNotice;
        if (isHeapDumpEnabled) {
            heapDumpNotice = getString(com.yolomate.Zentube.R.string.enable_leak_canary_notice);
        } else {
            heapDumpNotice = getString(com.yolomate.Zentube.R.string.disable_leak_canary_notice);
        }
        Toast.makeText(getApplicationContext(), heapDumpNotice, Toast.LENGTH_SHORT).show();
    }
    /*//////////////////////////////////////////////////////////////////////////
    // Menu
    //////////////////////////////////////////////////////////////////////////*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (DEBUG) Log.d(TAG, "onCreateOptionsMenu() called with: menu = [" + menu + "]");
        super.onCreateOptionsMenu(menu);

        Fragment fragment = getSupportFragmentManager().findFragmentById(com.yolomate.Zentube.R.id.fragment_holder);
        if (!(fragment instanceof VideoDetailFragment)) {
            findViewById(com.yolomate.Zentube.R.id.toolbar).findViewById(com.yolomate.Zentube.R.id.toolbar_spinner).setVisibility(View.GONE);
        }

        if (!(fragment instanceof SearchFragment)) {
            findViewById(com.yolomate.Zentube.R.id.toolbar).findViewById(com.yolomate.Zentube.R.id.toolbar_search_container).setVisibility(View.GONE);

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(com.yolomate.Zentube.R.menu.main_menu, menu);
        }

        if (DEBUG) {
            getMenuInflater().inflate(com.yolomate.Zentube.R.menu.debug_menu, menu);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        updateDrawerNavigation();

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem heapDumpToggle = menu.findItem(com.yolomate.Zentube.R.id.action_toggle_heap_dump);
        if (heapDumpToggle != null) {
            final boolean isToggled = PreferenceManager.getDefaultSharedPreferences(this)
                    .getBoolean(getString(com.yolomate.Zentube.R.string.allow_heap_dumping_key), false);
            heapDumpToggle.setChecked(isToggled);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (DEBUG) Log.d(TAG, "onOptionsItemSelected() called with: item = [" + item + "]");
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onHomeButtonPressed();
                return true;
            case com.yolomate.Zentube.R.id.action_settings:
                NavigationHelper.openSettings(this);
                return true;
            case com.yolomate.Zentube.R.id.action_show_downloads:
                return NavigationHelper.openDownloads(this);
            case com.yolomate.Zentube.R.id.action_about:
                NavigationHelper.openAbout(this);
                return true;
            case com.yolomate.Zentube.R.id.action_history:
                NavigationHelper.openHistory(this);
                return true;
            case com.yolomate.Zentube.R.id.action_toggle_heap_dump:
                onHeapDumpToggled(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*//////////////////////////////////////////////////////////////////////////
    // Init
    //////////////////////////////////////////////////////////////////////////*/

    private void initFragments() {
        if (DEBUG) Log.d(TAG, "initFragments() called");
        StateSaver.clearStateFiles();
        if (getIntent() != null && getIntent().hasExtra(Constants.KEY_LINK_TYPE)) {
            handleIntent(getIntent());
        } else NavigationHelper.gotoMainFragment(getSupportFragmentManager());
    }

    /*//////////////////////////////////////////////////////////////////////////
    // Utils
    //////////////////////////////////////////////////////////////////////////*/

    private void updateDrawerNavigation() {
        if (getSupportActionBar() == null) return;

        final Toolbar toolbar = findViewById(com.yolomate.Zentube.R.id.toolbar);
        final DrawerLayout drawer = findViewById(com.yolomate.Zentube.R.id.drawer_layout);

        final Fragment fragment = getSupportFragmentManager().findFragmentById(com.yolomate.Zentube.R.id.fragment_holder);
        if (fragment instanceof MainFragment) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            if (toggle != null) {
                toggle.syncState();
                toolbar.setNavigationOnClickListener(v -> drawer.openDrawer(GravityCompat.START));
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
            }
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> onHomeButtonPressed());
        }
    }

    private void handleIntent(Intent intent) {
        if (DEBUG) Log.d(TAG, "handleIntent() called with: intent = [" + intent + "]");

        if (intent.hasExtra(Constants.KEY_LINK_TYPE)) {
            String url = intent.getStringExtra(Constants.KEY_URL);
            int serviceId = intent.getIntExtra(Constants.KEY_SERVICE_ID, 0);
            String title = intent.getStringExtra(Constants.KEY_TITLE);
            switch (((StreamingService.LinkType) intent.getSerializableExtra(Constants.KEY_LINK_TYPE))) {
                case STREAM:
                    boolean autoPlay = intent.getBooleanExtra(VideoDetailFragment.AUTO_PLAY, false);
                    NavigationHelper.openVideoDetailFragment(getSupportFragmentManager(), serviceId, url, title, autoPlay);
                    break;
                case CHANNEL:
                    NavigationHelper.openChannelFragment(getSupportFragmentManager(), serviceId, url, title);
                    break;
                case PLAYLIST:
                    NavigationHelper.openPlaylistFragment(getSupportFragmentManager(), serviceId, url, title);
                    break;
            }
        } else if (intent.hasExtra(Constants.KEY_OPEN_SEARCH)) {
            String searchQuery = intent.getStringExtra(Constants.KEY_QUERY);
            if (searchQuery == null) searchQuery = "";
            int serviceId = intent.getIntExtra(Constants.KEY_SERVICE_ID, 0);
            NavigationHelper.openSearchFragment(getSupportFragmentManager(), serviceId, searchQuery);
        } else {
            NavigationHelper.gotoMainFragment(getSupportFragmentManager());
        }
    }
}
