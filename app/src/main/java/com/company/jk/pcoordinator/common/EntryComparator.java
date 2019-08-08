package com.company.jk.pcoordinator.common;

import com.github.mikephil.charting.data.Entry;

import java.util.Comparator;

// MPAndroidchart 작성할 때 사용하는 class
public class EntryComparator implements Comparator<Entry> {
    @Override
    public int compare(Entry entry1, Entry entry2) {
        return  Math.round(entry1.getX() - entry2.getX());
    }
}
