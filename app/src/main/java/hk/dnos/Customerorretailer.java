package hk.dnos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Customerorretailer extends AppCompatActivity {


    Button signin1,signup1,signin2,signup2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerorretailer);

        signin1=findViewById(R.id.signin1);
        signin2=findViewById(R.id.signin2);
        signup1=findViewById(R.id.signup1);
        signup2=findViewById(R.id.signup2);

        signin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent splashInt1=new Intent(Customerorretailer.this,login.class);
                startActivity(splashInt1);
            }
        });

        signin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent splashInt2=new Intent(Customerorretailer.this,loginretailer.class);
                startActivity(splashInt2);
            }
        });

        signup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent splashInt3=new Intent(Customerorretailer.this,register.class);
                startActivity(splashInt3);
            }
        });

        signup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent splashInt4=new Intent(Customerorretailer.this,registerretailer.class);
                startActivity(splashInt4);
            }
        });
    }
}
