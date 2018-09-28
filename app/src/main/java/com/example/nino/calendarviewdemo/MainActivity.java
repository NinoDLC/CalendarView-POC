package com.example.nino.calendarviewdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.style.ForegroundColorSpan;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.LocalDate;

public class MainActivity extends AppCompatActivity implements OnRangeSelectedListener, OnDateSelectedListener {
    
    // Arbitrary "anchor" starting date that user cannot modify when he's selecting the return date
    private LocalDate mAnchorDate;
    // Necessary only because MaterialCalendarView.selectRange() takes CalendarDays as parameter...
    private CalendarDay mAnchorDay;
    
    @NonNull
    private List<CalendarDay> mSelectedDays = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        mAnchorDate = LocalDate.now();
        
        DayViewDecorator pastDaysDecorator = new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay calendarDay) {
                return calendarDay.getDate().isBefore(mAnchorDate);
            }
            
            @Override
            public void decorate(DayViewFacade dayViewFacade) {
                dayViewFacade.setDaysDisabled(true);
            }
        };
        
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
                dayViewFacade.addSpan(new ForegroundColorSpan(Color.BLACK));
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
                dayViewFacade.addSpan(new ForegroundColorSpan(getResources().getColor(R.color.super_couleur_bleue)));
            }
        };
        
        MaterialCalendarView materialCalendarView = findViewById(R.id.material_calendar_view);
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
        materialCalendarView.setSelectionColor(R.color.super_couleur_bleue);
        materialCalendarView.setDateTextAppearance(R.style.McvDateText);
        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.setOnRangeSelectedListener(this);
        materialCalendarView.addDecorators(pastDaysDecorator,
                                           singleDayDecorator,
                                           firstDayDecorator,
                                           continuousDayDecorator,
                                           lastDayDecorator);
        materialCalendarView.setSelectedDate(mAnchorDate); // Should change in production code
        mAnchorDay = materialCalendarView.getSelectedDate();
        mSelectedDays.add(mAnchorDay);
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
        // User is not allowed to select a single date, only a range
        materialCalendarView.selectRange(mAnchorDay, calendarDay);
    }
}
