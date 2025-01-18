package geometry.position;

import android.os.Build;

import androidx.annotation.RequiresApi;

import geometry.framework.GeometryObject;
import geometry.framework.Point;
import geometry.framework.Tracer;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * NOTE: Uncommented
 */

public class Bezier extends GeometryObject implements Tracer {

    private final Point start;
    private final Point end;
    private final Point control1;
    private final Point control2;

    private final double mx;

    private final double my;
    public ArrayList<Point> pointsList = new ArrayList<Point>();

    public Bezier(Point start, Point end, Point control1, Point control2){
        this.start = start; this.end = end; this.control1 = control1; this.control2 = control2;
        addPoints(start, end); this.mx=end.getX()-start.getX(); this.my=end.getY()-start.getY();
        for(double i=0; i<=1; i+=1/100){
            pointsList.add(getAt(i));
        }

    }
    public Bezier(){
        this(new Point(), new Point(),new Point(),new Point());
    }

    public Point getStartPoint(){ return start; }
    public Point getEndPoint(){ return end; }

    public ArrayList<Point> getPointsList(double t){
        double startIndex = Math.round(t * 100.0) / 100.0;
        pointsList= (ArrayList<Point>) pointsList.subList(10, pointsList.size());
        Iterator<Point> iter = pointsList.iterator();
        while (iter.hasNext()) {
            Point p = iter.next();
            if (pointsList.indexOf(p)>startIndex) iter.remove();
        }
        return pointsList;
    }
    public double getTime(Pose p){
        pointsList= (ArrayList<Point>) pointsList.subList(10, pointsList.size());
        double min=Integer.MAX_VALUE;
        int minIndex=pointsList.size()-1;
        for(int i =0; i<pointsList.size(); i++){
            double distanceSquared=Math.pow(p.getX()-pointsList.get(i).getX(),2)+Math.pow(p.getY()-pointsList.get(i).getY(),2);
            if (distanceSquared<min){
                min=distanceSquared;
                minIndex=i;
            }
        }
        return minIndex;
    }
    public double getTime(Point p){
        pointsList= (ArrayList<Point>) pointsList.subList(10, pointsList.size());
        double min=Integer.MAX_VALUE;
        int minIndex=pointsList.size()-1;
        for(int i =0; i<pointsList.size(); i++){
            double distanceSquared=Math.pow(p.getX()-pointsList.get(i).getX(),2)+Math.pow(p.getY()-pointsList.get(i).getY(),2);
            if (distanceSquared<min){
                min=distanceSquared;
                minIndex=i;
            }
        }
        return minIndex;
    }

    public Point getAt(double t, double totalTime){
        //Bezier cubic function
        t=t/totalTime;
        double x= Math.pow(1 - t, 3) * start.getX() + 3 * Math.pow(1 - t, 2) * t * control1.getX() + 3 * (1 - t) * Math.pow(t, 2) * control2.getX() + Math.pow(t, 3) * end.getX();
        double y= Math.pow(1 - t, 3) * start.getY() + 3 * Math.pow(1 - t, 2) * t * control1.getY() + 3 * (1 - t) * Math.pow(t, 2) * control2.getY() + Math.pow(t, 3) * end.getY();
        return new Point (x, y);
    }
    public Point getAt(double t){
        //Bezier cubic function
        double x= Math.pow(1 - t, 3) * start.getX() + 3 * Math.pow(1 - t, 2) * t * control1.getX() + 3 * (1 - t) * Math.pow(t, 2) * control2.getX() + Math.pow(t, 3) * end.getX();
        double y= Math.pow(1 - t, 3) * start.getY() + 3 * Math.pow(1 - t, 2) * t * control1.getY() + 3 * (1 - t) * Math.pow(t, 2) * control2.getY() + Math.pow(t, 3) * end.getY();
        return new Point (x, y);
    }

    public double getMx(){
        return mx;

    }

    public double getMy(){
        return my;

    }
    public double getDx(double t){
        //finds dx/dt
        double t1=t-0.01;
        double t2=t+0.01;
        double x1=Math.pow(1 - t1, 3) * start.getX() + 3 * Math.pow(1 - t1, 2) * t1 * control1.getX() + 3 * (1 - t1) * Math.pow(t1, 2) * control2.getX() + Math.pow(t1, 3) * end.getX();
        double x2=Math.pow(1 - t2, 3) * start.getX() + 3 * Math.pow(1 - t2, 2) * t2 * control1.getX() + 3 * (1 - t2) * Math.pow(t2, 2) * control2.getX() + Math.pow(t2, 3) * end.getX();
        return (x2-x1)/(t2-t1);
    }

    public double getDy(double t){
        //finds dy/dt
        double t1=t-0.01;
        double t2=t+0.01;
        double y1=Math.pow(1 - t1, 3) * start.getY() + 3 * Math.pow(1 - t1, 2) * t1 * control1.getY() + 3 * (1 - t1) * Math.pow(t1, 2) * control2.getY() + Math.pow(t1, 3) * end.getY();
        double y2=Math.pow(1 - t2, 3) * start.getY() + 3 * Math.pow(1 - t2, 2) * t2 * control1.getY() + 3 * (1 - t2) * Math.pow(t2, 2) * control2.getY() + Math.pow(t2, 3) * end.getY();
        return (y2-y1)/(t2-t1);
    }
    public double getDerivativeParametricSquared(double t){
        //returns sqrt((dx/dt)^2+(dy/dt)^2) helper function for arclength
        return Math.sqrt(Math.pow(getDx(t),2)+Math.pow(getDy(t),2));
    }

    public double getLength(double t, int indices){
        //returns arc length from specified t to the end of the curve
        double sum=0;
        for(int i=0; i<=indices-t; i++) {
            t+=1/indices;
            if (t<1){
                sum+=getDerivativeParametricSquared(t);
            }
        }
        return sum;
    }
    public double getTangentLine(double t){
        if(t>0.01 && t<0.99){
            //calculates approximate tangent line by using nearest points
            return (getAt(t+0.01).getY()-getAt(t-0.01).getY())/(getAt(t+0.01).getX()-getAt(t-0.01).getX());
        }
        else if(t<0.01){
            return (getAt(0.01).getY()-getAt(0).getY())/(getAt(0.01).getX()-getAt(0).getX());

        }
        else{
            return (getAt(1).getY()-getAt(0.99).getY())/(getAt(1).getX()-getAt(0.99).getX());

        }
    }
    public double getNormal(double t){
        if(getTangentLine(t)!=0){
            return 1/(getTangentLine(t));
        }
        else{
            return 0;
        }
    }


    public Point getMidpoint(){return getAt(0.5); }


    @Override
    public Bezier getCopy() {
        return new Bezier(start.getCopy(), end.getCopy(), control1.getCopy(), control2.getCopy());
    }
}
