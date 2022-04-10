package hk.dnos;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class cartMenuAdapter extends RecyclerView.Adapter<cartMenuAdapter.ViewHolder> {



    List<cartMenuModel> model;

    public static int Totalpraice=0;

    FirebaseFirestore fs;
    FirebaseAuth f_auth;
    public cartMenuAdapter(List<cartMenuModel> model) {
        this.model = model;

        fs=FirebaseFirestore.getInstance();
        f_auth=FirebaseAuth.getInstance() ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        // Totalpraice+=Integer.parseInt(model.get(position).getMenuPrice());

        holder.menuname.setText(model.get(position).getMenuName());
        holder.menuPrice.setText(model.get(position).getMenuPrice());
        holder.menuQuantity.setText(model.get(position).getMenuQuantity());


        //holder.delete.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //
        //    }
        //});
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView menuPrice,menuname,menuQuantity;
        Button delete;


        public ViewHolder(View itemView) {
            super(itemView);

            menuPrice=itemView.findViewById(R.id.OrderPrice);
            menuname=itemView.findViewById(R.id.OrderName);
            menuQuantity=itemView.findViewById(R.id.OrderQuantity);
            //delete=itemView.findViewById(R.id.OrderDelete);
        }
    }
}

