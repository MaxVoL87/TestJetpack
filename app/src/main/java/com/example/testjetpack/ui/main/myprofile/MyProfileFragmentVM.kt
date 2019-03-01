package com.example.testjetpack.ui.main.myprofile

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.ObservableBoolean
import com.example.testjetpack.MainApplication
import com.example.testjetpack.dataflow.repository.IDataRepository
import com.example.testjetpack.models.Profile
import com.example.testjetpack.ui.base.BaseViewModel
import javax.inject.Inject
import androidx.databinding.ObservableField
import com.squareup.picasso.Picasso
import androidx.databinding.BindingAdapter
import com.example.testjetpack.utils.UiUtils.hideKeyboard
import com.example.testjetpack.utils.UiUtils.showKeyBoard
import com.example.testjetpack.utils.picasso.CircleTransform


class MyProfileFragmentVM : BaseViewModel() {

    @Inject
    lateinit var picasso: Picasso

    @Inject
    lateinit var repository: IDataRepository

    init {
        MainApplication.component.inject(this)
    }

    val photoUri: ObservableField<String?> = ObservableField()
    val name: ObservableField<String?> = ObservableField()
    val dateOfBirth: ObservableField<String?> = ObservableField()
    val driverLicense: ObservableField<String?> = ObservableField()
    val stateOfIssue: ObservableField<String?> = ObservableField()

    val emailOnEdit: ObservableBoolean = ObservableBoolean(false)
    val phoneOnEdit: ObservableBoolean = ObservableBoolean(false)


    private var profile: Profile? = null
        set(value) {
            field = value

            photoUri.set(value?.photoUri)
            name.set("${if (value?.name == null) "" else value.name}${if (value?.surname == null) "" else " ${value.surname}"}")
            dateOfBirth.set(value?.dateOfBirth)
            driverLicense.set(value?.driverLicense)
            stateOfIssue.set(value?.stateOfIssue)
        }

    fun getProfile() {
        processAsyncCall(
            call = { repository.getProfileAsync() },
            onSuccess = { profile ->
                this.profile = profile
            },
            onError = {
                this.profile = null
                onError(it)
            },
            showProgress = true
        )
    }


    fun onClickedEditEmail(view: View){
        val curEditing = emailOnEdit.get()
        emailOnEdit.set(!curEditing)
        onClickedEdit(view, curEditing)
    }

    fun onClickedPhoneEdit(view: View){
        val curEditing = phoneOnEdit.get()
        phoneOnEdit.set(!curEditing)
        onClickedEdit(view, curEditing)
    }

    fun onClickedAddCard(view: View){

    }


    private fun onClickedEdit(view: View, curEditing: Boolean){
        if(curEditing) {
            hideKeyboard(view)
        } else{
            view.requestFocus()
            showKeyBoard(view)
        }
    }


    companion object {

        @JvmStatic
        @BindingAdapter("imageUrl", "picasso")
        fun setImageUrl(view: ImageView, imageUrl: String?, picasso: Picasso) {
            if (imageUrl == null) return
            picasso.load(Uri.parse(imageUrl))
                .fit()
                .transform(CircleTransform())
                .into(view)
        }


    }

}