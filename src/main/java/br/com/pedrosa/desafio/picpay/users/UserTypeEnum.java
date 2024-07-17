package br.com.pedrosa.desafio.picpay.users;

import br.com.pedrosa.desafio.picpay.exception.UserTypeException;

import java.util.Arrays;

public enum UserTypeEnum {
    COMMON(1, "COMMON"),
    SELLER(2, "SELLER");

    private final int value;
    private final String name;

    UserTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static String findById(int id){
        return Arrays.stream(UserTypeEnum.values())
                .filter(type -> type.getValue() == id)
                .findFirst().map(UserTypeEnum::getName)
                .orElseThrow(() -> new UserTypeException("Tipo de usuario informado invalido, deve ser COMMON OU SELLER"));
    }
}
