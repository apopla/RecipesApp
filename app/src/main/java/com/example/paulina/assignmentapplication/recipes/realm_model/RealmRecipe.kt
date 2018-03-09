package com.example.paulina.assignmentapplication.recipes.realm_model

import io.realm.RealmList
import io.realm.RealmObject



/**
 * Created by Paulina on 2018-03-09.
 */
open class RealmRecipe : RealmObject() {
    var id: Long? = null

    var title: String? = null
    var description: String? = null

    var images: RealmList<RealmImageInfo>? = null
    var ingredients: RealmList<RealmIngredient>? = null

}