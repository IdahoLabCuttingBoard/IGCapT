/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.gui.jmapviewer;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.Arrays;
import java.util.List;

import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;
import org.openstreetmap.gui.jmapviewer.interfaces.MapLine;
/**
 *(c) 2018 BATTELLE ENERGY ALLIANCE, LLC
 *ALL RIGHTS RESERVED 
 *
 * @author kur
 */
public class MapLineImpl extends MapObjectImpl implements MapLine {
    Color lineColor = Color.black;

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    private List<? extends ICoordinate> points;
    String id;

    public MapLineImpl(ICoordinate ... points) {
        this(null, null, points);
    }

    public MapLineImpl(List<? extends ICoordinate> points) {
        this(null, null, points);
    }

    public MapLineImpl(String name, List<? extends ICoordinate> points) {
        this(null, name, points);
    }

    public MapLineImpl(String name, ICoordinate ... points) {
        this(null, name, points);
    }

    public MapLineImpl(Layer layer, List<? extends ICoordinate> points) {
        this(layer, null, points);
    }

    public MapLineImpl(Layer layer, String name, List<? extends ICoordinate> points) {
        this(layer, name, points, getDefaultStyle());
    }

    public MapLineImpl(Layer layer, String name, ICoordinate ... points) {
        this(layer, name, Arrays.asList(points), getDefaultStyle());
    }

    public MapLineImpl(Layer layer, String name, List<? extends ICoordinate> points, Style style) {
        super(layer, name, style);
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public void setId(String someId) {
        id = someId;
    }

    @Override
    public List<? extends ICoordinate> getPoints() {
        return this.points;
    }

    @Override
    public void paint(Graphics g, List<Point> points) {
        //System.out.println("paint(Graphics g, List<Point> points) number of points = " + points.size());
        Polygon polygon = new Polygon();
        for (Point p : points) {
            polygon.addPoint(p.x, p.y);
        }
        paint(g, polygon);
    }

    @Override
    public void paint(Graphics g, Polygon polygon) {
        // Prepare graphics
        Color oldColor = g.getColor();
        g.setColor(getColor());

        Stroke oldStroke = null;
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            oldStroke = g2.getStroke();
            g2.setStroke(getStroke());
        }
        // get the first and second point
        // Draw
        //int numberOfPoints = polygon.npoints;
        //System.out.println("number of points = " + numberOfPoints);
        //System.out.println("start and end x,y = " + polygon.xpoints[0] + ", " + polygon.ypoints[0] + "     "  + polygon.xpoints[1] + ", " + polygon.ypoints[1]);
        //if (numberOfPoints == 2) {
            //g.setColor(Color.black);
            g.setColor(getColor());
            g.drawLine(polygon.xpoints[0], polygon.ypoints[0], polygon.xpoints[1], polygon.ypoints[1]);
            int baseX = 0;
            int baseY = 0;
            int x0 = 0;
            int x1 = 0;
            int y0 = 0;
            int y1 = 0;
            
            int x0minusX1 = Math.abs(polygon.xpoints[0] - polygon.xpoints[1]);
            int y0minusY1 = Math.abs(polygon.ypoints[0] - polygon.ypoints[1]);
            if (polygon.xpoints[0] > polygon.xpoints[1]) {
                baseX = polygon.xpoints[0];
                x0 = baseX;
                x1 = polygon.xpoints[1];
            }
            else {
                baseX = polygon.xpoints[1];
                x0 = baseX;
                x1 = polygon.xpoints[0];                
            }
            if (polygon.ypoints[0] > polygon.ypoints[1]) {
                baseY = polygon.ypoints[0];
                y0 = baseY;
                y1 = polygon.ypoints[1];
            }
            else {
                baseY = polygon.ypoints[1];
                y0 = baseY;
                y1 = polygon.ypoints[0];                
            }
            int distanceBetweenPoints = (int)Math.sqrt((Math.pow(x0 - x1, 2)) + (Math.pow(y0 - y1, 2)));
            //System.out.println("distance between points = " + polygon.xpoints[0] + ", " + polygon.ypoints[0] + " and " + polygon.xpoints[1] + ", " + polygon.ypoints[1] + " = " + distanceBetweenPoints);
            //endPt1 = (SgNode)endpoints.getFirst();
           // endPt2 = (SgNode)endpoints.getSecond();
            double pt1X = polygon.xpoints[0];
            double pt1Y = polygon.ypoints[0];
            double pt2X = polygon.xpoints[1];
            double pt2Y = polygon.ypoints[1];
            double slope = (pt2Y - pt1Y)/(pt2X - pt1X);
            int newY = (int) ((pt2Y - pt1Y)/2 + pt1Y);
            int newX = (int) ((pt2X - pt1X)/2 + pt1X);
            g.setColor(Color.black);
            g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, g.getFont().getSize()));
            g.drawString(getId(), newX, newY);

            // y = mx + b
            // y2 - y1 = m (x2 - x1)
            // get x,y of both endpoint         
            /*
            // this doesn't quite work!
            g.setColor(Color.black);
            g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, g.getFont().getSize()));
            //g.drawString(getId(), x0 - (x1 + distanceBetweenPoints/2), y0 - (y1 + distanceBetweenPoints/2));
            if (slope < 0)
                g.drawString(getId(), x1 + distanceBetweenPoints/2, y1 + distanceBetweenPoints/2);
            else
                g.drawString(getId(), x1 + distanceBetweenPoints/2, y0 - distanceBetweenPoints/2);
             */   
            
        //}
    /*    g.drawPolygon(polygon);
        if (g instanceof Graphics2D && getBackColor() != null) {
            Graphics2D g2 = (Graphics2D) g;
            Composite oldComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2.setPaint(getBackColor());
            g2.fillPolygon(polygon);
            g2.setComposite(oldComposite);
        }
        // Restore graphics
        g.setColor(oldColor);
        if (g instanceof Graphics2D) {
            ((Graphics2D) g).setStroke(oldStroke);
        }
        Rectangle rec = polygon.getBounds();
        Point corner = rec.getLocation();
        Point p = new Point(corner.x+(rec.width/2), corner.y+(rec.height/2));
        if (getLayer() == null || getLayer().isVisibleTexts()) paintText(g, p); */
    }

    public static Style getDefaultStyle() {
        return new Style(Color.BLACK, new Color(100, 100, 100, 50), new BasicStroke(1), getDefaultFont());
    }

    @Override
    public String toString() {
        return "MapLine [points=" + points + ']';
    }
}
