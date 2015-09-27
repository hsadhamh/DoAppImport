package factor.labs.indiancalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import factor.labs.indiancalendar.CalendarInterfaces.CalendarGetContextInterface;

import factor.labs.indiancalendar.CalendarUI.CalendarDialogUI.core.MaterialDialog;
import factor.labs.indiancalendar.CalendarUI.CalendarRateUsLib.RateThisApp;
import factor.labs.indiancalendar.CalendarUI.CalendarViews.CalendarAboutUsFragment;
import factor.labs.indiancalendar.CalendarUI.CalendarViews.CalendarMdScheduleView;
import factor.labs.indiancalendar.CalendarUI.CalendarViews.CalendarMonthViewFragment;
import factor.labs.indiancalendar.CalendarUI.CalendarViews.CalendarReligiousScheduleEvent;
import factor.labs.indiancalendar.CalendarUI.CalendarViews.CalendarReligiousScheduleView;
import factor.labs.indiancalendar.CalendarUI.CalendarViews.CalendarScheduleViewFragment;
import factor.labs.indiancalendar.CalendarUtils.CalendarConstants;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;

//
//  Calendar Main activity
//

public class CalendarMainActivity extends AppCompatActivity
        implements CalendarGetContextInterface {

    NavigationView moListSliderMenuItems;
    DrawerLayout moSliderMenuHolder;
    private ActionBarDrawerToggle moDrawerToggle;
    View moShadowView;
    Toolbar moActionBar = null;
    boolean mDrawerOpenState = false;

    Context moActivityContext;
    Context moApplicationContext;
    FragmentManager moSupportFragmentManger;

    private InterstitialAd interstitial;
    MenuItem prevMenuItem;
    String sTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            sTag = "CalendarMainActivity.onCreate";
            Log.d(sTag, "Creating calendar activity. Set layout activity_calendar_main -before");

            labsCalendarUtils.initializeBase();
            labsCalendarUtils.initDatabase(getApplicationContext());

            Log.d(sTag, "Creating calendar activity. Set layout activity_calendar_main");

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_calendar_main);
            Log.d(sTag, "Created calendar activity. Set constant object.");

            moSliderMenuHolder = (DrawerLayout) findViewById(R.id.calendar_slider_holder);

            moShadowView = (View) findViewById(R.id.id_cal_shadow_view);
            if(Build.VERSION.SDK_INT >= 21)
                moShadowView.setVisibility(View.GONE);

            setDrawerItemsAndActions();

            moActivityContext = CalendarMainActivity.this;
            moApplicationContext = getApplicationContext();
            moSupportFragmentManger = getSupportFragmentManager();
            // Set custom criteria
            RateThisApp.init(new RateThisApp.Config(3, 5));
            addMobileAdModule();
        }
        catch (Exception exec)
        {
            // exception caught
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle oBundle)
    {
        super.onSaveInstanceState(oBundle);
    }

    public void setDrawerItemsAndActions()
    {
        sTag = "CalendarMainActivity.setDrawerItemsAndActions";
        try {
            moActionBar = (Toolbar) findViewById(R.id.id_cal_action_bar);
            setSupportActionBar(moActionBar);

            moActionBar.setNavigationIcon(R.drawable.ic_nav_menu_cal);
            getSupportActionBar().setDisplayShowTitleEnabled(true);

            moActionBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(sTag, "Slider Menu icon clicked. State for drawer [" + mDrawerOpenState + "].");
                    if (mDrawerOpenState) {
                        moSliderMenuHolder.closeDrawer(GravityCompat.START);
                    } else {
                        moSliderMenuHolder.openDrawer(GravityCompat.START);
                    }
                }
            });

            moActionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.id_cal_action_home:
                            setMonthViewForFragment(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear());
                            break;
                        case R.id.id_cal_action_share:
                            ShareTheApp();
                            break;
                        /* hiding Preferences menu
                        case R.id.action_dialog:
                            ShowPopupMenu();
                            break;
                            */
                        default:
                            Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

            moListSliderMenuItems = (NavigationView) findViewById(R.id.calendar_list_slider);

            //Setting the first menu as selected
            moListSliderMenuItems.getMenu().getItem(0).setChecked(true);
            prevMenuItem= moListSliderMenuItems.getMenu().getItem(0);

            //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
            moListSliderMenuItems.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                // This method will trigger on item Click of navigation menu
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    //Checking if the item is in checked state or not, if not make it in checked state
                    if (prevMenuItem != null) {
                        prevMenuItem.setChecked(false);
                    }
                    menuItem.setChecked(true);
                    //Closing drawer on item click
                    moSliderMenuHolder.closeDrawers();
                    prevMenuItem = menuItem;

                  /* Bug in google support library so I replaced above code
                    if (menuItem.isChecked())
                    menuItem.setChecked(false);
                    else
                        menuItem.setChecked(true);
                    */
                    ShowElevationOrShadow();

                    //Check to see which item was being clicked and perform appropriate action
                    switch (menuItem.getItemId()) {

                        case R.id.id_cal_menu_home:
                            moListSliderMenuItems.setCheckedItem(R.id.id_cal_menu_home);
                            new LoadSharedPreference().execute(0);
                            return true;

                        case R.id.id_cal_menu_events_schedule:
                            HideElevationOrShadow();
                            moListSliderMenuItems.setCheckedItem(R.id.id_cal_menu_events_schedule);
                            setScheduleView();
                            return true;

                        case R.id.id_cal_menu_religious:
                            HideElevationOrShadow();
                            moListSliderMenuItems.setCheckedItem(R.id.id_cal_menu_religious);
                            setReligiousScheduleView();
                            return true;

                        case R.id.id_cal_menu_about:
                            setAboutUsPage();
                            return true;
                        case R.id.id_cal_menu_rate_app:
                            RateThisApp.showRateDialog(CalendarMainActivity.this);
                            return true;
                        case R.id.id_cal_menu_share_app:
                            ShareTheApp();
                            return true;

                        default:
                            Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                            return true;

                    }
                }
            });

            moDrawerToggle = new ActionBarDrawerToggle(this,moSliderMenuHolder,moActionBar ,R.string.app_name, R.string.app_name){
                public void onDrawerClosed(View view) {
                    mDrawerOpenState = false;
                    super.onDrawerClosed(view);
                }

                public void onDrawerOpened(View drawerView) {
                    mDrawerOpenState = true;
                    super.onDrawerOpened(drawerView);
                }
            };

            moSliderMenuHolder.setDrawerListener(moDrawerToggle);
            Log.d(sTag, "Completed drawer action settings.");
        }
        catch(Exception exec)
        {
            Log.e(sTag, "Exception caught : " + exec.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar_main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        sTag = "CalendarMainActivity.onPostCreate";
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        Log.d(sTag, "Sync state for onPostCreate.");
        //moDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        sTag = "CalendarMainActivity.onConfigurationChanged";
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        Log.d(sTag, "Configure for onConfigurationChanged.");
        moDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void setMonthViewForFragment(int nMon, int nYr)
    {
        sTag = "CalendarMainActivity.setMonthViewForFragment";
        //new LoadMonthUIAsync().execute(1);
        getSupportFragmentManager().beginTransaction().replace(R.id.calendar_frame_holder,
                CalendarMonthViewFragment.init(nMon, nYr)).commit();
        Log.d(sTag, "Set month view for fragment.");
    }

    public void setAboutUsPage()
    {
        sTag = "CalendarMainActivity.setAboutUsPage";
        CalendarAboutUsFragment fragment = CalendarAboutUsFragment.init();
        getSupportFragmentManager().beginTransaction().replace(R.id.calendar_frame_holder, fragment).commit();
        Log.d(sTag, "Set about us fragment.");
    }

    public void setScheduleView()
    {
        sTag = "CalendarMainActivity.setScheduleView";
        //new LoadMonthUIAsync().execute(2);
        getSupportFragmentManager().beginTransaction().replace(R.id.calendar_frame_holder,
                CalendarMdScheduleView.init(moSupportFragmentManger, moApplicationContext)).commit();
        Log.d(sTag, "Set about us fragment.");
    }

    public void setReligiousScheduleView()
    {
        sTag = "CalendarMainActivity.setReligiousScheduleView";
        //new LoadMonthUIAsync().execute(2);
        getSupportFragmentManager().beginTransaction().replace(R.id.calendar_frame_holder,
                CalendarReligiousScheduleView.init(moSupportFragmentManger, moApplicationContext)).commit();
        Log.d(sTag, "Set religious fragment.");
    }

    @Override
    protected void onStart() {
        sTag = "CalendarMainActivity.onStart";

        super.onStart();
        // Monitor launch times and interval from installation
        RateThisApp.onStart(this);
        // Show a dialog if criteria is satisfied
        RateThisApp.showRateDialogIfNeeded(this);
        Log.d(sTag, "Show rate us view, if needed.");
    }

    @Override
    protected void onResume() {
        sTag = "CalendarMainActivity.onResume";
        super.onResume();
        labsCalendarUtils.initDatabase(getApplicationContext());
        new LoadSharedPreference().execute(1);
        Log.d(sTag, "base initialize.");
    }

    void ShowPopupMenu() {
        sTag = "CalendarMainActivity.onShowPopupMenu";
        Log.d(sTag, "Prefs selected..");
        new MaterialDialog.Builder(CalendarMainActivity.this)
                .title("Select Country")
                .items(R.array.fl_arr_country)
                .itemsCallbackSingleChoice(labsCalendarUtils.getCountryPref(), new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        SaveCountryPreference(getApplicationContext(), "" + which);
                        refreshScreen();
                        return true;
                    }
                }).show();
    }

    void addMobileAdModule()
    {
        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(CalendarMainActivity.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId("ca-app-pub-7462033170287511/4051707386");

        //Locate the Banner Ad in activity_main.xml
        final AdView adView = (AdView) this.findViewById(R.id.adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        });


        // Request for Ads
        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Banner Ads
        adView.loadAd(adRequest);

        // Load ads into Interstitial Ads
        interstitial.loadAd(adRequest);

        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                // If Ads are loaded, show Interstitial else show nothing.
                if (interstitial.isLoaded()) {
                    interstitial.show();
                }
            }
        });
    }

    @Override
    public Context getContextActivity() {
        return getApplicationContext();
    }

    @Override
    public android.support.v4.app.FragmentManager getActivityFragmentManager()
    {
        return  getSupportFragmentManager();
    }

    public void ShareTheApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Join hands to know about Indian Festivals!!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey dude!! Excited to share that 'Indian Calendar' helps to know holidays and festivals in India. To get app, click goo.gl/wmjCgJ.");
        startActivity(Intent.createChooser(shareIntent, "Share about us"));
    }

    public void SaveCountryPreference(Context context, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(CalendarConstants.CALENDAR_EVENT_SHARED_PREF, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(CalendarConstants.CALENDAR_SHARED_PREF_COUNTRY, text);
        editor.commit();

        labsCalendarUtils.setCountryPref(Integer.parseInt(text));
    }

    public String getCountryPreference(Context context) {
        SharedPreferences settings;
        String text = "" + 1;
        try {
            //settings = context.getSharedPreferences(CalendarConstants.CALENDAR_EVENT_SHARED_PREF, Context.MODE_PRIVATE); //1
            //text = settings.getString(CalendarConstants.CALENDAR_SHARED_PREF_COUNTRY, null); //2
            if(text == null || text.isEmpty())
                text = "" + 0;

        }
        catch (Exception ex){
            Log.e("getCountryPreference", "Exception caught : " + ex.getMessage());
        }
        return text;
    }

    public void refreshScreen()
    {
        android.support.v4.app.Fragment fragment = moSupportFragmentManger.findFragmentById(R.id.calendar_frame_holder);
        if(fragment == null || fragment instanceof CalendarMonthViewFragment)
        {
            setMonthViewForFragment(((CalendarMonthViewFragment) fragment).getCurrentMonthInView(), ((CalendarMonthViewFragment) fragment).getCurrentYearInView());
        }
        else if(fragment instanceof CalendarMdScheduleView)
        {
            setScheduleView();
        }
        else if(fragment instanceof CalendarReligiousScheduleView)
        {
            setReligiousScheduleView();
        }
    }

    private class LoadSharedPreference extends AsyncTask<Integer, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(params[0] == 1)
                labsCalendarUtils.setCountryPref(Integer.parseInt(getCountryPreference(getApplicationContext())));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            setMonthViewForFragment(labsCalendarUtils.getCurrentMonth(), labsCalendarUtils.getCurrentYear());
        }
    }

    public void HideElevationOrShadow()
    {
        if (Build.VERSION.SDK_INT <= 21)
            moShadowView.setVisibility(View.GONE);
        else
            getSupportActionBar().setElevation(0);
    }

    public void ShowElevationOrShadow() {
        if (Build.VERSION.SDK_INT <= 21)
            moShadowView.setVisibility(View.VISIBLE);
        else
            getSupportActionBar().setElevation(4);
    }
}
