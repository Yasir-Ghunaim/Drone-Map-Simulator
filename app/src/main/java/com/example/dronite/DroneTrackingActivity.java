package com.example.dronite;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.util.Arrays;
import java.util.List;

import static com.example.dronite.R.id.map;


/**
 * An activity that tracks a drone on a Google map while showing no fly zones
 */
public class DroneTrackingActivity extends AppCompatActivity
        implements
                OnMapReadyCallback {

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_RED_ARGB = 0xffd32f2f;
    private static final int COLOR_DARK_RED_ARGB = 0xffef5350;
    private static final int COLOR_GREEN_ARGB = 0xff66bb6a;
    private static final int COLOR_DARK_GREEN_ARGB = 0xff2e7d32;
    private static final int COLOR_BLUE_ARGB = 0xff64b5f6;
    private static final int COLOR_DARK_BLUE_ARGB = 0xff1565c0;

    private static final double RADIUS_OF_EARTH_METERS = 6371009;

    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);

    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);

    private GoogleMap mMap;

    private int mStrokeWidthBar = 5;

    public static final CameraPosition ORIGIN_CLOSE =
            new CameraPosition.Builder().target(new LatLng(26.329, 50.147))
                    .zoom(17)
                    .bearing(310)
                    .tilt(60)
                    .build();

    public static final CameraPosition ORIGIN =
            new CameraPosition.Builder().target(new LatLng(26.329, 50.147))
                    .zoom(14.5f)
                    .bearing(310)
                    .tilt(30)
                    .build();

    public static final CameraPosition FIRST_POINT =
            new CameraPosition.Builder().target(new LatLng(26.339, 50.135))
                    .zoom(14.5f)
                    .bearing(305)
                    .tilt(30)
                    .build();


    public static final CameraPosition SECOND_POINT =
            new CameraPosition.Builder().target(new LatLng(26.345, 50.122))
                    .zoom(14.5f)
                    .bearing(290)
                    .tilt(40)
                    .build();

    public static final CameraPosition THIRD_POINT =
            new CameraPosition.Builder().target(new LatLng(26.345, 50.106))
                    .zoom(14.5f)
                    .bearing(270)
                    .tilt(40)
                    .build();



    public static final CameraPosition DESTINATION =
            new CameraPosition.Builder().target(new LatLng(26.338, 50.083))
                    .zoom(14.5f)
                    .bearing(250)
                    .tilt(40)
                    .build();

    public static final CameraPosition DESTINATION_CLOSE =
            new CameraPosition.Builder().target(new LatLng(26.338, 50.083))
                    .zoom(17)
                    .bearing(250)
                    .tilt(60)
                    .build();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }


    public void animateCameraTo(CameraPosition cameraPosition, int duration) {

        changeCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                duration,
                new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }


    /**
     * Change the camera position by moving or animating the camera depending on the state of the
     * animate toggle button.
     */
    private void changeCamera(CameraUpdate update, int duration, GoogleMap.CancelableCallback callback) {
        // The duration must be strictly positive so we make it at least 1.
        mMap.animateCamera(update, Math.max(duration, 1), callback);

    }


    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this tutorial, we add polylines and polygons to represent routes and areas on the map.
     */
    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;


        // Add first no fly zone.
        Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
                .clickable(false)
                .add(
                        new LatLng(26.346, 50.082),
                        new LatLng(26.349, 50.093),
                        new LatLng(26.359, 50.087),
                        new LatLng(26.354, 50.077)));
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        polygon1.setTag("alpha");
        // Style the polygon.
        stylePolygon(polygon1);

        // Add second no fly zone.
        Polygon polygon2 = mMap.addPolygon(new PolygonOptions()
                .clickable(false)
                .add(
                        new LatLng(26.304, 50.047),
                        new LatLng(26.319, 50.061),
                        new LatLng(26.338, 50.115),
                        new LatLng(26.324, 50.143),
                        new LatLng(26.296, 50.164),
                        new LatLng(26.260, 50.087)));
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        polygon2.setTag("alpha");
        // Style the polygon.
        stylePolygon(polygon2);

        // Origin
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(26.329, 50.147))
                .radius(300)
                .strokeWidth(mStrokeWidthBar)
                .strokeColor(COLOR_DARK_BLUE_ARGB)
                .fillColor(COLOR_BLUE_ARGB)
                .clickable(false));

        // Destination
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(26.338, 50.083))
                .radius(300)
                .strokeWidth(mStrokeWidthBar)
                .strokeColor(COLOR_DARK_GREEN_ARGB)
                .fillColor(COLOR_GREEN_ARGB)
                .clickable(false));

        // Add Route from origin to destination
        Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                .clickable(false)
                .add(
                        new LatLng(26.338, 50.083),
                        new LatLng(26.345, 50.106),
                        new LatLng(26.345, 50.122),
                        new LatLng(26.339, 50.135),
                        new LatLng(26.329, 50.147)));
        // Store a data object with the polyline, used here to indicate an arbitrary type.
        polyline1.setTag("A");
        // Style the polyline.
        stylePolyline(polyline1);

        /*map.addMarker(new MarkerOptions()
                .position(new LatLng(26.350, 50.061))
                .title("Hello world"));*/




        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.347, 50.109), 12));


        animateAfterDelay(ORIGIN_CLOSE, 5000, 3000);
        animateAfterDelay(ORIGIN, 4000, 8000);
        animateAfterDelay(FIRST_POINT, 4000, 12000);
        animateAfterDelay(SECOND_POINT, 4000, 16000);
        animateAfterDelay(THIRD_POINT, 4000, 20000);
        animateAfterDelay(DESTINATION, 4500, 24000);
        animateAfterDelay(DESTINATION_CLOSE, 4000, 28500);



    }

    private void animateAfterDelay(final CameraPosition cameraPosition, final int duration, int delay){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("MY DEBUGGER", "run()");
                animateCameraTo(cameraPosition, duration);
            }
        }, delay);
    }

    /**
     * Styles the polyline, based on type.
     * @param polyline The polyline object that needs styling.
     */
    private void stylePolyline(Polyline polyline) {
        String type = "";
        // Get the data object stored with the polyline.
        if (polyline.getTag() != null) {
            type = polyline.getTag().toString();
        }

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "A":
                // Use a custom bitmap as the cap at the start of the line.
                polyline.setStartCap(
                        new CustomCap(
                                BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow), 10));
                break;
            case "B":
                // Use a round cap at the start of the line.
                polyline.setStartCap(new RoundCap());
                break;
        }

        polyline.setEndCap(new RoundCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(COLOR_BLACK_ARGB);
        polyline.setJointType(JointType.ROUND);
    }

    /**
     * Styles the polygon, based on type.
     * @param polygon The polygon object that needs styling.
     */
    private void stylePolygon(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "alpha":
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = COLOR_RED_ARGB;
                fillColor = COLOR_DARK_RED_ARGB;
                break;
            case "beta":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA;
                strokeColor = COLOR_ORANGE_ARGB;
                fillColor = COLOR_BLUE_ARGB;
                break;
        }

        polygon.setStrokePattern(pattern);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
    }


}
