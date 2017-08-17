package de.rpgstupe.travellite;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVGParseException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import de.rpgstupe.travellite.activities.MainActivity;

public class WorldMap implements View.OnTouchListener {
    private static String colorDeactivated;
    public static SVGImageView svgImageView;
    private static final String TAG = "WorldMap";
    public static String svgString = "";
    private MainActivity activity;
    private Map<String, Boolean> countryMap;
    private static String colorPrimary;


    public WorldMap(SVGImageView svgImageView, Map<String, Boolean> countryMap, final MainActivity activity) {
        this.activity = activity;
        colorPrimary = String.format("%06X", 0xFFFFFF & activity.getResources().getColor(R.color.colorPrimary));
        colorDeactivated = String.format("%06X", 0xFFFFFF & activity.getResources().getColor(R.color.colorDeactivated));
        this.countryMap = countryMap;
        this.svgImageView = svgImageView;
        svgImageView.setScaleType(ImageView.ScaleType.MATRIX);

        LinearLayout.LayoutParams test = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ((FrameLayout) activity.findViewById(R.id.map_frame)).addView(svgImageView, test);


        svgImageView.setOnTouchListener(this);
        initializeSvgStringFromFile();
        try {
            svgImageView.setSVG(SVG.getFromString(svgString));
        } catch (SVGParseException e) {
            e.printStackTrace();
        }
        activity.findViewById(R.id.map_frame).post(new Runnable() {
            @Override
            public void run() {
                setSize();
            }
        });
    }

    private void setSize() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(svgString));
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            Node node = doc.getFirstChild();


            Element element2 = (Element) node;
            int width = Integer.parseInt(element2.getAttribute("width"));
            int height = Integer.parseInt(element2.getAttribute("height"));
            element2.setAttribute("width", Integer.toString(activity.findViewById(R.id.map_frame).getWidth()));
            element2.setAttribute("height", Double.toString(1d * height / width * activity.findViewById(R.id.map_frame).getWidth()));
            svgImageView.setMinimumHeight(activity.findViewById(R.id.map_background).getHeight());
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            svgString = writer.toString();
            svgImageView.setSVG(SVG.getFromString(svgString));
        } catch (SVGParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void colorizeCountry(String countryCode, boolean activated) {
        System.out.println(countryCode + "    " + activated);
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(svgString));
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            Node node = doc.getElementById("style4");


            Element element2 = (Element) node;
            if (!element2.getTextContent().contains("." + countryCode + "{fill:#")) {
                element2.setTextContent(element2.getTextContent() + "." + countryCode + "{fill:#" + (activated ? colorPrimary : colorDeactivated) + "!important;}");
            } else {
                System.out.println(element2.getTextContent());
                int index = element2.getTextContent().indexOf("." + countryCode + "{fill:#");
                String s = element2.getTextContent().substring(index, index + 17);
                String sNew = s.replaceAll("[A-Z0-9]{6}", activated ? colorPrimary : colorDeactivated);
                element2.setTextContent(element2.getTextContent().substring(0, index) + sNew + element2.getTextContent().substring(index + 17));
                System.out.println(element2.getTextContent());
            }

            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            svgString = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeSvgStringFromFile() {
        try {
            InputStream is = activity.getAssets().open("world_map.svg");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            svgString = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float mVal[] = new float[9];
        SVGImageView view = (SVGImageView) v;
        dumpEvent(event);

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    // ...
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                            - start.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        matrix.getValues(mVal);
        if (mVal[Matrix.MTRANS_X] > 0) {
            matrix.setScale(mVal[Matrix.MSCALE_X], mVal[Matrix.MSCALE_Y]);
            matrix.postTranslate(0, mVal[Matrix.MTRANS_Y]);
            view.setImageMatrix(matrix);
        } else if (mVal[Matrix.MTRANS_X] < view.getWidth() - view.getWidth() * mVal[Matrix.MSCALE_X]) {
            matrix.setScale(mVal[Matrix.MSCALE_X], mVal[Matrix.MSCALE_Y]);
            matrix.postTranslate(view.getWidth() - view.getWidth() * mVal[Matrix.MSCALE_X], mVal[Matrix.MTRANS_Y]);
            view.setImageMatrix(matrix);
        }

        matrix.getValues(mVal);
        if (mVal[Matrix.MTRANS_Y] > 0) {
            matrix.setScale(mVal[Matrix.MSCALE_X], mVal[Matrix.MSCALE_Y]);
            matrix.postTranslate(mVal[Matrix.MTRANS_X], 0);
            view.setImageMatrix(matrix);
        } else if (mVal[Matrix.MTRANS_Y] < view.getHeight() - view.getHeight() * mVal[Matrix.MSCALE_Y]) {
            matrix.setScale(mVal[Matrix.MSCALE_X], mVal[Matrix.MSCALE_Y]);
            matrix.postTranslate(mVal[Matrix.MTRANS_X], view.getHeight() - view.getHeight() * mVal[Matrix.MSCALE_Y]);
            view.setImageMatrix(matrix);
        }

        matrix.getValues(mVal);
        if (mVal[Matrix.MSCALE_X] < 1) {
            matrix.setScale(1, 1);
            matrix.postTranslate(mVal[Matrix.MTRANS_X], mVal[Matrix.MTRANS_Y]);
            view.setImageMatrix(matrix);
        }

        view.setImageMatrix(matrix);
        return true;
    }

    private void dumpEvent(MotionEvent event) {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
        Log.d(TAG, sb.toString());
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public static void update() {
        try {
            System.out.println(svgString);
            svgImageView.setSVG(SVG.getFromString(svgString));
        } catch (SVGParseException e) {
            e.printStackTrace();
        }
    }
}
