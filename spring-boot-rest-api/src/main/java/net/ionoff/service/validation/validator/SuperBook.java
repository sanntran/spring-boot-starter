package net.ionoff.service.validation.validator;


public class SuperBook implements TypeChecker {

    @Override
    public String getType() {
        return "SuperAuthor";
    }

    @Override
    public boolean checkType(Object author) {
        return false;
    }
}
