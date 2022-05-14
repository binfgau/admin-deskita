package com.example.admin_deskita.entity

import java.io.Serializable

class Discount(
    val createAt: String,
    val _id: String,
    val name: String,
    val categoryProduct: String,
    val validDate: String,
    val quantity: Int,
    val value: Int,
    val userId: String,
    val used: String
): Serializable

class ListDiscountContainer(val discounts:ArrayList<Discount>)