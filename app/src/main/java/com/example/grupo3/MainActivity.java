package com.example.grupo3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
//Variable para insertar texto  por microfono
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    //variable para mostrar texto dictado por voz
    private TextView  EntradaVoz;
    //variable para ingresar texto para pasarlo a audio
    private EditText ingresa;
    //variables para botones
    private ImageButton botonHablar, botonLeer;
    //variable para leer datos y pasarlos a voz
    TextToSpeech mTTS;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //instanciar objetos
        EntradaVoz = findViewById(R.id.txtEntrada);
        botonHablar = findViewById(R.id.btHablar);
        ingresa=(EditText) findViewById(R.id.txtIngresa);
        botonLeer=findViewById(R.id.btParlante);

        botonHablar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresa.setText("");
                iniciarEntradaVoz();
            }
        });

        mTTS= new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                Locale spanish = new Locale("es", "ES");
                if(i!=TextToSpeech.ERROR){
                mTTS.setLanguage(spanish);
                }else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //click en boton de parlante
        botonLeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EntradaVoz.setText("");
                //obtener texto del text Edit
                String Tospeack = ingresa.getText().toString().trim();
                if (Tospeack.equals("")){
                    Toast.makeText(MainActivity.this, "Por favor Ingrese el texto", Toast.LENGTH_SHORT).show();
                }else {
                    mTTS.speak(Tospeack, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    private void iniciarEntradaVoz() {
        Intent inten = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        inten.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        inten.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        inten.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hola, Dime lo que sea");
        try {
            startActivityForResult(inten, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException er) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    EntradaVoz.setText(result.get(0));
                }
                break;
            }
        }
    }




}