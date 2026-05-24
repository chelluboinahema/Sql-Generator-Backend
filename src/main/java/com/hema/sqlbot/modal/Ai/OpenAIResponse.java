package com.hema.sqlbot.modal.Ai;


import java.util.List;


public class OpenAIResponse {

    private List<Choice> choices;
    public OpenAIResponse() {
    }
    public OpenAIResponse(List<Choice> choices) {
        this.choices = choices;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}