package com.depa.form.model.field;

import java.util.ArrayList;
import java.util.List;

public class Checkbox extends Field {

    private List<ChoiceList> choiceList ;

    public Checkbox(FieldType fieldType) {
        super(fieldType);
    }

    public static Checkbox create(FieldData fieldData, List<Attribute> attributes, List<ChoiceList> choiceList) {
        Checkbox field = new Checkbox(FieldType.CHECKBOX);
        field.setFieldData(fieldData);
        field.setAttributes(attributes);
        field.setChoiceList(choiceList);
        return field;
    }

//    public List<String> getChoiceList() {
//        return choiceList;
//    }
//
//    public void setChoiceList(List<String> choiceList) {
//        this.choiceList = choiceList;
//    }

    public List<ChoiceList> getChoiceList() {
        return choiceList;
    }

    public void setChoiceList(List<ChoiceList> choiceList) {
        this.choiceList = choiceList;
    }

    
}
