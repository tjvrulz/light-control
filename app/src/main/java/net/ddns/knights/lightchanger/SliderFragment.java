package net.ddns.knights.lightchanger;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tejas on 5/31/2017.
 */

public class SliderFragment extends android.support.v4.app.Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private View mView;

    private int red;
    private int green;
    private int blue;

    private SeekBar redBar;
    private SeekBar greenBar;
    private SeekBar blueBar;

    Button fadeToColor;
    Button switchToColor;
    Button saveButton;

    private ImageView preview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sliders,container,false);

        redBar = (SeekBar) mView.findViewById(R.id.redSlider);
        greenBar = (SeekBar) mView.findViewById(R.id.greenSlider);
        blueBar = (SeekBar) mView.findViewById(R.id.blueSlider);

        preview = (ImageView) mView.findViewById(R.id.preview);

        fadeToColor = (Button) mView.findViewById(R.id.fadeButton);
        switchToColor = (Button)mView.findViewById(R.id.switchButton);
        saveButton = (Button)mView.findViewById(R.id.saveButton);

        redBar.setOnSeekBarChangeListener(this);
        greenBar.setOnSeekBarChangeListener(this);
        blueBar.setOnSeekBarChangeListener(this);

        fadeToColor.setOnClickListener(this);
        switchToColor.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(seekBar.getId() == redBar.getId()) red = i;
        else if(seekBar.getId() == greenBar.getId()) green = i;
        else if(seekBar.getId() == blueBar.getId()) blue = i;
        preview.setImageDrawable(new ColorDrawable(Color.rgb(red,green,blue)));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == switchToColor.getId()) changeColors(1);
        if(view.getId() == fadeToColor.getId()) changeColors(0);
        if(view.getId() == saveButton.getId()){
            ColorDbHelper helper = new ColorDbHelper(getActivity());
            helper.addColor(red,green,blue);
            Toast.makeText(getActivity(),"Color Saved Successfully",Toast.LENGTH_SHORT).show();
        }
    }

    public void changeColors(final int instant){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Request request = new StringRequest(Request.Method.POST, "http://192.168.1.100/ChangeLights.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map m = new HashMap();
                m.put("r",String.valueOf(red));
                m.put("g",String.valueOf(green));
                m.put("b",String.valueOf(blue));
                m.put("instant",String.valueOf(instant));

                return m;
            }
        };
        queue.add(request);
    }

}
