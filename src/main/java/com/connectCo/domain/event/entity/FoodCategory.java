package com.connectCo.domain.event.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FoodCategory {
     족발_보쌈("족발/보쌈"),
     야식("야식"),
     떡볶이("떡볶이"),
     치킨("치킨");

     private final String message;
}
