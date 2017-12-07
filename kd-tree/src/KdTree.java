import java.util.Comparator;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;


public class KdTree {
	// constructor
	private Node root;
	private int size;
	
	public KdTree() {
		root = null;
		size = 0;
	}
	
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
        return root == null;
    }
	
	//create private static node class
	private static class Node{
		private Point2D point;
		private RectHV rect;
		private boolean isVertical;
		private Node left;
		private Node right;
		
		private Node(Point2D p, boolean vertical, Node left, Node right, RectHV rectangle) {
			this.point = p;
			this.isVertical = vertical;
			this.left = left;
			this.right = right;
			this.rect = rectangle;
		}
		
		public RectHV getRect() {
			return this.rect;
		}
		public Point2D getPoint() {
			return point;
		}
		public boolean vertical() {
			return isVertical;
		}
		public Node getLeft() {
			return left;
		}
		public Node getRight() {
			return right;
		}
		public void setLeft(Node leftNode) {
			this.left = leftNode;
		}
		public void setRightNode(Node rightNode) {
			this.right = rightNode;
		}
	}
	
	// add the point to the set
	public void insert(Point2D p) {
		// point current node to root
		Node currNode = root;
		Comparator<Point2D> comparator = Point2D.Y_ORDER;
		// if the tree is empty
		if(size == 0) {
			RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
			root = new Node(p, true, null, null, rect);
			size++;
			return;
		}
		
		while(currNode != null) {
			// return if inserted point is already in a tree
			if(currNode.getPoint().equals(p))
				return;
			// if current node is vertical, set comparator to x order
			if(currNode.vertical()) {
				comparator = Point2D.X_ORDER;
			}
			else {
				comparator = Point2D.Y_ORDER;
			}
			
			//
			if(comparator.compare(p, currNode.getPoint()) < 0) {
				if(currNode.getLeft() == null){
					RectHV rect = null;
					// if current node is vertical type, point to left rectangle, else point to bottom
					if(currNode.vertical()) {
						rect = new RectHV(currNode.getRect().xmin(), currNode.getRect().ymin(), currNode.getPoint().x(), currNode.getRect().ymax());
					}
					else {
						rect = new RectHV(currNode.getRect().xmin(), currNode.getRect().ymin(), currNode.getRect().xmax(), currNode.getPoint().y());
					}
					
					// create new left node and insert the point p into it
					Node leftNode = new Node(p, !currNode.vertical(), null, null, rect);
					//point the left node of current node to new left node
					currNode.setLeft(leftNode);
					size++;
					break;	
				}
				// set current node to new left node
				currNode = currNode.getLeft();
			}
			else {
				if(currNode.getRight() == null) {
					RectHV rect = null;
					// if current node is vertical type, point to right rectangle, else point to top
					if(currNode.vertical()) {
						rect = new RectHV(currNode.getPoint().x(), currNode.getRect().ymin(), currNode.getRect().xmax(), currNode.getRect().ymax());
					}
					else {
						rect = new RectHV(currNode.getRect().xmin(), currNode.getPoint().y(), currNode.getRect().xmax(), currNode.getRect().ymax());
					}
					Node rightNode = new Node(p, !currNode.vertical(), null, null, rect);
					currNode.setRightNode(rightNode);
					size++;
					break;
				}
				currNode = currNode.getRight();
				
			}
		}	
	}
	
	
	// does the set contains point p?
	public boolean contains(Point2D p) {
		return containsRecursive(root,p);
	}
	
	private boolean containsRecursive(Node currNode, Point2D targetPoint) {
		if (currNode == null) {
			return false;
		}
		Point2D currPoint = currNode.getPoint();
		if(currPoint.equals(targetPoint)) {
			return true;
		}
		
		Comparator<Point2D> comparator = null;
		if(currNode.vertical()) {
			comparator = Point2D.X_ORDER;
		}
		else {
			comparator = Point2D.Y_ORDER;
		}
		if(comparator.compare(targetPoint, currPoint) < 0) {
			return containsRecursive(currNode.getLeft(), targetPoint);
		}
		else {
	        return containsRecursive(currNode.getRight(), targetPoint);
	    }	
	}
	
	public void draw() {
	    StdDraw.setPenColor(StdDraw.BLACK);
	    Point2D bottomMin = new Point2D(0.0, 0.0);
	    Point2D bottomMax = new Point2D(1.0, 0.0);
	    bottomMin.drawTo(bottomMax);
	    Point2D leftMin = new Point2D(0.0, 0.0);
	    Point2D leftMax= new Point2D(0.0, 1.0);
	    leftMin.drawTo(leftMax);
	    Point2D topMin = new Point2D(0.0, 1.0);
	    Point2D topMax = new Point2D(1.0, 1.0);
	    topMin.drawTo(topMax);
	    Point2D rightMin = new Point2D(1.0, 0.0);
	    Point2D rightMax = new Point2D(1.0, 1.0);
	    rightMin.drawTo(rightMax);
	    drawRecursive(root);
	}
	
	private void drawRecursive(Node node) {
	    if (node == null) {
	        return;
	    }
	    RectHV rect = node.getRect();
	    Point2D point = node.getPoint();
	    
	    StdDraw.setPenColor(StdDraw.BLACK);
	    StdDraw.setPenRadius(0.01);
	    point.draw();
	    
	    if (node.vertical()) {
	        StdDraw.setPenColor(StdDraw.RED);
	        StdDraw.setPenRadius();
	        
	        Point2D bottom = new Point2D(point.x(), rect.ymin());
	        Point2D top = new Point2D(point.x(), rect.ymax());
	        bottom.drawTo(top);
	        
	        drawRecursive(node.getLeft());
	        drawRecursive(node.getRight());
	    } 
	    else
	    {
	        StdDraw.setPenColor(StdDraw.BLUE);
	        StdDraw.setPenRadius();
	        
	        Point2D left = new Point2D(rect.xmin(), point.y());
	        Point2D right = new Point2D(rect.xmax(), point.y());
	        left.drawTo(right);
	        
	        drawRecursive(node.getLeft());
	        drawRecursive(node.getRight());
	    }
	}
	
	// start at the root and recursively search for points in both subtrees to find all points contained in a give rectangle
	public Iterable<Point2D> range(RectHV rect){
		ArrayList<Point2D> pointsInRange = new ArrayList<Point2D>();
		rangeRecursive(pointsInRange, rect, root);
		return pointsInRange;
	}
	
	private void rangeRecursive(ArrayList<Point2D> rangeList, RectHV rect, Node currNode) {
		if(currNode == null)
			return;
		Point2D currPoint = currNode.getPoint();
		if(rect.contains(currPoint))
			rangeList.add(currPoint);
		double pointCoord = currPoint.x();
		double rectMin = rect.ymin();
		double rectMax = rect.xmin();
		if(!currNode.vertical()) {
		     pointCoord = currPoint.x();
             rectMin = rect.xmin();
             rectMax = rect.xmax();
		}
		
	    if (pointCoord > rectMin)
	    	rangeRecursive(rangeList, rect, currNode.getLeft());
	    if (pointCoord <= rectMax)
	    	rangeRecursive(rangeList, rect, currNode.getRight());
		
	}
	
	private Comparator<Point2D> distanceOrder(Point2D targetPoint){
		return new Comparator<Point2D>(){
			public int compare(Point2D p1, Point2D p2) {
				double distance1 = targetPoint.distanceSquaredTo(p1);
				double distance2 = targetPoint.distanceSquaredTo(p2);
				if(distance1 < distance2) return -1;
				else if (distance1 > distance2) return 1;
				else return 0;
			}
			
		};
	}
	
	public Point2D nearest(Point2D p) {
		if(size == 0)
			return null;
		MinPQ<Point2D> minPQ = new MinPQ<Point2D>(distanceOrder(p));
		nearestRecursive(minPQ, root, p, new RectHV(0.0, 0.0, 1.0, 1.0));
		return minPQ.min();
	}
	
    private void nearestRecursive(MinPQ<Point2D> m, Node n, Point2D p, RectHV rect)
    {
        if (n == null)
            return;
        
        Point2D nPoint = n.getPoint();
        m.insert(nPoint);
        //  this is so that it doesnt make too much calls to  methods of Point2D and RectHV
        double rectXmin = rect.xmin();
        double rectXmax = rect.xmax();
        double rectYmin = rect.ymin();
        double rectYmax = rect.ymax();
        double pointx = nPoint.x();
        double pointy = nPoint.y();
        
        if (n.vertical())
        {
           RectHV leftRect = new RectHV(rectXmin, rectYmin,
                   pointx, rectYmax);
           RectHV rightRect = new RectHV(pointx, rectYmin,
                   rectXmax, rectYmax);


            if (p.x() < pointx)
            {
                nearestRecursive(m, n.getLeft(), p, leftRect);
                double distanceToNearest = m.min().distanceSquaredTo(p);
                double distanceToRect = rightRect.distanceSquaredTo(p);
                if (distanceToRect < distanceToNearest) 
                {
                    nearestRecursive(m, n.getRight(), p, rightRect);
                }
            }
            else
            {
                nearestRecursive(m, n.getRight(), p, rightRect);
                double distanceToNearest = m.min().distanceSquaredTo(p);
                double distanceToRect = leftRect.distanceSquaredTo(p);
                if (distanceToRect < distanceToNearest) 
                {
                    nearestRecursive(m, n.getLeft(), p, leftRect);
                }
            }
        }
        else
        {
            //  for horizontal lines
            
            RectHV bottomRect = new RectHV(rectXmin, rectYmin, rectXmax, pointy);
            RectHV topRect = new RectHV(rectXmin, pointy, rectXmax, rectYmax);
            //  if point lies below the horizonal line of current search point
            if (p.y() < pointy)
            {
                nearestRecursive(m, n.getLeft(), p, bottomRect);
                double distanceToNearest = m.min().distanceSquaredTo(p);
                double distanceToRect = topRect.distanceSquaredTo(p);
                if (distanceToRect < distanceToNearest)
                {
                    nearestRecursive(m, n.getRight(), p, topRect);
                }
            }
            else
            {
                nearestRecursive(m, n.getRight(), p, topRect);
                double distanceToNearest = m.min().distanceSquaredTo(p);
                double distanceToRect = bottomRect.distanceSquaredTo(p);
                if (distanceToRect < distanceToNearest)
                {
                    nearestRecursive(m, n.getLeft(), p, bottomRect);
                }
            }
        }
    }
	
	
	
	

}
