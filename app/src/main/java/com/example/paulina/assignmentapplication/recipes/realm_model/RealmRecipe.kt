package com.example.paulina.assignmentapplication.recipes.realm_model

import io.realm.RealmList
import io.realm.RealmObject



/**
 * Created by Paulina on 2018-03-09.
 */
open class RealmRecipe : RealmObject() {
    var id: Long = 0L

    var title: String = ""
    var description: String = ""

    var images: RealmList<RealmImageInfo> = RealmList<RealmImageInfo>()
    var ingredients: RealmList<RealmIngredient> = RealmList<RealmIngredient>()

}