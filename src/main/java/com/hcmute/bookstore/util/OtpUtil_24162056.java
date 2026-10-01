package com.hcmute.bookstore.util; import java.security.SecureRandom;
public final class OtpUtil_24162056 { private static final SecureRandom R=new SecureRandom(); private OtpUtil_24162056(){} public static String create(){return String.format("%06d",R.nextInt(1_000_000));} }
