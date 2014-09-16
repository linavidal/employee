/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.controller;

import com.example.jpa.entities.Employee;
import com.example.jpa.sessions.EmployeeFacade;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ADSI TARDE
 */
@ManagedBean
@SessionScoped
public class EmployeeController {

    private Employee empleadoActual;
    private List<Employee> listaEmpleados = null;
    @EJB
    private EmployeeFacade employeFacade;

    /**
     * Creates a new instance of EmployeeController
     */
    public EmployeeController() {
        
    }

    public Employee getEmpleadoActual() {
        if (empleadoActual == null) {
            empleadoActual = new Employee();
        }
        return empleadoActual;
    }

    public void setEmpleadoActual(Employee empleadoActual) {
        this.empleadoActual = empleadoActual;
    }

    public EmployeeFacade getEmployeeFacade() {
        return employeFacade;
    }

    public List<Employee> getListaEmpleados() {


        if (listaEmpleados == null) {
            try {
                listaEmpleados = getEmployeeFacade().findAll();
            } catch (Exception e) {
                System.out.println("Error closing resource " + e.getClass().getName());
                System.out.println("Message: " + e.getMessage());
            }
        }
        return listaEmpleados;
    }

    private void recargarLista() {
        listaEmpleados = null;
    }

    public String prepareList() {
        recargarLista();
        return "/employee/List";
    }

    public String prepareEdit() {
        return "Edit";
    }

    public String prepareView() {
        return "View";
    }

    public String addEmployee() {
        try {
            getEmployeeFacade().create(empleadoActual);
           addSuccessMessage("crear empleado","empleado creado Exitosamente");
           recargarLista();
            return "View";
        
        } catch (Exception e) {
            addErrorMessage("Error closing resource " + e.getClass().getName(), "Message: " + e.getMessage());
           
            return null;
        }
    }

    public String updateEmployee() {
        try {
            getEmployeeFacade().edit(empleadoActual);
            addSuccessMessage("Actualizar empleado","empleado actualizado Exitosamente");
            return "View";
        }catch (Exception e) {
            addErrorMessage("Error closing resource " + e.getClass().getName(), "Message: " + e.getMessage());
           
            return null;
        }
    }
    public String deleteEmployee() {
        try {
            getEmployeeFacade().remove(empleadoActual);
            addSuccessMessage("Eliminar empleado","empleado eliminado Exitosamente");
            recargarLista();
        } catch (Exception e) {
            addErrorMessage("Error closing resource " + e.getClass().getName(), "Message: " + e.getMessage());
        }
        return "List";
    }
    public String prepareCreate() {
        empleadoActual = new Employee();
        return "Create";
    }

    private void addErrorMessage(String title, String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, title, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    private void addSuccessMessage(String title, String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, title, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }
}
