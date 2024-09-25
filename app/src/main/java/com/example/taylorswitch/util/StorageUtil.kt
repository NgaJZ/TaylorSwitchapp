package com.example.taylorswitch.util

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.UUID
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2

class StorageUtil{


    companion object {

        private val db = Firebase.firestore





        fun uploadToStorage(uri: Uri, context: Context, type: String, postReference: CollectionReference, postId: String) {
            val storage = Firebase.storage

            // Create a storage reference from our app
            var storageRef = storage.reference

            val unique_image_name = UUID.randomUUID()
            var spaceRef: StorageReference

            if (type == "image"){
                spaceRef = storageRef.child("images/$unique_image_name.jpg")

            }else{
                spaceRef = storageRef.child("videos/$unique_image_name.mp4")
            }

            val byteArray: ByteArray? = context.contentResolver
                .openInputStream(uri)
                ?.use { it.readBytes() }

            byteArray?.let{

                var uploadTask = spaceRef.putBytes(byteArray)
                uploadTask.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "upload failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { taskSnapshot ->
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    // ...

                    spaceRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        // Store the download URL in Firestore
                        postReference
                            .document(postId)
                            .update("imageRef", FieldValue.arrayUnion(downloadUrl.toString()))

                        .addOnSuccessListener {
                            Toast.makeText(context, "Upload and reference saved successfully", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener { e ->
                            Toast.makeText(context, "Failed to save reference: $e", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        // Handle error while getting download URL
                        Toast.makeText(context, "Failed to get download URL", Toast.LENGTH_SHORT).show()
                    }
                }
            }



        }

    }
}