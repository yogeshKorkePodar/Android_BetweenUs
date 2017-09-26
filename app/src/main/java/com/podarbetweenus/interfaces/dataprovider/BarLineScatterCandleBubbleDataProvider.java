package com.podarbetweenus.interfaces.dataprovider;


import com.podarbetweenus.components.YAxis;
import com.podarbetweenus.data.BarLineScatterCandleBubbleData;
import com.podarbetweenus.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(YAxis.AxisDependency axis);
    int getMaxVisibleCount();
    boolean isInverted(YAxis.AxisDependency axis);
    
    int getLowestVisibleXIndex();
    int getHighestVisibleXIndex();

    BarLineScatterCandleBubbleData getData();
}
