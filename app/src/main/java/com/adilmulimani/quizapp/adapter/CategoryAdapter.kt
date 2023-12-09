package com.adilmulimani.quizapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.adilmulimani.quizapp.QuizActivity
import com.adilmulimani.quizapp.databinding.CategoryitemBinding
import com.adilmulimani.quizapp.model.CategoryModelClass

class CategoryAdapter(
    private var categoryList: ArrayList<CategoryModelClass>,
    private var requireActivity: FragmentActivity
)
    : RecyclerView.Adapter<CategoryAdapter.MyCategoryViewHolder>()
{
    class MyCategoryViewHolder(var binding:CategoryitemBinding)
        : RecyclerView.ViewHolder(binding.root)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoryViewHolder {
        return MyCategoryViewHolder(CategoryitemBinding.
        inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
            return categoryList.size
    }

    override fun onBindViewHolder(holder: MyCategoryViewHolder, position: Int) {
        var dataList = categoryList[position]
        holder.binding.categoryImage.setImageResource(dataList.categoryImage)
        holder.binding.categoryText.text=dataList.categoryText
        holder.binding.categoryButton.setOnClickListener {
            var i = Intent(requireActivity,QuizActivity::class.java)
            i.putExtra("quizImage",dataList.categoryImage)
            i.putExtra("questionType",dataList.categoryText)
            requireActivity.startActivity(i)
        }
    }
}