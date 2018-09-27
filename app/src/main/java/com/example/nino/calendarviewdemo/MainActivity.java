package com.example.nino.calendarviewdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnRangeSelectedListener, OnDateSelectedListener {
    
    @NonNull
    private List<CalendarDay> mSelectedDays = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        DayViewDecorator singleDayDecorator = new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay calendarDay) {
                return mSelectedDays.size() == 1 && mSelectedDays.get(0).getDate().equals(calendarDay.getDate());
            }
            
            @Override
            public void decorate(DayViewFacade dayViewFacade) {
                dayViewFacade.setSelectionDrawable(getResources().getDrawable(R.drawable.day));
            }
        };
        
        DayViewDecorator firstDayDecorator = new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay calendarDay) {
                if (mSelectedDays.isEmpty() || mSelectedDays.size() == 1) {
                    return false;
                }
                
                return mSelectedDays.get(0).getDate().equals(calendarDay.getDate());
            }
            
            @Override
            public void decorate(DayViewFacade dayViewFacade) {
                dayViewFacade.setSelectionDrawable(getResources().getDrawable(R.drawable.start_of_range));
            }
        };
        
        DayViewDecorator continuousDayDecorator = new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay calendarDay) {
                if (mSelectedDays.isEmpty() || mSelectedDays.size() == 1) {
                    return false;
                }
                
                for (int i = 0; i < mSelectedDays.size(); i++) {
                    CalendarDay selectedDay = mSelectedDays.get(i);
                    
                    if (selectedDay.getDate().equals(calendarDay.getDate())) {
                        // Don't decorate the first day nor the last day of selection
                        return i != 0 && i != mSelectedDays.size() - 1;
                    }
                }
                
                return false;
            }
            
            @Override
            public void decorate(DayViewFacade dayViewFacade) {
                dayViewFacade.setSelectionDrawable(getResources().getDrawable(R.drawable.rectangle));
            }
        };
        
        DayViewDecorator lastDayDecorator = new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay calendarDay) {
                if (mSelectedDays.isEmpty() || mSelectedDays.size() == 1) {
                    return false;
                }
                
                return mSelectedDays.get(mSelectedDays.size() - 1).getDate().equals(calendarDay.getDate());
            }
            
            @Override
            public void decorate(DayViewFacade dayViewFacade) {
                dayViewFacade.setSelectionDrawable(getResources().getDrawable(R.drawable.end_of_range));
            }
        };
        
        MaterialCalendarView materialCalendarView = findViewById(R.id.material_calendar_view);
        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.setOnRangeSelectedListener(this);
        materialCalendarView.addDecorators(singleDayDecorator,
                                           firstDayDecorator,
                                           continuousDayDecorator,
                                           lastDayDecorator);
    }
    
    @Override
    public void onRangeSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull List<CalendarDay> list) {
        mSelectedDays.clear();
        mSelectedDays.addAll(list);
        
        materialCalendarView.invalidateDecorators();
    }
    
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
                               @NonNull CalendarDay calendarDay,
                               boolean selected) {
        mSelectedDays.clear();
        
        if (selected) {
            mSelectedDays.add(calendarDay);
        }
        
        materialCalendarView.invalidateDecorators();
    }
}
