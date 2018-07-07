package com.blockchain.him.landchainapp;

import android.Manifest;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    Web3j  web3j;
    Credentials credentials;
    LandRegistry landRegistry;
    String walletAddress;
    Task task;
    BigInteger bigInteger;

    TextView textView7,textView8,owner,textView10,exowner,textView12,district,textView14,taluk,textView16,village,
            textView18,pattaNumber;
    EditText id_edit_text;
    Button query_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {

                    } else {
                        Toast.makeText(this, "Permissions needed", Toast.LENGTH_SHORT).show();
                    }
                });

        query_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bigInteger=new BigInteger(id_edit_text.getText().toString());
                task = new Task();
                task.execute();
            }
        });
    }

    public void init(){
        textView7 =findViewById(R.id.textView7);
        textView8=findViewById(R.id.textView8);
        owner=findViewById(R.id.owner);
        textView10=findViewById(R.id.textView10);
        exowner=findViewById(R.id.exowner);
        textView12=findViewById(R.id.textView12);
        district=findViewById(R.id.district);
        textView14=findViewById(R.id.textView14);
        taluk=findViewById(R.id.taluk);
        textView16=findViewById(R.id.textView16);
        village=findViewById(R.id.village);
        textView18=findViewById(R.id.textView18);
        pattaNumber=findViewById(R.id.pattaNumber);
        id_edit_text=findViewById(R.id.id_edit_text);
        query_button=findViewById(R.id.query_button);

        textView7.setVisibility(View.INVISIBLE);
        textView8.setVisibility(View.INVISIBLE);
        textView10.setVisibility(View.INVISIBLE);
        textView12.setVisibility(View.INVISIBLE);
        textView14.setVisibility(View.INVISIBLE);
        textView16.setVisibility(View.INVISIBLE);
        textView18.setVisibility(View.INVISIBLE);
    }

    public class Task extends AsyncTask<Void,Void,Void>{

        @Override

        protected Void doInBackground(Void... voids) {
            web3j= Web3jFactory.build(new HttpService("https://rinkeby.infura.io/uv2A4DBLeYIY3ZnFmH3C"));
            try {
                Log.d("web3j", "Version" + web3j.web3ClientVersion().send().getWeb3ClientVersion());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                credentials = WalletUtils.loadCredentials("happynewyear",
                        "/sdcard/Download/UTC--2018-07-06T19-31-33.617987500Z--518a9531272441bd625c8d7db813d4f1751d81ba.json");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CipherException e) {
                e.printStackTrace();
            }

            walletAddress = credentials.getAddress();
            Log.d("web3j", "walletAddress: "+walletAddress);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            BigInteger id = new BigInteger("1");
            loadContract(bigInteger);

            textView7.setVisibility(View.VISIBLE);
            textView8.setVisibility(View.VISIBLE);
            textView10.setVisibility(View.VISIBLE);
            textView12.setVisibility(View.VISIBLE);
            textView14.setVisibility(View.VISIBLE);
            textView16.setVisibility(View.VISIBLE);
            textView18.setVisibility(View.VISIBLE);
        }
    }

    public void loadContract(BigInteger id){
        landRegistry = LandRegistry.load("0x66f2097e3b1e2f2fdeb2cd834a276f55e64a870e",web3j,credentials, Contract.GAS_PRICE,
                Contract.GAS_LIMIT);
        try {
            Log.d("web3j", "loadContract: "+landRegistry.getLandData(id).sendAsync().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            owner.setText(landRegistry.getLandData(id).sendAsync().get().getValue1());
            exowner.setText(landRegistry.getLandData(id).sendAsync().get().getValue2());
            district.setText(landRegistry.getLandData(id).sendAsync().get().getValue3());
            taluk.setText(landRegistry.getLandData(id).sendAsync().get().getValue4());
            village.setText(landRegistry.getLandData(id).sendAsync().get().getValue5());
            pattaNumber.setText(landRegistry.getLandData(id).sendAsync().get().getValue6().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
