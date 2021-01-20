package net.ionoff.service.error.validator;


import net.ionoff.common.validation.constraints.TypeChecker;

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
