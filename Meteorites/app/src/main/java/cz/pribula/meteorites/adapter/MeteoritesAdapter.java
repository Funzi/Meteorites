package cz.pribula.meteorites.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.pribula.meteorites.R;
import cz.pribula.meteorites.db.Meteorite;

public class MeteoritesAdapter extends RealmAdapter<Meteorite> {

    private MeteoritesAdapter.OnAdapterItemClickListener listener;

    public interface OnAdapterItemClickListener {
        void onAdapterItemClick(Meteorite item);
    }

    public MeteoritesAdapter(MeteoritesAdapter.OnAdapterItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MeteoritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meteorite_layout, parent, false);
        MeteoritesAdapter.ViewHolder vh = new MeteoritesAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final Meteorite meteorite = getItem(position);

        final MeteoritesAdapter.ViewHolder holder = (MeteoritesAdapter.ViewHolder) viewHolder;
        holder.itemName.setText(meteorite.getName());
        holder.itemDate.setText(formatDate(meteorite));
        holder.itemMass.setText(meteorite.getMass() + " g");
        holder.itemType.setText(meteorite.getType());
        holder.bind(meteorite, listener);
    }

    @NonNull
    private String formatDate(Meteorite meteorite) {
        return meteorite.getTimestamp().substring(0,10);
    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public TextView itemDate;
        public TextView itemMass;
        public TextView itemType;

        public ViewHolder(CardView v) {
            super(v);
            itemName = (TextView) v.findViewById(R.id.meteorite_name);
            itemDate = (TextView) v.findViewById(R.id.meteorite_date);
            itemMass = (TextView) v.findViewById(R.id.meteorite_mass);
            itemType = (TextView) v.findViewById(R.id.meteorite_type);
        }

        public void bind(final Meteorite item, final MeteoritesAdapter.OnAdapterItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAdapterItemClick(item);
                }
            });
        }
    }
}
