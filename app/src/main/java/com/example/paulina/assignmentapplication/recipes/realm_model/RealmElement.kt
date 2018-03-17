package com.example.paulina.assignmentapplication.recipes.realm_model

import io.realm.RealmObject

/**
 * Created by Paulina on 2018-03-09.
 */
open class RealmElement : RealmObject() {
    var id: Long = 0L

    var name: String = ""


}