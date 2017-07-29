package cz.pribula.meteorites.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.pribula.meteorites.R;
import cz.pribula.meteorites.db.Meteorite;
import cz.pribula.meteorites.db.RealmController;
import io.realm.Realm;

public class MeteoritesAdapter extends RealmAdapter<Meteorite> {

    MeteoritesAdapter.OnAdapterItemClickListener listener;
    final Context context;
    private Realm realm;

    public interface OnAdapterItemClickListener {
        void onAdapterItemClick(Meteorite item);
    }

    public MeteoritesAdapter(Context context, MeteoritesAdapter.OnAdapterItemClickListener listener) {
        this.listener = listener;
        this.context = context;
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
        realm = RealmController.getInstance().getRealm();
        final Meteorite meteorite = getItem(position);

        final MeteoritesAdapter.ViewHolder holder = (MeteoritesAdapter.ViewHolder) viewHolder;
        holder.itemName.setText(position + 1 + "." + meteorite.getName());
        holder.itemDate.setText(formatDate(meteorite));
        holder.itemMass.setText(meteorite.getMass() + " g");
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

        public ViewHolder(CardView v) {
            super(v);
            itemName = (TextView) v.findViewById(R.id.meteorite_name);
            itemDate = (TextView) v.findViewById(R.id.meteorite_date);
            itemMass = (TextView) v.findViewById(R.id.meteorite_mass);
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
