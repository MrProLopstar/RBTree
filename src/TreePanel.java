import rbtree.RBNode;
import rbtree.RBTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TreePanel extends JPanel {
	private RBTree<?> tree;
	private int radius=20, yOffset=50;
	private Color textColor = new Color(230, 230, 230);
	private Comparable<?> toSearch;
	private RBNode<?> selectedNode = null;
	private int dragDx = 0, dragDy = 0;
	private HashMap<RBNode<?>, Point> nodeLocations = new HashMap<>();
	public TreePanel(RBTree<?> tree){
		this.tree = tree;
		addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				selectedNode = getClickedNode(e.getX(), e.getY());
				if (selectedNode != null) {
					Point nodeLocation = nodeLocations.get(selectedNode);
					dragDx = nodeLocation.x - e.getX();
					dragDy = nodeLocation.y - e.getY();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) { selectedNode = null; }
		});

		addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e) {
				if (selectedNode != null) {
					int newX = e.getX() + dragDx;
					int newY = e.getY() + dragDy;
					moveNodeAndChildren(selectedNode, newX - getNodeLocation(selectedNode).x, newY - getNodeLocation(selectedNode).y);
					repaint();
				}
			}
		});
	};
	private RBNode<?> getClickedNode(int x, int y){
		for(Map.Entry<RBNode<?>, Point> entry : nodeLocations.entrySet()){
			Point point = entry.getValue();
			if(Math.abs(point.x-x)<=radius && Math.abs(point.y-y)<=radius) return entry.getKey();
		}
		return null;
	}
	public void setSearch(Comparable<?> c) {
		toSearch = c;
	}
	@Override
	protected void paintComponent(Graphics graphics){
		super.paintComponent(graphics);
		if(tree.isEmpty()) return;
		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //сглаживание
		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //сглаживание текста
		paintTree(graphics2d, (RBNode<?>) tree.getRoot(), getWidth() / 2, 30, getGap());
	}

	private void paintTree(Graphics2D g, RBNode<?> root, int x, int y, int xOffset) {
		if(x<0) setPreferredSize(new Dimension(2 * getWidth(), getHeight()));
		if(toSearch != null && RBNode.compare(root, toSearch) == 0) drawHalo(g, x, y);

		Point location = getNodeLocation(root, x, y);
		drawNode(g, root, location.x, location.y);

		if(root.getLeft() != null) {
			Point leftLocation = getNodeLocation(root.getLeft(), location.x - xOffset, location.y + yOffset);
			join(g, location, leftLocation);
			paintTree(g, (RBNode<?>) root.getLeft(), leftLocation.x, leftLocation.y, xOffset/2);
		}
		if(root.getRight() != null){
			Point rightLocation = getNodeLocation(root.getRight(), location.x + xOffset, location.y + yOffset);
			join(g, location, rightLocation);
			paintTree(g, (RBNode<?>) root.getRight(), rightLocation.x, rightLocation.y, xOffset/2);
		}
	}
	private Point getNodeLocation(RBNode<?> node) {
		return nodeLocations.get(node);
	}
	private Point getNodeLocation(RBNode<?> node, int defaultX, int defaultY) {
		if(!nodeLocations.containsKey(node)) nodeLocations.put(node, new Point(defaultX, defaultY));
		return nodeLocations.get(node);
	}
	private void moveNodeAndChildren(RBNode<?> node, int dx, int dy) {
		Point location = getNodeLocation(node);
		if(location!=null){
			location.x += dx;
			location.y += dy;
		}
		if(node.getLeft()!=null) moveNodeAndChildren((RBNode<?>) node.getLeft(), dx, dy);
		if(node.getRight()!=null) moveNodeAndChildren((RBNode<?>) node.getRight(), dx, dy);
	}

	private void drawHalo(Graphics2D g, int x, int y) {
		g.setColor(new Color(160,100,250));
		radius += 5;
		g.fillOval(x-radius, y-radius, 2*radius, 2*radius);
		radius -= 5;
	}

	private void drawNode(Graphics2D g, RBNode<?> node, int x, int y) {
		g.setColor(node.getActualColor());
		g.fillOval(x-radius, y-radius, 2*radius, 2*radius);
		g.setColor(textColor);
		String text = node.toString();
		drawCentreText(g, text, x, y);
		g.setColor(Color.GRAY);
	}

	private void drawCentreText(Graphics2D g, String text, int x, int y) {
		FontMetrics fm = g.getFontMetrics();
		double t_width = fm.getStringBounds(text, g).getWidth();
		g.drawString(text,(int)(x-t_width/2),(int)(y+fm.getMaxAscent()/2));
	}

	private void join(Graphics2D g, Point p1, Point p2){
		double d = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
		int x1 = (int) (p1.x+radius*(p2.x-p1.x)/d);
		int y1 = (int) (p1.y+radius*(p2.y-p1.y)/d);
		int x2 = (int) (p2.x-radius*(p2.x-p1.x)/d);
		int y2 = (int) (p2.y-radius*(p2.y-p1.y)/d);
		g.drawLine(x1,y1,x2,y2);
	}

	private int getGap() {
		int depth = tree.getDepth();
		int multiplier = 30;
		float exponent = (float) 1.4;
		if(depth>6){
			multiplier += depth * 3;
			exponent += .1;
		}
		return (int) Math.pow(depth,exponent)*multiplier;
	}
}