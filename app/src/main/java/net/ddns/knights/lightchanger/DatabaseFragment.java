package net.ddns.knights.lightchanger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tejas on 5/31/2017.
 */

public class DatabaseFragment extends Fragment {

    private View mView;

    ColorListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_database,container,false);
        RecyclerView list = (RecyclerView) mView.findViewById(R.id.recycler);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

        adapter = new ColorListAdapter(getActivity());

        list.setLayoutManager(manager);
        list.setAdapter(adapter);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.refresh();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) adapter.refresh();
    }
}
