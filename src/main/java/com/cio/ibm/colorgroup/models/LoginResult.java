package com.cio.ibm.colorgroup.models;

public class LoginResult {


   private String jwt;

   public LoginResult(String jwt) {
      this.jwt = jwt;
   }

   public String getJwt() {
      return jwt;
   }

   public void setJwt(String jwt) {
      this.jwt = jwt;
   }

}