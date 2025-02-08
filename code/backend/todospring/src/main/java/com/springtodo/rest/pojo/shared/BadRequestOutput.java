package com.springtodo.rest.pojo.shared;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestOutput {

    ArrayList<String> messages;
}
