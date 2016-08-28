package factor.labs.indiancalendar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import factor.labs.indiancalendar.CalendarAdapters.CalendarHeaderListAdapter;
import factor.labs.indiancalendar.CalendarAdapters.CalendarWeekNameDisplayAdapter;
import factor.labs.indiancalendar.CalendarAdapters.DayonPentaMonthAdapter;
import factor.labs.indiancalendar.CalendarDbHelper.CalendarEventMaster;
import factor.labs.indiancalendar.CalendarInterfaces.CalendarDateClickListenerInterface;
import factor.labs.indiancalendar.CalendarInterfaces.DayOnHeaderClickListener;
import factor.labs.indiancalendar.CalendarInterfaces.IDayOnEventInfoClick;
import factor.labs.indiancalendar.CalendarObjects.CalendarEmptyEventListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarEventAdListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarEventDateListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarEventListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarEventMonthListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarMonthYearClass;
import factor.labs.indiancalendar.CalendarUI.CalendarDialogUI.core.MaterialDialog;
import factor.labs.indiancalendar.CalendarUI.CalendarHeaderList.HeaderListView;
import factor.labs.indiancalendar.CalendarUI.CalendarHeaderList.SectionAdapter;
import factor.labs.indiancalendar.CalendarUI.CalendarRateUsLib.RateThisApp;
import factor.labs.indiancalendar.CalendarUI.CalendarViews.CalendarViewPager;
import factor.labs.indiancalendar.CalendarUI.CalendarViews.DayOnMonthGridFragment;
import factor.labs.indiancalendar.CalendarUI.DayOnDatePicker.date.DatePickerDialog;
import factor.labs.indiancalendar.CalendarUI.DayOnFAB.FloatingActionButton;
import factor.labs.indiancalendar.CalendarUI.DayOnFAB.FloatingActionMenu;
import factor.labs.indiancalendar.CalendarUI.DayOnViews.DayOnTypeFaceSpan;
import factor.labs.indiancalendar.CalendarUtils.CalendarDateClass;
import factor.labs.indiancalendar.CalendarUtils.Typefaces;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.CalendarViewHolders.DayOnEventListAd;
import factor.labs.indiancalendar.CalendarViewHolders.DayOnEventListDate;
import factor.labs.indiancalendar.CalendarViewHolders.DayOnEventListEmpty;
import factor.labs.indiancalendar.CalendarViewHolders.DayOnEventListHeader;
import factor.labs.indiancalendar.CalendarViewHolders.DayOnEventListItem;
import factor.labs.indiancalendar.DayOnActivities.CalendarReligiousViewActivity;
import factor.labs.indiancalendar.DayOnActivities.DayOnPreferenceActivity;
import factor.labs.indiancalendar.DayOnActivities.DayOnScheduleViewActivity;
import factor.labs.indiancalendar.DayOnActivities.DayOnYearViewActivity;

/**
 * Created by hassanhussain on 9/30/2015.
 */
public class DayOnMonthHomeActivity extends AppCompatActivity implements
        DayOnHeaderClickListener,
        CalendarDateClickListenerInterface,
        IDayOnEventInfoClick {

    String sTag;
    int mnSelectedPage = 1;
    int mnSelectedSection = 1;
    int mnSelectedOffsetList = 0;
    int mnCurrentMonthInView, mnCurrentYearInView;
    int mLastFirstVisibleItem;
    int mnLastDateSelected;
    boolean mDrawerOpenState = false;
    MenuItem prevMenuItem;
    CalendarViewPager moPager = null;
    TextView moMonthName = null;
    HeaderListView moEventsList = null;
    LinearLayout moGridHeaderHolder = null;
    GridView moGridForWeekName = null;
    FloatingActionMenu moFabMenu = null;
    Toolbar moActionBar = null;
    FloatingActionButton moFabRefreshBtn = null;
    FloatingActionButton moFabFilterBtn = null;
    NavigationView moListSliderMenuItems;
    DrawerLayout moSliderMenuHolder;

    private ActionBarDrawerToggle moDrawerToggle;

    DayonPentaMonthAdapter moPageAdapter = null;
    CalendarHeaderListAdapter moListAdapter = null;

    CalendarDateClass moCurrentDateClass = null;
    private InterstitialAd interstitial;
    private AdRequest mAdRequest;

    List<Fragment> moListMonthFragments = new ArrayList<>(3);
    Map<Integer,Object> moListHeaders = new LinkedHashMap<>();
    Map<Integer, List<Object>> moListDayEvents = new LinkedHashMap<>();

    Queue<DayOnLoadMoreEventsObject> moQueForLoadEvents = new LinkedList<>();

    boolean mbAsyncRunning = false;
    boolean mbPageSelectedInPage = false;
    boolean mbLockScroll = false;

    AdView mAds;
    int mShouldShowInterstitial = 1;
    int mDoShowAdOnMonthChange = 1;
    Date mLastShownAdTime = null;
    long MAX_DURATION = 20*60*1000;
    final Object mSyncLock = new Object();
    boolean mbLoadBannerAdDone = false;
    /*boolean mStartType = false;
    private ScheduledExecutorService mScheduleExec;
    Runnable mRun = new Runnable() {
        public void run() {
            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                        // update your UI component here.
                        new ShouldLoadAd().execute();
                    }
                });
            } catch (Exception e) {
                Log.e("Timer Exception", e.getMessage());
            }
        }
    };*/

    class DayOnLoadMoreEventsObject{
        int UpOrDown;
        DayOnMonthGridFragment dayonObj;
    }

    public boolean onSwipeDetect() {
        // UP
        moGridHeaderHolder.setVisibility(View.GONE);
        moEventsList.showHeader();
        return true;
    }

    @Override
    public void onHandleClickForHeader() {
        moGridHeaderHolder.setVisibility(View.VISIBLE);
        moEventsList.hideHeader();
    }


    @Override
    protected void onResume() {
        super.onResume();
        labsCalendarUtils.initDatabase(getApplicationContext());
        /*mScheduleExec = Executors.newScheduledThreadPool(5);
        mScheduleExec.scheduleAtFixedRate(mRun, 0, 10, MINUTES);*/
    }

    @Override
    protected void onPause(){
        super.onPause();
        /*mScheduleExec.shutdown();
        mStartType = false;*/
    }

    void addMobileAdModule(){
        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(DayOnMonthHomeActivity.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId("ca-app-pub-7462033170287511/4051707386");
        // Request for Ads
        mAdRequest = new
                AdRequest.Builder()
                /*.addTestDevice("F3D0EE493657AD2952233060D190BFBF")*/
                .build();
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                // If Ads are loaded, show Interstitial else show nothing.
                if (interstitial.isLoaded()) {
                    interstitial.show();
                    mLastShownAdTime = Calendar.getInstance().getTime();
                }
            }
        });

        mAds = (AdView)findViewById(R.id.adView);
        mAds.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAds.setVisibility(View.VISIBLE);
                mbLoadBannerAdDone = true;
                moFabMenu.setTranslationY(-110.0f);
                super.onAdLoaded();
            }
        });
        // Load ads into Banner Ads
        mAds.loadAd(mAdRequest);
    }

    @Override
    protected void onSaveInstanceState(Bundle o){
        // do mothing
    }

    @Override
    protected void onRestoreInstanceState(Bundle o){
        // do mothing
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Monitor launch times and interval from installation
        RateThisApp.onStart(this);
        // Show a dialog if criteria is satisfied
        RateThisApp.showRateDialogIfNeeded(this);
    }

    @Override
    public void doProcessClickEventForDate(CalendarDateClass oDate) {
        mnLastDateSelected = oDate.getDate();
        if(oDate.isNextMonthDate())
        {
            setMonthName();
            mnSelectedOffsetList = 0;
            addNextMonthOnLeftSwipe();
            startAsyncTask(1);
            return;
        }
        else if(oDate.isPreviousMonthDate())
        {
            setMonthName();
            mnSelectedOffsetList = 0;
            addPreviousMonthOnRightSwipe();
            startAsyncTask(1);
            return;
        }
        refreshMonthGrid(mnLastDateSelected);
        moEventsList.setSelection(mnSelectedSection, oDate.getListOffset());
    }

    protected void onCreate(Bundle savedInstanceState) {
        sTag = "DayOnMonthHomeActivity.onCreate";
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layout_dayon_month_home);

            moMonthName = (TextView) findViewById(R.id.id_cal_txt_month);

            moPager = (CalendarViewPager) findViewById(R.id.id_cal_month_grid_pager);

            moEventsList = (HeaderListView) findViewById(R.id.id_cal_month_home_holder).findViewWithTag("header_list_tag");
            moGridHeaderHolder = (LinearLayout) findViewById(R.id.id_cal_grid_holder);
            moGridForWeekName = (GridView) findViewById(R.id.id_cal_week_names);

            moFabMenu = (FloatingActionMenu) findViewById(R.id.id_cal_fab_menu_actions);
            //moFabMenu.setTranslationY(100);

            moFabRefreshBtn = (FloatingActionButton) findViewById(R.id.id_cal_goto_fab);

            moFabFilterBtn = (FloatingActionButton) findViewById(R.id.id_cal_filter_fab);

            moActionBar = (Toolbar) findViewById(R.id.id_cal_action_bar);
            moListSliderMenuItems = (NavigationView) findViewById(R.id.calendar_list_slider);
            moSliderMenuHolder = (DrawerLayout) findViewById(R.id.calendar_slider_holder);

            // Set custom criteria
            RateThisApp.init(new RateThisApp.Config(3, 5));

            setToolBarActions();
            setFabActions();


            mnCurrentMonthInView = labsCalendarUtils.getCurrentMonth();
            mnCurrentYearInView = labsCalendarUtils.getCurrentYear();
            mnLastDateSelected = labsCalendarUtils.getTodaysDate();
            moEventsList.setHeaderTouchListener(this);
            moPager.setOffscreenPageLimit(1);

            addMobileAdModule();

            new LoadFirstMonths().execute();

            // sequence example
            /*
            // Removing showcase on request
            ShowcaseConfig config = new ShowcaseConfig();
            config.setDelay(500); // half second between each showcase view

            MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this,"SHOWCASE_ID");

            sequence.setConfig(config);

            sequence.addSequenceItem(moGridHeaderHolder,
                    "Swipe up to get full list events", "GOT IT");

           sequence.addSequenceItem(moFabMenu.getMenuIconView(), "Filter out events to select this option", "GOT IT");
            sequence.addSequenceItem(moEventsList, "Click the event to get details of the event", "GOT IT");
            sequence.start();*/

        }
        catch(Exception ex){
            Log.e(sTag," Exception : " + ex.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar_new, menu);
        return true;
    }

    public void setFabActions() {
        moFabMenu.setTranslationY(30.0f);
        moFabMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {

            @Override
            public void onMenuToggle(boolean opened) {
                moFabMenu.getMenuIconView().
                        setImageDrawable(
                                opened ?
                                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_plus_white_24dp)
                                        : ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_dots_horizontal_white_24dp));

            }


        });

        moFabFilterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowPopupMenu();
                moFabMenu.toggle(true);


            }

        });

        moFabRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDatePicker();
                moFabMenu.toggle(true);
            }
        });
    }

    public void setToolBarActions(){
        setSupportActionBar(moActionBar);

        moActionBar.setNavigationIcon(R.drawable.ic_nav_menu_cal);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        moActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        mnCurrentMonthInView = labsCalendarUtils.getCurrentMonth();
                        mnCurrentYearInView = labsCalendarUtils.getCurrentYear();
                        mnLastDateSelected = labsCalendarUtils.getTodaysDate();
                        new LoadFirstMonths().execute();
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        //Setting the first menu as selected
        moListSliderMenuItems.getMenu().getItem(0).setChecked(true);
        prevMenuItem = moListSliderMenuItems.getMenu().getItem(0);

        //  set typeface for all menu items
        for(int i =0; i < moListSliderMenuItems.getMenu().size(); i++){
            MenuItem mi = moListSliderMenuItems.getMenu().getItem(i);

            SpannableString mNewTitle = new SpannableString(mi.getTitle());
            mNewTitle.setSpan(new DayOnTypeFaceSpan("", Typefaces.getRobotoRegular(getApplicationContext())),
                    0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mi.setTitle(mNewTitle);
        }

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

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.id_cal_menu_home:
                        moListSliderMenuItems.setCheckedItem(R.id.id_cal_menu_home);
                        mnCurrentMonthInView = labsCalendarUtils.getCurrentMonth();
                        mnCurrentYearInView = labsCalendarUtils.getCurrentYear();
                        mnLastDateSelected = labsCalendarUtils.getTodaysDate();
                        new LoadFirstMonths().execute();
                        return true;

                    case R.id.id_cal_menu_year:
                        ShowTest();
                        return true;

                    case R.id.id_cal_menu_holiday:
                        ShowHolidays();
                        return true;

                    case R.id.id_cal_menu_religious:
                        ShowReligiousEvents();
                        return true;

                    case R.id.id_cal_menu_settings:
                        showPreferenceDialogs();
                        return true;

                    case R.id.id_cal_menu_rate_app:
                        RateThisApp.showRateDialog(DayOnMonthHomeActivity.this);
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
    }

    public void runInitialTask(){
        setWeekNameAdapter();

        setMonthPagerAdapter();
        setListEventsAdapter();

        moPager.setCurrentItem(mnSelectedPage);
        setPagerSelectListener();

        setEndlessScrollForList();

        setMonthName();
        doSetListAdapter(false);
        if(moGridHeaderHolder.getVisibility() == View.VISIBLE)
            moEventsList.hideHeader();
        else
            moEventsList.showHeader();
    }

    public void ShowDatePicker(){
        DatePickerDialog.OnDateSetListener oDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                mnCurrentYearInView = year;
                mnCurrentMonthInView = month+1;
                mnLastDateSelected = day;
                new LoadFirstMonths().execute();
            }
        };
        DatePickerDialog pickerDialog = DatePickerDialog.newInstance(oDateSetListener,
                labsCalendarUtils.getCurrentYear(), labsCalendarUtils.getCurrentMonth()-1,
                labsCalendarUtils.getTodaysDate(), false);
        pickerDialog.setYearRange(1985, 2028);
        pickerDialog.setCloseOnSingleTapDay(false);
        pickerDialog.show(getSupportFragmentManager(), "DatePicker");
    }

    public void startAsyncTask(int n){
        if (!mbAsyncRunning)
            new LoadMonthEvents().execute(n);
    }

    public void setWeekNameAdapter(){
        sTag = "DayOnMonthHomeActivity.setWeekNameAdapter";
        try {
            CalendarWeekNameDisplayAdapter oAdapter = new CalendarWeekNameDisplayAdapter(getApplicationContext());
            moGridForWeekName.setAdapter(oAdapter);
            Log.d(sTag, "week names set.");
        }
        catch(Exception exec)
        { Log.e(sTag, "Exception caught : " + exec.getMessage()); }
    }

    public void setPagerSelectListener(){
        moPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                mnSelectedPage = position;
                setMonthName();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    mnSelectedOffsetList = 0;
                    if (mnSelectedPage == 0) {
                        Log.d(sTag, "Month view adapter : swiped right!!");
                        addPreviousMonthOnRightSwipe();
                        updateListForMonth();

                    } else if (mnSelectedPage == 2) {
                        Log.d(sTag, "Month view adapter : swiped left!!");
                        addNextMonthOnLeftSwipe();
                        updateListForMonth();
                    }
                    mbPageSelectedInPage = true;
                    doLoadAd(1);
                    if (!mbAsyncRunning)
                        new LoadMonthEvents().execute(1);
                }
            }
        });
    }

    public void setMonthName() {
        DayOnMonthGridFragment fr = (DayOnMonthGridFragment) moListMonthFragments.get(mnSelectedPage);

        String strMonthName = labsCalendarUtils.getMonthName(fr.getMonth()) + " " + fr.getYear();
        moMonthName.setText(strMonthName);

        mnCurrentMonthInView = fr.getMonth();
        mnCurrentYearInView = fr.getYear();
    }

    public void setMonthPagerAdapter(){
        moPageAdapter = new DayonPentaMonthAdapter(this.getSupportFragmentManager(), moListMonthFragments);
        moPager.setAdapter(moPageAdapter);
    }

    public void setListEventsAdapter(){
        ListView oList = moEventsList.getListView();
        oList.setDivider(null);
        oList.setDividerHeight(0);
        moListAdapter = new CalendarHeaderListAdapter(getApplicationContext(),
                moListHeaders, moListDayEvents, this);
        moEventsList.setAdapter(moListAdapter);
        moListAdapter.notifyDataSetChanged();
    }

    public void onPrepareListMonths(){
        int mon = mnCurrentMonthInView;
        int yr = mnCurrentYearInView;

        moListMonthFragments.clear();

        int i = -1, nSeq = 0;
        while(i<2 && i>-2)
        {
            int n = i > 0 ? i : -i;
            CalendarMonthYearClass MonYrObj = new CalendarMonthYearClass(mon, yr);
            CalendarMonthYearClass resObj = null;
            if(i<0)
                resObj = labsCalendarUtils.subtractMonthYear(MonYrObj, n);
            else if(i>0)
                resObj = labsCalendarUtils.addMonthYear(MonYrObj, n);
            else if(i==0)
                resObj = MonYrObj;

            DayOnMonthGridFragment doMonGrid = DayOnMonthGridFragment.init(resObj.getMonth(), resObj.getYear(), this);
            moListMonthFragments.add(doMonGrid);

            moListHeaders.put(nSeq, doMonGrid.getMonthClass().getHeaderItem());
            moListDayEvents.put(nSeq, doMonGrid.getMonthClass().getEventsAndViewsForMonth());

            if(i==0) {
                moCurrentDateClass = doMonGrid.getMonthClass().getDateObject(mnLastDateSelected, resObj.getMonth());
                if(moCurrentDateClass != null)
                    mnSelectedOffsetList = moCurrentDateClass.getListOffset();
                doMonGrid.setSelectedDate(mnLastDateSelected);
            }

            DayOnLoadMoreEventsObject QueObj = new DayOnLoadMoreEventsObject();
            QueObj.UpOrDown= i==0? -1 : i<0? 0 : 1;
            QueObj.dayonObj = doMonGrid;
            moQueForLoadEvents.add(QueObj);

            i++;
            nSeq++;
        }
    }

    public void addPreviousMonthOnRightSwipe(){
        sTag = "CalendarMonthViewFragment.addPreviousMonthOnRightSwipe";
        try {
            DayOnMonthGridFragment currentMonth = (DayOnMonthGridFragment) moListMonthFragments.get(1);
            int nSelectedDate = currentMonth.getSelectedDate();

            for(int n=2; n>0; n--) {
                ((DayOnMonthGridFragment)moListMonthFragments.get(n - 1)).setSelectedDate(nSelectedDate);
                moListMonthFragments.set(n, moListMonthFragments.get(n - 1));
            }

            int mon = ((DayOnMonthGridFragment)moListMonthFragments.get(1)).getMonth();
            int yr = ((DayOnMonthGridFragment)moListMonthFragments.get(1)).getYear();

            Log.d(sTag, "Selected Date [" + nSelectedDate + "] month [" + mon + "] year [" + yr + "].");

            if (mon == 1) {
                mon = 12;
                yr--;
            } else {
                mon--;
            }
            Log.d(sTag, "Creating new view for Date [" + nSelectedDate + "] month [" + mon + "] year [" + yr + "].");

            DayOnMonthGridFragment newMonthView = DayOnMonthGridFragment.init(mon, yr, this);
            newMonthView.setSelectedDate(nSelectedDate);
            moListMonthFragments.set(0, newMonthView);

            DayOnLoadMoreEventsObject obj = new DayOnLoadMoreEventsObject();
            obj.UpOrDown = 0;
            obj.dayonObj = newMonthView;
            moQueForLoadEvents.add(obj);
        }
        catch (Exception exec)
        { Log.e(sTag, "Exception caught : " + exec.getMessage()); }
    }

    public void addNextMonthOnLeftSwipe(){
        sTag = "CalendarMonthViewFragment.addNextMonthOnLeftSwipe";
        try {
            DayOnMonthGridFragment currentMonth = (DayOnMonthGridFragment) moListMonthFragments.get(1);
            int nSelectedDate = currentMonth.getSelectedDate();

            for(int n=0; n<2; n++) {
                ((DayOnMonthGridFragment) moListMonthFragments.get(n+1)).setSelectedDate(nSelectedDate);
                moListMonthFragments.set(n, moListMonthFragments.get(n+1));
            }

            int mon = ((DayOnMonthGridFragment)moListMonthFragments.get(1)).getMonth();
            int yr = ((DayOnMonthGridFragment)moListMonthFragments.get(1)).getYear();

            Log.d(sTag, "Selected Date [" + nSelectedDate + "] month [" + mon + "] year [" + yr + "].");
            if (mon == 12) {
                mon = 1;
                yr++;
            } else
                mon++;

            Log.d(sTag, "Creating new view for Date [" + nSelectedDate + "] month [" + mon + "] year [" + yr + "].");
            DayOnMonthGridFragment newMonthView = DayOnMonthGridFragment.init(mon, yr, this);
            newMonthView.setSelectedDate(nSelectedDate);
            moListMonthFragments.set(2, newMonthView);

            DayOnLoadMoreEventsObject obj = new DayOnLoadMoreEventsObject();
            obj.UpOrDown = 1;
            obj.dayonObj = newMonthView;
            moQueForLoadEvents.add(obj);
        }
        catch (Exception exec)
        { Log.e(sTag, "Exception caught : " + exec.getMessage()); }
    }

    void updateListForMonth(){
        sTag = "CalendarMonthViewFragment.updateListForMonth";
        try {
            //  set changes to adapter and notify the changes
            moPageAdapter.setTriViews(moListMonthFragments);
            moPageAdapter.notifyDataSetChanged();
            Log.d(sTag, "Month view update..!!");
            //  set center screen for view.
            mnSelectedPage = 1;
            moPager.setCurrentItem(mnSelectedPage, false);
            setMonthName();
        }
        catch (Exception exec){ Log.e(sTag, "Exception caught : " + exec.getMessage()); }
    }

    void doSetListAdapter(boolean bLoadAgain){
        if(bLoadAgain) {
            moListAdapter.setHeaderList(moListHeaders);
            moListAdapter.setDayEventsList(moListDayEvents);
            moListAdapter.notifyDataSetChanged();
        }
        mLastFirstVisibleItem = moEventsList.setSelection(mnSelectedSection, mnSelectedOffsetList);
    }

    void UpdateMapListAndHeaderAtTop(){
        for(int i = 2; i>0; i--){
            moListHeaders.put(i, moListHeaders.get(i - 1));
            moListDayEvents.put(i, moListDayEvents.get(i - 1));
        }
    }

    void UpdateMapListAndHeaderAtBottom(){
        for(int i = 1; i <=2 ; i ++) {
            moListHeaders.put(i - 1, moListHeaders.get(i));
            moListDayEvents.put(i - 1, moListDayEvents.get(i));
        }
    }

    void setEndlessScrollForList(){
        moEventsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                    if(mbLoadBannerAdDone) {
                        mAds.setVisibility(View.VISIBLE);
                        moFabMenu.setTranslationY(-110.0f);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int offset = 0;
                SectionAdapter adap = moEventsList.getAdapter();

                if(mbLoadBannerAdDone) {
                    mAds.setVisibility(View.GONE);
                    moFabMenu.setTranslationY(30.0f);
                }

                if (adap != null && !mbLockScroll) {
                    View firstVisibleView = adap.getView(firstVisibleItem, null, moEventsList);
                    View secondVisibleView = adap.getView(firstVisibleItem + 1, null, moEventsList);
                    if (firstVisibleView != null) {

                        int mon = 0, yr = 0, date = 1;
                        Object obj = firstVisibleView.getTag();
                        Object obj1 = moEventsList.isHeaderVisible() ? secondVisibleView.getTag() : obj;

                        if (obj1 instanceof DayOnEventListHeader) {
                            DayOnEventListHeader castObj = (DayOnEventListHeader) obj1;
                            if (castObj.hidden != null) {
                                CalendarEventMonthListItem objList = (CalendarEventMonthListItem) castObj.hidden.getTag();
                                if (objList != null) {
                                    date = 1;
                                    mon = objList.getMonth();
                                    yr = objList.getYear();
                                    offset = objList.getOffset();
                                }
                            }
                        } else if (obj1 instanceof DayOnEventListEmpty) {
                            DayOnEventListEmpty castObj = (DayOnEventListEmpty) obj1;
                            if (castObj.hidden != null) {
                                CalendarEmptyEventListItem objList = (CalendarEmptyEventListItem) castObj.hidden.getTag();
                                if (objList != null) {
                                    date = objList.date;
                                    mon = objList.mon;
                                    yr = objList.yr;
                                    offset = objList.offset;
                                }
                            }
                        } else if (obj1 instanceof DayOnEventListDate) {
                            DayOnEventListDate castObj = (DayOnEventListDate) obj1;
                            if (castObj.hidden != null) {
                                CalendarEventDateListItem objList = (CalendarEventDateListItem) castObj.hidden.getTag();
                                if (objList != null) {
                                    date = objList.date;
                                    mon = objList.mon;
                                    yr = objList.yr;
                                    offset = objList.offset;
                                }
                            }
                        } else if (obj1 instanceof DayOnEventListItem) {
                            DayOnEventListItem castObj = (DayOnEventListItem) obj1;
                            if (castObj.hidden != null) {
                                CalendarEventListItem objList = (CalendarEventListItem) castObj.hidden.getTag();
                                if (objList != null) {
                                    date = objList.date;
                                    mon = objList.mon;
                                    yr = objList.yr;
                                    offset = objList.offset;
                                }
                            }
                        }
                        else if (obj1 instanceof DayOnEventListAd) {
                            DayOnEventListAd castObj = (DayOnEventListAd) obj1;
                            if (castObj.hidden != null) {
                                CalendarEventAdListItem objList = (CalendarEventAdListItem) castObj.hidden.getTag();
                                if (objList != null) {
                                    date = objList.date;
                                    mon = objList.mon;
                                    yr = objList.yr;
                                    offset = objList.offset;
                                }
                            }
                        }

                        moEventsList.setDayTitleToHeader(date, mon, yr);
                        if (!mbLockScroll) {
                            int n = CompareMonthYear(mon, yr);
                            if (n == -1) {
                                setMonthName();
                                mnSelectedOffsetList = offset;
                                addPreviousMonthOnRightSwipe();
                                startAsyncTask(1);
                                doLoadAd(1);
                            } else if (n == 1) {
                                setMonthName();
                                mnSelectedOffsetList = offset;
                                addNextMonthOnLeftSwipe();
                                startAsyncTask(1);
                                doLoadAd(1);
                            }

                            if (date != mnLastDateSelected) {
                                // refresh UI of GRID
                                refreshMonthGrid(date);
                            }
                        }

                        mnCurrentMonthInView = mon;
                        mnCurrentYearInView = yr;
                    }
                }
            }
        });
    }

    void refreshMonthGrid(int date){
        DayOnMonthGridFragment fr = ((DayOnMonthGridFragment) moListMonthFragments.get(mnSelectedPage));
        fr.setSelectedDate(date);
        mnLastDateSelected = date;
        fr.refreshMonthGridView();
    }

    int CompareMonthYear(int mon, int yr){

        if((mon < mnCurrentMonthInView && yr == mnCurrentYearInView)
            || (mon > mnCurrentMonthInView && yr < mnCurrentYearInView))
            return -1;
        else if((mon > mnCurrentMonthInView && yr == mnCurrentYearInView) ||
                (mon < mnCurrentMonthInView && yr > mnCurrentYearInView))
            return 1;
        return 0;

    }

    public void ShowPopupMenu() {
        sTag = "CalendarMainActivity.onShowPopupMenu";
        Log.d(sTag, "Prefs selected..");
        new MaterialDialog.Builder(DayOnMonthHomeActivity.this)
                .title("Filter Options")
                .items(R.array.event_category)
                .itemsCallbackSingleChoice(labsCalendarUtils.getShowPreference(), new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        SaveShowPreference("" + which);
                        new UpdateMonthAndEvents().execute();
                        return true;
                    }
                }).show();
    }

    public void ShareTheApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Join hands to know about Indian Festivals!!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey dude!! Excited to share that 'Indian Calendar' helps to know holidays and festivals in India. To get app, click goo.gl/wmjCgJ.");
        startActivity(Intent.createChooser(shareIntent, "Share about us"));
    }

    public void SaveShowPreference(String text) {
        labsCalendarUtils.setShowPreference(Integer.parseInt(text));
    }

    void doUpdateListAdapters(){
        int n =0;
        for(Fragment fr : moListMonthFragments){
            DayOnMonthGridFragment doFr = (DayOnMonthGridFragment) fr;
            doFr.setSelectedDate(mnLastDateSelected);
            List<Object> listEventsView = doFr.getMonthClass().getEventsAndViewsForMonth();
            moListDayEvents.put(n, listEventsView);
            if(n==1)
                mnSelectedOffsetList = doFr.getMonthClass().getDateObject(mnLastDateSelected, doFr.getMonth()).getListOffset();
            n++;
        }
    }

    private class LoadMonthEvents extends AsyncTask<Integer, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mbAsyncRunning = true;
            mbLockScroll = true;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int nSeq = 0;
            while(moQueForLoadEvents.size() > 0){
                Log.d("bugit", "size : " + moQueForLoadEvents.size());
                //  if -1, we need get all events and add to list.
                //  if 0, we need to get all events, add it up and update offset for all grid.
                //  if 1, we need to get all events and add it down, update offset for all grid.
                DayOnLoadMoreEventsObject oMon = moQueForLoadEvents.remove();

                Log.d("bugit", "size after : " + moQueForLoadEvents.size());

                if(oMon.UpOrDown == 0 && params[0].equals(1)) {
                    Log.d("bugit", " Month up ");
                    UpdateMapListAndHeaderAtTop();
                    nSeq = 0;
                }
                else if(oMon.UpOrDown == 1 && params[0].equals(1)) {
                    Log.d("bugit", " Month down ");
                    UpdateMapListAndHeaderAtBottom();
                    nSeq = 2;
                }
                Log.d("bugit", "size after : " + moQueForLoadEvents.size());
                moListHeaders.put(nSeq, oMon.dayonObj.getMonthClass().getHeaderItem());
                moListDayEvents.put(nSeq, oMon.dayonObj.getMonthClass().getEventsAndViewsForMonth());
            }
            mbAsyncRunning = false;
            mbPageSelectedInPage = false;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mbLockScroll = false;
            updateListForMonth();
            doSetListAdapter(true);
        }
    }

    private class LoadFirstMonths extends AsyncTask<Void, Void, Void> {
        MaterialDialog mDialog = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new MaterialDialog.Builder(DayOnMonthHomeActivity.this)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .progressIndeterminateStyle(false).show();
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onPrepareListMonths();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mDialog.dismiss();
            runInitialTask();
        }
    }

    private class UpdateMonthAndEvents extends AsyncTask<Void, Void, Void> {
        MaterialDialog mDialog = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new MaterialDialog.Builder(DayOnMonthHomeActivity.this)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .progressIndeterminateStyle(false).show();
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            doUpdateListAdapters();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mDialog.dismiss();
            updateListForMonth();
            doSetListAdapter(true);
        }
    }

    void ShowTest(){
        Intent myIntent = new Intent(DayOnMonthHomeActivity.this, DayOnYearViewActivity.class);
        myIntent.putExtra("key", "test"); //Optional parameters
        DayOnMonthHomeActivity.this.startActivity(myIntent);
    }

    void ShowReligiousEvents(){
        Intent myIntent = new Intent(DayOnMonthHomeActivity.this, CalendarReligiousViewActivity.class);
        myIntent.putExtra("key", "test"); //Optional parameters
        DayOnMonthHomeActivity.this.startActivity(myIntent);
    }

    void ShowHolidays(){
        Intent myIntent = new Intent(DayOnMonthHomeActivity.this, DayOnScheduleViewActivity.class);
        myIntent.putExtra("key", "test"); //Optional parameters
        DayOnMonthHomeActivity.this.startActivity(myIntent);
    }

    @Override
    public void ShowInfoDialog(CalendarEventMaster oEve){
        new MaterialDialog.Builder(DayOnMonthHomeActivity.this)
                .title(oEve.getInfoDescription())
                .content(oEve.getInfoWiki())
                .positiveText("Close")
                .autoDismiss(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        doLoadAd(0);
                        super.onPositive(dialog);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void showPreferenceDialogs() {
        startActivityForResult(new Intent(getApplicationContext(), DayOnPreferenceActivity.class), 9000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            int n = data.getIntExtra("result", 0);
            Toast.makeText(getApplicationContext(), "Got the result from preference : " + n, Toast.LENGTH_LONG).show();
        }
    }

    private boolean DidTimeOutElapseForAd(){
        if(mLastShownAdTime != null)
            return ((Calendar.getInstance().getTime().getTime() - mLastShownAdTime.getTime()) >= MAX_DURATION);
        return true;
    }

    private void doLoadAd(int type){
        switch(type) {
            case 0:
                if ((((++mShouldShowInterstitial) % 10) == 0)
                        && (DidTimeOutElapseForAd())) {
                    synchronized (mSyncLock) {
                        interstitial.loadAd(mAdRequest);
                    }
                }
                break;

            case 1:
                if ((((++mDoShowAdOnMonthChange) % 10) == 0)
                        && (DidTimeOutElapseForAd()))
                {
                    synchronized (mSyncLock) {
                        interstitial.loadAd(mAdRequest);
                    }
                }
                break;

            case 2:
                if(DidTimeOutElapseForAd()) {
                    synchronized (mSyncLock) {
                        /*if(mStartType) {
                            interstitial.loadAd(mAdRequest);
                        }
                        mStartType = true;*/
                    }
                }
                break;
        }
    }

    /*private class ShouldLoadAd extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            doLoadAd(2);
            return null;
        }
    }*/
}
