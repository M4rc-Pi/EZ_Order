package cat.inspedralbes.projecte2dam.ezorderv2.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import cat.inspedralbes.projecte2dam.ezorderv2.R;


public class HomeFragment extends Fragment {

    ImageButton imgBtnQRScanner;
    View view;
    Activity mainActivity;
    private View.OnClickListener listener;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_clients, container, false);
        mainActivity = getActivity();
        imgBtnQRScanner = view.findViewById(R.id.imgbutton_home_clients_qrscanner);
        imgBtnQRScanner.setOnClickListener(listener);
        return view;
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
    public View.OnClickListener getOnClickListener(){return listener;}

//    public void onClick(View v) {
//        IntentIntegrator intentIntegrator = new IntentIntegrator(mainActivity);
//        intentIntegrator.setPrompt("QR Scanner");
//        intentIntegrator.setBeepEnabled(true);
//        intentIntegrator.setOrientationLocked(true);
//        intentIntegrator.setCaptureActivity(QRScanner.class);
//        intentIntegrator.initiateScan();
//    }

}