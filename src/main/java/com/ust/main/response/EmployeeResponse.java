package com.ust.main.response;

import com.ust.main.model.Employee;

import java.util.List;

public class EmployeeResponse {


   private Employee employeeResponse;

   private List<Employee> employeeResponseList;

   private String responseMessage;

//   private HttpStatus httpStatus;
//
//
//   public HttpStatus getHttpStatus() {
//      return httpStatus;
//   }
//
//   public void setHttpStatus(HttpStatus httpStatus) {
//      this.httpStatus = httpStatus;
//   }

   public Employee getEmployeeResponse() {
      return employeeResponse;
   }

   public void setEmployeeResponse(Employee employeeResponse) {
      this.employeeResponse = employeeResponse;
   }

   public String getResponseMessage() {
      return responseMessage;
   }

   public void setResponseMessage(String responseMessage) {
      this.responseMessage = responseMessage;
   }

   public List<Employee> getEmployeeResponseList() {
      return employeeResponseList;
   }

   public void setEmployeeResponseList(List<Employee> employeeResponseList) {
      this.employeeResponseList = employeeResponseList;
   }
}
