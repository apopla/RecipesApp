package com.example.paulina.assignmentapplication.recipes.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.paulina.assignmentapplication.R
import com.example.paulina.assignmentapplication.recipes.model.Recipe
import kotlinx.android.synthetic.main.item_recipe_list_layout.view.*
import javax.inject.Inject
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.paulina.assignmentapplication.recipes.realm_model.RealmRecipe
import io.realm.Realm
import javax.sql.DataSource


/**
 * Created by Paulina on 2018-03-08.
 */
class RecipesListAdapter @Inject constructor(val context: Context) : RecyclerView.Adapter<RecipesListAdapter.RecipesViewHolder>() {

    private var recipesList: MutableList<RealmRecipe> = mutableListOf()

    fun setRecipesList(recipesList: List<RealmRecipe>) {
        if(!this.recipesList.isEmpty()) this.recipesList.clear()
        this.recipesList.addAll(recipesList)
        notifyDataSetChanged()
    }

    fun updateRecipes(recipesList: List<RealmRecipe>) {
        val realm = Realm.getDefaultInstance()

        this.recipesList = realm.copyFromRealm(recipesList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecipesViewHolder {
        return  RecipesViewHolder(LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_recipe_list_layout, parent, false))
    }

    override fun getItemCount(): Int = recipesList.size

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        val item = recipesList[position]
        item.let { holder.bind(item) }
    }

    inner class RecipesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: RealmRecipe) {
            itemView.title.text = item.title
            itemView.description.text = Html.fromHtml(item.description)

            if(!item.ingredients!!.isEmpty() && item.ingredients!=null){
                var ingrText = ""
                for (ingredient in item.ingredients!!) {
                    ingrText += ingredient.name + " "
                }
                if(ingrText.trim().isEmpty()){
                    itemView.ingredients.visibility = View.GONE
                }
                else{
                    itemView.ingredients.text = ingrText
                }

            }

            if(!item.images!!.isEmpty() && item.images!!.first()!!.url != "") {
                Glide.with(context).load(item.images!!.first()!!.url).into(itemView.image)
                Glide.with(context)
                        .load(item.images!!.first()!!.url)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }
                            override fun onResourceReady(p0: Drawable?, p1: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: com.bumptech.glide.load.DataSource?, p4: Boolean): Boolean {
                                return false
                            }
                        })
                        .into(itemView.image)
                itemView.image.visibility = View.VISIBLE
            }

        }
    }


}