package Classes;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Parrish on 7/25/2015.
 */
public class User implements Serializable {
    private Integer id;
    private String email;
    private String password;
    private String question1;
    private String answer1;
    private String question2;
    private String answer2;
    private String question3;
    private String answer3;
    private String active;
    private String recent;
    private String createDate;
    private String loginDate;

    //getters
    public Integer getId(){
        return this.id;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public String getQuestion1(){
        return this.question1;
    }

    public String getQuestion2(){
        return this.question2;
    }

    public String getQuestion3(){
        return this.question3;
    }

    public String getAnswer1(){
        return this.answer1;
    }

    public String getAnswer2(){
        return this.answer2;
    }

    public String getAnswer3(){
        return this.answer3;
    }

    public String getActive(){
        return this.active;
    }

    public String getRecent(){
        return this.recent;
    }

    public String getCreateDate(){
        return this.createDate;
    }

    public String getLoginDate(){
        return this.loginDate;
    }

    //setters
    public void setID(Integer id){this.id = id;}

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String pass){
        this.password = pass;
    }

    public void setQuestion1(String q1){
        this.question1 = q1;
    }

    public void setQuestion2(String q2){
        this.question2 = q2;
    }

    public void setQuestion3(String q3){
        this.question3 = q3;
    }

    public void setAnswer1(String answer){
        this.answer1 = answer;
    }

    public void setAnswer2(String answer){
        this.answer2 = answer;
    }

    public void setAnswer3(String answer){
        this.answer3 = answer;
    }

    public void setActive(String act){
        this.active = act;
    }

    public void setRecent(String recent){
        this.recent = recent;
    }

    public void setCreateDate(String date){
        this.createDate = date;
    }

    public void setLoginDate(String date){
        this.loginDate = date;
    }

    //end properties
}
