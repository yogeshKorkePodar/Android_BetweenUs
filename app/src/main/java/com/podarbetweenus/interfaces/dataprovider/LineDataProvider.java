package com.podarbetweenus.interfaces.dataprovider;


import com.podarbetweenus.components.YAxis;
import com.podarbetweenus.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
