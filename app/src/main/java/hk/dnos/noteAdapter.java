package hk.dnos;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class noteAdapter extends RecyclerView.Adapter<noteAdapter.ViewHolder> {


    public List<noteclass> notelist;

    public noteAdapter(List<noteclass> notelist){

        this.notelist=notelist;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.noteitem,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.name.setText(notelist.get(position).getMenuName());
        holder.price.setText(notelist.get(position).getTotal());
        holder.quantity.setText(notelist.get(position).getMenuQuantity());
        holder.total.setText(notelist.get(position).getMenuPrice());

    }

    @Override
    public int getItemCount() {
        return notelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //View mv;
        public TextView name;
        public TextView price;
        public TextView quantity;
        public TextView total;

        public ViewHolder(View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.name);
            price=(TextView)itemView.findViewById(R.id.price);
            quantity=(TextView)itemView.findViewById(R.id.quantity);
            total=(TextView)itemView.findViewById(R.id.total);

        }
    }

}
