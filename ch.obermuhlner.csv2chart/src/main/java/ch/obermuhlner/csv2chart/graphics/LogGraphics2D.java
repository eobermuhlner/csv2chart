package ch.obermuhlner.csv2chart.graphics;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.AttributedCharacterIterator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LogGraphics2D extends Graphics2D {

	private int width;
	private int height;

	private PrintWriter out;
	private Font font;
	private Paint paint;
	private Stroke stroke;
	private Color color;
	private Color backgroundColor;
	private Map<Key, Object> renderingHint = new HashMap<>();
	private FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), false, false);
	private AffineTransform affineTransform;
	private Composite composite;

	public LogGraphics2D(int width, int height, Writer writer) {
		this.width = width;
		this.height = height;
		
		this.out = new PrintWriter(writer);
	}

	@Override
	public void draw(Shape shape) {
		out.println("draw " + toString(shape));
	}

	@Override
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
		out.println("drawImage");
		return true;
	}

	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
		out.println("drawImage");
	}

	@Override
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
		out.println("drawRenderedImage");
	}

	@Override
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
		out.println("drawRenderableImage");
	}

	@Override
	public void drawString(String str, int x, int y) {
		out.println("drawString '" + str + "' " + x + " " + y);
	}

	@Override
	public void drawString(String str, float x, float y) {
		out.println("drawString '" + str + "' " + x + " " + y);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		out.println("drawString '" + iterator + "' " + x + " " + y);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, float x, float y) {
		out.println("drawString '" + iterator + "' " + x + " " + y);
	}

	@Override
	public void drawGlyphVector(GlyphVector g, float x, float y) {
		out.println("drawGlyphVector '" + g + "' " + x + " " + y);
	}

	@Override
	public void fill(Shape shape) {
		out.println("fill " + toString(shape));
	}

	@Override
	public boolean hit(Rectangle rect, Shape shape, boolean onStroke) {
		out.println("hit " + rect + " " + toString(shape) + " " + onStroke);
		return true;
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setComposite(Composite composite) {
		this.composite = composite;
	}

	@Override
	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	@Override
	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}

	@Override
	public void setRenderingHint(Key hintKey, Object hintValue) {
		renderingHint.put(hintKey, hintValue);
	}

	@Override
	public Object getRenderingHint(Key hintKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRenderingHints(Map<?, ?> hints) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRenderingHints(Map<?, ?> hints) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RenderingHints getRenderingHints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void translate(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void translate(double tx, double ty) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotate(double theta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotate(double theta, double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scale(double sx, double sy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shear(double shx, double shy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transform(AffineTransform Tx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTransform(AffineTransform Tx) {
		affineTransform = Tx;
		
	}

	@Override
	public AffineTransform getTransform() {
		return affineTransform;
	}

	@Override
	public Paint getPaint() {
		return paint;
	}

	@Override
	public Composite getComposite() {
		return composite;
	}

	@Override
	public void setBackground(Color color) {
		backgroundColor = color;
	}

	@Override
	public Color getBackground() {
		return backgroundColor;
	}

	@Override
	public Stroke getStroke() {
		return stroke;
	}

	@Override
	public void clip(Shape shape) {
		out.println("clip " + shape);
	}

	@Override
	public FontRenderContext getFontRenderContext() {
		return fontRenderContext;
	}

	@Override
	public Graphics create() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void setPaintMode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setXORMode(Color c1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Font getFont() {
		return font;
	}

	@Override
	public void setFont(Font font) {
		this.font = font;
	}

	@Override
	public FontMetrics getFontMetrics(Font f) {
		return new DummyFontMetrics(f);
	}

	@Override
	public Rectangle getClipBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clipRect(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClip(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Shape getClip() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClip(Shape clip) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		out.println("copyArea " + x + " " + y + " " + width + " " + height + " " + dx + " " + dy);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		out.println("drawLine " + x1 + " " + y1 + " " + x2 + " " + y2);
	}

	@Override
	public void fillRect(int x, int y, int width, int height) {
		out.println("fillRect " + x + " " + y + " " + width + " " + height);
	}

	@Override
	public void clearRect(int x, int y, int width, int height) {
		out.println("clearRect " + x + " " + y + " " + width + " " + height);
	}

	@Override
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		out.println("drawRoundRect " + x + " " + y + " " + width + " " + height + " " + arcWidth + " " + arcHeight);
	}

	@Override
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		out.println("fillRoundRect " + x + " " + y + " " + width + " " + height + " " + arcWidth + " " + arcHeight);
	}

	@Override
	public void drawOval(int x, int y, int width, int height) {
		out.println("drawOval " + x + " " + y + " " + width + " " + height);
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		out.println("fillOval " + x + " " + y + " " + width + " " + height);
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		out.println("drawArc " + x + " " + y + " " + width + " " + height + " " + startAngle + " " + arcAngle);
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		out.println("fillArc " + x + " " + y + " " + width + " " + height + " " + startAngle + " " + arcAngle);
	}

	@Override
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		out.println("drawPolyline " + Arrays.toString(xPoints) + " " + Arrays.toString(xPoints) + " " + nPoints);
	}

	@Override
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		out.println("drawPolygon " + Arrays.toString(xPoints) + " " + Arrays.toString(xPoints) + " " + nPoints);
	}

	@Override
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		out.println("fillPolygon " + Arrays.toString(xPoints) + " " + Arrays.toString(xPoints) + " " + nPoints);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		out.println("drawImage img " + x + " " + y + " ");
		return true;
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
		out.println("drawImage img " + x + " " + y + " " + width + " " + height);
		return true;
	}

	@Override
	public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
		out.println("drawImage img " + x + " " + y + " " + bgcolor);
		return true;
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
		out.println("drawImage img " + x + " " + y + " " + width + " " + height + " " + bgcolor);
		return true;
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
		out.println("drawImage img " + dx1 + " " + dy1 + " " + dx2 + " " + dy2 + " " + sx1 + " " + sy1 + " " + sx2 + " " + sy2);
		return true;
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
		out.println("drawImage img " + dx1 + " " + dy1 + " " + dx2 + " " + dy2 + " " + sx1 + " " + sy1 + " " + sx2 + " " + sy2 + " " + bgcolor);
		return true;
	}

	@Override
	public void dispose() {
		// do nothing
	}

	private String toString(Shape shape) {
		StringBuilder result = new StringBuilder();
		
		if (shape instanceof Rectangle) {
			Rectangle that = (Rectangle) shape;
			result.append(that);
		} else if (shape instanceof Line2D.Double) {
			Line2D.Double that = (Line2D.Double) shape;
			result.append(shape.getClass().getName());
			result.append(" ");
			result.append(that.x1);
			result.append(" ");
			result.append(that.y1);
			result.append(" ");
			result.append(that.x2);
			result.append(" ");
			result.append(that.y2);
		} else if (shape instanceof Rectangle2D.Double) {
			Rectangle2D.Double that = (Rectangle2D.Double) shape;
			result.append(shape.getClass().getName());
			result.append(" ");
			result.append(that.x);
			result.append(" ");
			result.append(that.y);
			result.append(" ");
			result.append(that.width);
			result.append(" ");
			result.append(that.height);
		} else if (shape instanceof Path2D.Double) {
			Path2D.Double that = (Path2D.Double) shape;
			result.append(shape.getClass().getName());
			PathIterator pathIterator = that.getPathIterator(null);
			float[] coords = new float[6];
			while (!pathIterator.isDone()) {
				pathIterator.next();
				int segment = pathIterator.currentSegment(coords);
				
				result.append(" ");
				result.append(convertPathIteratorSegmentToString(segment));
				result.append(":");
				result.append(Arrays.toString(coords));
			}
		} else {
			result.append(String.valueOf(shape));
		}
		
		return result.toString();
	}


	private String convertPathIteratorSegmentToString(int segment) {
		switch(segment) {
		case PathIterator.SEG_CLOSE:
			return "close";
		case PathIterator.SEG_CUBICTO:
			return "cubicto";
		case PathIterator.SEG_LINETO:
			return "lineto";
		case PathIterator.SEG_MOVETO:
			return "moveto";
		case PathIterator.SEG_QUADTO:
			return "quadto";
		}
		
		return String.valueOf(segment);
	}


	private class LogGraphicsConfiguration extends GraphicsConfiguration {

		@Override
		public GraphicsDevice getDevice() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ColorModel getColorModel() {
			return new DirectColorModel(24, 0xff0000, 0x00ff00, 0x0000ff);
		}

		@Override
		public ColorModel getColorModel(int transparency) {
			return getColorModel();
		}

		@Override
		public AffineTransform getDefaultTransform() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AffineTransform getNormalizingTransform() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Rectangle getBounds() {
			return new Rectangle(0, 0, width, height);
		}
		
	}
	
}
