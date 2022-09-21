package autoutil.vision;

import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import elements.Case;

public class TeamElementScanner extends CaseScanner{

    @Override
    protected Rect[] defineRegions() {
        return defaultRegionGenerator(new Point(width/2.0,(height/2.0)+0.0), 60, 130);
    }

    @Override
    protected Case detectCase() {
        List<Integer> averages = Arrays.asList(
                processor.getAverage(processor.getSubmat(Cb, rects[0])),
                processor.getAverage(processor.getSubmat(Cb, rects[1])),
                processor.getAverage(processor.getSubmat(Cb, rects[2]))
        );
        int minIndex = averages.indexOf(Collections.min(averages));
        return cases[minIndex];
    }

}
