package com.bookmygame.ui.groundOwner

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bookmygame.network.ApiClient
import com.bookmygame.network.model.GroundOwnerGroundListResponse
import com.bookmygame.network.model.GroundOwnerGroundUpdateResponse
import com.bookmygame.util.Event
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class GroundOwnerGroundDetailViewModel : ViewModel() {

//    fun updateGround(ground: GroundOwnerGroundListResponse.Ground, images: List<String>) {
//        val builder = MultipartBody.Builder()
//        builder.setType(MultipartBody.FORM)
//
//        builder.addFormDataPart("user_id", ground.userId)
//        builder.addFormDataPart("ground_name", ground.groundName)
//        builder.addFormDataPart("ground_location", ground.groundAddress)
//        builder.addFormDataPart("ground_id", ground.groundId)
//        builder.addFormDataPart("pitch_type", "2")
//
//        for (image in images) {
//            val file = File(image)
//            builder.addFormDataPart(
//                "photos[]",
//                file.name,
//                RequestBody.create(MediaType.parse("multipart/form-data"), file)
//            )
//        }
//
//        val requestBody = builder.build()
//
//        val updateGroundResponse =
//            ApiClient.apiService.updateGround(requestBody)
//        updateGroundResponse.enqueue(object : Callback<GroundOwnerGroundUpdateResponse> {
//            override fun onResponse(
//                call: Call<GroundOwnerGroundUpdateResponse>,
//                response: Response<GroundOwnerGroundUpdateResponse>
//            ) {
//                Log.d(
//                    "Basava",
//                    "updateGroundResponse onResponse isSuccessful -> ${response.isSuccessful}"
//                )
//                Log.d(
//                    "Basava",
//                    "updateGroundResponse onResponse body -> ${response.body()}"
//                )
//                if (response.isSuccessful) {
////                    mutableLoginTypeResponse.value = Event(response.body())
//                } else {
//                    Log.e("Basava", "Something went wrong")
//                    Log.e("Basava", "Something went wrong: message -> ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<GroundOwnerGroundUpdateResponse>, t: Throwable) {
//                Log.d("Basava", "updateGroundResponse onFailure message -> ${t.message}")
////                mutableErrorMessage.value = Event(t.message ?: "Something went wrong!")
//            }
//        })
//    }

    private val mutableGroundOwnerUpdateGroundResponse =
        MutableLiveData<Event<GroundOwnerGroundUpdateResponse>>()
    val groundOwnerUpdateGroundResponse: LiveData<Event<GroundOwnerGroundUpdateResponse>> get() = mutableGroundOwnerUpdateGroundResponse

    private val mutableErrorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> get() = mutableErrorMessage

    fun prepareImagesToBeLoaded(images: List<GroundOwnerGroundListResponse.Ground.Image>): List<GroundImageSource> =
        images.map { image ->
            GroundImageSource.URL(image.imageUrl)
        }

    fun updateGround(
        ground: GroundOwnerGroundListResponse.Ground,
        images: List<String>
    ) {
        Log.d("Basava", "updateGround: images -> $images")
        Log.d("Basava", "updateGround: ground -> $ground")
//        val imageFile = File(images[0])
//        val imageRequest = RequestBody.create(MediaType.parse("image/*"), imageFile)
//        val imagePart = MultipartBody.Part.createFormData("photos[]", imageFile.name, imageRequest)

        val size = images.size
        val imageParts = arrayOfNulls<MultipartBody.Part>(size)

        images.forEachIndexed { index, imagePath ->
            val imageFile = File(imagePath)
            val imageRequest = RequestBody.create(MediaType.parse("image/*"), imageFile)
            val imagePart =
                MultipartBody.Part.createFormData("photos[]", imageFile.name, imageRequest)
            imageParts[index] = imagePart
        }
//        for(imagePath in images) {
//            val imageFile = File(imagePath)
//            val imageRequest = RequestBody.create(MediaType.parse("image/*"), imageFile)
//            val imagePart = MultipartBody.Part.createFormData("photos[]", imageFile.name, imageRequest)
//            imageParts[]
//        }

        val userIdRB = RequestBody.create(MediaType.parse("text/plain"), ground.userId)
        val groundIdRB = RequestBody.create(MediaType.parse("text/plain"), ground.groundId)
        val groundNameRB =
            RequestBody.create(MediaType.parse("text/plain"), ground.groundName)
        val groundLocationRB =
            RequestBody.create(MediaType.parse("text/plain"), ground.groundAddress)
        val pitchTypeRB =
            RequestBody.create(MediaType.parse("text/plain"), ground.pitchType)

        val updateGroundResponse =
            ApiClient.apiService.updateGround(
                imageParts,
                userIdRB,
                groundIdRB,
                groundNameRB,
                groundLocationRB,
                pitchTypeRB
            )
        updateGroundResponse.enqueue(object : Callback<GroundOwnerGroundUpdateResponse> {
            override fun onResponse(
                call: Call<GroundOwnerGroundUpdateResponse>,
                response: Response<GroundOwnerGroundUpdateResponse>
            ) {
                Log.d(
                    "Basava",
                    "updateGroundResponse onResponse isSuccessful -> ${response.isSuccessful}"
                )
                Log.d(
                    "Basava",
                    "updateGroundResponse onResponse body -> ${response.body()}"
                )
                if (response.isSuccessful) {
                    mutableGroundOwnerUpdateGroundResponse.value = Event(response.body())
                } else {
                    Log.e("Basava", "Something went wrong")
                    Log.e("Basava", "Something went wrong: message -> ${response.message()}")
                    mutableErrorMessage.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<GroundOwnerGroundUpdateResponse>, t: Throwable) {
                Log.e("Basava", "updateGroundResponse onFailure message -> ${t.message}")
                mutableErrorMessage.value = Event(t.message ?: "Something went wrong!")
            }
        })
    }
}