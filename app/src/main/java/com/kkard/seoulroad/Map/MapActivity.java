package com.kkard.seoulroad.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kkard.seoulroad.R;
import com.kkard.seoulroad.utils.DialogView_C;

import java.util.ArrayList;

/**
 * Created by SuGeun on 2017-08-30.
 */

public class MapActivity extends Fragment implements OnMapReadyCallback{
    Marker selectedMarker;
    View marker_root_view;
    TextView tv_marker;
    private View.OnClickListener exit,call;
    private DialogView_C mDialog;
    private GoogleMap mMap1;
    private MapView mMap = null;
    private LatLngBounds ADELAIDE = new LatLngBounds(
            new LatLng(37.554575, 126.967306), new LatLng(37.557825, 126.977383));
    Button recommandBtn;
    Context context;
    @Nullable
    private void setCustomMarkerView() {

        marker_root_view = LayoutInflater.from(this.getContext()).inflate(R.layout.marker_layout, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);
    }
    private void getSampleMarkerItems() {
        ArrayList<MarkerItem> sampleList = new ArrayList();


        sampleList.add(new MarkerItem(37.556706, 126.972701, "수국식빵"));
        sampleList.add(new MarkerItem(37.555367, 126.968318, "담쟁이 극장"));
        sampleList.add(new MarkerItem(37.557363, 126.976054, "서울로 가게"));
        sampleList.add(new MarkerItem(37.557470, 126.976399, "호기심화분"));
        sampleList.add(new MarkerItem(37.555588, 126.968323, "정원교실"));

        for (MarkerItem markerItem : sampleList) {
            addMarker(markerItem);
        }
    }
    private Marker addMarker(MarkerItem markerItem) {


        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        String price = markerItem.getTitle();

        tv_marker.setText(price);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(price);
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this.getContext(), marker_root_view)));


        return mMap1.addMarker(markerOptions);

    }
    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_map,container,false);
        mMap =(MapView)layout.findViewById(R.id.map);
        mMap.getMapAsync(this);
        return layout;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMap.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMap.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMap.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMap.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMap.onLowMemory();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap1 = googleMap;
        LatLng SeoulloCenter = new LatLng(37.556506, 126.972488);
        mMap1.moveCamera(CameraUpdateFactory.newLatLng(SeoulloCenter));
        mMap1.animateCamera(CameraUpdateFactory.zoomTo(17));
        mMap1.setLatLngBoundsForCameraTarget(ADELAIDE);
        setCustomMarkerView();
        getSampleMarkerItems();
        mMap1.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                exit = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                    }
                };
                final String title = marker.getTitle();
                Log.e("지도",title);
                switch (title){
                    case "수국식빵":
                        call = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:02-312-1892"));
                                startActivity(intent);
                            }
                        };
                        mDialog = new DialogView_C(DialogView_C.DIA_TYPE_MAP,context,title,"한국식 철판토스트, 커피판매",R.drawable.suguk,exit,call);
                        break;
                    case "담쟁이 극장":
                        call = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:02-312-9575"));
                                startActivity(intent);
                            }
                        };
                        mDialog = new DialogView_C(DialogView_C.DIA_TYPE_MAP,context,title,"어린이 인령극 정례공연, 구현동화",R.drawable.damjang,exit,call);
                        break;
                    case "서울로 가게":
                        call = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:02-312-9836"));
                                startActivity(intent);
                            }
                        };
                        mDialog = new DialogView_C(DialogView_C.DIA_TYPE_MAP,context,title,"서울로 기념품 전시, 판매",R.drawable.gage,exit,call);
                        break;
                    case "호기심화분":
                        call = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:02-312-9575"));
                                startActivity(intent);
                            }
                        };
                        mDialog = new DialogView_C(DialogView_C.DIA_TYPE_MAP,context,title,"서울의 대표적 장소 18개소의 소리와 영상",R.drawable.hogisim,exit,call);
                        break;
                    case "정원교실":
                        call = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:02-312-9575"));
                                startActivity(intent);
                            }
                        };
                        mDialog = new DialogView_C(DialogView_C.DIA_TYPE_MAP,context,title,"식물과 책과 그림이 있는곳",R.drawable.jungwon,exit,call);
                        break;
                }
                mDialog.show();
                return false;
            }
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mMap !=null)mMap.onCreate(savedInstanceState);
        recommandBtn = (Button)getView().findViewById(R.id.recommand_btn);
        context=getContext();
        recommandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),CourseActivity.class));
                ((Activity)context).finish();
            }
        });
    }
}