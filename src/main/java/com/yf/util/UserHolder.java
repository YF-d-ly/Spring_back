//package com.yf.util;
//
//
//
////这个类UserHolder使用了ThreadLocal来存储当前线程的用户信息（UserDTO）。
//// 它的作用是在同一个线程内共享用户数据，
////通常用于在一次请求的处理过程中，多个方法或组件之间传递用户信息
//// ，而无需显式地通过参数传递
//
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserHolder {
//    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();
//
//    public void saveUser(UserDTO user){
//        tl.set(user);
//    }
//
//    public static UserDTO getUser(){
//        return tl.get();
//    }
//
//    public void removeUser(){
//        tl.remove();
//    }
//
//
//
//}