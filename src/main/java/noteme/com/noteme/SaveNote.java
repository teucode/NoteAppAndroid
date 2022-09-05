package noteme.com.noteme;

public class SaveNote {

    private SaveNote() {}

    String txtJudul, txtIsi;

    public String getTxtJudul() {
        return txtJudul;
    }

    public String getTxtIsi() {
        return txtIsi;
    }

    public void setTxtJudul(String txtJudul) {
        this.txtJudul = txtJudul;
    }

    public void setTxtIsi(String txtIsi) {
        this.txtIsi = txtIsi;
    }

    public SaveNote(String txtJudul, String txtIsi) {
        this.txtJudul = txtJudul;
        this.txtIsi = txtIsi;
    }
}
