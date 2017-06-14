package net.ddns.knights.lightchanger;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tejas on 5/31/2017.
 */

public class ColorListAdapter extends RecyclerView.Adapter<ColorListAdapter.ColorListViewHolder> {


    List<Integer> colors = new ArrayList<>();

    private Context mContext;

    ColorDbHelper helper;

    public ColorListAdapter(Context context){
        this.mContext = context;
        helper = new ColorDbHelper(mContext);
        colors = helper.getAllColors();
    }

    @Override
    public ColorListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_color,parent,false);
        return new ColorListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ColorListViewHolder holder, int position) {
        holder.bind(colors.get(position));
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public void doDelete(int color){
        helper.deleteColorItem(color);
        refresh();
    }

    public void refresh(){
        colors = helper.getAllColors();
        notifyDataSetChanged();
    }

    void changeColor(int color){
        final int red = Color.red(color);
        final int green = Color.green(color);
        final int blue = Color.blue(color);
        RequestQueue queue = Volley.newRequestQueue(mContext);
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
                m.put("instant",String.valueOf(0));

                return m;
            }
        };
        queue.add(request);
    }

    class ColorListViewHolder extends RecyclerView.ViewHolder{

        private ImageView preview;
        private View mView;

        public ColorListViewHolder(View itemView) {
            super(itemView);
            preview = (ImageView) itemView.findViewById(R.id.list_item_preview);
            mView = itemView;
        }

        public void bind(final int color){
            preview.setImageDrawable(new ColorDrawable(color));
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeColor(color);
                }
            });
            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Confirm Delete?")
                            .setMessage("Are you sure you want to delete this color?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    doDelete(color);
                                }
                            }).setNegativeButton("No", null).show();
                    return true;
                }
            });
        }
    }
}
