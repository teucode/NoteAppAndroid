package noteme.com.noteme;

import android.support.v7.app.AppCompatActivity;

public class Note extends AppCompatActivity{
    private String judul;
    private String isi;

    public Note(String judul, String isi) {
        this.judul = judul;
        this.isi = isi;
    }

    public String getJudul() {
        return judul;
    }

    public String getIsi() {
        return isi;
    }
}
