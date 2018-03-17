package com.example.paulina.assignmentapplication.recipes.realm_model

import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by Paulina on 2018-03-09.
 */
open class RealmIngredient : RealmObject() {
    var id: Long = 0L

    var name: String = ""
    var elements: RealmList<RealmElement> = RealmList<RealmElement>()

}