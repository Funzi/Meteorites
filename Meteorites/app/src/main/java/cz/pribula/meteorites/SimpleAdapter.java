package cz.pribula.meteorites;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder>{

    protected List<Meteorite> items;

    public SimpleAdapter(List<Meteorite> myDataset) {
        items = myDataset;
    }

    @Override
    public SimpleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meteorite_layout, parent, false);
        SimpleAdapter.ViewHolder vh = new SimpleAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(SimpleAdapter.ViewHolder holder, final int position) {
        holder.itemName.setText(position + 1 + "."+ items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;

        public ViewHolder(CardView v) {
            super(v);
            itemName = (TextView) v.findViewById(R.id.meteorite_name);
        }
    }
}
