package com.example.kai.contact;


/**
 * Created by kai on 5/25/2015.
 */
public class Contact {

    private String _name, _phone,_email, _address;
    private int _id;
    public Contact (int id, String name, String phone, String email, String address) {
           _name = name;
           _address = address;
           _phone = phone;
           _email = email;
           _id = id;
    }

    public int get_id(){
        return _id;
    }
    public String get_name(){
        return _name;
    }
    public String get_phone(){
        return _phone;
    }
    public String get_email(){
        return _email;
    }
    public String get_address(){
        return _address;
    }
}
