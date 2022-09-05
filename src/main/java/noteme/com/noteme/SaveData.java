package noteme.com.noteme;

public class SaveData  {

    private SaveData () {

    }

    String txtName , txtEmail, txtPassword;

    public String getTxtName() {
        return txtName;
    }

    public String getTxtEmail() {
        return txtEmail;
    }

    public String getTxtPassword() {
        return txtPassword;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public void setTxtEmail(String txtEmail) {
        this.txtEmail = txtEmail;
    }

    public void setTxtPassword(String txtPassword) {
        this.txtPassword = txtPassword;
    }

    public SaveData(String txtName, String txtEmail, String txtPassword) {
        this.txtName = txtName;
        this.txtEmail = txtEmail;
        this.txtPassword = txtPassword;
    }
}
