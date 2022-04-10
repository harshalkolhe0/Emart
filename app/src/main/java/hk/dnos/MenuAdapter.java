package hk.dnos;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {


    List<MenuModel> menu;
    int quantity;


    public static List<Map<String,Object>> orderList=new ArrayList<>();
    public static int Totalpraice=0;
    public static int sum=0;
    public static int flag=0;
    public MenuAdapter(List<MenuModel> menu) {
        this.menu = menu;
    }

    int i;

    public MenuAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.menucard_layout,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,  int position) {


        holder.menuCheck.setText(menu.get(position).getMenuName());
        holder.menuPrice.setText(menu.get(position).getMenuPrice());

        i=position;

        holder.menuCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {


                    holder.menucnt.requestFocus();

                    if(holder.menucnt.getText().toString().isEmpty())
                    {
                        // holder.menucnt.setError("Enter Quantity");
                        holder.menucnt.requestFocus();
                    }
                    else {



                        quantity = Integer.parseInt(String.valueOf(holder.menucnt.getText()));



                        sum += Integer.parseInt(menu.get(holder.getAdapterPosition()).getMenuPrice()) * quantity;

                        Totalpraice=Integer.parseInt(menu.get(holder.getAdapterPosition()).getMenuPrice()) * quantity;
                        flag=1;


                        int price=Integer.parseInt(menu.get(holder.getAdapterPosition()).getMenuPrice()) * quantity;
                        //  holder.totalPrice.setText(Totalpraice);


                        Map<String,Object> order=new HashMap<>();
                        Date currentTime = Calendar.getInstance().getTime();
                        order.put("menuName", menu.get(holder.getAdapterPosition()).getMenuName());
                        order.put("menuPrice", menu.get(holder.getAdapterPosition()).getMenuPrice());
                        order.put("total",Totalpraice);
                        order.put("menuQuantity",String.valueOf(quantity));

                        order.put("date",currentTime);

                        orderList.add(order);

                        // order.put(menu.get(position).getMenuName(),String.valueOf(price));
                    }
                }
                else
                {
                    flag=0;
                    sum-=Integer.parseInt(menu.get(i).getMenuPrice());

                    //    order.remove(menu.get(position).getMenuName());

                    orderList.get(i).clear();

                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView menuPrice;
        EditText menucnt;
        CheckBox menuCheck;


        public ViewHolder(View itemView) {
            super(itemView);

            menuPrice=itemView.findViewById(R.id.MenuPrice);
            menuCheck=itemView.findViewById(R.id.MenuCheck);
            menucnt=itemView.findViewById(R.id.menuCnt);


        }
    }
}

