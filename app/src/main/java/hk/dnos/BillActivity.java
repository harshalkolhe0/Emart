package hk.dnos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class BillActivity extends AppCompatActivity {

    private TextView amount;
    private Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        amount=findViewById(R.id.amount);
        pay =findViewById(R.id.pay);

        Bundle data=getIntent().getExtras();

        Toast.makeText(BillActivity.this,"Total price: "+ Objects.requireNonNull(data).getString("total"),Toast.LENGTH_LONG).show();

        amount.setText(Objects.requireNonNull(data).getString("total"));

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(BillActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
