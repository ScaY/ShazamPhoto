package fr.isen.shazamphoto.ui;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.isen.shazamphoto.R;
import fr.isen.shazamphoto.database.Monument;
import fr.isen.shazamphoto.events.EventSearchMonumentByName;
import fr.isen.shazamphoto.model.ModelNavigation;
import fr.isen.shazamphoto.ui.ItemUtils.SearchableItem;
import fr.isen.shazamphoto.ui.SlidingTab.SlidingTabLayout;
import fr.isen.shazamphoto.utils.GetMonumentTask.GetMonumentsByName;
import fr.isen.shazamphoto.views.ViewDetailMonument;
import fr.isen.shazamphoto.views.ViewMonumentsResult;
import fr.isen.shazamphoto.views.ViewUndentifiedMonument;

public class Home extends ActionBarActivity implements SearchableItem {

    private static SearchView searchView;
    private MenuItem searchMenuItem;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ModelNavigation modelNavigation;
    private NetworkInfoArea networkInfo;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set the Model of the navigation with his views
        this.modelNavigation = new ModelNavigation();
        this.modelNavigation.addView(new ViewMonumentsResult());
        this.modelNavigation.addView(new ViewDetailMonument());
        this.modelNavigation.addView(new ViewUndentifiedMonument());

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(sectionsPagerAdapter);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

        networkInfo = (NetworkInfoArea) findViewById(R.id.home_info_network);

        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.actionbar_title, null);

        TextView title = (TextView) v.findViewById(R.id.title);

        title.setText(this.getTitle());

        this.getSupportActionBar().setCustomView(v);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        setListenerSearchViewListener(searchView, this);

        // Hide the keyboard

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

        this.menu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Hide the keyboard
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Set the  focus on the search bar
        switch (item.getItemId()) {
            case R.id.action_search:
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setListenerSearchViewListener(final SearchView searchView, final Home home) {

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    // Close the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);


                    // Hide the list view of the result
                    /*Shazam shazam = (Shazam) getSectionsPagerAdapter().getItem(Shazam.POSITION);
                    shazam.hideUIResult();
                    shazam.clearMonuments();*/
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Shazam shazam = (Shazam) getSectionsPagerAdapter().getItem(Shazam.POSITION);
                shazam.displayLoading();

                // Make the search
                GetMonumentsByName getMonumentsByName = new GetMonumentsByName(networkInfo, home, home, query);
                getMonumentsByName.execute();
                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                // Hide the searchview
                searchView.setIconified(true);
                searchView.clearFocus();

                if (menu != null) {
                    (menu.findItem(R.id.action_search)).collapseActionView();
                }
                return true;
            }
        });
    }

    @Override
    public void onPostSearch(ArrayList<Monument> monuments) {
        Shazam shazam = (Shazam) getSectionsPagerAdapter().getItem(Shazam.POSITION);
        modelNavigation.changeAppView(new EventSearchMonumentByName(monuments, shazam, this));

        //Set the view on the shazam fragment
        mViewPager.setCurrentItem(Shazam.POSITION);
        sectionsPagerAdapter.getItem(Shazam.POSITION);
    }

    public SectionsPagerAdapter getSectionsPagerAdapter() {
        return sectionsPagerAdapter;
    }

    public ModelNavigation getModelNavigation() {
        return modelNavigation;
    }

}
