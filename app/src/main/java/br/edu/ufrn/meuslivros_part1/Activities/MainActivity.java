package br.edu.ufrn.meuslivros_part1.Activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import br.edu.ufrn.meuslivros_part1.BancoRoom.AppDatabase;
import br.edu.ufrn.meuslivros_part1.Interfaces.LivroDao;
import br.edu.ufrn.meuslivros_part1.R;
import br.edu.ufrn.meuslivros_part1.classes.Livro;

public class MainActivity extends AppCompatActivity {

    AppDatabase db;
    LivroDao livroDao;
    private boolean tf = false;
    private static final int duraction = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getDatabase(this);
        livroDao = db.livroDao();
    }

    public void click1(View v){
        Intent t = new Intent(this, CadastraActivity.class);
        startActivityForResult(t, 1);
    }

    public void click2(View v){
        if(livroDao.listAll().isEmpty()) {
            Log.i("lista", "lista vazia");
            return;
        }

        Intent t = new Intent(this, VisualizaActivity.class);
        startActivity(t);
    }

    public void click3(View v){
        Intent t = new Intent(this, BuscarActivity.class);
        startActivity(t);
    }

    public void click4(View v){
        Intent t = new Intent(this, ListarActivity.class);
        startActivity(t);
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent t){

        if(t == null)
            return;

        String label;
        final Bundle b = t.getExtras();

        if(resultcode == RESULT_OK)
            label = "Livro Cadastrado";
        else
            label = "Operação Cancelada";

        View v = findViewById(R.id.constraintLayout);

        Snackbar snack = Snackbar.make((View) v.getParent(), ""+label, duraction)
                .setAction("Cancelar", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tf = true;
                    }
                });
        snack.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(duraction);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(tf == false) {
                    Livro livro = new Livro(b.getString("autor"), b.getString("titulo"), b.getInt("ano"), b.getDouble("nota"), R.drawable.livro_icon_png_3);
                    livroDao.inserir(livro);
                    Log.i("cadastrou", "Cadastrou");
                }else{
                    Log.i("cancelou", "Cancelou");
                    tf = false;
                }

            }
        }).start();
    }
}
