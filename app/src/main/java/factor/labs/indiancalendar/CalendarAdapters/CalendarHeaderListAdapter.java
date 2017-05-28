package factor.labs.indiancalendar.CalendarAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import factor.labs.indiancalendar.CalendarInterfaces.IDayOnEventInfoClick;
import factor.labs.indiancalendar.CalendarObjects.CalendarEmptyEventListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarEventAdListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarEventDateListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarEventListItem;
import factor.labs.indiancalendar.CalendarObjects.CalendarEventMonthListItem;
import factor.labs.indiancalendar.CalendarUI.CalendarHeaderList.SectionAdapter;
import factor.labs.indiancalendar.CalendarUtils.labsCalendarUtils;
import factor.labs.indiancalendar.CalendarViewHolders.DayOnEventListAd;
import factor.labs.indiancalendar.CalendarViewHolders.DayOnEventListDate;
import factor.labs.indiancalendar.CalendarViewHolders.DayOnEventListEmpty;
import factor.labs.indiancalendar.CalendarViewHolders.DayOnEventListHeader;
import factor.labs.indiancalendar.CalendarViewHolders.DayOnEventListItem;
import factor.labs.indiancalendar.R;
import factor.labs.indiancalendar.utils.database.Events;
import factor.labs.indiancalendar.utils.json.EventCategory;
import factor.labs.indiancalendar.utils.json.EventProperty;
import factor.labs.indiancalendar.utils.serializer.JsonSerializer;

/**
 * Created by hassanhussain on 10/13/2015.
 */
public class CalendarHeaderListAdapter extends SectionAdapter {

    Context mContext;
    LayoutInflater mInflater;

    Map<Integer,Object> moHeaderObjects = new LinkedHashMap<>();
    Map<Integer,List<Object>> moRowObjects = new LinkedHashMap<>();

    IDayOnEventInfoClick mCallback;

    public CalendarHeaderListAdapter(Context oCon,
                                     Map<Integer,Object> listheader,
                                     Map<Integer,List<Object>> listRows,
                                     IDayOnEventInfoClick callback) {
        mContext = oCon;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        moHeaderObjects.putAll(listheader);
        moRowObjects.putAll(listRows);
        mCallback = callback;
    }

    Map<Integer,Object> getHeaderList() { return moHeaderObjects; }
    Map<Integer, List<Object>> getDayEventsList() { return moRowObjects; }

    public void setHeaderList(Map<Integer,Object> hdList) { moHeaderObjects.clear(); moHeaderObjects.putAll(hdList); }
    public void setDayEventsList(Map<Integer,List<Object>> hdList) { moRowObjects.clear(); moRowObjects.putAll(hdList); }

    @Override
    public int numberOfSections() {
        return moHeaderObjects.size();
    }

    @Override
    public int numberOfRows(int section) {
        if(moRowObjects.get(section) != null)
            return moRowObjects.get(section).size();
        return 0;
    }

    @Override
    public Object getSectionHeaderItem(int section) {
        return moHeaderObjects.get(section);
    }

    @Override
    public int getItemViewType(int section, int position) {
        return getRowItemViewType(section, position);
    }

    @Override
    public int getRowViewTypeCount() {
        return 4;
    }

    @Override
    public int getRowItemViewType(int section, int position) {

        Object oTemp = getRowItem(section, position);
        if(oTemp != null)
        {
            if(oTemp instanceof CalendarEventDateListItem)
                return 0;
            else if(oTemp instanceof CalendarEmptyEventListItem)
                return 1;
            else if(oTemp instanceof CalendarEventListItem)
                return 2;
            else if(oTemp instanceof CalendarEventAdListItem)
                return 3;
        }
        return 0;
    }

    @Override
    public View getRowView(int section, int row, View convertView, ViewGroup parent) {
        try
        {
            int switch_no = getRowItemViewType(section, row);
            switch (switch_no)
            {
                case 0:
                    if(!(convertView != null && convertView.getTag() instanceof DayOnEventListDate))
                        convertView = null;

                    if (convertView == null) {
                        convertView = mInflater.inflate(R.layout.layout_dayon_list_day_header, parent, false);

                        DayOnEventListDate dateHolder = new DayOnEventListDate();
                        dateHolder.txtDateName = (TextView)convertView.findViewById(R.id.id_cal_date_month_view);
                        dateHolder.txtDayName = (TextView)convertView.findViewById(R.id.id_cal_day_name_view);
                        dateHolder.hidden = (TextView)convertView.findViewById(R.id.id_cal_hidden_object_day);
                        convertView.setTag(dateHolder);
                    }
                    CalendarEventDateListItem obj1 = (CalendarEventDateListItem) getRowItem(section, row);
                    if(obj1 != null) {
                        DayOnEventListDate holder2 = (DayOnEventListDate) convertView.getTag();
                        holder2.txtDayName.setText(labsCalendarUtils.getWeekForDate(obj1.date, obj1.mon, obj1.yr));
                        holder2.txtDateName.setText(labsCalendarUtils.getMonthName(obj1.mon) + " " + obj1.date + ", " + obj1.yr);
                        holder2.txtDateName.setTextColor(ContextCompat.getColor(mContext, R.color.Grey800));
                        holder2.txtDayName.setTextColor(ContextCompat.getColor(mContext, R.color.Grey700));

                        if (obj1.mon == labsCalendarUtils.getCurrentMonth() &&
                                obj1.date == labsCalendarUtils.getTodaysDate() &&
                                obj1.yr == labsCalendarUtils.getCurrentYear()) {
                            holder2.txtDateName.setTextColor(ContextCompat.getColor(mContext, R.color.primary_dark));
                            holder2.txtDayName.setTextColor(ContextCompat.getColor(mContext, R.color.primary_dark));
                        }

                        holder2.hidden.setTag(obj1);
                    }
                    break;
                case 1:
                    if(!(convertView != null && convertView.getTag() instanceof DayOnEventListEmpty))
                        convertView = null;

                    if (convertView == null) {
                        convertView = mInflater.inflate(R.layout.layout_dayon_list_event_empty, parent, false);

                        DayOnEventListEmpty emptyHolder = new DayOnEventListEmpty();
                        emptyHolder.txt = (TextView)convertView.findViewById(R.id.id_cal_empty_events);
                        emptyHolder.hidden = (TextView)convertView.findViewById(R.id.id_cal_hidden_object_empty);
                        convertView.setTag(emptyHolder);
                    }
                    CalendarEmptyEventListItem obj2 = (CalendarEmptyEventListItem) getRowItem(section, row);
                    if(obj2 != null) {
                        DayOnEventListEmpty holder3 = (DayOnEventListEmpty) convertView.getTag();
                        holder3.hidden.setTag(obj2);
                    }
                    break;
                case 2:
                    if(!(convertView != null && convertView.getTag() instanceof DayOnEventListItem))
                        convertView = null;

                    if (convertView == null) {
                        convertView = mInflater.inflate(R.layout.layout_dayon_list_event, parent, false);

                        DayOnEventListItem eventHolder = new DayOnEventListItem();
                        eventHolder.img = (ImageView)convertView.findViewById(R.id.id_cal_event_category);
                        eventHolder.hidden = (TextView)convertView.findViewById(R.id.id_cal_hidden_object_event);
                        eventHolder.txtEventLocation = (TextView)convertView.findViewById(R.id.id_cal_event_detail_location);
                        eventHolder.txtEventName = (TextView)convertView.findViewById(R.id.id_cal_event_detail_name);
                        eventHolder.txtEventTime = (TextView) convertView.findViewById(R.id.id_cal_event_detail_time);
                        eventHolder.layoutHolder = (LinearLayout) convertView.findViewById(R.id.id_cal_event_item_holder);
                        eventHolder.layoutLocationHolder = (LinearLayout) convertView.findViewById(R.id.id_cal_event_loc_holder);
                        convertView.setTag(eventHolder);
                    }
                    CalendarEventListItem obj3 = (CalendarEventListItem) getRowItem(section, row);
                    if(obj3 != null) {
                        DayOnEventListItem holder4 = (DayOnEventListItem) convertView.getTag();
                        holder4.txtEventName.setText(obj3.oEventInfo.getName());
                        holder4.txtEventTime.setText("Full Day");

                        holder4.txtEventLocation.setVisibility(View.VISIBLE);
                        holder4.txtEventName.setTextColor(ContextCompat.getColor(mContext, R.color.Grey800));
                        holder4.img.setAlpha(1.0f);
                        holder4.txtEventName.setAlpha(1.0f);

                        holder4.layoutLocationHolder.setVisibility(View.VISIBLE);
                        if (obj3.oEventInfo.getName().length() == 0)
                            holder4.layoutLocationHolder.setVisibility(View.GONE);
                        else
                            holder4.txtEventLocation.setText(obj3.oEventInfo.getName());


                        if ((obj3.oEventInfo.getCategory()
                                & EventCategory.Category.HOLIDAY.getValue()) != 0 ) {
                            holder4.txtEventName.setTextColor(ContextCompat.getColor(mContext, R.color.event_ring_holiday));
                            holder4.img.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.shape_rectangle_event_notify));
                            holder4.img.setAlpha(.75f);
                            holder4.txtEventName.setAlpha(.8f);
                        } else if ((obj3.oEventInfo.getCategory()
                                & EventCategory.Category.RELIGIOUS.getValue()) != 0 ) {
                            EventProperty oProps =
                                    (EventProperty) JsonSerializer.getInstance().UnserializeToObject(obj3.oEventInfo.getProperty(), EventProperty.class);
                            int nReligionID = oProps.getReligion().getValue();
                            if (nReligionID != 0) {
                                holder4.txtEventName.setTextColor(ContextCompat.getColor(mContext, R.color.c_event_religious));
                                Drawable d = ContextCompat.getDrawable(mContext, labsCalendarUtils.getIconFormReligion(nReligionID));
                                holder4.img.setImageDrawable(d);
                                holder4.img.setAlpha(.4f);
                            }
                        }
                        holder4.hidden.setTag(obj3);
                    }

                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Object obj = v.getTag();
                            if(obj instanceof DayOnEventListItem){
                                Events oEve = ((CalendarEventListItem)((DayOnEventListItem)obj).hidden.getTag()).oEventInfo;
                                if(mCallback != null)
                                    mCallback.ShowInfoDialog(oEve);
                            }
                        }
                    });
                    break;
                case 3:
                    if(!(convertView != null && convertView.getTag() instanceof DayOnEventListAd))
                        convertView = null;

                    DayOnEventListAd Holder = null;
                    if (convertView == null) {
                        convertView = mInflater.inflate(R.layout.layout_ad_between_events, parent, false);

                        Holder = new DayOnEventListAd();
                        Holder.ads = (AdView)convertView.findViewById(R.id.adView);
                        Holder.hidden = (TextView)convertView.findViewById(R.id.hidden_txt);
                        convertView.setTag(Holder);
                    }
                    CalendarEventAdListItem adObj = (CalendarEventAdListItem) getRowItem(section, row);
                    if(adObj != null) {
                        final DayOnEventListAd Holder1 = (DayOnEventListAd) convertView.getTag();
                        Holder1.hidden.setTag(adObj);
                        final View finalConvertView = convertView;
                        Holder1.ads.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                //Holder1.ads.setVisibility(View.VISIBLE);
                                finalConvertView.setVisibility(View.VISIBLE);
                            }
                        });
                        // Request for Ads
                        AdRequest adRequest = new AdRequest
                                .Builder()
                                .addTestDevice("F3D0EE493657AD2952233060D190BFBF")
                                .build();
                        // Load ads into Banner Ads
                        //Holder1.ads.loadAd(adRequest);
                    }
                    break;
            }
        }
        catch(Exception ex){
            Log.e("getRowView", "Exception :" +ex.getMessage() );
        }
        return convertView;
    }

    @Override
    public Object getRowItem(int section, int row) {
        if(section<3)
            return moRowObjects.get(section).get(row);
        return null;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        try
        {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.layout_dayon_list_month_header, parent, false);

                DayOnEventListHeader mholder = new DayOnEventListHeader();
                mholder.txtMonthName = (TextView)convertView.findViewById(R.id.id_cal_month_name_header);
                mholder.hidden = (TextView)convertView.findViewById(R.id.id_cal_hidden_object_month);
                mholder.txtDayName = (TextView)convertView.findViewById(R.id.id_cal_day_name_header);
                convertView.setTag(mholder);
            }
            CalendarEventMonthListItem obj4 = (CalendarEventMonthListItem) getSectionHeaderItem(section);
            if(obj4 != null) {
                DayOnEventListHeader holder5 = (DayOnEventListHeader) convertView.getTag();

                String sDayName =
                        labsCalendarUtils.getWeekForDate(1, obj4.getMonth(), obj4.getYear()) + " "
                                + labsCalendarUtils.getMonthName(obj4.getMonth()) + " 1, " + obj4.getYear();
                holder5.txtMonthName.setText(labsCalendarUtils.getMonthName(obj4.getMonth()) + " " + obj4.getYear());

                //holder5.imgView.setImageDrawable(ContextCompat.getDrawable(mContext, labsCalendarUtils.getMonImg(obj4.getMonth())));

                holder5.txtDayName.setText(sDayName);
                holder5.hidden.setTag(obj4);
            }
        }
        catch(Exception ex){
            Log.e("getHeadView", "Exception :" +ex.getMessage() );
        }
        return convertView;
    }

    @Override
    public boolean hasSectionHeaderView(int section) {
        if(section < 3 && section >= 0) return true;
        return false;
    }

    public static Drawable setTint(Drawable d, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(d);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }
}
