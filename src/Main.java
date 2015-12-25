import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.ui.RefineryUtilities;

public class Main {

    public static void main(String[] args) {
        LineChart_AWT chart = new LineChart_AWT(
                "Siła sygnału" ,
                "Siła sygnału");
        chart.pack( );
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );
    }
}
