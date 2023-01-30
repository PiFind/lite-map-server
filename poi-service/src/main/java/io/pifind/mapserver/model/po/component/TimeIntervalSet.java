package io.pifind.mapserver.model.po.component;

import java.util.Comparator;
import java.util.TreeSet;

public class TimeIntervalSet extends TreeSet<TimeIntervalPO> {

    public static final Comparator<TimeIntervalPO> COMPARATOR = new Comparator<TimeIntervalPO>() {
        @Override
        public int compare(TimeIntervalPO o1, TimeIntervalPO o2) {
            return (int)(o1.getEnd()*10 - o2.getStart()*10);
        }
    };

    public TimeIntervalSet() {
        super(COMPARATOR);
    }

}
