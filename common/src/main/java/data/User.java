package data;

import java.io.Serializable;

public class User implements Serializable {

    private String username;
    private String password;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }


    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        User user = (User) o;
        if (this.hashCode() != user.hashCode()) return false;
        return (this.getUsername().equals(user.getUsername()) && this.getPassword().equals(user.getPassword()));
    }

    @Override
    public int hashCode(){
        int ans = 0;
        for (int i = 0; i < this.getPassword().length(); i++){
            ans = (ans + (int)this.getPassword().charAt(i)) % 2000000000;
        }
        ans = (ans + this.getUsername().length()) % 2000000000;
        return ans;
    }

    @Override
    public String toString(){
        String res = "Логин: " + this.getUsername() + "\n"
                + "Пароль: " + this.getPassword() + "\n";
        return res;
    }
}
