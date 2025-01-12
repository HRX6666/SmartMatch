package com.example.smartmatch.ui.construction.collectionscene

import com.example.smartmatch.base.activity.BaseAdapter
import com.example.smartmatch.databinding.ItemCollectionScene1Binding
import com.example.smartmatch.logic.model.ScenariosData

/**
 * @className: CollectionSceneAdapter1
 * @author: Voyager
 * @description: 集合场景的第一个Adapter（命名实在不想想了）
 * @date:  2023/10/6 22:41
 * @version 1.0
 **/
class CollectionSceneAdapter1(private val fragment:CollectionSceneFragment) :
    BaseAdapter<ScenariosData, ItemCollectionScene1Binding>() {

    override fun ItemCollectionScene1Binding.onBindViewHolder(bean: ScenariosData, position: Int) {
        tvName.text=bean.name
    }



}