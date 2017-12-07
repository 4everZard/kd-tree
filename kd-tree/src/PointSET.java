import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;
import java.util.Set;

import edu.princeton.cs.algs4.SET;
public class PointSET {
	private SET<Point2D> points;
	public PointSET() {
		points = new SET<Point2D>();
	}
	public boolean isEmpty() {
		return points.isEmpty();
	}
	public int size() {
		return points.size();
	}
	public void insert(Point2D p) {
		points.add(p);
	}
	public boolean contains(Point2D p) {
		return points.contains(p);
	}
	public void draw() {
		for(Point2D p: points) {
			p.draw();
		}
	}
	public Iterable<Point2D> range(RectHV rect){
		ArrayList<Point2D> inRange = new ArrayList<Point2D>();
		for(Point2D p: points) {
			if(rect.contains(p)) {
				inRange.add(p);			
			}		
		}
		return inRange;
	}
	public Point2D nearest(Point2D p) {
		Point2D minPoint = null;
		double distance =0.0;
		double minDistance = Double.POSITIVE_INFINITY;
		for(Point2D point: points) {
			distance = p.distanceTo(point);
			if(distance < minDistance) {
				minPoint = point;
				minDistance = distance;
			}
		}
		return minPoint;
	}
}
