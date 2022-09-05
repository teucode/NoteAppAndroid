package noteme.com.noteme;

public class NoteModel {

    String Judul, Isi;

    public NoteModel(String judul, String isi) {
        Judul = judul;
        Isi = isi;
    }

    public NoteModel() {
    }

    public String getJudul() {
        return Judul;
    }

    public String getIsi() {
        return Isi;
    }

    public void setJudul(String judul) {
        Judul = judul;
    }

    public void setIsi(String isi) {
        Isi = isi;
    }

}
