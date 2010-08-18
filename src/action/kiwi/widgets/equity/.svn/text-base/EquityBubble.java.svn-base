

package kiwi.widgets.equity;


import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import kiwi.api.equity.EquityService;
import kiwi.model.content.ContentItem;

class EquityBubble {

    private static Map<ContentItem, BubbleValue> bubbleValues = new HashMap<ContentItem, BubbleValue>();

    static BufferedImage getImageFor(EquityService service, ContentItem contentItem) {
        final BubbleValue bubbleValue = getBubbleValue(service, contentItem);
        final BufferedImage image = getImage(bubbleValue);
        return image;
    }
    
    private static BubbleValue getBubbleValue(EquityService service, ContentItem contentItem) {
        final double actual = service.getContentItemEquity(contentItem);
        BubbleValue lastBubbleValue = bubbleValues.get(contentItem);
        if (lastBubbleValue == null) {
            lastBubbleValue = new BubbleValue();
        }
        
        final Point2D lastPoint = lastBubbleValue.getPoint2d();
        final double y = lastPoint.getY();
        final Point2D newPoint;
        
        if (actual == 0) {
            final BubbleValue zeroBubbleValue = new BubbleValue();
            bubbleValues.put(contentItem, zeroBubbleValue);
            return new BubbleValue();
        }
        
        if (y <= actual) {
            newPoint = new Point2D.Double(actual, actual);
            
        } else {
            newPoint = new Point2D.Double(lastPoint.getX(), actual);
        }

        final int scale = 3; 
        final Ellipse2D.Double shape = 
                new Ellipse2D.Double(0, 0, newPoint.getX() * scale, newPoint.getY() * scale);

        final BubbleValue result = new BubbleValue();
        result.setShape(shape);
        result.setPoint2d(newPoint);

        final Color endColor = new Color(computeColor(actual),computeColor(actual),255);
        final GradientPaint redtowhite =
                new GradientPaint(0, 0, Color.WHITE, (int) (newPoint.getX() * scale), (int) (newPoint.getY() * scale), endColor);
        result.setGradientPaint(redtowhite);
        
        bubbleValues.put(contentItem, result);

        return result;
    }
    
    private static int computeColor(double value) {
        int result = (int) (255 - value * 40);
        return result <= 0 ? 0 : result;
    }
    
    private static BufferedImage getImage(BubbleValue bubbleValue) {
        
        final Shape shape = bubbleValue.getShape(); 
        final Rectangle bounds = shape.getBounds();
        final int width = bounds.width + 10;
        final int height = bounds.height + 10;
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final GradientPaint gradientPaint = bubbleValue.getGradientPaint();
        
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setPaint(gradientPaint);
        g.fill(shape);
        g.dispose();
        
        return image;
    }
    
    private static class BubbleValue {
        private Shape shape;
        private GradientPaint gradientPaint;
        private BasicStroke stroke;
        private Point2D point2d;
        
        BubbleValue(){
            point2d = new Point2D.Double(0, 0);
            shape = new Ellipse2D.Double(0, 0, 20, 20);
            final Color fromColor = Color.GRAY;
            final Color toColor = Color.BLACK;
            gradientPaint = new GradientPaint(10,10,fromColor, 20, 20, toColor);
            
            float dash1[] = {10.0f};
            stroke = new BasicStroke(3.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, dash1, 0.0f);
        }

        public Shape getShape() {
            return shape;
        }

        public void setShape(Shape shape) {
            this.shape = shape;
        }

        public GradientPaint getGradientPaint() {
            return gradientPaint;
        }
        
        public BasicStroke getBasicStroke() {
            return stroke;
        }
        
        public Point2D getPoint2d() {
            return point2d;
        }
        
        public void setGradientPaint(GradientPaint gradientPaint) {
            this.gradientPaint = gradientPaint;
        }
        
        public void setStroke(BasicStroke basicStroke) {
            this.stroke = basicStroke;
        }
        
        public void setPoint2d(Point2D point2d) {
            this.point2d = point2d;
        }
    }
}
