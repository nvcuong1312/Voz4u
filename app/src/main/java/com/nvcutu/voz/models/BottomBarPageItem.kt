package com.nvcutu.voz.models

class BottomBarPageItem : BaseModel<BottomBarPageItem> {
    var page = 1
    var isSelected = false

    override fun isSame(model: BottomBarPageItem): Boolean {
        return isSelected == model.isSelected
    }
}