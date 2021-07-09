package cat.inspedralbes.projecte2dam.ezorderv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

import cat.inspedralbes.projecte2dam.ezorderv2.model.Producte;

/**
 * Carrega les imatges des-de la memoria del telefon (o també desde Google drive)
 */
public class CarregarImatgeActivity extends Activity {

    private static final int SELECT_PICTURE = 1;

    private ImageView imageView;
    byte[] bytesFoto;
    Producte producte;
    Intent intentToFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carregar_imatge);

        imageView = findViewById(R.id.imageview_carregarimg_activity);
        producte = (Producte) getIntent().getSerializableExtra("producte");

        Log.d("TAG", "onCreate: " + producte.getId_categoria() + "/" +producte.getNom() + "/" + producte.getDescripcio() + "/" + producte.getPreu());

        findViewById(R.id.button_carregarimatge_activity_buscar)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        // in onCreate or any event where your want the user to
                        // select a file
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,
                                "Select Picture"), SELECT_PICTURE);
                    }
                });

//        findViewById(R.id.button_carregarimatge_carregar)
//                .setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(bytesFoto != null) {
////                            producte = new Producte();
////                            producte.setFoto(bytesFoto);
//
//                        }else {
//                            Log.d("CarregarImatgeActivity", "onClick:  NO ES VA SELECCIONAR");
//                        }
//                    }
//                });
    }

    public Producte getProducte() {
        return producte;
    }

    public void setProducte(Producte producte) {
        this.producte = producte;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap imgBitmap = null;
        byte[] imgBytes = null;
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    imgBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    imgBytes = stream.toByteArray();
//                    imgBitmap.recycle();

                    //pujarla a firebase Storage
                    Random rand = new Random();
                    FirebaseStorage storage = FirebaseStorage.getInstance("gs://ez-order-projecte.appspot.com");
                    StorageReference refStorage = storage.getReference();
                    StorageReference refImatge = refStorage.child("img/" + producte.getNom().trim().toLowerCase());
                    UploadTask uploadTask = refImatge.putBytes(imgBytes);
                    uploadTask
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "NO S'HA PUJAT LA FOTO A FIREBASE", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getApplicationContext(), "FOTO PUJADA A FIREBASE CORRECTAMENT", Toast.LENGTH_SHORT).show();
                                    Log.d("***FINAL URL IMATGE", "onSuccess: " + producte.getNom().trim().toLowerCase());
                                    producte.setFoto("gs://ez-order-projecte.appspot.com/img" + producte.getNom().trim());
                                    getIntent().putExtra("producteAmbFoto", (Serializable) producte);
                                    finish();
                                    //guardar la url a sharedPreferences per afegirla a la bd.
                                    SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    String urlFoto = "gs://ez-order-projecte.appspot.com/img/"+ producte.getNom().replaceAll(" ", "").toLowerCase();
                                    editor.putString("foto", urlFoto);
                                    editor.commit();
                                }
                            });



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}