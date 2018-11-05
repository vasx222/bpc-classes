package com.bpcbt.lessons.spring.manager;

import com.bpcbt.lessons.spring.exception.MoneyTransferException;
import com.bpcbt.lessons.spring.model.Account;
import com.bpcbt.lessons.spring.model.Customer;
import com.bpcbt.lessons.spring.repository.MainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Scope("request")
public class TransferMoneyManager {
    private Customer sender;
    private Customer recipient;
    private Account senderAccount;
    private Account recipientAccount;

    private String currency;
    private Integer amount;

    private MainRepository mainRepository;

    private Map<String, Customer> customers;
    private Map<Integer, Account> accounts;
    private List<String> currencies;

    private String senderName;
    private String recipientName;

    @Autowired
    public TransferMoneyManager(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
        customers = mainRepository.getCustomers().stream()
                .collect(Collectors.toMap(Customer::getName, Function.identity()));
        accounts = mainRepository.getAccounts().stream()
                .collect(Collectors.toMap(Account::getAccountNumber, Function.identity()));
        this.currencies = mainRepository.getCurrencies();

        sender = customers.values().iterator().next();
        recipient = customers.values().iterator().next();
        senderName = sender.getName();
        recipientName = recipient.getName();
        senderAccount = accounts.get(sender.getAccount().getAccountNumber());
        recipientAccount = accounts.get(recipient.getAccount().getAccountNumber());
    }

    public String transferMoney() {
        boolean success = true;

        if (senderName.equals(recipientName)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Transfer error", "Sender and recipient names shouldn't be equal"));
            success = false;
        }
        try {
            mainRepository.transfer(senderName, recipientName, amount, currency);
        } catch (MoneyTransferException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Transfer error", "Sender doesn't have enough money"));
            success = false;

        }

        if (!success) {
            return null;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Success", senderName + " sent " + amount + " " + currency + " to " + recipientName));
        return "list";
    }

    public List<String> getCustomersNames() {
        return new ArrayList<>(customers.keySet());
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
        sender = customers.get(senderName);
        senderAccount = accounts.get(sender.getAccount().getAccountNumber());
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
        recipient = customers.get(recipientName);
        recipientAccount = accounts.get(recipient.getAccount().getAccountNumber());
    }

    public Customer getSender() {
        return sender;
    }

    public void setSender(Customer sender) {
        this.sender = sender;
    }

    public Customer getRecipient() {
        return recipient;
    }

    public void setRecipient(Customer recipient) {
        this.recipient = recipient;
    }

    public Account getSenderAccount() {
        return senderAccount;
    }

    public Account getRecipientAccount() {
        return recipientAccount;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getAmount() {
        return amount;
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
