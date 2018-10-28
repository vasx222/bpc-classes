package com.bpcbt.lessons.spring.manager;

import com.bpcbt.lessons.spring.SimpleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;

@Component
@Scope("request")
public class CreateCustomerManager {
    private String customerName;
    private Integer accountNumber;
    private String currency;
    private Integer amount;
    private List<String> currencies;

    private SimpleController controller;

    @Autowired
    public CreateCustomerManager(SimpleController controller) {
        this.controller = controller;
        this.customerName = "";
        this.accountNumber = 0;
        this.currency = "RUB";
        this.amount = 0;
        this.currencies = controller.getCurrencies();
    }

    private boolean validate() {
        boolean validate = true;

        if (controller.customerWithNameExists(customerName)) {
            validate = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Validation error", "Customer with such name already exists"));
        }
        if (controller.accountWithAccountNumberExists(accountNumber)) {
            validate = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Validation error", "Account with such number already exists"));
        }

        return validate;
    }

    public String createCustomer() {
        if (validate()) {
            controller.insertCustomerWithAccount(customerName, accountNumber, currency, amount);
            return "list";
        }
        return null;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<String> currencies) {
        this.currencies = currencies;
    }
}
